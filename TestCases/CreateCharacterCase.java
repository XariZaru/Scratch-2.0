import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.components.Client;
import ecs.components.DatabaseId;
import ecs.system.ItemCreationSystem;
import ecs.system.ItemLibrarySystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import systems.CreateCharSystemHandler;
import tools.Pair;

public class CreateCharacterCase {
    WorldManager manager;
    ItemLibrarySystem library;

    @BeforeAll
    public static void init() {
        System.setProperty("wzpath", "/Users/jonnguyen/Documents/Repositories/Scratch 2.0/wz");
    }

    @BeforeEach
    public void setup() {
        manager = new WorldManager(EntityCreationSystem.class, ItemCreationSystem.class, ItemLibrarySystem.class, CreateCharSystemHandler.class);

        Thread thread = new Thread(manager);
        thread.start();

        while (!manager.started());

        library = manager.getSystem(ItemLibrarySystem.class);
        library.generate();
    }

    @Test
    public void insertItems() {
        Pair<Integer, Integer> items[] =  new Pair[] {
                /* top */   new Pair(5, 1040006), new Pair(6, 1060002), /* bottom */
                /* shoes */ new Pair(7, 1072038), new Pair(11, 1322005)}; // weapon
        Client client = new Client();
        client.accountId = 1;

        DatabaseId dbId = new DatabaseId();
        dbId.dbId = 30021;
        manager.getSystem(CreateCharSystemHandler.class).insertItems(items, dbId, client);

    }

}
