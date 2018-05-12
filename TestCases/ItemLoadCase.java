import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.components.item.Equip;
import ecs.components.library.EquipStatRequirement;
import ecs.components.library.EquipStaticProperties;
import ecs.system.ItemLibrarySystem;
import org.dozer.DozerBeanMapper;
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

        assertEquals(library.getIdByName("Sword"), 1302000);
        assertEquals(library.getIdByName("Reverse Bardiche"), 1312038);
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
    public void stats() throws CloneNotSupportedException {
        EquipStaticProperties swordStaticProps = library.getEquipStaticProperties(1302000);
        EquipStaticProperties bardicheStaticProps = library.getEquipStaticProperties(1312038);
        assertNotNull(swordStaticProps);
        assertNotNull(bardicheStaticProps);
        assertNull(library.getEquipStaticProperties(4001126));
        assertNull(library.getEquipStaticProperties(1));
        assertEquals(library.getName(1), "NO-NAME");

        Equip sword = library.getEquip(1302000);
        Equip bardiche = library.getEquip(1312038);

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.map(bardiche, sword);

        int pad = sword.properties.get("PAD");
        assertEquals(pad, 108);

        // Did it really change in the mapper
        sword = library.getEquip(1302000);
        pad = sword.properties.get("PAD");
        assertEquals(pad, 108);

        sword.properties.put("PAD", (short) 1);
        pad = bardiche.properties.get("PAD");
        assertEquals(pad, 108);

        // Map of properties was successfully copied
        pad = sword.properties.get("PAD");
        assertEquals(pad, 1);

        assertFalse(bardicheStaticProps.cash);
        assertFalse(swordStaticProps.cash);
    }

    @Test
    public void slotMax() {
        assertEquals(library.getSlotMax(1302000), 1);
        assertEquals(library.getSlotMax(1312038), 1);
        assertEquals(library.getSlotMax(1), 1);
        assertEquals(library.getSlotMax(4001126), 1000);
        assertEquals(library.getSlotMax(2070006), 800);
    }

    @Test
    public void canLevel() {
        assertFalse(library.equipCanLevel(1302000));
        assertTrue(library.equipCanLevel(1312038));

        // Fake item
        assertFalse(library.equipCanLevel(1000000));

    }

}
