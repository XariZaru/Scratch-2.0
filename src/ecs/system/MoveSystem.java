package ecs.system;

import com.artemis.BaseSystem;

import java.util.LinkedList;

import static ecs.components.movement.Movement.Codec;
import static ecs.components.movement.Movement.MovementCodec;

public class MoveSystem extends BaseSystem {

    public void updatePosition(LinkedList<Codec> movement, int entity, int yOffset) {
        movement.stream().filter(move -> move instanceof MovementCodec).forEach(move -> ((MovementCodec) move).update(world, entity, yOffset));
    }

    @Override
    protected void processSystem() {

    }
}
