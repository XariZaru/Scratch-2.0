package main;

import ecs.EntityCreationSystem;
import ecs.WorldManager;
import net.coders.PacketDecoder;
import net.coders.PacketEncoder;
import src.constants.ScratchConstants;
import src.net.MasterServerHandler;
import src.net.Server;

public class MasterServer extends Server {

    public static final MasterServer instance = new MasterServer(ScratchConstants.MASTER_SERVER_PORT, new MasterServerHandler());
    public static final WorldManager manager = new WorldManager(
            EntityCreationSystem.class, MasterServer.src.systems.HandshakeSystem.class, MasterServer.src.systems.GameServerAssignmentSystem.class,
            MasterServer.src.systems.ServerInfoRequestSystemHandler.class, MasterServer.src.systems.ServerStatusRequestSystemHandler.class,
            MasterServer.src.systems.ClientConnectToServerSystem.class);
    private MasterServerHandler handler;

    public MasterServer(int port, MasterServerHandler handler) {
        super(ScratchConstants.MASTER_SERVER_IP, port, null,
                handler, PacketDecoder.class, PacketEncoder.class);
        this.handler = handler;
    }

    public static void main(String[] args) {

        Thread WorldManagerThread = new Thread(manager);
        WorldManagerThread.start();

        while (!manager.started());

        instance.handler.initialize();

        for (byte x = 0; x < ScratchConstants.NUM_WORLDS; x++)
            manager.world.getSystem(MasterServer.src.systems.GameServerAssignmentSystem.class).create(x, 2, "Welcome!");

        Thread MasterServerThread = new Thread(instance);
        MasterServerThread.start();

    }
}
