package ecs.system;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import ecs.EntityCreationSystem;
import ecs.components.Location;
import ecs.components.Name;
import ecs.components.life.*;
import ecs.components.life.Banish.BanishInfo;
import ecs.components.life.ElementalEffectiveness.Effectiveness;
import ecs.components.life.LoseItem.LostItem;
import org.dozer.DozerBeanMapper;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import provider.wz.MapleDataType;
import tools.Pair;
import tools.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LifeLibrarySystem extends BaseSystem {

    private MapleDataProvider data;
    private MapleDataProvider stringDataWZ;
    private static MapleData mobStringData;
    private static MapleData npcStringData;
    private Map<Integer, Integer> monsterIdToEntity = new HashMap();
    private Map<Integer, Integer> npcIdToEntity = new HashMap();

    EntityCreationSystem ecs;
    ComponentMapper<Location> locations;                            ComponentMapper<Level> levels;
    ComponentMapper<MonsterStat> monsterStats;                      ComponentMapper<OffensiveStat> offensiveStats;
    ComponentMapper<ConstantMonsterStat> constantMonsterStats;      ComponentMapper<DefensiveStat> defensiveStats;
    ComponentMapper<RemoveAfter> removeAfters;                      ComponentMapper<RemoveOnMiss> removeOnMisses;
    ComponentMapper<Boss> bosses;                                   ComponentMapper<Undead> undeads;
    ComponentMapper<ExplosiveReward> explosiveRewards;
    ComponentMapper<CP> cps;                                        ComponentMapper<PublicReward> publicRewards;
    ComponentMapper<CoolDamage> coolDamages;                        ComponentMapper<LoseItem> loseItems;
    ComponentMapper<Name> names;                                    ComponentMapper<SelfDestruction> selfDestructions;
    ComponentMapper<FirstAttack> firstAttacks;                      ComponentMapper<TagColor> tagColors;
    ComponentMapper<AnimationTime> animationTimes;                  ComponentMapper<Revive> revives;
    ComponentMapper<MonsterSkill> monsterSkills;                    ComponentMapper<ElementalEffectiveness> elementalEffectivenesses;
    ComponentMapper<Banish> banishes;                               ComponentMapper<AngerGauge> angerGauges;
    ComponentMapper<ChargeCounter> chargeCounters;                  ComponentMapper<ChatBalloon> chatBalloons;
    private DozerBeanMapper mapper = new DozerBeanMapper();

    public void generate() {
        data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Mob.wz"));;
        stringDataWZ = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz"));
        npcStringData = stringDataWZ.getData("Npc.img");;
        mobStringData = stringDataWZ.getData("Mob.img");
    }

    public int getLife(int id, String type) {
        if (type.equalsIgnoreCase("n")) {
            return getNPC(id);
        } else if (type.equalsIgnoreCase("m")) {
            return getMonster(id);
        } else {
            System.out.println("Unknown Life type: " + type);
            return -1;
        }
    }

    public String getMonsterName(int mid) {
        return getPropertyForLibraryMonster(Name.class, mid).name;
    }

    public int getMonster(int mid) {
        if (getPropertyForLibraryMonster(MonsterStat.class, mid) == null)
            return -1;
        int e = ecs.create();
        map(MonsterStat.class, e, mid);
        map(Exp.class, e, mid);
        map(ChargeCounter.class, e, mid);
        return e;
    }

    private <T extends Component> T getPropertyForLibraryMonster(Class<T> type, int mobId) {
        int entityId;
        if ((entityId = loadMonster(mobId)) == -1)
            return null;
        return world.getMapper(type).get(entityId);
    }

    public boolean isBoss(int mobId) {
        return getPropertyForLibraryMonster(Boss.class, mobId) != null;
    }

    private <T extends Component> void map(Class<T> type, int entity, int mid) {
        Integer entityId = monsterIdToEntity.get(mid);
        // If null, try loading
        if (entityId == null)
            entityId = loadMonster(mid);
        // If still null, get outta here
        if (entityId == null)
            return;
        ComponentMapper<T> componentMapper = world.getMapper(type);
        T libraryComponent = componentMapper.get(entityId);
        if (libraryComponent == null) return;
        T component = componentMapper.create(entity);
        mapper.map(libraryComponent, component);
    }

    public <T extends Component> T getMonsterComponent(Class<T> type, int entityId) {
        T component;
        MonsterStat monsterStat;
        if ((monsterStat = world.getMapper(MonsterStat.class).get(entityId)) == null)
            return null;
        if ((component = world.getMapper(type).get(entityId)) != null)
            return component;
        return getPropertyForLibraryMonster(type, monsterStat.mobId);
    }

    public int getNPC(int nid) {
        if (loadNPC(nid) == -1)
            return -1;
        return ecs.create();
    }

    private Integer loadNPC(int nid) {
        if (npcIdToEntity.containsKey(nid)) return npcIdToEntity.get(nid);

        String name = MapleDataTool.getString(nid + "/name", npcStringData, null);
        if (name == null) {
            name = "NO-NAME";
        }
        int e = ecs.create();
        Name npcName = names.create(e);
        npcName.name = name;
        return e;
    }

    private Integer loadMonster(int mid) {
        Integer entityId = monsterIdToEntity.get(mid);
        if (entityId == null) {
            MapleData monsterData = data.getData(StringUtil.getLeftPaddedStr(mid + ".img", '0', 11));
            if (monsterData == null)
                return -1;
            MapleData monsterInfoData = monsterData.getChildByPath("info");
            int e = ecs.create();
            MonsterStat monsterStat = monsterStats.create(e);
            ConstantMonsterStat constantMonsterStat = constantMonsterStats.create(e);
            DefensiveStat defensiveStat = defensiveStats.create(e);
            OffensiveStat offensiveStat = offensiveStats.create(e);
            Level level = levels.create(e);

            monsterStat.hp = MapleDataTool.getIntConvert("maxHP", monsterInfoData);
            monsterStat.friendly = MapleDataTool.getIntConvert("damagedByMob", monsterInfoData, 0) == 1;

            offensiveStat.PAD = MapleDataTool.getIntConvert("PADamage", monsterInfoData);
            defensiveStat.PDD = MapleDataTool.getIntConvert("PDDamage", monsterInfoData);
            offensiveStat.MAD = MapleDataTool.getIntConvert("MADamage", monsterInfoData);
            defensiveStat.MDD = MapleDataTool.getIntConvert("MDDamage", monsterInfoData);

            monsterStat.mp = MapleDataTool.getIntConvert("maxMP", monsterInfoData, 0);
            monsterStat.exp = MapleDataTool.getIntConvert("exp", monsterInfoData, 0);
            monsterStat.mobId = mid;

            level.level = (byte) MapleDataTool.getIntConvert("level", monsterInfoData);
            int removeAfter = MapleDataTool.getIntConvert("removeAfter", monsterInfoData, 0);
            if (removeAfter != 0)
                removeAfters.create(e);

            if (MapleDataTool.getIntConvert("boss", monsterInfoData, 0) > 0)
                bosses.create(e);

            if (MapleDataTool.getIntConvert("explosiveReward", monsterInfoData, 0) > 0)
                explosiveRewards.create(e);

            if (MapleDataTool.getIntConvert("publicReward", monsterInfoData, 0) > 0)
                publicRewards.create(e);

            if (MapleDataTool.getIntConvert("undead", monsterInfoData, 0) > 0)
                undeads.create(e);

            Name name = names.create(e);
            name.name = MapleDataTool.getString(mid + "/name", mobStringData, "MISSINGNO");

            constantMonsterStat.buffToGive = MapleDataTool.getIntConvert("buff", monsterInfoData, -1);

            int cpPoint = MapleDataTool.getIntConvert("getCP", monsterInfoData, 0);
            if (cpPoint > 0)
                cps.create(e);

            if (MapleDataTool.getIntConvert("removeOnMiss", monsterInfoData, 0) > 0)
                removeOnMisses.create(e);

            offensiveStat.acc = MapleDataTool.getIntConvert("acc", monsterInfoData, 0);
            constantMonsterStat.speed = MapleDataTool.getIntConvert("speed", monsterInfoData, 0);

            MapleData special = monsterInfoData.getChildByPath("coolDamage");
            if (special != null) {
                CoolDamage coolDamage = coolDamages.create(e);
                coolDamage.damage = MapleDataTool.getIntConvert("coolDamage", monsterInfoData);
                coolDamage.probability = MapleDataTool.getIntConvert("coolDamageProb", monsterInfoData, 0);
            }

            special = monsterInfoData.getChildByPath("loseItem");
            if (special != null) {
                LoseItem loseItem = loseItems.create(e);
                special.getChildren().forEach(data ->
                    loseItem.items.add(new LostItem(MapleDataTool.getInt(data.getChildByPath("id")),
                                        (byte) MapleDataTool.getInt(data.getChildByPath("prop")),
                                        (byte) MapleDataTool.getInt(data.getChildByPath("x")))));
            }

            special = monsterInfoData.getChildByPath("selfDestruction");
            if (special != null) {
                SelfDestruction selfDestruction = selfDestructions.create(e);
                selfDestruction.action = (byte) MapleDataTool.getInt(special.getChildByPath("action"));
                selfDestruction.removeAfter = MapleDataTool.getIntConvert("removeAfter", special, -1);
                selfDestruction.hp = MapleDataTool.getIntConvert("hp", special, -1);
            }

            MapleData firstAttackData = monsterInfoData.getChildByPath("firstAttack");
            int firstAttack = 0;
            if (firstAttackData != null) {
                if (firstAttackData.getType() == MapleDataType.FLOAT)
                    firstAttack = Math.round(MapleDataTool.getFloat(firstAttackData));
                else
                    firstAttack = MapleDataTool.getInt(firstAttackData);
            }

            if (firstAttack > 0)
                firstAttacks.create(e);


            constantMonsterStat.dropPeriod = MapleDataTool.getIntConvert("dropItemPeriod", monsterInfoData, 0) * 10000;

            TagColor tagColor = tagColors.create(e);
            tagColor.hpTagColor = MapleDataTool.getIntConvert("hpTagColor", monsterInfoData, 0);
            tagColor.hpTagBgColor = MapleDataTool.getIntConvert("hpTagBgcolor", monsterInfoData, 0);

            AnimationTime animationTime = animationTimes.create(e);

            for (MapleData idata : monsterData) {
                if (!idata.getName().equals("info")) {
                    int delay = 0;
                    for (MapleData pic : idata.getChildren()) {
                        delay += MapleDataTool.getIntConvert("delay", pic, 0);
                    }
                    animationTime.animationTimes.put(idata.getName(), delay);
                }
            }

            MapleData reviveInfo = monsterInfoData.getChildByPath("revive");
            if (reviveInfo != null) {
                Revive revive = revives.create(e);
                reviveInfo.forEach(data -> revive.revives.add(MapleDataTool.getInt(data)));
            }

            String elemAttr = MapleDataTool.getString("elemAttr", monsterInfoData, "");
            if (elemAttr.length() > 0) {
                ElementalEffectiveness elementalEffectiveness = elementalEffectivenesses.create(e);
                for (int i = 0; i < elemAttr.length(); i += 2) {
                    elementalEffectiveness.resistance.put(Element.getFromChar(elemAttr.charAt(i)), Effectiveness.getByNumber(Integer.parseInt(elemAttr.charAt(i + 1) + "")));
                }
            }

            MapleData monsterSkillData = monsterInfoData.getChildByPath("skill");
            if (monsterSkillData != null) {
                int i = 0;
                MonsterSkill monsterSkill = monsterSkills.create(e);
                while (monsterSkillData.getChildByPath(Integer.toString(i)) != null) {
                    monsterSkill.skills.add(new Pair<>(MapleDataTool.getInt(i + "/skill", monsterSkillData, 0),
                            MapleDataTool.getInt(i + "/level", monsterSkillData, 0)));
                    i++;
                }
            }

            MapleData banishData = monsterInfoData.getChildByPath("ban");
            if (banishData != null) {
                Banish banish = banishes.create(e);
                if (banishData.getChildByPath("banType") != null) {
                    banish.banishInfo = new BanishInfo(MapleDataTool.getInt("banType", banishData), MapleDataTool.getString("banMsg", banishData),
                            MapleDataTool.getInt("banMap/0/field", banishData, -1),
                            MapleDataTool.getString("banMap/0/portal", banishData, "sp"));
                } else {
                    banish.banishInfo = new BanishInfo(MapleDataTool.getString("banMsg", banishData),
                            MapleDataTool.getInt("banMap/0/field", banishData, -1),
                            MapleDataTool.getString("banMap/0/portal", banishData, "sp"));
                }
            }
            MapleData chargeCount = monsterInfoData.getChildByPath("ChargeCount");
            if (chargeCount != null) {
                ChargeCounter chargeCounter = chargeCounters.create(e);
                chargeCounter.counter = MapleDataTool.getInt(chargeCount, 0);
            }

            MapleData angerGauge = monsterInfoData.getChildByPath("AngerGauge");
            if (angerGauge != null && MapleDataTool.getInt(angerGauge, 0) > 0) {
                AngerGauge angerGauger = angerGauges.create(e);
            }

            MapleData speak = monsterInfoData.getChildByPath("speak");
            if (speak != null) {
                ChatBalloon chatBalloon = chatBalloons.create(e);
                chatBalloon.balloon = MapleDataTool.getInt("chatBalloon", speak, 0);
            }

            monsterIdToEntity.put(mid, e);
            entityId = e;
        }
        return entityId == null ? -1 : entityId;
    }

    @Override
    protected void processSystem() {

    }
}
