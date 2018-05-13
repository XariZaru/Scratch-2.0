package net;

import com.artemis.Aspect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import main.GameServer;
import main.GameServersLauncher;
import net.packets.InboundPacket;
import net.systems.ClientHandshakeSystem;
import systems.PlayerCleanupSystem;
import systems.PlayerLoggedInSystemHandler;

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

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object packet) {
		System.out.println("Receiving packet.");

		InboundPacket buffer = (InboundPacket) packet;
		short packetId = buffer.readShort();
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

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		System.out.println("Client disconnecting on " + channel.remoteAddress());
		Integer entity = channel.attr(Key.ENTITY).get();

		System.out.println("Size of entities before deletion " + server.manager.world.getAspectSubscriptionManager()
				.get(Aspect.all())
				.getActiveEntityIds()
				.cardinality());

		if (entity != null) {
			server.manager.getSystem(PlayerCleanupSystem.class).create(entity);
			System.out.println("Size of entities now " + server.manager.world.getAspectSubscriptionManager()
					.get(Aspect.all())
					.getActiveEntityIds()
					.cardinality());
		}
		// Should never be null because all clients are assigned an entity id upon connection
//		int entityId = channel.attr(Key.ENTITY).get();
//		LoginServer.manager.delete(entityId);

//		Integer e = channel.attr(Key.ENTITY).get();
//		ClientType type = channel.attr(Key.TYPE).get();
//
//		if (type == ClientType.PLAYER) {
//			Client client = LoginServerLauncher.manager.world.getMapper(Client.class).get(e);
//			Integer playerEntityId = client.playerEntityId;
//			if (playerEntityId != null)
//				LoginServerLauncher.manager.world.getSystem(RemoveCharacterSystem.class).create(playerEntityId);
//		}
//
//		if (e != null)
//			LoginServerLauncher.manager.world.delete(e);
//
//		//TODO: make sure to delete associated player to the client...
////		if (type == ClientType.PLAYER)
////			channel.attr(Key.ENTITY)
//
//		System.out.println(String.format("Client disconnected from {%s}!", channel.remoteAddress()));
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

    }
	
}
