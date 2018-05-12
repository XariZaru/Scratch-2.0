package ecs;

import com.artemis.*;

public class WorldManager implements Runnable {

    private final WorldConfiguration config;
    public final World world;
    private boolean started = false;

    /**
     * Remember to wrap this class in a thread and call start to access the run method.
     * @param classes BaseSystem classes to be processed
     */
    @SafeVarargs
    public WorldManager(Class<? extends BaseSystem>... classes) {
        BaseSystem[] systems = new BaseSystem[classes.length];
        for (int x = 0; x < systems.length; x++)
            try {
//                System.out.println(classes[x]);
                systems[x] = classes[x].newInstance();
//                System.out.println(systems[x]);
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("Failed to create systems for ECS World Manager.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        config = new WorldConfigurationBuilder().with(
                systems
        ).build();
        world = new World(config);
    }

    public <T extends Component> T getComponent(Class<? extends Component> componentClass, int entityId) {
        return (T) world.getMapper(componentClass).get(entityId);
    }

    public <T extends BaseSystem> T getSystem(Class<T> type) {
        return world.getSystem(type);
    }
    public synchronized int create() {
        return world.create();
    }

    public AspectSubscriptionManager getSubscriptionManager() {
        return world.getAspectSubscriptionManager();
    }

    public void delete(int entityId) {
        world.delete(entityId);
    }

    public boolean started() {
        return started;
    }

    @Override
    public void run() {


        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        long lastFpsTime = 0, fps = 0;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        System.out.println("ECS established!");

        // keep looping round til the game ends
        while (true)
        {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            float delta = (updateLength / ((float)OPTIMAL_TIME));

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            world.setDelta(delta);

            try {
                world.process();
            } catch (ArrayIndexOutOfBoundsException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }

            // update our FPS counter if ten second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000)
            {
//	    		System.out.println("(FPS: "+fps+")");
                lastFpsTime = 0;
                fps = 0;
            }

            started = true;

            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
                Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
            } catch (IllegalArgumentException e) {
                // Do nothing if negative because we want the game loop to not sleep and catch up
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
