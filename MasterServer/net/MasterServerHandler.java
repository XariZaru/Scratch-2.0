package net;

import ecs.WorldManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import main.MasterServer;
import net.packets.InboundPacket;

@ChannelHandler.Sharable
public class MasterServerHandler extends ChannelInboundHandlerAdapter {

	PacketHandler[] handlers = new PacketHandler[net.opcodes.MasterServerOpcode.values().length];

	public void initialize() {
		WorldManager world = MasterServer.manager;
		handlers[net.opcodes.MasterServerOpcode.HANDSHAKE.getValue()] = world.getSystem(systems.HandshakeSystem.class);
		handlers[net.opcodes.MasterServerOpcode.SERVER_LIST_REQUEST.getValue()] = world.getSystem(systems.ServerInfoRequestSystemHandler.class);
		handlers[net.opcodes.MasterServerOpcode.GAME_SERVER_STATUS_REQUEST.getValue()] = world.getSystem(systems.ServerStatusRequestSystemHandler.class);
		handlers[net.opcodes.MasterServerOpcode.CONNECT_CLIENT_TO_SERVER.getValue()] = world.getSystem(systems.ClientConnectToServerSystem.class);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
//		System.out.println("Connection to Master on " + ctx.channel().remoteAddress() + " for channel " + ctx.channel());
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		int e = channel.attr(Key.ENTITY).get();
		System.out.println(channel.attr(Key.TYPE).get() + " " + e + " disconnected from master.");
		MasterServer.manager.delete(e);
//		Integer e = channel.attr(Key.ENTITY).get();
//
//		if (e != null) {
//			System.out.println("Deleting entity for disconnection: " + e);
//			MasterServerLauncher.manager.world.delete(e);
//		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		InboundPacket buf = (InboundPacket) msg;
		short opcode = buf.readShort();
		System.out.println("Received packet with opcode " + net.opcodes.MasterServerOpcode.getOpcode(opcode));
		PacketHandler handler = getHandler(opcode);
		if (handler != null) {
			handler.receive(ctx.channel(), buf, null);
		} else {
			System.out.println("Unhandled packetId " + opcode);
		}
		/*Client c = ctx.attr(Client.CLIENT_KEY).get();
		ByteBuf buffer = (ByteBuf) msg;
		if (buffer.readableBytes() < 1) {
			return;
		}
		short code = buffer.readShort();
		ReceivePacketHandler handler = processor.getHandler(code);
		if (handler != null && handler.isLoggedIn(c)) {
			handler.handlePacket(c, buffer);
		} */
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

	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("event");
	}
	
}
