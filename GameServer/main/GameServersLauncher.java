package main;

import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.system.ItemLibrarySystem;
import net.GameClientHandler;

import java.util.ArrayList;
import java.util.List;

public class GameServersLauncher {

    public static final List<GameServer> gameServers = new ArrayList();
    private static WorldManager manager;
    public  static ItemLibrarySystem librarySystem;

    public static void main(String[] args) {

        System.setProperty("wzpath", "/Users/jonnguyen/Documents/Repositories/Scratch 2.0/wz");

        manager = new WorldManager(EntityCreationSystem.class, ItemLibrarySystem.class);
        Thread thread = new Thread(manager);
        thread.start();

        while (!manager.started());

        librarySystem = manager.getSystem(ItemLibrarySystem.class);
        librarySystem.generate();

        for (int world = 0; world < 2; world++) {
            for (int server = 0; server < 1; server++) {
                GameServer gs = new GameServer((100 * world) + 7575 + server, new GameClientHandler(gameServers.size()),
                        gameServers.size());
                gameServers.add(gs);

                Thread ManagerThread = new Thread(gs.manager);
                ManagerThread.start();

                Thread ServerThread = new Thread(gs);
                ServerThread.start();

                gs.handler.initialize();
            }
        }
    }

}
