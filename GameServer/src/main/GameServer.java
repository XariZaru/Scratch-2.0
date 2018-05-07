package main;

import constants.ScratchConstants;
import ecs.EntityCreationSystem;
import ecs.WorldManager;
import net.*;
import net.coders.MaplePacketDecoder;
import net.coders.MaplePacketEncoder;
import net.coders.PacketDecoder;
import net.coders.PacketEncoder;
import net.connector.MasterServerConnector;

public class GameServer extends Server implements Runnable {

    public final WorldManager manager = new WorldManager(
            EntityCreationSystem.class);

    public GameServer(int port) {
        super(port, new MasterServerConnector(ClientType.GAME_SERVER, new GameServerTrafficHandler()), MaplePacketDecoder.class, GameClientHandler.class, MaplePacketEncoder.class);
    }

    @Override
    public void run() {

        Thread WorldManagerThread = new Thread(manager);
        WorldManagerThread.start();

        while (!manager.started());

    }

}
