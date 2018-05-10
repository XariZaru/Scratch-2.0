package net.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.Key;
import net.packets.MasterServerPacketCreator;
import net.App;
import net.ClientType;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Connector implements Runnable {
	
	private Bootstrap boot;
	private EventLoopGroup workerGroup;
	private Channel channel;
	private String host;
	private int port;
	private ClientType clientType;
	private ChannelInitializer<SocketChannel> handler;
	
	public Connector(ClientType clientType, String host, int port, ChannelInitializer<SocketChannel> handler) {
		this.clientType = clientType;
		this.host = host;
		this.port = port;
		this.handler = handler;
		boot = new Bootstrap();
		workerGroup = new NioEventLoopGroup();
	}
	
	public boolean connected() {
		return channel != null && channel.isActive();
	}
	
	public void write(Object msg) {
		if (channel != null) channel.writeAndFlush(msg);
	}
	
	public void serverConnectionCount(int connected) {
		if (connected())
			channel.attr(Key.CONNECTED_AMOUNT).set(connected);
	}
	
	public boolean start() {
		boot.group(workerGroup);
		boot.channel(NioSocketChannel.class);
		boot.option(ChannelOption.SO_KEEPALIVE, true);
		boot.handler(handler);			
		
		try {
			
			ScheduledFuture<?> connectionTask = boot.group().scheduleAtFixedRate(()-> {
				if(!connected()){
					try{
						System.out.printf(String.format(clientType + " attempting reconnect on %s: %d and connector " + this + " ...%n", host, port));
						channel = boot.connect(host, port).sync().channel();
						channel.writeAndFlush(MasterServerPacketCreator.clientType(clientType));
					}catch(Exception e){
					}
				}
			}, 0, 5 * 1000, TimeUnit.MILLISECONDS);
			
		} catch (Exception e) {
//			App.log.error("MASTER_SERVER_CONNECTION_ISSUE", "The client could not bind with the server.", e);
			return false;
		}
		App.log.trace("MASTER_SERVER_CONNECTION_SUCCESS","A connection to the server has been established.");
		return true;
	}
	
	public boolean close() {
		try {
			channel.close();
			workerGroup.shutdownGracefully();
			App.log.trace("MASTER_SERVER_SHUTDOWN_SUCCESS","Successfully terminated the connection with the server.");
			return true;
		} catch(Exception e) {
			App.log.error("MASTER_SERVER_SHUTDOWN_ISSUE", "There was an issue closing the connection to the server!", e);
		}
		return false;
	}

	@Override
	public void run() {
		start();
	}
	
	public boolean restart() { //@TODO: This might be an unsafe operation, probably needs a try catch or a delay in between
		if (close()) {
			return start();
		}
		return false;
	}
	
}
