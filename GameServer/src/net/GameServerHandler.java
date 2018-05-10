package net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import main.GameServersLauncher;
import src.net.opcodes.MasterServerOpcode;
import systems.ServerIdentifier;

public class GameServerHandler extends ChannelInboundHandlerAdapter {

	PacketHandler[] handlers = new PacketHandler[MasterServerOpcode.values().length];
	private int index;

	public GameServerHandler(int index) {
//		handlers[MasterServerOpcode.HANDSHAKE.getValue()] = new MasterServerClientCreationHandler();
		this.index = index;
	}

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {		
		System.out.println("Connection to Master on " + ctx.channel().remoteAddress() + " for channel " + ctx.channel());
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		
		Integer e = channel.attr(Key.ENTITY).get();
		
		if (e != null) {
			System.out.println("Deleting entity for disconnection: " + e);
//			MasterServerLauncher.manager.world.delete(e);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		GameServersLauncher.gameServers.get(index)
				.manager.world.getSystem(ServerIdentifier.class).channel = ctx.channel().attr(Key.CHANNEL_NUMBER).get();
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
