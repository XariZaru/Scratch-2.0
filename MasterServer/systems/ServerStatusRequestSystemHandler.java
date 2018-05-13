package systems;

import io.netty.channel.Channel;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;

public class ServerStatusRequestSystemHandler extends PacketHandler {

    GameServerAssignmentSystem gsas;

    @Override
    public void receive(Channel channel, InboundPacket packet, OutboundPacket outBound) {
        int e = packet.readInt();
        byte world = packet.readByte();
        OutboundPacket send = gsas.getWorld(world).encodeStatus(e);
        channel.writeAndFlush(send);
    }
}
