package main;

import constants.ScratchConstants;
import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.system.InventorySystem;
import ecs.system.ItemCreationSystem;
import ecs.system.ItemInfoEncodingSystem;
import ecs.system.OpcodeLoggingSystem;
import net.ClientType;
import net.GameClientHandler;
import net.GameServerTrafficHandler;
import net.Server;
import net.coders.MaplePacketDecoder;
import net.coders.MaplePacketEncoder;
import net.connector.MasterServerConnector;
import net.systems.ClientHandshakeSystem;
import systems.*;

public class GameServer extends Server implements Runnable {

    public final WorldManager manager = new WorldManager(
            /* Backend Systems */
            EntityCreationSystem.class, ClientHandshakeSystem.class, ServerIdentifier.class,
            CooldownSystem.class, InventorySystem.class, CharacterInfoEncodingSystem.class,
            ItemCreationSystem.class, ItemInfoEncodingSystem.class, LoadCharacterSystem.class,
            MonsterBookSystem.class, PlayerLoggedInSystemHandler.class, QuestSystem.class,
            RingSystem.class, SkillSystem.class, TeleportRockSystem.class, PlayerMoveSystemHandler.class,

            /* Logging Systems */
            OpcodeLoggingSystem.class,

            /* Cleanup Systems */
            PlayerCleanupSystem.class);
    public final GameClientHandler handler;

    public GameServer(int port, GameClientHandler handler, int index) {
        super(ScratchConstants.GAME_SERVER_IP, port,
                new MasterServerConnector(ClientType.GAME_SERVER, new GameServerTrafficHandler(index)),
                handler, MaplePacketDecoder.class, MaplePacketEncoder.class);
        this.handler = handler;
    }

}
