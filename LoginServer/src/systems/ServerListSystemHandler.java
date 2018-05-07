package systems;

import com.artemis.Component;
import io.netty.channel.Channel;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;

public class ServerListSystemHandler extends PacketHandler {
    public ServerListSystemHandler() {
    }

    @Override
    public void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound) {

    }
}
