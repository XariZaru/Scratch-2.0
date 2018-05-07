package net.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class InboundPacket {
	
	ByteBuf buf;
	
	public InboundPacket(byte[] data) {
		buf = Unpooled.wrappedBuffer(data);
	}

	public ByteBuf getPacket() {
		return buf;
	}
	
	public int readableBytes() {
		return buf.readableBytes();
	}
	
	public byte[] readBytes(int length){
		byte[] data = new byte[length];
		buf.readBytes(data);
		return data;
	}
	
	public final String readAsciiString(int n) {
        char ret[] = new char[n];
        for (int x = 0; x < n; x++) {
            ret[x] = (char) readByte();
        }
        return String.valueOf(ret);
    }
	
    public String readMapleAsciiString() {
        return readAsciiString(readShort());
    }
	
	public byte readByte() {
		return buf.readByte();
	}
	
	public int readInt() {
		return buf.readIntLE();
	}
	
	public short readShort() {
		return buf.readShortLE();
	}
	
	public boolean readBool() {
		return buf.readBoolean();
	}

}
