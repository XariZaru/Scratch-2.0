package net.coders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.packets.InboundPacket;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {			
		System.out.println("Decoding incoming packet between servers.");
		byte[] buf = new byte[in.readableBytes()];
		in.readBytes(buf);
		InboundPacket packet = new InboundPacket(buf);
		out.add(packet);
	}

}
