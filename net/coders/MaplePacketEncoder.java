/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation version 3 as published by
the Free Software Foundation. You may not use, modify or distribute
this program under any other version of the GNU Affero General Public
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.coders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.Key;
import net.components.AESOFB;
import net.encryption.MapleAESOFB;
import net.encryption.MapleCustomEncryption;
import net.packets.OutboundPacket;

@ChannelHandler.Sharable
public class MaplePacketEncoder extends MessageToByteEncoder<OutboundPacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, OutboundPacket message, ByteBuf out) throws Exception {
		System.out.println("Encoding to maple client.");
		Channel ch = ctx.channel();
		final Integer entity = ch.attr(Key.ENTITY).get();
		final AESOFB a = ch.attr(Key.AESOFB).get();

		final byte[] input = message.getBytes();

        if (entity != null) {
            final MapleAESOFB send_crypto = a.sendCypher;
            final byte[] header = send_crypto.getPacketHeader(input.length);

            MapleCustomEncryption.encryptData(input);
            a.encodeLock.lock();
            try {
                send_crypto.crypt(input);
            } finally {
                a.encodeLock.unlock();
            }
            out.writeBytes(header);
            out.writeBytes(input);
        } else {
            out.writeBytes(input);
        }
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}
	
}