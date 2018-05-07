package systems;

import com.artemis.Component;
import io.netty.channel.Channel;
import main.LoginServer;
import net.Key;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.MasterServerPacketCreator;
import net.packets.OutboundPacket;

public class ServerListRequestSystemHandler extends PacketHandler {
    public ServerListRequestSystemHandler() {
    }

    @Override
    public void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound) {
        int e = channel.attr(Key.ENTITY).get();
        LoginServer.instance.writeToMaster(MasterServerPacketCreator.masterServerListRequest(e));
    }
}
