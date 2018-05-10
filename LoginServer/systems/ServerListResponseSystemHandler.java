package systems;

import com.artemis.ComponentMapper;
import components.Pipeline;
import io.netty.channel.Channel;
import net.PacketHandler;
import net.opcodes.SendOpcode;
import net.packets.InboundPacket;
import net.packets.MaplePacketCreator;
import net.packets.OutboundPacket;

public class ServerListResponseSystemHandler extends PacketHandler {

    ComponentMapper<Pipeline> pipelines;

    @Override
    public void receive(Channel channel, InboundPacket msg, OutboundPacket outBound) {
        int e = msg.readInt();
        Pipeline ch = pipelines.get(e);
        int worlds = msg.readByte();
        for (byte x = 0; x < worlds; x++) {
            OutboundPacket send  = new OutboundPacket();
            encodeWorld(send, msg);
            ch.write(send);
        }

        ch.write(MaplePacketCreator.getEndOfServerList());
        ch.write(MaplePacketCreator.selectWorld(0));
        ch.write(MaplePacketCreator.sendRecommended());
    }

    private byte encodeWorld(OutboundPacket send, InboundPacket msg) {
        send.writeShort(SendOpcode.SERVERLIST.getValue());
        byte worldEntityId = msg.readByte();
        send.write(worldEntityId);
        send.writeMapleAsciiString(msg.readMapleAsciiString());
        send.write(msg.readByte());
        send.writeMapleAsciiString(msg.readMapleAsciiString());
        send.write(msg.readByte()); // rate modifier
        send.write(msg.readByte()); // event exp * 2.6
        send.write(msg.readByte()); // rate modifier
        send.write(msg.readByte()); // drop rate * 2.6
        send.write(msg.readByte());
        int servers = msg.readByte();
        send.write(servers); // How many game servers connected to this world

        for (int x = 0; x < servers; x++)
            encodeGameServer(send, msg);

        send.writeShort(0);
        return worldEntityId;
    }

    private void encodeGameServer(OutboundPacket send, InboundPacket msg) {
        send.writeMapleAsciiString(msg.readMapleAsciiString());
        send.writeInt(msg.readInt());
        send.write(msg.readByte());
        send.writeShort(msg.readShort());
    }

}
