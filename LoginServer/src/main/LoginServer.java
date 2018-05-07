package main;

import constants.ScratchConstants;
import ecs.EntityCreationSystem;
import ecs.WorldManager;
import net.ClientHandler;
import net.ClientType;
import net.LoginServerTrafficHandler;
import net.Server;
import net.coders.MaplePacketDecoder;
import net.coders.MaplePacketEncoder;
import net.connector.MasterServerConnector;
import systems.ClientHandshakeSystem;
import systems.LoginSystemHandler;

public class LoginServer extends Server {

    public static final LoginServer instance = new LoginServer(ScratchConstants.LOGIN_SERVER_PORT);
    public static final WorldManager manager = new WorldManager(
            EntityCreationSystem.class, ClientHandshakeSystem.class, LoginSystemHandler.class);

    public LoginServer(int port) {
        super(port,
            new MasterServerConnector(ClientType.LOGIN, new LoginServerTrafficHandler()),
            MaplePacketDecoder.class, ClientHandler.class, MaplePacketEncoder.class);
    }

    public static void main(String[] args) {

        Thread WorldManagerThread = new Thread(manager);
        WorldManagerThread.start();

        while (!manager.started());

        Thread LoginThread = new Thread(instance);
        LoginThread.start();
    }

}
