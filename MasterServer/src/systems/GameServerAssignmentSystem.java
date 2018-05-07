package systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import components.WorldInformation;
import ecs.EntityCreationSystem;
import io.netty.channel.Channel;
import net.Key;

public class GameServerAssignmentSystem extends BaseEntitySystem {

    ComponentMapper<WorldInformation> wiMapper;
    private EntityCreationSystem entities;

    public GameServerAssignmentSystem() {
        super(Aspect.all(WorldInformation.class));
    }

    @Override
    protected void processSystem() {

    }

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

}
