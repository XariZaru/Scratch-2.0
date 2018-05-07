package net.coders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.packets.OutboundPacket;

public class PacketEncoder extends MessageToByteEncoder<OutboundPacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, OutboundPacket msg, ByteBuf out) throws Exception {
		byte[] send = msg.getBytes();	
//		System.out.println("Encoding");
		out.writeBytes(send);		
	}

}
