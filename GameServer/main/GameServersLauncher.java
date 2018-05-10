package main;

import net.GameClientHandler;

import java.util.ArrayList;
import java.util.List;

public class GameServersLauncher {

    public static final List<GameServer> gameServers = new ArrayList();

    public static void main(String[] args) {
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
