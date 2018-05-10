package systems;

import io.netty.channel.Channel;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;

public class ClientConnectToServerSystem extends PacketHandler {

    private GameServerAssignmentSystem gsas;

    @Override
    public void receive(Channel channel, InboundPacket packet, OutboundPacket outBound) {
        int dbId = packet.readInt();
        int clientEntityId = packet.readInt();
        byte world = packet.readByte();
        byte server = packet.readByte();
        OutboundPacket send = gsas.encodeConnectInformation(world, server);
        send.writeInt(dbId);
        send.writeInt(clientEntityId);
        send.writeByte(world);
        send.writeByte(server);
        channel.writeAndFlush(send);
    }
}
