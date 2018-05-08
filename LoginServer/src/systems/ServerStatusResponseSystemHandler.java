package systems;

import com.artemis.ComponentMapper;
import io.netty.channel.Channel;
import net.packets.MaplePacketCreator;
import net.PacketHandler;
import net.components.Pipeline;
import net.packets.InboundPacket;
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
