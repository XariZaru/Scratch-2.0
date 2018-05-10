/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import components.Name;
import components.item.library.EquipStat;
import components.item.library.EquipStatRequirement;
import ecs.EntityCreationSystem;
import ecs.WorldManager;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataProvider;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Matze
 *
 */
public class ItemLibrarySystem extends BaseSystem {

    private HashMap<Integer, Integer> items = new HashMap<>();
    protected MapleDataProvider itemData;
    protected MapleDataProvider equipData;
    protected MapleDataProvider stringData;
    protected MapleDataProvider etcData;
    protected MapleData cashStringData;
    protected MapleData consumeStringData;
    protected MapleData eqpStringData;
    protected MapleData etcStringData;
    protected MapleData insStringData;
    protected MapleData petStringData;

    EntityCreationSystem ecs;
    ComponentMapper<Name> names;
    ComponentMapper<EquipStat> equipStats;
    ComponentMapper<EquipStatRequirement> equipRequirements;

//    protected Map<Integer, Short> slotMaxCache = new HashMap<>();
//    protected Map<Integer, String> uiDataCache = new HashMap<>();
//    protected Map<Integer, MapleStatEffect> itemEffects = new HashMap<>();
//    protected Map<Integer, String> fireworkEffects = new HashMap<>();
//    protected Map<Integer, Map<String, Integer>> equipStatsCache = new HashMap<>();
//    protected Map<Integer, Equip> equipCache = new HashMap<>();
//    protected Map<Integer, Double> priceCache = new HashMap<>();
//    protected Map<Integer, Integer> wholePriceCache = new HashMap<>();
//    protected Map<Integer, Integer> projectileWatkCache = new HashMap<>();
//    protected Map<Integer, Integer> itemLevelCache = new HashMap<>();
////    protected Map<Integer, String> nameCache = new HashMap<>(); //no longer need it
//    protected Map<Integer, String> descCache = new HashMap<>();
//    protected Map<Integer, String> msgCache = new HashMap<>();
//    protected Map<Integer, Boolean> dropRestrictionCache = new HashMap<>();
//    protected Map<Integer, Boolean> pickupRestrictionCache = new HashMap<>();
//    protected Map<Integer, Boolean> pickUpBlock = new HashMap<>();
//    protected Map<Integer, Boolean> partyQuestItem = new HashMap<>();
//    protected Map<Integer, Integer> getMesoCache = new HashMap<>();
//    protected Map<Integer, Integer> monsterBookID = new HashMap<>();
//    protected Map<Integer, Boolean> onEquipUntradableCache = new HashMap<>();
//    protected Map<Integer, ScriptedItem> scriptedItemCache = new HashMap<>();
//    protected Set<Integer> aesthetics = new HashSet<>();
//    protected Map<Integer, Boolean> onlyPickupCache = new HashMap<>();
//    protected Map<Integer, Boolean> karmaCache = new HashMap<>();
//    protected Map<Integer, Integer> triggerItemCache = new HashMap<>();
//    protected Map<Integer, Integer> expCache = new HashMap<>();
//    protected Map<Integer, Integer> levelCache = new HashMap<>();
//    protected Map<Integer, Set<Integer>> itemDropCache = new HashMap<>(); // Item to Monster ID for quick searching of items
//    protected Map<Integer, Pair<Integer, List<RewardItem>>> rewardCache = new HashMap<>();
//    protected List<Pair<Integer, String>> itemNameCache = new ArrayList<>();
//    protected LinkedHashMap<Integer, String> itemIdAndName = new LinkedHashMap<>();
//    protected Map<Integer, Boolean> consumeOnPickupCache = new HashMap<>();
//    protected Map<Integer, Boolean> isQuestItemCache = new HashMap<>();
//	protected Map<Integer, Boolean> expireOnLogout = new HashMap<>();
//    protected Map<Integer, String> equipmentSlotCache = new HashMap<>();
//	protected Map<Integer, Integer> summonBagTypeCache = new HashMap<>();
//	protected Map<Integer, Pair<Integer, String>> replaceOnExpireCache = new HashMap<>();
//	protected Map<Integer, Map<Integer, Integer>> updateQuestCache = new HashMap<>(); // Items that when dragged into UI window increase infoex
//	protected List<Integer> itemExpQuestCache = new ArrayList<>(); // Items that are part of quests that require player to grind for exp
//	protected Map<Integer, Integer> questIdCache = new HashMap<>();
//	protected Map<Integer, Integer> chatBubbleCache = new HashMap<>();
//	protected Map<Integer, Integer> nameTagCache = new HashMap<>();
//    protected Map<Integer, HashMap<Integer, LevelUpInformation>> levelUpCache = new HashMap<>();
//    protected Map<Integer, List<PetEvolutionInformation>> evolutionCache = new HashMap<>();
//    protected Map<Integer, List<Integer>> skillsOnItem = new HashMap<>();
//    protected Map<Integer, Boolean> bigItem = new HashMap<>();
//    protected Map<Integer, Integer> chairHPRecovery = new HashMap<>();
//    protected Map<Integer, Integer> chairMPRecovery = new HashMap<>();
//    protected Map<Integer, Float> equipRecovery = new HashMap<>();
//    protected Map<Integer, Float> vegaSpell = new HashMap<>();

	public ItemLibrarySystem() {
//		loadCardIdData();
        itemData = provider.MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Item.wz"));
        equipData = provider.MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Character.wz"));
        stringData = provider.MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz"));
        etcData = provider.MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Etc.wz"));
        cashStringData = stringData.getData("Cash.img");
        consumeStringData = stringData.getData("Consume.img");
        eqpStringData = stringData.getData("Eqp.img");
        etcStringData = stringData.getData("Etc.img");
        insStringData = stringData.getData("Ins.img");
        petStringData = stringData.getData("Pet.img");
    }

    @Override
    protected void processSystem() {

    }

    public void loadAesthetics() {
//    	MapleDataDirectoryEntry root = equipData.getRoot();
//        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
//        	if (topDir.getName().equals("Hair") || topDir.getName().equals("Face")) {
//	            for (MapleDataFileEntry iFile : topDir.getFiles()) {
//	            	String cosmetic = iFile.getName().split(".img")[0];
//	            	if (cosmetic != null) {
//	            		aesthetics.add(Integer.parseInt(cosmetic));
//	            	}
//	            }
//        	}
//        }
//        for (MapleDataFileEntry iFile : root.getFiles()) {
//        	String cosmetic = iFile.getName().split(".img")[0];
//        	if (cosmetic != null) {
//        		int skin = Integer.parseInt(cosmetic) % 100;
//        		if (!aesthetics.contains(skin)) {
//        			aesthetics.add(skin);
//        		}
//        	}
//        }
    }
    
//    public boolean aestheticExists(int aestheticId) {
////    	if (aesthetics == null) {
////    		return false;
////    	}
////    	return aesthetics.contains(aestheticId);
//    }

//    public MapleInventoryType getInventoryType(int itemId) {
//        final byte type = (byte) (itemId / 1000000);
//        if (type < 1 || type > 5) {
//            return MapleInventoryType.UNDEFINED;
//        }
//        return MapleInventoryType.getByType(type);
////        if (inventoryTypeCache.containsKey(itemId)) {
////            return inventoryTypeCache.get(itemId);
////        }
////        MapleInventoryType ret;
////        String idStr = "0" + String.valueOf(itemId);
////        MapleDataDirectoryEntry root = itemData.getRoot();
////        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
////            for (MapleDataFileEntry iFile : topDir.getFiles()) {
////                if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
////                    ret = MapleInventoryType.getByWZName(topDir.getName());
////                    inventoryTypeCache.put(itemId, ret);
////                    return ret;
////                } else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
////                    ret = MapleInventoryType.getByWZName(topDir.getName());
////                    inventoryTypeCache.put(itemId, ret);
////                    return ret;
////                }
////            }
////        }
////        root = equipData.getRoot();
////        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
////            for (MapleDataFileEntry iFile : topDir.getFiles()) {
////                if (iFile.getName().equals(idStr + ".img")) {
////                    ret = MapleInventoryType.EQUIP;
////                    inventoryTypeCache.put(itemId, ret);
////                    return ret;
////                }
////            }
////        }
////        ret = MapleInventoryType.UNDEFINED;
////        inventoryTypeCache.put(itemId, ret);
////        return ret;
//    }
//
//    public int getQuestIdFromItem(int itemId) {
//    	if (questIdCache.containsKey(itemId)) {
//            return questIdCache.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        int questId = MapleDataTool.getIntConvert("info/questId", data, 0);
//        questIdCache.put(itemId, questId);
//        if (data.getChildByPath("info/consumeItem") == null && !itemExpQuestCache.contains(questId)) {
//        	itemExpQuestCache.add(questId);
//        }
//        return questId;
//    }
    
    public void reloadDropInformation() {
//    	itemDropCache.clear();
    }
    
//    public Map<Integer, Set<Integer>> getItemDrops() {
//    	if (itemDropCache == null)
//    		itemDropCache = new HashMap<Integer, Set<Integer>>();
//    	return itemDropCache;
//    }
//
//    public int getChatBubble(int itemId) {
//    	if (chatBubbleCache.containsKey(itemId)) {
//            return chatBubbleCache.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        int chatBubble = MapleDataTool.getIntConvert("info/chatBalloon", data, 0);
//        chatBubbleCache.put(itemId, chatBubble);
//        return chatBubble;
//    }
//
//    public int getNameTag(int itemId) {
//    	if (nameTagCache.containsKey(itemId)) {
//            return nameTagCache.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        int nameTag = MapleDataTool.getIntConvert("info/nameTagCache", data, 0);
//        chatBubbleCache.put(itemId, nameTag);
//        return nameTag;
//    }
    
