package systems;

import io.netty.channel.Channel;
import main.LoginServer;
import net.Key;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.MasterServerPacketCreator;
import net.packets.OutboundPacket;

public class ServerStatusRequestSystemHandler extends PacketHandler {
    @Override
    public void receive(Channel channel, InboundPacket packet, OutboundPacket outBound) {
        short world = packet.readShort();

        int e = channel.attr(Key.ENTITY).get();
        LoginServer.instance.master.write(MasterServerPacketCreator.serverStatusRequest(e, world));
    }
}
