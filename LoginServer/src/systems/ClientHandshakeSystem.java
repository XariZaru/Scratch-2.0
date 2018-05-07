package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import ecs.EntityCreationSystem;
import io.netty.channel.Channel;
import net.Key;
import net.MaplePacketCreator;
import net.components.AESOFB;
import net.components.Pipeline;

public class ClientHandshakeSystem extends BaseSystem {

    EntityCreationSystem ecs;

    @Override
    protected void processSystem() {

    }

    public AESOFB create(Channel channel) {
        return create(channel, false);
    }

    /**
     * Creates an associated Client for the channel through the ECS
     * @param channel Channel connection
     * @param handshake True if incoming connection is a handshake. If so, send getHello packet containing ivSend and ivRecv.
     * Otherwise, create regular client without sending.
     * @return Returns the entity ID associated with the client requests created
     */
    public AESOFB create(Channel channel, boolean handshake) {
        AESOFB aesofb = new AESOFB();
        Pipeline pipe = new Pipeline();
        pipe.channel = channel;

        if (handshake) {
            channel.attr(Key.AESOFB).set(aesofb);
            channel.attr(Key.PIPELINE).set(pipe);
        }

        return aesofb;
    }
}
