package ecs.system;

import com.artemis.Aspect;
import com.artemis.systems.DelayedIteratingSystem;

public class SampleDelayedSystem extends DelayedIteratingSystem {

    public SampleDelayedSystem() {
        super(Aspect.all());
    }

    @Override
    protected float getRemainingDelay(int entityId) {
        return 0;
    }

    @Override
    protected void processDelta(int entityId, float accumulatedDelta) {

    }

    @Override
    protected void processExpired(int entityId) {

    }
}