    public void loadVega() {
//        MapleData data = etcData.getData("VegaSpell.img");
//        int itemID = 0;
//        float prob = 0;
//        for (MapleData child : data) {
//        	itemID = MapleDataTool.getIntConvert("item", child, 0);
//        	String sProb = MapleDataTool.getString("prob", child, "");
//        	sProb = sProb.replace("[R8]", "");
//        	prob = Float.parseFloat(sProb);
//        	vegaSpell.put(itemID, prob);
//        }
    }
//
//    public float getVegaProbability(int itemId) {
//    	return vegaSpell.get(itemId);
//    }
//
//    public boolean isValidVegaSpell(int itemId) {
//        return vegaSpell.containsKey(itemId);
//    }
    
//    public int geChairHPRecovery(int itemId) {
//    	if (chairHPRecovery.containsKey(itemId)) {
//            return chairHPRecovery.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        int hp = MapleDataTool.getIntConvert("info/recoveryHP", data, 0);
//        chairHPRecovery.put(itemId, hp);
//        return hp;
//    }
//
//    public int geChairMPRecovery(int itemId) {
//    	if (chairMPRecovery.containsKey(itemId)) {
//            return chairMPRecovery.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        int mp = MapleDataTool.getIntConvert("info/recoveryMP", data, 0);
//        chairMPRecovery.put(itemId, mp);
//        return mp;
//    }
//
//    public boolean isExpItemQuest(int questid) {
//    	return itemExpQuestCache.contains(questid);
//    }
//
//
//    public int getRequiredLevel(int itemId) {
//    	if (itemLevelCache.containsKey(itemId)) {
//    		return itemLevelCache.get(itemId);
//    	}
//        MapleData item = getItemData(itemId);
//        if (item == null) {
//            return -1;
//        }
//        MapleData data = item.getChildByPath("info/reqLevel");
//        Integer level = MapleDataTool.getInt(data);
//        itemLevelCache.put(itemId, level);
//        return level;
//    }
//
//    public int getReqLevel(int itemId) {
//        return getEquipStats(itemId).get("reqLevel");
//    }
//
//    public int getETCMonsLvl(int itemid) {
//        MapleData itemdata = getItemData(itemid);
//        if (itemdata != null) {
//            MapleData lvl = itemdata.getChildByPath("info/lv");
//            if (lvl == null)
//                return -1;
//            return MapleDataTool.getInt(lvl);
//        }
//        return -1;
//    }
//

    public static void main(String[] args) {

	    System.setProperty("wzpath", "/Users/jonnguyen/Documents/Repositories/Scratch 2.0/wz");

        WorldManager worldManager = new WorldManager(
                EntityCreationSystem.class, ItemLibrarySystem.class
        );

        Thread thread = new Thread(worldManager);
        thread.start();

        while (!worldManager.started());

        ItemLibrarySystem miip = worldManager.getSystem(ItemLibrarySystem.class);
        miip.generate();

        int item = 1312038;
        miip.generateEquip(item);

        int entityId = miip.entity(item);
        EquipStat stat = miip.equipStats.get(entityId);
        EquipStatRequirement req = miip.equipRequirements.get(entityId);

        System.out.println("Name for 1302000 " + miip.names.get(miip.items.get(1302000)).name);

    }

    /**
     * Method needs to be called if you wish to generate item information. Creates respective entity IDs and assigns names components
     * along with putting entities into a HashMap assigned with ItemId to EntityId.
     */
    public void generate() {

	    if (items.size() > 0) return;

        MapleData[] itemsData = {
            stringData.getData("Cash.img"),
            stringData.getData("Consume.img"),
            stringData.getData("Etc.img").getChildByPath("Etc"),
            stringData.getData("Ins.img"), stringData.getData("Pet.img")
        };

        stringData.getData("Eqp.img").getChildByPath("Eqp").forEach(itemFolder -> itemFolder.forEach(equipItemFolder ->
        {
            int e = ecs.create();
            Name name = names.create(e);
            name.name = provider.MapleDataTool.getString("name", equipItemFolder, "NO-NAME");
            items.put(Integer.parseInt(equipItemFolder.getName()), e);
        }));

        Arrays.stream(itemsData).forEach(data -> data.getChildren().forEach(itemFolder -> {
            int e = ecs.create();
            Name name = names.create(e);
            name.name = provider.MapleDataTool.getString("name", itemFolder, "NO-NAME");
            items.put(Integer.parseInt(itemFolder.getName()), e);
        }));
    }

    public int entity(int itemId) {
        return items.get(itemId);
    }

    public void generateEquip(int itemId) {
        if (itemId / 1000000 != 1) return;

        int entityId = entity(itemId);
        if (equipStats.get(entityId) != null) return;

        MapleData item = getItemData(itemId);
        MapleData info;
        final EquipStat equipStat = equipStats.create(entityId);

        if (item == null || (info = item.getChildByPath("info")) == null) return;
//        incFatigue    incRMAF     incMDD        incACC
//        incLUK        incLUk      incDEX        incJump
//        incSpeed      incRMAS     incINT        incRMAL
//        incSTR        incMAD      incPDD        incEVA
//        incMMP        incSwim     incPAD        incCraft
//        incRMAI       incHP       incM

        info.getChildren().stream()
                .filter(data -> data.getName().startsWith("inc"))
                .forEach(data -> {
                    int value = provider.MapleDataTool.getInt(data.getName(), data, 0);
                    if (value > 0) equipStat.incStats.put(data.getName(), value);
                });

        int reqJob = provider.MapleDataTool.getInt("reqJob", info, 0);
        int reqLevel = provider.MapleDataTool.getInt("reqLevel", info, 0);
        int reqDex =  provider.MapleDataTool.getInt("reqDEX", info, 0);
        int reqStr = provider.MapleDataTool.getInt("reqSTR", info, 0);
        int reqIntel = provider.MapleDataTool.getInt("reqINT", info, 0);
        int reqLuk = provider.MapleDataTool.getInt("reqLUK", info, 0);
        int reqPop = provider.MapleDataTool.getInt("reqPOP", info, 0);

        // This means one of them has a req
        if ((reqDex + reqIntel + reqJob + reqLevel + reqStr + reqLuk + reqPop) > 0) {
            EquipStatRequirement equipStatRequirement = equipRequirements.create(entityId);
            equipStatRequirement.dex = reqDex;  equipStatRequirement.intel = reqIntel;  equipStatRequirement.job = reqJob;
            equipStatRequirement.level = reqLevel;  equipStatRequirement.str = reqStr;  equipStatRequirement.luk = reqLuk;
            equipStatRequirement.pop = reqPop;
        }

        equipStat.cash = provider.MapleDataTool.getInt("cash", info, 0);
        equipStat.upgradesPossible =  provider.MapleDataTool.getInt("upgradesPossible", info, 0);
        equipStat.cursed =  provider.MapleDataTool.getInt("cursed", info, 0);
        equipStat.success = provider.MapleDataTool.getInt("success", info, 0);
        equipStat.fs = provider.MapleDataTool.getInt("fs", info, 0);
        equipStat.expireOnLogout = provider.MapleDataTool.getInt("expireOnLogout", info, 0) == 1;
        equipStat.level = (info.getChildByPath("level") != null ? 1 : 0);
        equipStat.tradeBlock = provider.MapleDataTool.getInt("tradeBlock", info, 0) == 1;
        equipStat.only = provider.MapleDataTool.getInt("only", info, 0);
        equipStat.accountSharable = provider.MapleDataTool.getInt("accountSharable", info, 0) == 1;
        equipStat.quest = provider.MapleDataTool.getInt("quest", info, 0);
    }

//    public List<Pair<Integer, String>> getAllItems() { //ori
//        if (!itemNameCache.isEmpty()) {
//            return itemNameCache;
//        }
//        List<Pair<Integer, String>> itemPairs = new ArrayList<>();
//        MapleData itemsData;
//        itemsData = stringData.getData("Cash.img");
//        for (MapleData itemFolder : itemsData.getChildren()) {
//            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
//        }
//        itemsData = stringData.getData("Consume.img");
//        for (MapleData itemFolder : itemsData.getChildren()) {
//            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
//        }
//        itemsData = stringData.getData("Eqp.img").getChildByPath("Eqp");
//        for (MapleData eqpType : itemsData.getChildren()) {
//            for (MapleData itemFolder : eqpType.getChildren()) {
//                itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
//            }
//        }
//        itemsData = stringData.getData("Etc.img").getChildByPath("Etc");
//        for (MapleData itemFolder : itemsData.getChildren()) {
//            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
//        }
//        itemsData = stringData.getData("Ins.img");
//        for (MapleData itemFolder : itemsData.getChildren()) {
//            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
//        }
//        itemsData = stringData.getData("Pet.img");
//        for (MapleData itemFolder : itemsData.getChildren()) {
//            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
//        }
//        return itemPairs;
//    }
//    public List<Pair<Integer, String>> getAllEtcItems() {
//        if (!itemNameCache.isEmpty()) {
//            return itemNameCache;
//        }
//
//        List<Pair<Integer, String>> itemPairs = new ArrayList<>();
//        MapleData itemsData;
//
//        itemsData = stringData.getData("Etc.img").getChildByPath("Etc");
//        for (MapleData itemFolder : itemsData.getChildren()) {
//            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()),
//                    MapleDataTool.getString("name", itemFolder, "NO-NAME")));
//        }
//        return itemPairs;
//    }

    private MapleData getStringData(int itemId) {
        String cat = "null";
        MapleData theData;
        if (itemId >= 5010000) {
            theData = cashStringData;
        } else if (itemId >= 2000000 && itemId < 3000000) {
            theData = consumeStringData;
        } else if ((itemId >= 1010000 && itemId < 1040000)
                || (itemId >= 1122000 && itemId < 1123000)
                || (itemId >= 1142000 && itemId < 1143000)) {
            theData = eqpStringData;
            cat = "Eqp/Accessory";
        } else if (itemId >= 1000000 && itemId < 1010000) {
            theData = eqpStringData;
            cat = "Eqp/Cap";
        } else if (itemId >= 1102000 && itemId < 1103000) {
            theData = eqpStringData;
            cat = "Eqp/Cape";
        } else if (itemId >= 1040000 && itemId < 1050000) {
            theData = eqpStringData;
            cat = "Eqp/Coat";
        } else if (itemId >= 20000 && itemId < 22000) {
            theData = eqpStringData;
            cat = "Eqp/Face";
        } else if (itemId >= 1080000 && itemId < 1090000) {
            theData = eqpStringData;
            cat = "Eqp/Glove";
        } else if (itemId >= 30000 && itemId < 32000) {
            theData = eqpStringData;
            cat = "Eqp/Hair";
        } else if (itemId >= 1050000 && itemId < 1060000) {
            theData = eqpStringData;
            cat = "Eqp/Longcoat";
        } else if (itemId >= 1060000 && itemId < 1070000) {
            theData = eqpStringData;
            cat = "Eqp/Pants";
        } else if (itemId >= 1802000 && itemId < 1810000) {
            theData = eqpStringData;
            cat = "Eqp/PetEquip";
        } else if (itemId >= 1112000 && itemId < 1120000) {
            theData = eqpStringData;
            cat = "Eqp/Ring";
        } else if (itemId >= 1092000 && itemId < 1100000) {
            theData = eqpStringData;
            cat = "Eqp/Shield";
        } else if (itemId >= 1070000 && itemId < 1080000) {
            theData = eqpStringData;
            cat = "Eqp/Shoes";
        } else if (itemId >= 1900000 && itemId < 2000000) {
            theData = eqpStringData;
            cat = "Eqp/Taming";
        } else if (itemId >= 1300000 && itemId < 1800000) {
            theData = eqpStringData;
            cat = "Eqp/Weapon";
        } else if (itemId >= 4000000 && itemId < 5000000) {
            theData = etcStringData;
        } else if (itemId >= 3000000 && itemId < 4000000) {
            theData = insStringData;
        } else if (itemId >= 5000000 && itemId < 5010000) {
            theData = petStringData;
        } else {
            return null;
        }
        if (cat.equalsIgnoreCase("null")) {
            return theData.getChildByPath(String.valueOf(itemId));
        } else {
            return theData.getChildByPath(cat + "/" + itemId);
        }
    }

//    public boolean noCancelMouse(int itemId) {
//        MapleData item = getItemData(itemId);
//        if (item == null) {
//            return false;
//        }
//        return MapleDataTool.getIntConvert("info/noCancelMouse", item, 0) == 1;
//    }

