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
import systems.*;

public class LoginServer extends Server {

    public static final LoginServer instance = new LoginServer(ScratchConstants.LOGIN_SERVER_PORT, new ClientHandler());
    public static final WorldManager manager = new WorldManager(
            EntityCreationSystem.class, ClientHandshakeSystem.class, LoginSystemHandler.class,
            ServerListRequestSystemHandler.class, ServerListResponseSystemHandler.class,
            ServerStatusRequestSystemHandler.class, ServerStatusResponseSystemHandler.class,
            CharListRequestSystemHandler.class, CreateCharSystemHandler.class, CheckCharNameSystemHandler.class,
            CharSelectSystemHandler.class, ClientConnectToServerResponseSystem.class);
    private ClientHandler handler;

    public LoginServer(int port, ClientHandler handler) {
        super(ScratchConstants.LOGIN_SERVER_IP, port,
            new MasterServerConnector(ClientType.LOGIN, new LoginServerTrafficHandler()),
            handler, MaplePacketDecoder.class, MaplePacketEncoder.class);
        this.handler = handler;
    }

    public void writeToMaster(Object msg) {
        master.write(msg);
    }

    public static void main(String[] args) {

        Thread WorldManagerThread = new Thread(manager);
        WorldManagerThread.start();

        while (!manager.started());

        Thread LoginThread = new Thread(instance);
        LoginThread.start();

        instance.handler.initialize();
    }

}
