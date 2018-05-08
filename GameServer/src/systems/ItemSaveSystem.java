package systems;

import com.artemis.Aspect;
import com.artemis.systems.IntervalIteratingSystem;
import tools.Pair;

import java.util.List;

public class ItemSaveSystem extends IntervalIteratingSystem {
    /**
     * Creates a new IntervalEntityProcessingSystem.
     *
     * @param aspect   the aspect to match entities
     * @param interval
     */
    public ItemSaveSystem() {
        super(Aspect.all(), 100);
    }

    @Override
    protected void process(int entityId) {

    }

}
