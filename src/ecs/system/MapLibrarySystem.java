package ecs.system;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import ecs.EntityCreationSystem;
import ecs.components.Location;
import ecs.components.Name;
import ecs.components.life.MonsterStat;
import ecs.components.map.*;
import ecs.components.map.FootholdTree.Foothold;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.StringUtil;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MapLibrarySystem extends BaseSystem {

    java.util.Map<Integer, Integer> idToMap = new HashMap<>();
    java.util.Map<String, Integer> nameToId = new HashMap();
    ComponentMapper<Map> maps;
    ComponentMapper<FootholdTree> footholds;        ComponentMapper<Area> areas;
    ComponentMapper<MonsterRate> monsterRates;      ComponentMapper<NpcPool> npcPool;
    ComponentMapper<MonsterStat> monsterStats;      ComponentMapper<MonsterPool> monsterPool;
    ComponentMapper<Name> names;
    EntityCreationSystem ecs;                       ComponentMapper<Location> locations;
    LifeLibrarySystem lifeLibrarySystem;
    MapSystem mapSystem;

    private MapleDataProvider source;
    private MapleData nameData;
    private MapleDataProvider stringDataWZ;
    private MapleData mapStringData;

    // TODO: OnFirstUserEnter and OnUserEnter
    // TODO: Portals
    // TODO: TimeMob (area bosses?)

    public void generate(MapleDataProvider source, MapleDataProvider stringSource) {
        this.source = source;
        this.nameData = stringSource.getData("Map.img");
        stringDataWZ = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz"));
        mapStringData = stringDataWZ.getData("Map.img");
    }

    public Map getMap(int mapId) {
        return getProperty(Map.class, mapId);
    }

    private int generateMap(int mapId) {
        if (idToMap.containsKey(mapId)) return -1;
        if (mapId < 0) return -1;

        // Map Name
        String mapName = getMapName(mapId);
        MapleData mapData = source.getData(mapName);

        if (mapData == null) return -1;

        int mapEntityId = ecs.create();
        idToMap.put(mapId, mapEntityId);
        nameToId.put(mapName, mapId);

        // Linked Maps
        String link = MapleDataTool.getString(mapData.getChildByPath("info/link"), "");
        if (!link.equals("")) { //nexon made hundreds of dojo maps so to reduce the size they added links.
            mapName = getMapName(Integer.parseInt(link));
            mapData = source.getData(mapName);
        }

        // Monster Rate
        MonsterRate monsterRate = monsterRates.create(mapEntityId);
        MapleData mobRate = mapData.getChildByPath("info/mobRate");
        if (mobRate != null)
            monsterRate.rate = (float) mobRate.getData();

        Map map = maps.create(mapEntityId);
        map.returnedMap = MapleDataTool.getInt("info/returnMap", mapData);
        map.mapId = mapId;
        map.bgm = MapleDataTool.getString(mapData.getChildByPath("info/bgm"));
        map.fieldLimit = MapleDataTool.getInt(mapData.getChildByPath("info/fieldLimit"), 0);

//        String continent;
//        if (mapId < 100000000)
//        Name name = names.create(mapEntityId);
//        name.name = mapName;

        List<Foothold> allFootholds = new LinkedList<>();
        Point lBound = new Point(); 
        Point uBound = new Point();

        mapData.getChildByPath("foothold").forEach(footRoot -> footRoot.forEach(footCat -> footCat.forEach(foothold -> {
            int x1 = MapleDataTool.getInt(foothold.getChildByPath("x1"));
            int y1 = MapleDataTool.getInt(foothold.getChildByPath("y1"));
            int x2 = MapleDataTool.getInt(foothold.getChildByPath("x2"));
            int y2 = MapleDataTool.getInt(foothold.getChildByPath("y2"));
            Foothold fh = new Foothold(new Point(x1, y1), new Point(x2, y2),
                    Integer.parseInt(foothold.getName()));
            fh.setPrev(MapleDataTool.getInt(foothold.getChildByPath("prev")));
            fh.setNext(MapleDataTool.getInt(foothold.getChildByPath("next")));
            if (fh.getX1() < lBound.x)
                lBound.x = fh.getX1();
            if (fh.getX2() > uBound.x)
                uBound.x = fh.getX2();
            if (fh.getY1() < lBound.y)
                lBound.y = fh.getY1();
            if (fh.getY2() > uBound.y)
                uBound.y = fh.getY2();
            allFootholds.add(fh);
        })));

        FootholdTree fTree = footholds.create(mapEntityId);
        fTree.lBound = lBound;
        fTree.uBound = uBound;
        allFootholds.forEach(fTree::insert);

        if (mapData.getChildByPath("area") != null) {
            Area area = areas.create(mapEntityId);
            mapData.getChildByPath("a").forEach(area::add);
        }

        for (MapleData life : mapData.getChildByPath("life")) {
            String id = MapleDataTool.getString(life.getChildByPath("id"));
            String type = MapleDataTool.getString(life.getChildByPath("type"));
//            if (id.equals("9001105")) {
//                id = "9001108";//soz
//            }

            if (id == null) continue; // TODO: what happens if ID is null?? Ignore it atm. See consequences later.
            int myLife = loadLife(life, id, type);

            if (myLife == -1) continue;

            Location location = locations.get(myLife);
            location.mapId = mapId;

            boolean monster = monsterStats.get(myLife) != null;
            if (monster) {
                MonsterPool monsterPool = createIfNotExist(MonsterPool.class, mapEntityId);
                int mobTime = MapleDataTool.getInt("mobTime", life, 0);
                int team = MapleDataTool.getInt("team", life, -1);
                mapSystem.addSpawn(myLife, mobTime != -1);
//                if (mobTime == -1) { //does not respawn, force spawn once
//                map.spawnMonster(monster);
//                }
//                } else { TODO: monster respawning. Just spawn once atm.
//                    map.addMonsterSpawn(monster, mobTime, team);
//                }t
            } else {
                NpcPool npcPool = createIfNotExist(NpcPool.class, mapEntityId);
                npcPool.addNpcEntity(myLife);
//                MapleNPC npc = (MapleNPC) myLife;
//                map.addMapObject(myLife);
//                String limitedName = MapleDataTool.getString("limitedname", life, null);
//                if (limitedName != null) { // TODO: limited names?
//                    npc.setLimitedName(limitedName);
//
//                    //shouldnt these be the other way around?
//                    boolean disabled = true;
//                    for (String name : ServerConstants.LIMITED_NPCS) {
//                        if (name.equalsIgnoreCase(limitedName)) {
//                            disabled = false;
//                        }
//                    }
//                    if (disabled) {
//                        map.addDisabledNpc(npc.getId());
//                    } else {
//                        map.addMapObject(myLife);
//                    }
//                } else {
//                    map.addMapObject(myLife);
//                }
            }
//            } else {
//                map.addMapObject(myLife);
//            }
        }

        return mapEntityId;
    }

//    private String getContinent(int mapId) {
//        if (mapId < 100000000)
//            return "maple";
//        else if (mapId < 200000000)
//            return "victoria";
//        else if (mapId < 300000000)
//            return "ossyria";
//        else if (mapId < 400000000)
//            return "elin";
//        else
//    }

    private <T extends Component> T createIfNotExist(Class<T> type, int e) {
        ComponentMapper<T> componentMapper = world.getMapper(type);
        T component;
        if ((component = componentMapper.get(e)) == null)
            return componentMapper.create(e);
        return component;
    }

    private String getMapName(int mapid) {
        String mapName = StringUtil.getLeftPaddedStr(mapid + "", '0', 9);
        StringBuilder builder = new StringBuilder("Map/Map");
        int area = mapid / 100000000;
        builder.append(area);
        builder.append("/");
        builder.append(mapName);
        builder.append(".img");
        mapName = builder.toString();
        return mapName;
    }

    private int entity(int mapId) {
        try {
            return idToMap.get(mapId);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    private boolean exists(int mapId) {
        return idToMap.containsKey(mapId);
    }

    /**
     * Grabs associated component to the item. If item does not exist, return null.
     * @param property Component class type to grab
     * @param mapId ItemId
     * @param <T> Object of type Component
     * @return Returns Component of passed class property
     */
    public <T extends Component> T getProperty(Class<T> property, int mapId) {
        // If it doesn't exist first time around, try generating.
        if (!exists(mapId))
            generateMap(mapId);

        // If still doesn't exist, then it doesn't actually exist.
        if (!exists(mapId))
            return null;
        int entityId = entity(mapId);
        return world.getMapper(property).get(entityId);
    }

    public MonsterPool getMonsterPool(int mapId) {
        return getProperty(MonsterPool.class, mapId);
    }

    private int loadLife(MapleData life, String id, String type) {
        int e = lifeLibrarySystem.getLife(Integer.parseInt(id), type);

        Location location = locations.create(e);

        MapleData cy = life.getChildByPath("cy");
        if (cy != null) location.cy = MapleDataTool.getInt(cy);

        MapleData dF = life.getChildByPath("f");
        if (dF != null) location.f = MapleDataTool.getInt(dF);


        location.fh = MapleDataTool.getInt(life.getChildByPath("fh"));
        location.rx0 = MapleDataTool.getInt(life.getChildByPath("rx0"));
        location.rx1 = MapleDataTool.getInt(life.getChildByPath("rx1"));

        location.pos = new Point(MapleDataTool.getInt(life.getChildByPath("x")), MapleDataTool.getInt(life.getChildByPath("y")));
        location.hidden = MapleDataTool.getInt("hide", life, 0) == 1;

        return e;
    }

    @Override
    protected void processSystem() {

    }
}
