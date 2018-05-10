package systems;

import com.artemis.ComponentMapper;
import components.Pipeline;
import io.netty.channel.Channel;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.MaplePacketCreator;
import net.packets.OutboundPacket;

public class ServerStatusResponseSystemHandler extends PacketHandler {

    ComponentMapper<Pipeline> pipelines;

    @Override
    public void receive(Channel channel, InboundPacket packet, OutboundPacket outBound) {
        int e = packet.readInt();
        short status = packet.readShort();

        OutboundPacket send = MaplePacketCreator.getServerStatus(status);
        pipelines.get(e).write(send);
    }
}
