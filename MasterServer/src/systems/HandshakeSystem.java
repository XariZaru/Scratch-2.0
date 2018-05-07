package systems;

import ecs.EntityCreationSystem;
import io.netty.channel.Channel;
import main.MasterServer;
import net.ClientType;
import net.Key;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;

public class HandshakeSystem extends PacketHandler {

    EntityCreationSystem ecs;
    GameServerAssignmentSystem gsas;

    @Override
    public void receive(Channel channel, InboundPacket packet, OutboundPacket outBound) {
        if (channel.attr(Key.ENTITY).get() != null) return;

        ClientType clientType = ClientType.values()[packet.readByte()];
        int e = ecs.create();
        channel.attr(Key.TYPE).set(clientType);
        channel.attr(Key.ENTITY).set(e);
        System.out.println("Created a client for Master Server with id " + e + " of type " + clientType);

        if (clientType == ClientType.GAME_SERVER)
            gsas.assign(channel);

    }
}
