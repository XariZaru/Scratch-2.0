package net.coders;

import ecs.components.Pipeline;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.Key;
import net.encryption.MapleAESOFB;
import net.packets.InboundPacket;

import java.util.List;

public class MaplePacketDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception{
//        System.out.println("Decodin.");
	    Channel ch = ctx.channel();
		final int entity = ch.attr(Key.ENTITY).get();
		MapleAESOFB recv = ch.attr(Key.AESOFB).get().recvCypher;
		Pipeline pipe = ch.attr(Key.PIPELINE).get();

        if (in.readableBytes() >= 4) {
            int packetHeader = in.readInt();
            if (!recv.checkPacket(packetHeader)) {
                ctx.close();
                return;
            }
            pipe.nextPacketSize = MapleAESOFB.getPacketLength(packetHeader);
        } else if (in.readableBytes() < 4) {
            return;
        }
        if (in.readableBytes() >= pipe.nextPacketSize) {
        	byte[] decryptedPacket = new byte[pipe.nextPacketSize];
            in.readBytes(decryptedPacket);
            pipe.nextPacketSize = -1;
            recv.crypt(decryptedPacket);
            net.encryption.MapleCustomEncryption.decryptData(decryptedPacket);
            out.add(new InboundPacket(decryptedPacket));
            return;
        }
	}
}
