package net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.connector.Connector;
import net.packets.OutboundPacket;

import java.lang.reflect.Field;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Map;

public class Server implements Runnable {

    private int port;
    private Class<? extends ChannelHandler>[] handlers;
    ServerBootstrap b;
    EventLoopGroup bossGroup, workerGroup;
    public final ChannelGroup channels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public final Connector master;

    @SafeVarargs
    public Server(int port, Connector connector, Class<? extends ChannelHandler>... handlers) {
        this.port = port;
        this.master = connector;
        this.handlers = handlers;

        if (master != null) {
            Thread MasterServerConnectorInterim = new Thread(master);
            MasterServerConnectorInterim.start();
        }

    }

    public void writeForType(OutboundPacket msg, ClientType type) {
        channels.stream().filter(ch -> ch.attr(Key.TYPE).get() == type).forEach(ch ->
        {
            ch.writeAndFlush(msg);
        });
    }

    public ArrayList<Channel> getChannelsOfType(ClientType type) {
        ArrayList<Channel> ret = new ArrayList<Channel>();
        channels.stream().filter(ch -> ch.attr(Key.TYPE).get() == type).forEach(ch ->
        {
            ret.add(ch);
        });
        return ret;
    }

    private void updateConnection(Channel ch) {
        channels.add(ch);
        if (master != null) {
            master.serverConnectionCount(channels.size());
        }
    }

    public void run() {

        removeCryptographyRestrictions();

        bossGroup   = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            System.out.println("net.Server listening on port " + port);
            b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {

                            updateConnection(ch);

                            ChannelHandler[] ret = new ChannelHandler[handlers.length];
                            for (int x = 0; x < handlers.length; x++) {
                                Class<? extends ChannelHandler> handlerClass = handlers[x];
                                ret[x] = handlerClass.newInstance();
                            }

                            ch.pipeline().addLast(ret);

                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * Credits: ntoskrnl of StackOverflow
     * http://stackoverflow.com/questions/1179672/
     *
     */
    public void removeCryptographyRestrictions() {
//        if (!isRestrictedCryptography()) {
//            System.out.println("Cryptography restrictions removal not needed");
//            return;
//        }
        try {
            /*
             * Do the following, but with reflection to bypass access checks:
             *
             * JceSecurity.isRestricted = false;
             * JceSecurity.defaultPolicy.perms.clear();
             * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
             */
            Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
            Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
            Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");


            Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
            isRestrictedField.setAccessible(true);
            //isRestrictedField.set(null, false);


            Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
            defaultPolicyField.setAccessible(true);
            PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);


            Field perms = cryptoPermissions.getDeclaredField("perms");
            perms.setAccessible(true);
            ((Map<?, ?>) perms.get(defaultPolicy)).clear();


            Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            defaultPolicy.add((Permission) instance.get(null));


            System.out.println("Successfully removed cryptography restrictions");
        } catch (Exception e) {
            System.err.println("Failed to remove cryptography restrictions");
            e.printStackTrace();
        }
    }

}