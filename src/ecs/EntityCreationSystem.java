package ecs;

import com.artemis.BaseSystem;

public class EntityCreationSystem extends BaseSystem {


    @Override
    protected void processSystem() {

    }

    public synchronized int create() {
        return world.create();
    }

}
