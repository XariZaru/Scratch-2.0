package main;

import java.util.ArrayList;
import java.util.List;

public class GameServersLauncher {

    public static final List<GameServer> gameServers = new ArrayList();

    public static void main(String[] args) {
        for (int world = 0; world < 2; world++) {
            for (int server = 0; server < 1; server++) {
                GameServer gs = new GameServer((100 * world) + 7575 + server);
                gameServers.add(gs);

                Thread gsThread = new Thread(gs);
                gsThread.start();
            }
        }
    }

}
