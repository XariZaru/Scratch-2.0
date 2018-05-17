import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.components.life.ConstantMonsterStat;
import ecs.components.life.DefensiveStat;
import ecs.components.life.MonsterStat;
import ecs.components.life.OffensiveStat;
import ecs.system.LifeLibrarySystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LifeLoadCase {

    WorldManager manager;
    LifeLibrarySystem library;

    @BeforeAll
    public static void init() {
        System.setProperty("wzpath", "/Users/jonnguyen/Documents/Repositories/Scratch 2.0/wz");
    }

    @BeforeEach
    public void setup() {
        manager = new WorldManager(EntityCreationSystem.class, LifeLibrarySystem.class);

        Thread thread = new Thread(manager);
        thread.start();

        while (!manager.started());

        library = manager.getSystem(LifeLibrarySystem.class);
        library.generate();
    }

    @Test
    public void loadMonster() {
        int e = library.getMonster(100100);
        MonsterStat monsterStat = manager.getComponent(MonsterStat.class, e);
        ConstantMonsterStat constantMonsterStat = library.getMonsterComponent(ConstantMonsterStat.class, e);
        OffensiveStat offensiveStat = library.getMonsterComponent(OffensiveStat.class, e);
        DefensiveStat defensiveStat = library.getMonsterComponent(DefensiveStat.class, e);
        assertEquals(defensiveStat.PDD, 0);
        assertEquals(defensiveStat.MDD, 0);
        assertEquals(offensiveStat.PAD, 12);
        assertEquals(offensiveStat.acc, 20);
        assertEquals(constantMonsterStat.speed, -65);
        assertEquals(monsterStat.hp, 8);
        assertEquals(monsterStat.exp, 3);
        assertFalse(library.isBoss(100100));

        e = library.getMonster(6130101);
        monsterStat = manager.getComponent(MonsterStat.class, e);
        assertEquals(monsterStat.hp, 20000);
        assertTrue(library.isBoss(6130101));
    }

}

