package systems;

import com.artemis.ComponentMapper;
import io.netty.channel.Channel;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.MaplePacketCreator;
import net.packets.OutboundPacket;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientConnectToServerResponseSystem extends PacketHandler {

    private ComponentMapper<components.Pipeline> pipes;

    @Override
    public void receive(Channel channel, InboundPacket packet, OutboundPacket outBound) {
        String address = packet.readMapleAsciiString();
        int dbId = packet.readInt();
        int clientEntityId = packet.readInt();

        components.Pipeline pipe = pipes.get(clientEntityId);
        byte world = packet.readByte();
        byte server = packet.readByte();
        String[] addressAndPort = address.split(":"); // /127.0.0.1:6650
        address = addressAndPort[0];

//
        // TODO: change to some wan IP after for GameServer
//        if (address.equals("//127.0.0.1"))
        address = "72.211.214.33";
////        int port = 8483;

        int port = (100 * world) + 7575 + server;
        try {
            pipe.write(MaplePacketCreator.connect(InetAddress.getByName(address), port, dbId));
        } catch (UnknownHostException e) {

        }
    }

}
