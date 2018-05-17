package ecs.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import ecs.components.Location;
import ecs.components.life.AddRespawn;
import ecs.components.life.MonsterStat;
import ecs.components.life.Respawn;
import ecs.components.map.FootholdTree;
import ecs.components.map.Map;
import ecs.components.map.MonsterPool;

import java.awt.*;

public class MapSystem extends IntervalIteratingSystem {

    ComponentMapper<Location> locations;    ComponentMapper<Map> maps;
    ComponentMapper<Respawn> respawns;      ComponentMapper<AddRespawn> addRespawns;
    MapLibrarySystem mapLibrarySystem;      LifeLibrarySystem lifeLibrarySystem;

    public MapSystem() {
        super(Aspect.all(Respawn.class), 500);
    }

    @Override
    protected void process(int entityId) {
        Respawn respawn = respawns.get(entityId);
        Location location = locations.get(entityId);
        Map map = getMap(location.mapId);
        MonsterPool monsterPool = mapLibrarySystem.getProperty(MonsterPool.class, map.mapId);

        if (map.respawns) {
            try {
                if (respawn.infinite)
                    addRespawns.create(entityId);
                Point newPos = calcPointBelow(location.pos, map.mapId);
                if (newPos == null) {
                    newPos = new Point(location.pos.x, location.pos.y - 1);
                } else {
                    newPos.y -= 1;
                }
                location.pos = newPos;
                monsterPool.addMobEntity(entityId);

                MonsterStat monsterStat = world.getMapper(MonsterStat.class).get(entityId);
//                System.out.println("Monster " + lifeLibrarySystem.getMonsterName(monsterStat.mobId)
//                        + " at " + location.pos + " - " + location.mapId + ".");
            } finally {
                respawns.remove(entityId);
            }
        }
    }

    public void addSpawn(int e, boolean infinite) {
        Respawn respawn = respawns.create(e);
        respawn.infinite = infinite;
    }

    public void addPlayer(int playerEntityId) {

    }

    public Map getMap(int mapId) {
        return mapLibrarySystem.getMap(mapId);
    }

    private Point calcPointBelow(Point initial, int mapId) {
        FootholdTree footholdTree = mapLibrarySystem.getProperty(FootholdTree.class, mapId);
        FootholdTree.Foothold fh = footholdTree.findBelow(initial);
        if (fh == null)
            return null;

        int dropY = fh.getY1();
        if (!fh.isWall() && fh.getY1() != fh.getY2()) {
            double s1 = Math.abs(fh.getY2() - fh.getY1());
            double s2 = Math.abs(fh.getX2() - fh.getX1());
            double s5 = Math.cos(Math.atan(s2 / s1))
                    * (Math.abs(initial.x - fh.getX1()) / Math.cos(Math.atan(s1 / s2)));
            if (fh.getY2() < fh.getY1()) {
                dropY = fh.getY1() - (int) s5;
            } else {
                dropY = fh.getY1() + (int) s5;
            }
        }
        return new Point(initial.x, dropY);
    }

}
