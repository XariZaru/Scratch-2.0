import components.library.EquipStatRequirement;
import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.system.ItemLibrarySystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemLoadCase {

    WorldManager manager;
    ItemLibrarySystem library;

    @BeforeAll
    public static void init() {
        System.setProperty("wzpath", "/Users/jonnguyen/Documents/Repositories/Scratch 2.0/wz");
    }

    @BeforeEach
    public void setup() {
        manager = new WorldManager(EntityCreationSystem.class, ItemLibrarySystem.class);

        Thread thread = new Thread(manager);
        thread.start();

        while (!manager.started());

        library = manager.getSystem(ItemLibrarySystem.class);
        library.generate();
    }

    @Test
    public void names() {
        assertEquals(library.getName(1302000), "Sword");
        assertEquals(library.getName(1312038), "Reverse Bardiche");
    }

    @Test
    public void requirements() {
        assertNull(library.getEquipRequirements(1302000));
        assertNotNull(library.getEquipRequirements(1312038));
        assertNull(library.getEquipRequirements(4001126));

        EquipStatRequirement requirement = library.getEquipRequirements(1312038);
        assertEquals(requirement.level, 120);
    }

    @Test
    public void stats() {
        assertNotNull(library.getEquipStats(1302000));
        assertNull(library.getEquipStats(4001126));
        assertNull(library.getEquipStats(1));
        assertEquals(library.getName(1), "NO-NAME");
    }

    @Test
    public void slotMax() {
        assertEquals(library.getSlotMax(1302000), 1);
        assertEquals(library.getSlotMax(1312038), 1);
        assertEquals(library.getSlotMax(1), 1);
        assertEquals(library.getSlotMax(4001126), 1000);
        assertEquals(library.getSlotMax(2070006), 800);
    }

}
