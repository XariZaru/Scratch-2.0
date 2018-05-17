package systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import ecs.components.requests.PlayerSaveRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PlayerSaveSystem extends IteratingSystem {

    ComponentMapper<PlayerSaveRequest> requests;
    PreparedStatement ps;
    Connection con;

    public PlayerSaveSystem() {
        super(Aspect.all(PlayerSaveRequest.class));
    }

    @Override
    protected void begin() {
    }

    @Override
    protected void end() {

    }

    @Override
    protected void process(int entityId) {
        try {
            // TODO: saving
        } finally {
            requests.remove(entityId);
        }
    }

    public void create(int playerEntityId) {
        requests.create(playerEntityId);
    }
}