    private MapleData getItemData(int itemId) {
        MapleData ret = null;
        String idStr = "0" + String.valueOf(itemId);
        MapleDataDirectoryEntry root = itemData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (provider.MapleDataFileEntry iFile : topDir.getFiles()) {
            	try {
	                if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
	                	
		                    ret = itemData.getData(topDir.getName() + "/" + iFile.getName());
		                    if (ret != null) {
		                        ret = ret.getChildByPath(idStr);
		                    }
	                    return ret;
	                } else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
	                    return itemData.getData(topDir.getName() + "/" + iFile.getName());
	                }
            	} catch (Exception e) {
                    e.printStackTrace();
            	}
            }
        }
        root = equipData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (provider.MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr + ".img")) {
                    return equipData.getData(topDir.getName() + "/" + iFile.getName());
                }
            }
        }
        return ret;
    }

//    /**
//     * Should only be called on items that stack (such as USE and ETC)
//     * @param c MapleClient
//     * @param itemId ID of item to be passed
//     * @return
//     */
//    public short getSlotMax(MapleClient c, int itemId) {
//        if (slotMaxCache.containsKey(itemId)) {
//            return slotMaxCache.get(itemId);
//        }
//        short ret = 0;
//        MapleData item = getItemData(itemId);
//        if (item != null) {
//            MapleData smEntry = item.getChildByPath("info/slotMax");
//            if (smEntry == null) {
//                if (ItemConstants.getInventoryType(itemId).getType()
//                        == MapleInventoryType.EQUIP.getType()) {
//                    ret = 1;
//                } else {
//                    ret = 100;
//                }
//            } else {
//                ret = (short) MapleDataTool.getInt(smEntry);
//                MapleCharacter chr = c.getPlayer();
//                if (chr != null) {
//	                if (ItemConstants.isThrowingStar(itemId)) {
//	                	if (chr.getJob().isA(MapleJob.NIGHTWALKER1)) {
//	                        ret += c.getPlayer().getSkillLevel(
//	                                SkillFactory.getSkill(NightWalker.CLAW_MASTERY)) * 10;
//	                    } else {
//	                        ret += c.getPlayer().getSkillLevel(
//	                                SkillFactory.getSkill(Assassin.CLAW_MASTERY)) * 10;
//	                    }
//	                } else if (ItemConstants.isBullet(itemId)) {
//	                    ret += c.getPlayer().getSkillLevel(
//	                            SkillFactory.getSkill(Gunslinger.GUN_MASTERY)) * 10;
//	                }
//                }
//            }
//        }
//        if (!ItemConstants.isRechargable(itemId)) {
//            slotMaxCache.put(itemId, ret);
//        }
//        return ret;
//    }
//
//    public int getMeso(int itemId) {
//        if (getMesoCache.containsKey(itemId)) {
//            return getMesoCache.get(itemId);
//        }
//        MapleData item = getItemData(itemId);
//        if (item == null) {
//            return -1;
//        }
//        MapleData pData = item.getChildByPath("info/meso");
//        if (pData == null) {
//            return -1;
//        }
//        int pEntry;
//        pEntry = MapleDataTool.getInt(pData);
//        getMesoCache.put(itemId, pEntry);
//        return pEntry;
//    }
//
//    public int getWholePrice(int itemId) {
//        if (wholePriceCache.containsKey(itemId)) {
//            return wholePriceCache.get(itemId);
//        }
//        MapleData item = getItemData(itemId);
//        if (item == null) {
//            return -1;
//        }
//        MapleData pData = item.getChildByPath("info/price");
//        if (pData == null) {
//            return -1;
//        }
//        int pEntry;
//        pEntry = MapleDataTool.getInt(pData);
//        wholePriceCache.put(itemId, pEntry);
//        return pEntry;
//    }
//
//    public double getPrice(int itemId) {
//        if (priceCache.containsKey(itemId)) {
//            return priceCache.get(itemId);
//        }
//        MapleData item = getItemData(itemId);
//        if (item == null) {
//            return -1;
//        }
//        double pEntry;
//        MapleData pData = item.getChildByPath("info/unitPrice");
//        if (pData != null) {
//            try {
//                pEntry = MapleDataTool.getDouble(pData);
//            } catch (Exception e) {
//                pEntry = (double) MapleDataTool.getInt(pData);
//            }
//        } else {
//            pData = item.getChildByPath("info/price");
//            if (pData == null) {
//                return -1;
//            }
//            pEntry = (double) MapleDataTool.getInt(pData);
//        }
//        priceCache.put(itemId, pEntry);
//        return pEntry;
//    }
//
//    protected String getEquipmentSlot(int itemId) {
//        if (equipmentSlotCache.containsKey(itemId)) {
//            return equipmentSlotCache.get(itemId);
//        }
//
//        String ret = "";
//
//        MapleData item = getItemData(itemId);
//
//        if (item == null) {
//            return null;
//        }
//
//        MapleData info = item.getChildByPath("info");
//
//        if (info == null) {
//            return null;
//        }
//
//        ret = MapleDataTool.getString("islot", info, "");
//
//        equipmentSlotCache.put(itemId, ret);
//
//        return ret;
//    }

