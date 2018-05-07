package net.packets;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class OutboundPacket {

    final ByteBuf buf;
    final Charset ASCII = Charset.forName("US-ASCII");

    public OutboundPacket(){
        buf = Unpooled.buffer(2);
    }

    public OutboundPacket(int initialCapacity){
        buf = Unpooled.buffer(initialCapacity);
    }

    public ByteBuf getPacket() {
        return buf;
    }

    public void writeMapleAsciiString(String s) {
        writeShort((short) s.length());
        writeAsciiString(s);
    }

    public void writeAsciiString(String s) {
        write(s.getBytes(ASCII));
    }


    public byte[] getBytes() {
        byte[] ret = new byte[buf.writerIndex()];
        buf.readBytes(ret);
        buf.resetReaderIndex();
        return ret;
    }

    public void writeInt(int value) {
        buf.writeIntLE(value);
    }

    public void skip(int bytes) {
        this.write(new byte[bytes]);
    }

    public void writeShort(int value) {
        buf.writeShortLE(value);
    }

    public void writeLong(long value){
        buf.writeLongLE(value);
    }

    public void write(int value) {
        buf.writeByte(value);
    }

    public void writeString(String text){
        writeShort(text.length());
        writeBytes(text.getBytes(ASCII));
    }

    public void writeBytes(byte[] values) {
        buf.writeBytes(values);
    }

    public void writeByte(int value) {
        buf.writeByte(value);
    }

    public void write(byte[] values) {
        buf.writeBytes(values);
    }

}
