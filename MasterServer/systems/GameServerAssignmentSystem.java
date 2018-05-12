package systems;


import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import ecs.components.WorldInformation;
import ecs.EntityCreationSystem;
import io.netty.channel.Channel;
import net.Key;
import net.opcodes.MasterServerOpcode;
import net.packets.OutboundPacket;

public class GameServerAssignmentSystem extends BaseEntitySystem {

    ComponentMapper<WorldInformation> wiMapper;
    EntityCreationSystem entities;

    public GameServerAssignmentSystem() {
        super(Aspect.all(WorldInformation.class));
    }

    @Override
    protected void processSystem() {

    }

    /**
     * Assigns incoming Game Server a world.
     * @param ch Channel in which the server is communicating to master on.
     */
    public synchronized void assign(Channel ch) {
        IntBag worldIds = subscription.getEntities();
        for (int x = 0; x < worldIds.size(); x++) {
            int worldId = worldIds.get(x);
            WorldInformation wi = wiMapper.get(worldId);
            if (wi.addChannel(ch)) {
                ch.attr(Key.WORLD).set(wi.world);
                return;
            }
        }
    }

    public OutboundPacket encodeWorlds(int entityId) {
        OutboundPacket buf = new OutboundPacket();
        IntBag worldIds = subscription.getEntities();
        buf.writeShort(MasterServerOpcode.GAME_SERVER_LIST_RESPONSE.getValue());
        buf.writeInt(entityId);
        buf.write(worldIds.size());

        for (int x = 0; x < worldIds.size(); x++) {
            int worldId = worldIds.get(x);
            WorldInformation wi = wiMapper.get(worldId);
            wi.encode(buf);
        }
        return buf;
    }

    public OutboundPacket encodeConnectInformation(byte world, byte channel) {
        return getWorld(world).encodeConnectionInformation(channel);
    }

    public WorldInformation getWorld(byte num) {
        return wiMapper.get(subscription.getEntities().get(num));
    }

    public void create(byte worldNum, int flag, String msg) {
        int e = entities.create();
        WorldInformation wi = wiMapper.create(e);
        wi.flag = flag;
        wi.msg 	= msg;
        wi.world = worldNum;
    }

}
