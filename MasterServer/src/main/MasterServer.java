package main;

import constants.ScratchConstants;
import ecs.EntityCreationSystem;
import ecs.WorldManager;
import net.MasterServerHandler;
import net.Server;
import net.coders.PacketDecoder;
import net.coders.PacketEncoder;
import systems.GameServerAssignmentSystem;
import systems.HandshakeSystem;


public class MasterServer extends Server {

    public static final MasterServer instance = new MasterServer(ScratchConstants.MASTER_SERVER_PORT);
    public static final WorldManager manager = new WorldManager(
            EntityCreationSystem.class, HandshakeSystem.class, GameServerAssignmentSystem.class);

    public MasterServer(int port) {
        super(port, null, PacketDecoder.class, MasterServerHandler.class, PacketEncoder.class);
    }

    public static void main(String[] args) {

        Thread WorldManagerThread = new Thread(manager);
        WorldManagerThread.start();

        while (!manager.started());

        Thread MasterServerThread = new Thread(instance);
        MasterServerThread.start();

    }
}
