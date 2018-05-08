package systems;

import io.netty.channel.Channel;
import main.LoginServer;
import net.Key;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.MasterServerPacketCreator;
import net.packets.OutboundPacket;

public class CharSelectSystemHandler extends PacketHandler {
    @Override
    public void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound) {
        int charId = inBound.readInt();
        String macs = inBound.readMapleAsciiString();
//        c.updateMacs(macs);
//        if (c.hasBannedMac()) {
//            c.getSession().close(true);
//            return;
//        }

//        if (c.getIdleTask() != null) {
//            c.getIdleTask().cancel(true);
//        }

//        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
        LoginServer.instance.writeToMaster(MasterServerPacketCreator.connectClientToServer(
                channel.attr(Key.ENTITY).get(), channel.attr(Key.PIPELINE).get(), charId));
    }
}
