package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import ecs.EntityCreationSystem;
import io.netty.channel.Channel;
import net.Key;
import net.components.AESOFB;
import net.components.Pipeline;
import net.packets.MaplePacketCreator;

public class ClientHandshakeSystem extends BaseSystem {

    EntityCreationSystem ecs;
    ComponentMapper<Pipeline> pipes;
    ComponentMapper<AESOFB> aesofbs;

    @Override
    protected void processSystem() {

    }

    public void create(Channel channel) {
        create(channel, false);
    }

    /**
     * Creates an associated Client for the channel through the ECS
     * @param ch Channel connection
     * @param handshake True if incoming connection is a handshake. If so, send getHello packet containing ivSend and ivRecv.
     * Otherwise, create regular client without sending.
     * @return Returns the entity ID associated with the client requests created
     */
    public void create(Channel ch, boolean handshake) {
        int e = ecs.create();
        AESOFB aesofb = aesofbs.create(e);
        Pipeline pipe = pipes.create(e);

        ch.attr(Key.AESOFB).set(aesofb);
        ch.attr(Key.PIPELINE).set(pipe);
        pipe.channel = ch;

        if (handshake)
            ch.writeAndFlush(MaplePacketCreator.handshake(aesofb.ivSend, aesofb.ivRecv));

        ch.attr(Key.ENTITY).set(e);

    }
}
