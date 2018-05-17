package systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import ecs.components.requests.PlayerCleanupRequest;
import ecs.system.InventorySystem;

public class PlayerCleanupSystem extends IteratingSystem {

    InventorySystem inventorySystem;
    ComponentMapper<PlayerCleanupRequest> playerCleanupRequestComponentMapper;

    public PlayerCleanupSystem() {
        super(Aspect.all(PlayerCleanupRequest.class));
    }

    @Override
    protected void process(int entityId) {
        inventorySystem.destroy(entityId);
        world.delete(entityId);
    }

    public void create(int entityId) {
        playerCleanupRequestComponentMapper.create(entityId);
    }

}
