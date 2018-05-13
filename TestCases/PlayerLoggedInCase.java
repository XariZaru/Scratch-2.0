import ecs.components.item.CharacterInventory;
import ecs.components.item.Inventory;
import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.system.InventorySystem;
import ecs.system.ItemCreationSystem;
import ecs.system.ItemInfoEncodingSystem;
import net.systems.ClientHandshakeSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import systems.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerLoggedInCase {

    WorldManager manager;
    EntityCreationSystem entityCreationSystem;

    @BeforeAll
    public static void init() {
        System.setProperty("wzpath", "/Users/jonnguyen/Documents/Repositories/Scratch 2.0/wz");
    }

    @BeforeEach
    public void setup() {
        manager = new WorldManager(
                EntityCreationSystem.class, ClientHandshakeSystem.class,
                ServerIdentifier.class, CooldownSystem.class, InventorySystem.class,
                CharacterInfoEncodingSystem.class, ItemCreationSystem.class, ItemInfoEncodingSystem.class,
                LoadCharacterSystem.class, MonsterBookSystem.class, PlayerLoggedInSystemHandler.class,
                QuestSystem.class, RingSystem.class, SkillSystem.class, TeleportRockSystem.class);

        Thread thread = new Thread(manager);
        thread.start();

        while (!manager.started());
        entityCreationSystem = manager.getSystem(EntityCreationSystem.class);

    }

    @Test
    public void login() {
        LoadCharacterSystem loadCharacterSystem = manager.getSystem(LoadCharacterSystem.class);
        InventorySystem inventorySystem = manager.getSystem(InventorySystem.class);
        PlayerLoggedInSystemHandler playerLoggedInSystemHandler = manager.getSystem(PlayerLoggedInSystemHandler.class);

        int e = entityCreationSystem.create();
        int dbId = 30020;
        if (loadCharacterSystem.retrieve(dbId, e)) {
            inventorySystem.retrieve(e);
            playerLoggedInSystemHandler.getCharInfo(e);

            CharacterInventory characterInventory = manager.getComponent(CharacterInventory.class, e);
            Inventory equipped = manager.getComponent(Inventory.class, characterInventory.inventories[CharacterInventory.Type.EQUIPPED.getType()]);
            assertEquals(equipped.itemEntityIDs.length, 24);
        }
    }

}
