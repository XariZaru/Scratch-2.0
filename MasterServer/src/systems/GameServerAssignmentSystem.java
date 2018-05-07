package systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import components.WorldInformation;
import constants.ScratchConstants;
import ecs.EntityCreationSystem;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import net.Key;
import net.MasterServerOpcode;
import net.packets.OutboundPacket;

public class GameServerAssignmentSystem extends BaseEntitySystem {

    ComponentMapper<WorldInformation> wiMapper;
    private EntityCreationSystem entities;

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
            encodeGameServers(wi.channels, x, buf);
        }
        return buf;
    }

    private void encodeGameServers(ChannelGroup group, int world, OutboundPacket mplew) {
        String worldName = ScratchConstants.WORLD_NAMES[world];
        int x = 0;
        for (Channel ch : group) {
            Integer connected = ch.attr(Key.CONNECTED_AMOUNT).get();
            connected = connected == null ? 0 : connected;
            mplew.writeMapleAsciiString((worldName + "-" + (x + 1)));
            mplew.writeInt((connected * 1200) / ScratchConstants.GAME_SERVER_LOAD);
            mplew.write(1);
            mplew.writeShort(x);
            x++;
        }
    }

    public void create(byte worldNum, int flag, String msg) {
        int e = entities.create();
        WorldInformation wi = wiMapper.create(e);
        wi.flag = flag;
        wi.msg 	= msg;
        wi.world = worldNum;
    }

}
