package systems;

import com.artemis.ComponentMapper;
import requests.ServerListRequest;
import ecs.EntityCreationSystem;
import io.netty.channel.Channel;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;

public class ServerInfoRequestSystemHandler extends PacketHandler {

    EntityCreationSystem ecs;
    GameServerAssignmentSystem gsas;
    ComponentMapper<ServerListRequest> requests;

    public ServerInfoRequestSystemHandler() {
        super(ServerListRequest.class);
    }

    @Override
    public void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound) {
        ServerListRequest request = requests.create(ecs.create());
        request.ch = channel;
        request.packet = inBound;
    }

    @Override
    protected void process(int e) {
        try {
            ServerListRequest request = requests.get(e);
            InboundPacket packet = request.packet;
            Channel ch = request.ch;

            int entityId = packet.readInt();
            OutboundPacket send = gsas.encodeWorlds(entityId);
            ch.writeAndFlush(send);
        } finally {
            world.delete(e);
        }
    }

}