//    public Map<String, Integer> getEquipStats(int itemId) {
//        if (equipStatsCache.containsKey(itemId)) {
//            return equipStatsCache.get(itemId);
//        }
//        Map<String, Integer> ret = new LinkedHashMap<>();
//        MapleData item = getItemData(itemId);
//        if (item == null) {
//            return null;
//        }
//        MapleData info = item.getChildByPath("info");
//        if (info == null) {
//            return null;
//        }
//        for (MapleData data : info.getChildren()) {
//            if (data.getName().startsWith("inc")) {
//                ret.put(data.getName().substring(3), MapleDataTool.getIntConvert(data));
//            }
//            /*else if (data.getName().startsWith("req"))
//             ret.put(data.getName(), MapleDataTool.getInt(data.getName(), info, 0));*/
//        }
//        ret.put("reqJob", MapleDataTool.getInt("reqJob", info, 0));
//        ret.put("reqLevel", MapleDataTool.getInt("reqLevel", info, 0));
//        ret.put("reqDEX", MapleDataTool.getInt("reqDEX", info, 0));
//        ret.put("reqSTR", MapleDataTool.getInt("reqSTR", info, 0));
//        ret.put("reqINT", MapleDataTool.getInt("reqINT", info, 0));
//        ret.put("reqLUK", MapleDataTool.getInt("reqLUK", info, 0));
//        ret.put("reqPOP", MapleDataTool.getInt("reqPOP", info, 0));
//        ret.put("cash", MapleDataTool.getInt("cash", info, 0));
//        ret.put("upgradesPossible", MapleDataTool.getInt("upgradesPossible", info, 0));
//        ret.put("cursed", MapleDataTool.getInt("cursed", info, 0));
//        ret.put("success", MapleDataTool.getInt("success", info, 0));
//        ret.put("fs", MapleDataTool.getInt("fs", info, 0));
//        ret.put("expireOnLogout", MapleDataTool.getInt("expireOnLogout", info, 0));
//        ret.put("level", info.getChildByPath("level") != null ? 1 : 0);
//        ret.put("tradeBlock", MapleDataTool.getInt("tradeBlock", info, 0));
//        ret.put("only", MapleDataTool.getInt("only", info, 0));
//        ret.put("accountSharable", MapleDataTool.getInt("accountSharable", info, 0));
//        ret.put("quest", MapleDataTool.getInt("quest", info, 0));
//
//        equipStatsCache.put(itemId, ret);
//        return ret;
//    }

    
//    public float getRecoveryRate(int itemId) {
//        if (equipRecovery.containsKey(itemId)) {
//            return equipRecovery.get(itemId);
//        }
//        MapleData item = getItemData(itemId);
//        if (item == null) {
//            return 0;
//        }
//        MapleData info = item.getChildByPath("info");
//        if (info == null) {
//            return 0;
//        }
//        float recovery = MapleDataTool.getFloat(info.getChildByPath("recovery"), 1.0f);
//        equipRecovery.put(itemId, recovery);
//        return recovery;
//    }
//
//    public List<Integer> getScrollReqs(int itemId) {
//        List<Integer> ret = new ArrayList<>();
//        MapleData data = getItemData(itemId);
//        data = data.getChildByPath("req");
//        if (data != null) {
//            for (MapleData req : data.getChildren()) {
//                ret.add(MapleDataTool.getInt(req));
//            }
//        }
//        return ret;
//    }
//
//    public MapleWeaponType getWeaponType(int itemId) {
//        int cat = (itemId / 10000) % 100;
//        MapleWeaponType[] type
//                = {MapleWeaponType.SWORD1H,
//                    MapleWeaponType.GENERAL1H_SWING,
//                    MapleWeaponType.GENERAL1H_SWING,
//                    MapleWeaponType.DAGGER_OTHER,
//                    MapleWeaponType.NOT_A_WEAPON,
//                    MapleWeaponType.NOT_A_WEAPON,
//                    MapleWeaponType.NOT_A_WEAPON,
//                    MapleWeaponType.WAND,
//                    MapleWeaponType.STAFF,
//                    MapleWeaponType.NOT_A_WEAPON,
//                    MapleWeaponType.SWORD2H,
//                    MapleWeaponType.GENERAL2H_SWING,
//                    MapleWeaponType.GENERAL2H_SWING,
//                    MapleWeaponType.SPEAR_STAB,
//                    MapleWeaponType.POLE_ARM_SWING,
//                    MapleWeaponType.BOW,
//                    MapleWeaponType.CROSSBOW,
//                    MapleWeaponType.CLAW,
//                    MapleWeaponType.KNUCKLE,
//                    MapleWeaponType.GUN};
//        if (cat < 30 || cat > 49) {
//            return MapleWeaponType.NOT_A_WEAPON;
//        }
//        return type[cat - 30];
//    }
//
//    public boolean isCleanSlate(int scrollId) {
//        return scrollId >= 2049000 && scrollId <= 2049008;
//    }
//
//    public boolean isFlaggedScroll(int scrollId) {
//        return scrollId == 2040727 || scrollId == 2041058;
//    }
//
//    public boolean isChaosScroll(int scrollId) {
//    	return scrollId == 2049100 || scrollId == 2049101 || scrollId == 2049102 || scrollId == 2049113 || scrollId == 2049114;
//    }
//
//    public Equip addGodlyStats(Equip equip) {
//    	if (equip.getStr() != 0)
//    		equip.setStr((short) (equip.getStr() + Math.random() * 5 + 1));
//    	if (equip.getLuk() != 0)
//    		equip.setLuk((short) (equip.getLuk() + Math.random() * 5 + 1));
//    	if (equip.getInt() != 0)
//    		equip.setInt((short) (equip.getInt() + Math.random() * 5 + 1));
//    	if (equip.getDex() != 0)
//    		equip.setDex((short) (equip.getDex() + Math.random() * 5 + 1));
//    	if (equip.getMatk() != 0)
//    		equip.setMatk((short) (equip.getMatk() + Math.random() * 5 + 1));
//    	if (equip.getWatk() != 0)
//    		equip.setWatk((short) (equip.getWatk() + Math.random() * 5 + 1));
//    	if (equip.getAcc() != 0)
//    		equip.setAcc((short) (equip.getAcc() + Math.random() * 5 + 1));
//    	if (equip.getAvoid() != 0)
//    		equip.setAvoid((short) (equip.getAvoid() + Math.random() * 5 + 1));
//    	if (equip.getHp() != 0)
//    		equip.setHp((short) (equip.getHp() + Math.random() * 5 + 1));
//    	if (equip.getJump() != 0)
//    		equip.setJump((short) (equip.getJump() + Math.random() * 5 + 1));
//    	if (equip.getSpeed() != 0)
//    		equip.setSpeed((short) (equip.getSpeed() + Math.random() * 5 + 1));
//    	if (equip.getMdef() != 0)
//    		equip.setMdef((short) (equip.getMdef() + Math.random() * 5 + 1));;
//    	if (equip.getWdef() != 0)
//    		equip.setWdef((short) (equip.getWdef() + Math.random() * 5 + 1));;
//    	if (equip.getMp() != 0)
//    		equip.setMp((short) (equip.getMp() + Math.random() * 5 + 1));;
//    	return equip;
//    }
//
//    public Item scrollEquipWithId(Item equip, int scrollId, boolean usingWhiteScroll, boolean isGM, float multiplier) {
//        if (equip instanceof Equip) {
//            Equip nEquip = (Equip) equip;
//            Map<String, Integer> incStats = this.getEquipStats(scrollId);
//            Map<String, Integer> eqstats = this.getEquipStats(equip.getItemId());
//            int chance = (int) Math.ceil(Math.random() * 100.0);
//            float prop = incStats.get("success") * multiplier;
//            if (((nEquip.getUpgradeSlots() > 0 || isCleanSlate(scrollId)
//                    || isFlaggedScroll(scrollId)) && chance <= prop)
//            		|| incStats.get("success") == 100
//                    || isGM) {
//                short flag = nEquip.getFlag();
//                switch (scrollId) {
//                    case 2040727:
//                        flag |= ItemConstants.SPIKES;
//                        nEquip.setFlag((byte) flag);
//                        return equip;
//                    case 2041058:
//                        flag |= ItemConstants.COLD;
//                        nEquip.setFlag((byte) flag);
//                        return equip;
//                    case 2049000:
//                    case 2049001:
//                    case 2049002:
//                    case 2049003:
//                    case 2049004:
//                    case 2049005:
//                        if (nEquip.getLevel() + nEquip.getUpgradeSlots() < eqstats.get("upgradesPossible") + nEquip.getVicious()) {
//                            nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
//                        }
//                        break;
//                    case 2049006:
//                    case 2049007:
//                    case 2049008:
//                        if (nEquip.getLevel() + nEquip.getUpgradeSlots() < eqstats.get("upgradesPossible") + nEquip.getVicious()) {
//                            nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 2));
//                        }
//                        break;
//                    case 2049100:
//                    case 2049101:
//                    case 2049102:
//                    case 2049113:
//                    case 2049114:
//                        if (scrollId == 2049113
//                                && (nEquip.getItemId() != 1003027
//                                && nEquip.getItemId() != 1302131)) {
//                            // Normal Witch Scrolls only for Hat and Broomstick
//                            break;
//                        }
//                        if (scrollId == 2049114
//                                && (nEquip.getItemId() != 1132014
//                                && nEquip.getItemId() != 1132015
//                                && nEquip.getItemId() != 1132016)) {
//                            // Witch Belt Scrolls only for Witch Belts
//                            break;
//                        }
//                        int inc = 1;
//                        if (Randomizer.nextInt(2) == 0) {
//                            inc = -1;
//                        }
//                        if (nEquip.getStr() > 0) {
//                            nEquip.setStr((short) Math.max(0,
//                                    (nEquip.getStr() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getDex() > 0) {
//                            nEquip.setDex((short) Math.max(0,
//                                    (nEquip.getDex() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getInt() > 0) {
//                            nEquip.setInt((short) Math.max(0,
//                                    (nEquip.getInt() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getLuk() > 0) {
//                            nEquip.setLuk((short) Math.max(0,
//                                    (nEquip.getLuk() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getWatk() > 0) {
//                            nEquip.setWatk((short) Math.max(0,
//                                    (nEquip.getWatk() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getWdef() > 0) {
//                            nEquip.setWdef((short) Math.max(0,
//                                    (nEquip.getWdef() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getMatk() > 0) {
//                            nEquip.setMatk((short) Math.max(0,
//                                    (nEquip.getMatk() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getMdef() > 0) {
//                            nEquip.setMdef((short) Math.max(0,
//                                    (nEquip.getMdef() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getAcc() > 0) {
//                            nEquip.setAcc((short) Math.max(0,
//                                    (nEquip.getAcc() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getAvoid() > 0) {
//                            nEquip.setAvoid((short) Math.max(0,
//                                    (nEquip.getAvoid() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getSpeed() > 0) {
//                            nEquip.setSpeed((short) Math.max(0,
//                                    (nEquip.getSpeed() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getJump() > 0) {
//                            nEquip.setJump((short) Math.max(0,
//                                    (nEquip.getJump() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getHp() > 0) {
//                            nEquip.setHp((short) Math.max(0,
//                                    (nEquip.getHp() + Randomizer.nextInt(6) * inc)));
//                        }
//                        if (nEquip.getMp() > 0) {
//                            nEquip.setMp((short) Math.max(0,
//                                    (nEquip.getMp() + Randomizer.nextInt(6) * inc)));
//                        }
//                        break;
//                    default:
//                        for (Entry<String, Integer> stat : incStats.entrySet()) {
//                            switch (stat.getKey()) {
//                                case "STR":
//                                    nEquip.setStr((short) (nEquip.getStr() + stat.getValue()));
//                                    break;
//                                case "DEX":
//                                    nEquip.setDex((short) (nEquip.getDex() + stat.getValue()));
//                                    break;
//                                case "INT":
//                                    nEquip.setInt((short) (nEquip.getInt() + stat.getValue()));
//                                    break;
//                                case "LUK":
//                                    nEquip.setLuk((short) (nEquip.getLuk() + stat.getValue()));
//                                    break;
//                                case "PAD":
//                                    nEquip.setWatk((short) (nEquip.getWatk() + stat.getValue()));
//                                    break;
//                                case "PDD":
//                                    nEquip.setWdef((short) (nEquip.getWdef() + stat.getValue()));
//                                    break;
//                                case "MAD":
//                                    nEquip.setMatk((short) (nEquip.getMatk() + stat.getValue()));
//                                    break;
//                                case "MDD":
//                                    nEquip.setMdef((short) (nEquip.getMdef() + stat.getValue()));
//                                    break;
//                                case "ACC":
//                                    nEquip.setAcc((short) (nEquip.getAcc() + stat.getValue()));
//                                    break;
//                                case "EVA":
//                                    nEquip.setAvoid((short) (nEquip.getAvoid() + stat.getValue()));
//                                    break;
//                                case "Speed":
//                                    nEquip.setSpeed((short) (nEquip.getSpeed() + stat.getValue()));
//                                    break;
//                                case "Jump":
//                                    nEquip.setJump((short) (nEquip.getJump() + stat.getValue()));
//                                    break;
//                                case "MHP":
//                                    nEquip.setHp((short) (nEquip.getHp() + stat.getValue()));
//                                    break;
//                                case "MMP":
//                                    nEquip.setMp((short) (nEquip.getMp() + stat.getValue()));
//                                    break;
//                                case "afterImage":
//                                    break;
//                            }
//                        }
//                        break;
//                }
//                if (!isCleanSlate(scrollId) && !isFlaggedScroll(scrollId)) {
//                    if (!isGM) {
//                        nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
//                    }
//                    nEquip.setLevel((byte) (nEquip.getLevel() + 1));
//                }
//            } else {
//                if (!usingWhiteScroll && !isCleanSlate(scrollId)
//                        && !isFlaggedScroll(scrollId) && !isGM) {
//                    nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
//                }
//                if (Randomizer.nextInt(99) < incStats.get("cursed")) {
//                    return null;
//                }
//            }
//        }
//        return equip;
//    }
//
//    public Item getEquipById(int equipId) {
//        return getEquipById(equipId, -1, -1);
//    }
//
//    // Lists monsters that drops the specified item
//    public final String grabMonstersThatDropOutput(int itemid) {
//    	StringBuilder output = new StringBuilder("#eThese are the monsters that drop #z"+itemid+"##n\r\n(#i"+itemid+"#):");
//    	try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT dropperid, chance FROM drop_data WHERE itemid = ?")) {
//	    	ps.setInt(1, itemid);
//	    	ResultSet rs = ps.executeQuery();
//
//	    	while (rs.next()) {
//	    		double drop_rate = 1000000 / ServerConstants.DROP_RATE;
//	    		double chance = (100L * rs.getInt("chance") / drop_rate);
//	    		output.append("\r\n#L"+(rs.getInt("dropperid"))+"##b#o"+rs.getInt("dropperid")+"##k - #r" + (chance) + "%#k");
//	    	}
//
//	    	// Don't think I have to check for nulls here
//	    	rs.close();
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	return output.toString() + "\r\n#e#L999#Back#n";
//    }
//
//    //NOTE: improve this
//    public Item getEquipById(int equipId, int slots, int ringId) {
//        Equip nEquip;
//
//        if (ringId > -1)
//        	nEquip = new MapleRing(equipId, (short) 0, ringId);
//        else
//        	nEquip = new Equip(equipId, (short) 0, ringId);
//        nEquip.setQuantity((short) 1);
//        Map<String, Integer> incStats = this.getEquipStats(equipId);
//
//        if (incStats != null) {
//
//            if (isDropRestricted(equipId)) {
//                byte flag = nEquip.getFlag();
//                flag |= ItemConstants.UNTRADEABLE;
//                nEquip.setFlag(flag);
//            } else if (incStats.get("fs") > 0) {
//                byte flag = nEquip.getFlag();
//                flag |= ItemConstants.SPIKES;
//                nEquip.setFlag(flag);
//            }
//
//            for (Entry<String, Integer> stat : incStats.entrySet()) {
//                switch (stat.getKey()) {
//                    case "STR":
//                        nEquip.setStr(stat.getValue().shortValue());
//                        break;
//                    case "DEX":
//                        nEquip.setDex(stat.getValue().shortValue());
//                        break;
//                    case "INT":
//                        nEquip.setInt(stat.getValue().shortValue());
//                        break;
//                    case "LUK":
//                        nEquip.setLuk(stat.getValue().shortValue());
//                        break;
//                    case "PAD":
//                        nEquip.setWatk(stat.getValue().shortValue());
//                        break;
//                    case "PDD":
//                        nEquip.setWdef(stat.getValue().shortValue());
//                        break;
//                    case "MAD":
//                        nEquip.setMatk(stat.getValue().shortValue());
//                        break;
//                    case "MDD":
//                        nEquip.setMdef(stat.getValue().shortValue());
//                        break;
//                    case "ACC":
//                        nEquip.setAcc(stat.getValue().shortValue());
//                        break;
//                    case "EVA":
//                        nEquip.setAvoid(stat.getValue().shortValue());
//                        break;
//                    case "Speed":
//                        nEquip.setSpeed(stat.getValue().shortValue());
//                        break;
//                    case "Jump":
//                        nEquip.setJump(stat.getValue().shortValue());
//                        break;
//                    case "MHP":
//                        nEquip.setHp(stat.getValue().shortValue());
//                        break;
//                    case "MMP":
//                        nEquip.setMp(stat.getValue().shortValue());
//                        break;
//                    case "upgradesPossible":
//                        nEquip.setUpgradeSlots(stat.getValue().byteValue());
//                        break;
//                    case "expireOnLogout":
//                    	nEquip.setDisappearsAtLogout(stat.getValue().shortValue() == 1);
//                    	break;
//                    default:
//                        break;
//                }
//            }
//            nEquip.setRecovery(getRecoveryRate(equipId));
//            equipCache.put(equipId, nEquip);
//        }
//        return nEquip.copy();
//    }
//
////    public Equip randomizeStats(Equip equip) { //ori
////        equip.setStr(getRandStat(equip.getStr(), 5));
////        equip.setDex(getRandStat(equip.getDex(), 5));
////        equip.setInt(getRandStat(equip.getInt(), 5));
////        equip.setLuk(getRandStat(equip.getLuk(), 5));
////        equip.setMatk(getRandStat(equip.getMatk(), 5));
////        equip.setWatk(getRandStat(equip.getWatk(), 5));
////        equip.setAcc(getRandStat(equip.getAcc(), 5));
////        equip.setAvoid(getRandStat(equip.getAvoid(), 5));
////        equip.setJump(getRandStat(equip.getJump(), 5));
////        equip.setSpeed(getRandStat(equip.getSpeed(), 5));
////        equip.setWdef(getRandStat(equip.getWdef(), 10));
////        equip.setMdef(getRandStat(equip.getMdef(), 10));
////        equip.setHp(getRandStat(equip.getHp(), 10));
////        equip.setMp(getRandStat(equip.getMp(), 10));
////        return equip;
////    }
//    //ori, private and static?? no need for it being static in this specific case.
//    //    private static short getRandStat(short defaultValue, int maxRange) { //ori
//    //NOTE: changed it to public static, cuz i wanna use this shiet xD
//    public static short getRandStat(short defaultValue, int maxRange) {
//        if (defaultValue == 0) {
//            return 0;
//        }
//        int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1), maxRange);
//        return (short) ((defaultValue - lMaxRange) + Math.floor(Randomizer.nextDouble() * (lMaxRange * 2 + 1)));
//    }
//
//    //default, all the equip incStats.
//    public Equip randomizeStats(Equip equip) {
//        MapleStat[] incStats = {
//            MapleStat.INT,
//            MapleStat.STR,
//            MapleStat.LUK,
//            MapleStat.DEX,
//            MapleStat.HP,
//            MapleStat.MP};
//
//        MapleBuffStat[] buffStats = {
//            MapleBuffStat.WATK,
//            MapleBuffStat.MATK,
//            MapleBuffStat.ACC,
//            MapleBuffStat.AVOID,
//            MapleBuffStat.SPEED,
//            MapleBuffStat.JUMP,
//            MapleBuffStat.MDEF,
//            MapleBuffStat.WDEF};
//
//        return randomizeStats(equip, incStats, buffStats);
//    }
//
//    public Equip randomizeStats(Equip equip,
//            MapleStat[] incStats, MapleBuffStat[] buffStats) {
//
//        equip = randomizeStats(equip, incStats);
//        equip = randomizeStats(equip, buffStats);
//        return equip;
//    }
//
//    public Equip randomizeStats(Equip equip, MapleStat[] incStats) {
//        for (MapleStat stat : incStats) {
//            switch (stat) {
//                case HP:
//                case MP:
//                    equip.setStat(stat, getRandStat(equip.getStat(stat), 10));
//                    break;
//                default:
//                    equip.setStat(stat, getRandStat(equip.getStat(stat), 5));
//            }
//        }
//        return equip;
//    }
//
//    public Equip randomizeStats(Equip equip, MapleBuffStat[] buffStats) {
//        for (MapleBuffStat buffStat : buffStats) {
//            switch (buffStat) {
//                case WDEF:
//                case MDEF:
//                    equip.setStat(buffStat, getRandStat(equip.getStat(buffStat), 10));
//                    break;
//                default:
//                    equip.setStat(buffStat, getRandStat(equip.getStat(buffStat), 5));
//            }
//        }
//        return equip;
//    }
//
//    //only used in PQs for now.
//    public static short randomizeItemQty(int itemId, short qty,
//            double deviationPercentage, boolean includeLowerBound) {
//
//        MapleInventoryType type = ItemLibrarySystem.getInstance()
//                .getInventoryType(itemId);
//
//        //we dont allow scrolls to be more than 1
//        if (type.equals(MapleInventoryType.USE)
//                && DropCategory.getDefaultCategory(itemId).equals(DropCategory.SCROLL)) {
//            return 1;
//        }
//
//        short lowerBound = includeLowerBound
//                ? (short) Math.floor(qty * (1 - deviationPercentage))
//                : qty;
//
//        short upperBound = (short) Math.ceil(qty * (1 + deviationPercentage));
//
//        return (short) Randomizer.rand(lowerBound, upperBound);
//    }
//
//    public MapleStatEffect getItemEffect(int itemId) {
//        MapleStatEffect ret = itemEffects.get(itemId);
//        if (ret == null) {
//            MapleData item = getItemData(itemId);
//            if (item == null) {
//                return null;
//            }
//            MapleData spec = item.getChildByPath("spec");
//            ret = MapleStatEffect.loadItemEffectFromData(spec, itemId);
//            itemEffects.put(itemId, ret);
//        }
//        return ret;
//    }
//
//    public String getFireworkEffect(int itemId) {
//        String ret = fireworkEffects.get(itemId);
//        if (ret == null) {
//            MapleData item = getItemData(itemId);
//            if (item == null) {
//                return null;
//            }
//            MapleData effect = item.getChildByPath("info/effect");
//            ret = MapleDataTool.getString(effect);
//            fireworkEffects.put(itemId, ret);
//        }
//        return ret;
//    }
//
//    public int[][] getSummonMobs(int itemId) {
//        MapleData data = getItemData(itemId);
//        if (data != null) {
//	        int theInt = data.getChildByPath("mob").getChildren().size();
//	        int[][] mobs2spawn = new int[theInt][2];
//	        for (int x = 0; x < theInt; x++) {
//	            mobs2spawn[x][0] = MapleDataTool.getIntConvert("mob/" + x + "/id", data);
//	            mobs2spawn[x][1] = MapleDataTool.getIntConvert("mob/" + x + "/prob", data);
//	        }
//	        return mobs2spawn;
//        }
//        return null;
//    }
//
//	public int getSummonMobType(int itemId) {
//		if(summonBagTypeCache.containsKey(itemId))
//			return summonBagTypeCache.get(itemId);
//
//		MapleData data = getItemData(itemId);
//		int type = MapleDataTool.getInt(data.getChildByPath("info").getChildByPath("type"), 1);
//		summonBagTypeCache.put(itemId, type);
//		return type;
//	}
//
//    public int getWatkForProjectile(int itemId) {
//        Integer atk = projectileWatkCache.get(itemId);
//        if (atk != null) {
//            return atk;
//        }
//        MapleData data = getItemData(itemId);
//        atk = MapleDataTool.getInt("info/incPAD", data, 0);
//        projectileWatkCache.put(itemId, atk);
//        return atk;
//    }
//
//    //since getAllItems has changed thus we have static itemIdAndName and it's initialized on server startup
//    //plus the old one cannot get etc names, something to do with not traversing the child paths
//    public String getName(int itemId) {
//        if (itemIdAndName.isEmpty()) {
//            getAllItems();
//        }
//        return itemIdAndName.get(itemId);
//    }
//
////    public String getName(int itemId) { //ori
////        if (nameCache.containsKey(itemId)) {
////            return nameCache.get(itemId);
////        }
////        MapleData strings = getStringData(itemId);
////        if (strings == null) {
////            return null;
////        }
////        String ret = MapleDataTool.getString("name", strings, null);
////        nameCache.put(itemId, ret);
////        return ret;
////    }
//    public String getMsg(int itemId) {
//        if (msgCache.containsKey(itemId)) {
//            return msgCache.get(itemId);
//        }
//        MapleData strings = getStringData(itemId);
//        if (strings == null) {
//            return null;
//        }
//        String ret = MapleDataTool.getString("msg", strings, null);
//        msgCache.put(itemId, ret);
//        return ret;
//    }
//
//
//    public boolean isBigItemSize(int itemId) {
//    	if (bigItem.containsKey(itemId)) {
//    		return bigItem.get(itemId);
//    	}
//        MapleData data = getItemData(itemId);
//        boolean bigSize = false;
//        if (data != null) {
//        	bigSize = MapleDataTool.getIntConvert("info/bigSize", data, 0) == 1;
//        }
//        bigItem.put(itemId, bigSize);
//        return bigSize;
//    }
//
//    /**
//     *
//     * @param itemId Item ID to be checked.
//     * @return True if item cannot be dropped, account shared, or if it is a quest item. False otherwise.
//     */
//    public boolean isDropRestricted(int itemId) {
//        if (dropRestrictionCache.containsKey(itemId)) {
//            return dropRestrictionCache.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        boolean bRestricted = false;
//        if (data != null) {
//        	bRestricted = MapleDataTool.getIntConvert("info/tradeBlock", data, 0) == 1;
//	        if (!bRestricted) {
//	            bRestricted = MapleDataTool.getIntConvert("info/accountSharable", data, 0) == 1;
//	        }
//	        if (!bRestricted) {
//	            bRestricted = MapleDataTool.getIntConvert("info/quest", data, 0) == 1;
//	        }
//        }
//        dropRestrictionCache.put(itemId, bRestricted);
//        return bRestricted;
//    }
//
//    public boolean isStealRestricted(int itemId) {
//    	return isDropRestricted(itemId) || isPickupRestricted(itemId) || isPickUpBlocked(itemId);
//    }
//
//    public boolean isPickupRestricted(int itemId) {
//        if (pickupRestrictionCache.containsKey(itemId)) {
//            return pickupRestrictionCache.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        boolean bRestricted = false;
//        if (data != null)
//        	bRestricted = MapleDataTool.getIntConvert("info/only", data, 0) == 1;
//        pickupRestrictionCache.put(itemId, bRestricted);
//        return bRestricted;
//    }
//
//    public boolean isPickUpBlocked(int itemId) {
//        if (pickUpBlock.containsKey(itemId)) {
//            return pickUpBlock.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        boolean bRestricted = false;
//        if (data != null)
//        	bRestricted = MapleDataTool.getIntConvert("info/pickUpBlock", data, 0) == 1;
//        pickUpBlock.put(itemId, bRestricted);
//        return bRestricted;
//    }
//
//    public boolean isPartyQuestItem(int itemId) {
//        if (partyQuestItem.containsKey(itemId)) {
//            return partyQuestItem.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        boolean bRestricted = false;
//        if (data != null)
//        	bRestricted = MapleDataTool.getIntConvert("info/pquest", data, 0) == 1;
//        partyQuestItem.put(itemId, bRestricted);
//        return bRestricted;
//    }
//
//    public Map<String, Integer> getSkillStats(int itemId, double playerJob) {
//        Map<String, Integer> ret = new LinkedHashMap<>();
//        MapleData item = getItemData(itemId);
//        if (item == null) {
//            return null;
//        }
//        MapleData info = item.getChildByPath("info");
//        if (info == null) {
//            return null;
//        }
//        for (MapleData data : info.getChildren()) {
//            if (data.getName().startsWith("inc")) {
//                ret.put(data.getName().substring(3), MapleDataTool.getIntConvert(data));
//            }
//        }
//        ret.put("masterLevel", MapleDataTool.getInt("masterLevel", info, 0));
//        ret.put("reqSkillLevel", MapleDataTool.getInt("reqSkillLevel", info, 0));
//        ret.put("success", MapleDataTool.getInt("success", info, 0));
//        MapleData skill = info.getChildByPath("skill");
//        int curskill;
//        for (int i = 0; i < skill.getChildren().size(); i++) {
//            curskill = MapleDataTool.getInt(Integer.toString(i), skill, 0);
//            if (curskill == 0) {
//                break;
//            }
//            if (curskill / 10000 == playerJob) {
//                ret.put("skillid", curskill);
//                break;
//            }
//        }
//        if (ret.get("skillid") == null) {
//            ret.put("skillid", 0);
//        }
//        return ret;
//    }
//
//    public List<Integer> petsCanConsume(int itemId) {
//        List<Integer> ret = new ArrayList<>();
//        MapleData data = getItemData(itemId);
//        int curPetId;
//        for (int i = 0; i < data.getChildren().size(); i++) {
//            curPetId = MapleDataTool.getInt("spec/" + i, data, 0);
//            if (curPetId == 0) {
//                break;
//            }
//            ret.add(curPetId);
//        }
//        return ret;
//    }
//
//    public boolean isQuestItem(int itemId) {
//        if (isQuestItemCache.containsKey(itemId)) {
//            return isQuestItemCache.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        boolean questItem = MapleDataTool.getIntConvert("info/quest", data, 0) == 1;
//        isQuestItemCache.put(itemId, questItem);
//        return questItem;
//    }
//
//    private void loadCardIdData() {
//        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
//                "SELECT cardid, mobid FROM monstercarddata");
//                ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                monsterBookID.put(rs.getInt(1), rs.getInt(2));
//            }
//        } catch (SQLException e) {
//        }
//    }
//
//    public int getCardMobId(int id) {
//        if(monsterBookID.containsKey(id)) {
//			return monsterBookID.get(id);
//		}
//		int mobid = MapleDataTool.getIntConvert("info/mob", getItemData(id), 0);
//		monsterBookID.put(id, mobid);
//		return mobid;
//    }
//
//    /*
//     * @param itemid Item that is being looked up
//     * Returns a map of items and their infoex update values when consumed by the currently searched item
//     */
//    public Map<Integer, Integer> getQuestConsumables(int itemid) {
//    	if (updateQuestCache.containsKey(itemid)) {
//            return updateQuestCache.get(itemid);
//        }
//    	MapleData consumeItem = getItemData(itemid).getChildByPath("info/consumeItem");
//    	Map<Integer, Integer> consumables = new HashMap<>();
//    	for (MapleData consumable : consumeItem.getChildren()) {
//    		int consumableId = MapleDataTool.getIntConvert("0", consumable, 0);
//    		int updateValue = MapleDataTool.getIntConvert("1", consumable, 0);
//    		consumables.put(consumableId, updateValue);
//    	}
//    	updateQuestCache.put(itemid, consumables);
//    	return consumables;
//    }
//
//    public boolean isUntradeableOnEquip(int itemId) {
//        if (onEquipUntradableCache.containsKey(itemId)) {
//            return onEquipUntradableCache.get(itemId);
//        }
//        boolean untradableOnEquip = MapleDataTool.getIntConvert(
//                "info/equipTradeBlock", getItemData(itemId), 0) > 0;
//        onEquipUntradableCache.put(itemId, untradableOnEquip);
//        return untradableOnEquip;
//    }
//
//    public ScriptedItem getScriptedItemInfo(int itemId) {
//        if (scriptedItemCache.containsKey(itemId)) {
//            return scriptedItemCache.get(itemId);
//        }
//        if ((itemId / 10000) != 243) {
//            return null;
//        }
//        ScriptedItem script = new ScriptedItem(
//                MapleDataTool.getInt("spec/npc", getItemData(itemId), 0),
//                MapleDataTool.getString("spec/script", getItemData(itemId), ""),
//                MapleDataTool.getInt("spec/runOnPickup", getItemData(itemId), 0) == 1);
//        scriptedItemCache.put(itemId, script);
//        return scriptedItemCache.get(itemId);
//    }
//
//    public boolean isOnlyOnePickup(int itemId) {
//        if (onlyPickupCache.containsKey(itemId)) {
//            return onlyPickupCache.get(itemId);
//        }
//        boolean onlyPickup = MapleDataTool.getInt("spec/onlyPickup", getItemData(itemId), 0) == 1;
//        onlyPickupCache.put(itemId, onlyPickup);
//        return onlyPickup;
//    }
//
//    public boolean isKarmaAble(int itemId) {
//        if (karmaCache.containsKey(itemId)) {
//            return karmaCache.get(itemId);
//        }
//        boolean bRestricted = MapleDataTool.getIntConvert(
//                "info/tradeAvailable", getItemData(itemId), 0) > 0;
//        karmaCache.put(itemId, bRestricted);
//        return bRestricted;
//    }
//
//	public Pair<Integer, String> getReplaceOnExpire(int itemId) {
//		if(replaceOnExpireCache.containsKey(itemId)) {
//			return replaceOnExpireCache.get(itemId);
//		}
//
//		int itemReplacement = MapleDataTool.getInt("info/replace/itemid", getItemData(itemId), 0);
//		String msg = MapleDataTool.getString("info/replace/msg", getItemData(itemId));
//
//		Pair<Integer, String> ret = new Pair<>(itemReplacement, msg);
//		replaceOnExpireCache.put(itemId, ret);
//
//		return ret;
//	}
//
//	public boolean isExpireOnLogout(int itemId) {
//		if (expireOnLogout.containsKey(itemId)) {
//            return expireOnLogout.get(itemId);
//        }
//        boolean expire = MapleDataTool.getIntConvert(
//                "info/expireOnLogout", getItemData(itemId), 0) > 0;
//        expireOnLogout.put(itemId, expire);
//        return expire;
//	}
//
//    public int getStateChangeItem(int itemId) {
//        if (triggerItemCache.containsKey(itemId)) {
//            return triggerItemCache.get(itemId);
//        } else {
//            int triggerItem = MapleDataTool.getIntConvert(
//                    "info/stateChangeItem", getItemData(itemId), 0);
//            triggerItemCache.put(itemId, triggerItem);
//            return triggerItem;
//        }
//    }
//
//    public int getExpById(int itemId) {
//        if (expCache.containsKey(itemId)) {
//            return expCache.get(itemId);
//        } else {
//            int exp = MapleDataTool.getIntConvert(
//                    "spec/exp", getItemData(itemId), 0);
//            expCache.put(itemId, exp);
//            return exp;
//        }
//    }
//
//    public int getMaxLevelById(int itemId) {
//        if (levelCache.containsKey(itemId)) {
//            return levelCache.get(itemId);
//        } else {
//            int level = MapleDataTool.getIntConvert(
//                    "info/maxLevel", getItemData(itemId), 256);
//            levelCache.put(itemId, level);
//            return level;
//        }
//    }
//
//    public Pair<Integer, List<RewardItem>> getItemReward(int itemId) {//Thanks Celino, used some stuffs :)
//        if (rewardCache.containsKey(itemId)) {
//            return rewardCache.get(itemId);
//        }
//        int totalprob = 0;
//        List<RewardItem> rewards = new ArrayList<RewardItem>();
//        for (MapleData child : getItemData(itemId).getChildByPath("reward").getChildren()) {
//            RewardItem reward = new RewardItem();
//            reward.itemid = MapleDataTool.getInt("item", child, 0);
//            reward.prob = (byte) MapleDataTool.getInt("prob", child, 0);
//            reward.quantity = (short) MapleDataTool.getInt("count", child, 0);
//            reward.effect = MapleDataTool.getString("Effect", child, "");
//            reward.worldmsg = MapleDataTool.getString("worldMsg", child, null);
//            reward.period = MapleDataTool.getInt("period", child, -1);
//
//            totalprob += reward.prob;
//
//            rewards.add(reward);
//        }
//        Pair<Integer, List<RewardItem>> hmm = new Pair<>(totalprob, rewards);
//        rewardCache.put(itemId, hmm);
//        return hmm;
//    }
//
//    public boolean isConsumeOnPickup(int itemId) {
//        if (consumeOnPickupCache.containsKey(itemId)) {
//            return consumeOnPickupCache.get(itemId);
//        }
//        MapleData data = getItemData(itemId);
//        boolean consume = MapleDataTool.getIntConvert("spec/consumeOnPickup", data, 0) == 1
//                || MapleDataTool.getIntConvert("specEx/consumeOnPickup", data, 0) == 1;
//        consumeOnPickupCache.put(itemId, consume);
//        return consume;
//    }
//
//    public final boolean isTwoHanded(int itemId) {
//        switch (getWeaponType(itemId)) {
//            case GENERAL2H_SWING:
//            case BOW:
//            case CLAW:
//            case CROSSBOW:
//            case POLE_ARM_SWING:
//            case SPEAR_STAB:
//            case SWORD2H:
//            case GUN:
//            case KNUCKLE:
//                return true;
//            default:
//                return false;
//        }
//    }
//
//    public boolean isItemValid(int itemId) {
//    	if (itemId / 1000000 < 1)
//    		return false;
//        getAllItems();
//        return itemIdAndName.containsKey(itemId);
//    }
//
//    public boolean isCash(int itemId) {
//    	if (itemId / 1000000 == 5) {
//    		return true;
//    	}
//    	Integer cs = getEquipStats(itemId).get("cash");
//    	if (cs != null) {
//    		return cs == 1;
//    	}
//        return false;
//    }
//
//    public boolean cannotRevive(int petId) {
//    	MapleData data = itemData.getData("Pet/" + petId + ".img").getChildByPath("info").getChildByPath("noRevive");
//    	if (data == null) {
//    		return false;
//    	}
//    	return MapleDataTool.getInt(data) == 1;
//    }
//
//    public int getPetLife(int petId) {
//    	MapleData data = itemData.getData("Pet/" + petId + ".img").getChildByPath("info").getChildByPath("life");
//    	if (data == null) {
//    		return 90;
//    	}
//    	return MapleDataTool.getInt(data);
//    }
//
//    public Collection<Item> canWearEquipment(MapleCharacter chr, Collection<Item> items) {
//        MapleInventory inv = chr.getInventory(MapleInventoryType.EQUIPPED);
//        if (inv.checked()) {
//            return items;
//        }
//        Collection<Item> itemz = new LinkedList<>();
//        if (chr.getJob() == MapleJob.SUPERGM || chr.getJob() == MapleJob.GM) {
//            for (Item item : items) {
//                Equip equip = (Equip) item;
//                itemz.add(item);
//            }
//            return itemz;
//        }
//        boolean highfivestamp = false;
//        /* Removed because players shouldn't even get this, and gm's should just be gm job.
//         try {
//         for (Pair<Item, MapleInventoryType> ii : ItemFactory.INVENTORY.generate(chr.getId(), false)) {
//         if (ii.getRight() == MapleInventoryType.CASH) {
//         if (ii.getLeft().getItemId() == 5590000) {
//         highfivestamp = true;
//         }
//         }
//         }
//         } catch (SQLException ex) {
//         }*/
//        int tdex = chr.getDex(), tstr = chr.getStr(), tint = chr.getInt(), tluk = chr.getLuk(), fame = chr.getFame();
//        if (chr.getJob() != MapleJob.SUPERGM || chr.getJob() != MapleJob.GM) {
//            for (Item item : inv.list()) {
//                Equip equip = (Equip) item;
//                tdex += equip.getDex();
//                tstr += equip.getStr();
//                tluk += equip.getLuk();
//                tint += equip.getInt();
//            }
//        }
//        for (Item item : items) {
//            Equip equip = (Equip) item;
//            Map<String, Integer> equipStats = getEquipStats(equip.getItemId());
//            int reqLevel = equipStats.get("reqLevel");
//            if (highfivestamp) {
//                reqLevel -= 5;
//                if (reqLevel < 0) {
//                    reqLevel = 0;
//                }
//            }
//            /*
//             int reqJob = equipStats.get("reqJob");
//             if (reqJob != 0) {
//             Really hard check, and not really needed in this one
//             Gm's should just be GM job, and players cannot change jobs.
//             }*/
//            if (reqLevel > chr.getLevel()) {
//                continue;
//            } else if (equipStats.get("reqDEX") > tdex) {
//                continue;
//            } else if (equipStats.get("reqSTR") > tstr) {
//                continue;
//            } else if (equipStats.get("reqLUK") > tluk) {
//                continue;
//            } else if (equipStats.get("reqINT") > tint) {
//                continue;
//            }
//            int reqPOP = equipStats.get("reqPOP");
//            if (reqPOP > 0 && reqPOP > fame) {
////                if (equipStats.get("reqPOP") > fame) { //ori, ??
//                continue;
////                }
//            }
//            itemz.add(equip);
//        }
//        inv.checked(true);
//        return itemz;
//    }
//
//    public boolean canWearEquipment(MapleCharacter chr, Equip equip, int dst) {
//        int id = equip.getItemId();
//
//        if (!EquipSlot.getFromItemId(id).isAllowed(dst, isCash(id))) {
//            String itemName = ItemLibrarySystem.getInstance().getName(equip.getItemId());
//            Server.getInstance().broadcastGMMessage(
//                    MaplePacketCreator.sendYellowTip("[WARNING]: " + chr.getName()
//                            + " tried to equip " + itemName + " into slot " + dst + "."));
//            AutobanFactory.PACKET_EDIT.alert(chr, chr.getName() + " tried to forcibly equip an item.");
//            //FilePrinter.printError(FilePrinter.EXPLOITS + chr.getName() + ".txt", chr.getName()
//            //        + " tried to equip " + itemName + " into " + dst + " slot.\r\n");
//            return false;
//        }
//
//        if (chr.getJob() == MapleJob.SUPERGM || chr.getJob() == MapleJob.GM) {
//            return true;
//        }
//
//        boolean highfivestamp = false;
//        /* Removed check above for message ><
//         try {
//         for (Pair<Item, MapleInventoryType> ii : ItemFactory.INVENTORY.generate(chr.getId(), false)) {
//         if (ii.getRight() == MapleInventoryType.CASH) {
//         if (ii.getLeft().getItemId() == 5590000) {
//         highfivestamp = true;
//         }
//         }
//         }
//         } catch (SQLException ex) {
//         }*/
//
//        Map<String, Integer> equipStats = getEquipStats(equip.getItemId());
//        int reqLevel = equipStats.get("reqLevel");
//        if (highfivestamp) {
//            reqLevel -= 5;
//        }
//        int i = 0; //lol xD
//        //Removed job check. Shouldn't really be needed.
//        if (reqLevel > chr.getLevel()) {
//            i++;
//        } else if (equipStats.get("reqDEX") > chr.getTotalDex()) {
//            i++;
//        } else if (equipStats.get("reqSTR") > chr.getTotalStr()) {
//            i++;
//        } else if (equipStats.get("reqLUK") > chr.getTotalLuk()) {
//            i++;
//        } else if (equipStats.get("reqINT") > chr.getTotalInt()) {
//            i++;
//        }
//        int reqPOP = equipStats.get("reqPOP");
//        if (reqPOP > 0 && equipStats.get("reqPOP") > chr.getFame()) {
//            i++;
//        }
//
////        if (i > 0) { //ori
////            equip.wear(false);
////            return false;
////        }
////        equip.wear(true);
////        return true;
//        return i <= 0;
//    }
//
//    public ArrayList<Integer> getRelevantItemDataByName(String queryString) {
//        return getRelevantItemDataByName(queryString, (byte) 3);
//    }
//
//    //improved search result return algorithm, sorting the results by relevance
//    //exact match > results that start with query string > results that contain query string
//    //edit: i think start with is good enough, exact match removed for now, to be tested.
//    //this only returns id
//    public ArrayList<Integer> getRelevantItemDataByName(String queryString, byte resultExpected) {
//        ArrayList<Integer> ret = new ArrayList<>();
//        queryString = queryString.toLowerCase();
//        //this copies itemIdAndName instead of just a reference of it, to pevent element (key and values) removal/ modification.
//        Set<Entry<Integer, String>> miip = new LinkedHashMap<>(itemIdAndName).entrySet();
//        Iterator<Entry<Integer, String>> miipIterator = miip.iterator();
////        System.out.println(miip.size());
//
//        while (ret.size() < resultExpected && miipIterator.hasNext()) { //find the matches that start with query string
//            Entry<Integer, String> next = miipIterator.next();
//
//            if (next.getValue().toLowerCase().startsWith(queryString)) {
//                ret.add(next.getKey());
//                miipIterator.remove();
//            }
//        }
////            System.out.println(miip.size());
//
//        miipIterator = miip.iterator();
//        //if not enough results were found, find the name that contains the query string
//        while (ret.size() < resultExpected && miipIterator.hasNext()) {
//            Entry<Integer, String> next = miipIterator.next();
//
//            if (next.getValue().toLowerCase().contains(queryString)) {
//                ret.add(next.getKey());
//            }
//        }
//        return ret;
//    }
//    //this uses pair for id and name
////    public ArrayList<Pair<Integer, String>> getRelevantItemDataByName(String queryString) {
////        return getRelevantItemDataByName(queryString, (byte) 3);
////    }
////    public ArrayList<Pair<Integer, String>> getRelevantItemDataByName(String queryString, byte resultExpected) {
////        ArrayList<Pair<Integer, String>> ret = new ArrayList<>();
////        queryString = queryString.toLowerCase();
////        //this copies itemIdAndName instead of just a reference of it, to pevent element (key and values) removal/ modification.
////        Set<Entry<Integer, String>> miip = new LinkedHashMap<>(itemIdAndName).entrySet();
////        Iterator<Entry<Integer, String>> miipIterator = miip.iterator();
//////        System.out.println(miip.size());
////
////        //find the exact match, case-insensitive
//////        while (miipIterator.hasNext()) {
//////                Entry<Integer,String> next=miipIterator.next();
//////
//////            if (next.getValue().toLowerCase().equals(queryString)) {
//////                ret.add(new Pair(next.getKey(),next.getValue()));
//////                miipIterator.remove();
//////            }
//////        }
//////        System.out.println(miip.size());
//////        if (ret.size() < resultExpected) {//if not enough results were found, find the name that begins with the query string
//////            miipIterator = miip.iterator();
////        while (ret.size() < resultExpected && miipIterator.hasNext()) { //find the matches that start with query string
////            Entry<Integer, String> next = miipIterator.next();
////
////            if (next.getValue().toLowerCase().startsWith(queryString)) {
////                ret.add(new Pair(next.getKey(), next.getValue()));
////                miipIterator.remove();
////            }
////        }
//////            System.out.println(miip.size());
////
////        miipIterator = miip.iterator();
////        //if not enough results were found, find the name that contains the query string
////        while (ret.size() < resultExpected && miipIterator.hasNext()) {
////            Entry<Integer, String> next = miipIterator.next();
////
////            if (next.getValue().toLowerCase().contains(queryString)) {
////                ret.add(new Pair(next.getKey(), next.getValue()));
////            }
////        }
////
//////        }
////        return ret;
////    }
//
//    public ArrayList<Pair<Integer, String>> getItemDataByName(String queryString) { //for the new getAllItems
//        ArrayList<Pair<Integer, String>> ret = new ArrayList<>();
//        queryString = queryString.toLowerCase();
//
//        for (Entry<Integer, String> itemEntry : itemIdAndName.entrySet()) {
//            if (itemEntry.getValue().toLowerCase().contains(queryString)) {
//                ret.add(new Pair<Integer, String>(itemEntry.getKey(), itemEntry.getValue()));
//            }
//        }
//        return ret;
//    }
//
//
//    public boolean canEvolve(int petId) {
//    	MapleData data = itemData.getData("Pet/" + petId + ".img").getChildByPath("info").getChildByPath("evol");
//    	if (data == null) {
//    		return false;
//    	}
//    	return MapleDataTool.getInt(data) == 1;
//    }
//
//    public boolean needsEvolutionItem(int petId) {
//    	MapleData data = itemData.getData("Pet/" + petId + ".img").getChildByPath("info").getChildByPath("evolReqItemID");
//    	if (data == null) {
//    		return false;
//    	}
//    	return MapleDataTool.getInt(data) == 1;
//    }
//
//
//    public List<PetEvolutionInformation> getEvolutions(int petId) {
//    	if (evolutionCache.containsKey(petId)) {
//    		return evolutionCache.get(petId);
//    	}
//    	MapleData data = itemData.getData("Pet/" + petId + ".img").getChildByPath("info");
//    	int evolNo = 0;
//    	MapleData evolutionAmount = data.getChildByPath("evolNo");
//    	if (evolutionAmount != null) {
//    		evolNo = MapleDataTool.getInt(evolutionAmount);
//    	} else {
//    		return null;
//    	}
//    	int evolReqItemId = 0;
//    	MapleData reqItem = data.getChildByPath("evolReqItemID");
//    	if (reqItem != null) {
//    		evolReqItemId = MapleDataTool.getInt(reqItem);
//    	}
//    	int evolReqPetLvl = 0;
//    	MapleData reqLevel = data.getChildByPath("evolReqPetLvl");
//    	if (reqLevel != null) {
//    		evolReqPetLvl = MapleDataTool.getInt(reqLevel);
//    	}
//    	List<PetEvolutionInformation> evolutions = new ArrayList<>(evolNo);
//    	for (int i = 0; i < evolNo; i++) {
//    		MapleData evolId = data.getChildByPath("evol" + (i+ 1));
//    		int petEvolId = 0;
//    		if (evolId != null) {
//    			petEvolId = MapleDataTool.getInt(evolId);
//    		}
//    		MapleData evolProb = data.getChildByPath("evolProb" + (i+ 1));
//    		int petEvolProb = 0;
//    		if (evolProb != null) {
//    			petEvolProb = MapleDataTool.getInt(evolProb);
//    		}
//    		evolutions.add(new PetEvolutionInformation(petEvolId, petEvolProb, evolReqPetLvl, evolReqItemId));
//    	}
//    	evolutionCache.put(petId, evolutions);
//    	return evolutions;
//    }
//
//    public LevelUpInformation getItemLevelupStats(int itemId, int level) {
//    	LevelUpInformation info = null;
//    	if (levelUpCache.containsKey(itemId)) {
//    		info = levelUpCache.get(itemId).get(level);
//    		if (info != null) {
//    			return info;
//    		}
//    	}
//    	HashMap<Integer, LevelUpInformation> information = new HashMap<>();
//        MapleData data = getItemData(itemId);
//        MapleData data1 = data.getChildByPath("info").getChildByPath("level");
//        if (data1 != null) {
//        	for (int i = 1; i <= 6; i++) {
//	            MapleData data2 = data1.getChildByPath("info").getChildByPath(Integer.toString(i));
//	            if (data2 != null) {
//	            	info = new LevelUpInformation();
//	                for (MapleData da : data2.getChildren()) {
//	                	info.getStats().put(da.getName(), MapleDataTool.getInt(da));
//	                }
//	                information.put(i, info);
//	            } else {
//	            	break;
//	            }
//        	}
//        }
//        levelUpCache.put(itemId, information);
//        return information.get(level);
//    }
//
//
//    public boolean hasSkill(int itemId, int skillId) {
//    	if (skillsOnItem.containsKey(itemId)) {
//    		List<Integer> skills = skillsOnItem.get(itemId);
//    		return skills.contains(skillId);
//    	}
//    	List<Integer> skillSet = new ArrayList<>();
//        MapleData data = getItemData(itemId);
//        if (data == null) {
//        	return false;
//        }
//        MapleData data1 = data.getChildByPath("info/level/case/1/6/Skill");
//        if (data1 != null) {
//        	for (MapleData dat : data1.getChildren()) {
//        		MapleData skill = dat.getChildByPath("id");
//        		skillSet.add(MapleDataTool.getInt(skill));
//        	}
//    		skillsOnItem.put(itemId, skillSet);
//        } else {
//        	return false;
//        }
//        return skillSet.contains(skillId);
//    }
//
//    public boolean canLevelUp(int itemId) {
//        return getEquipStats(itemId).get("level") > 0;
//    }
//
//    public int getStatLevelupProbability(int itemId, int type) {
//        MapleData data = getItemData(itemId);
//        MapleData data1 = data.getChildByPath("info").getChildByPath("level").getChildByPath("case").getChildByPath("" + type).getChildByPath("prob");
//    	return MapleDataTool.getIntConvert(data1, 0);
//    }
//
//    // For Maker skills, not all ETC. drops have lv child in XML
//    public final short getItemLevel(final int itemId) {
//        if (itemId / 10000 != 400) {
//            return 0;
//        }
//        final short itemLevel = (short) MapleDataTool.getIntConvert("info/lv", getItemData(itemId), 0);
//        return itemLevel;
//    }
//
//    // Gem sockets
//    public final Map<String, Byte> getItemINCStats(final int itemId) {
//        if (itemId / 10000 != 425) { // Must be a gem
//            return null;
//        }
//        final Map<String, Byte> ret = new LinkedHashMap<>();
//        final MapleData item = getItemData(itemId);
//        if (item == null) {
//            return null;
//        }
//        final MapleData info = item.getChildByPath("info");
//        if (info == null) {
//            return null;
//        }
//        ret.put("incPAD", (byte) MapleDataTool.getInt("incPAD", info, 0)); // W.ATT
//        ret.put("incMAD", (byte) MapleDataTool.getInt("incMAD", info, 0)); // M.ATT
//        ret.put("incACC", (byte) MapleDataTool.getInt("incACC", info, 0)); // ACC
//        ret.put("incEVA", (byte) MapleDataTool.getInt("incEVA", info, 0)); // AVOID
//        ret.put("incSpeed", (byte) MapleDataTool.getInt("incSpeed", info, 0)); // SPEED
//        ret.put("incJump", (byte) MapleDataTool.getInt("incJump", info, 0)); // JUMP
//        ret.put("incMaxHP", (byte) MapleDataTool.getInt("incMaxHP", info, 0)); // MaxHP
//        ret.put("incMaxMP", (byte) MapleDataTool.getInt("incMaxMP", info, 0)); // MaxMP
//        ret.put("incSTR", (byte) MapleDataTool.getInt("incSTR", info, 0)); // STR
//        ret.put("incDEX", (byte) MapleDataTool.getInt("incDEX", info, 0)); // DEX
//        ret.put("incINT", (byte) MapleDataTool.getInt("incINT", info, 0)); // INT
//        ret.put("incLUK", (byte) MapleDataTool.getInt("incLUK", info, 0)); // LUK
//        ret.put("incReqLevel", (byte) MapleDataTool.getInt("incReqLevel", info, 0)); // Decreases required level to wear an equipment
//        ret.put("randOption", (byte) MapleDataTool.getInt("randOption", info, 0)); // Black Crystal - Who know's what'll happen? xD
//        ret.put("randStat", (byte) MapleDataTool.getInt("randStat", info, 0)); // Dark Crystal - STR/DEX/INT/LUK
//        return ret;
//    }

    public static final class RewardItem {

        public int itemid, period;
        public short prob, quantity;
        public String effect, worldmsg;
    }

    public class ScriptedItem {

        private boolean runOnPickup;
        private int npc;
        private String script;

        public ScriptedItem(int npc, String script, boolean rop) {
            this.npc = npc;
            this.script = script;
            this.runOnPickup = rop;
        }

        public int getNpc() {
            return npc;
        }

        public String getScript() {
            return script;
        }

        public boolean runOnPickup() {
            return runOnPickup;
        }
    }
}
