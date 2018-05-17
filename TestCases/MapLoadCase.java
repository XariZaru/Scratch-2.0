import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.system.LifeLibrarySystem;
import ecs.system.MapLibrarySystem;
import ecs.system.MapSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import provider.MapleDataProviderFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapLoadCase {

    WorldManager manager;
    MapLibrarySystem library;

    @BeforeAll
    public static void init() {
        System.setProperty("wzpath", "/Users/jonnguyen/Documents/Repositories/Scratch 2.0/wz");
    }

    @BeforeEach
    public void setup() {
        manager = new WorldManager(EntityCreationSystem.class, LifeLibrarySystem.class, MapSystem.class, MapLibrarySystem.class);

        Thread thread = new Thread(manager);
        thread.start();

        while (!manager.started());

        library = manager.getSystem(MapLibrarySystem.class);
        library.generate(MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Map.wz")),
                         MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz")));

        manager.getSystem(LifeLibrarySystem.class).generate();
    }

    @Test
    public void load() {
        // Real Map
        assertNotNull(library.getMap(100000000));

        // Fake Map
        assertNull(library.getMap(1013013));
        assertNotNull(library.getMap(100000000));

        assertNotNull(library.getMap(104040000));

        while (true);
    }

}
