package main;

import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.system.ItemCreationSystem;
import ecs.system.ItemLibrarySystem;
import net.GameClientHandler;

import java.util.ArrayList;
import java.util.List;

public class GameServersLauncher {

    public static final List<GameServer> gameServers = new ArrayList();
    private static WorldManager libraryManager;
    public  static ItemLibrarySystem librarySystem;

    public static void main(String[] args) {

        System.setProperty("wzpath", "/Users/jonnguyen/Documents/Repositories/Scratch 2.0/wz");

        libraryManager = new WorldManager(EntityCreationSystem.class, ItemCreationSystem.class, ItemLibrarySystem.class);
        Thread thread = new Thread(libraryManager);
        thread.start();

        while (!libraryManager.started());

        librarySystem = libraryManager.getSystem(ItemLibrarySystem.class);
        librarySystem.generate();

        libraryManager.getSystem(ItemCreationSystem.class).libraryWorldManager = libraryManager;

        for (int world = 0; world < 2; world++) {
            for (int server = 0; server < 1; server++) {
                GameServer gs = new GameServer((100 * world) + 7575 + server, new GameClientHandler(gameServers.size()),
                        gameServers.size());
                gameServers.add(gs);

                Thread ManagerThread = new Thread(gs.manager);
                ManagerThread.start();

                while (!gs.manager.started());

                gs.manager.getSystem(ItemCreationSystem.class).libraryWorldManager = libraryManager;

                Thread ServerThread = new Thread(gs);
                ServerThread.start();

                gs.handler.initialize();
            }
        }
    }

}
