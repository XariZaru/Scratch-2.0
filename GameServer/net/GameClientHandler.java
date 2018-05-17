package net;

import com.artemis.Aspect;
import ecs.system.OpcodeLoggingSystem;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import main.GameServer;
import main.GameServersLauncher;
import net.opcodes.RecvOpcode;
import net.packets.InboundPacket;
import net.systems.ClientHandshakeSystem;
import systems.PlayerCleanupSystem;
import systems.PlayerLoggedInSystemHandler;
import systems.PlayerMoveSystemHandler;

import java.util.HashSet;
import java.util.Set;

@ChannelHandler.Sharable
public class GameClientHandler extends ChannelInboundHandlerAdapter {

	PacketHandler[] handlers = new PacketHandler[net.opcodes.RecvOpcode.values().length];

	private GameServer server;
	private final int index;

	public GameClientHandler(int index) {
	    this.index = index;
    }

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		Channel ch = ctx.channel();

		server.manager.getSystem(ClientHandshakeSystem.class).create(ch, true);

        System.out.println("Current entity size " + server.manager.world.getAspectSubscriptionManager()
                .get(Aspect.all())
                .getActiveEntityIds()
                .cardinality());

//		ch.attr(Key.ENTITY).set(LoginServer.manager.create());
		System.out.println(String.format("Client connected from {%s}", ch.remoteAddress()));

		// TODO: check if master server connection is up otherwise close
//		// Closes connection with opening MapleClient if Login Server has no connected to the master server yet.
//		if (!LoginServer.instance.master.connected()) {
//			ch.close();
//			return;
//		}
//
//		System.out.println(String.format("Client connected from {%s}!", ch.remoteAddress()));
//		System.out.println("There are " + LoginServerLauncher.manager.world.getAspectSubscriptionManager().get(Aspect.all()).getEntities().size() + " entities");

	}

	private static Set<Short> ignoredOpcodes = new HashSet<>();
	static {
		ignoredOpcodes.add((short) 207);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object packet) {
		System.out.println("Receiving packet.");
		InboundPacket buffer = (InboundPacket) packet;
		short packetId = buffer.readShort();

		server.manager.getSystem(OpcodeLoggingSystem.class).create(RecvOpcode.getOpcode(packetId));

		if (!ignoredOpcodes.contains(packetId)) {
			System.out.println("Handling opcode for " + net.opcodes.RecvOpcode.getOpcode(packetId));
			PacketHandler handler = getHandler(packetId);

			if (handler != null) {
				try {
					handler.receive(ctx.channel(), buffer, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("Processing packet id " + net.opcodes.RecvOpcode.getOpcode(packetId) + " with ID " + packetId + " with handler " + handler);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		System.out.println("Client disconnecting on " + channel.remoteAddress());
		Integer entity = channel.attr(Key.ENTITY).get();

		if (entity != null) {
            server.manager.getSystem(PlayerCleanupSystem.class).create(entity);
        }
	}

	public PacketHandler getHandler(short opcode) {
		try {
			return handlers[opcode];
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}

	public void initialize() {

	    this.server = GameServersLauncher.gameServers.get(index);
	    handlers[net.opcodes.RecvOpcode.PLAYER_LOGGEDIN.getValue()] = server.manager.getSystem(PlayerLoggedInSystemHandler.class);
	    handlers[RecvOpcode.MOVE_PLAYER.getValue()] = server.manager.getSystem(PlayerMoveSystemHandler.class);

    }
	
}
