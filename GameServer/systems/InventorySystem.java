package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import ecs.components.DatabaseId;
import ecs.components.character.CharacterStat;
import ecs.components.item.*;
import database.DatabaseConnection;
import ecs.EntityCreationSystem;
import ecs.system.ItemCreationSystem;
import ecs.system.ItemInfoEncodingSystem;
import net.packets.OutboundPacket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventorySystem extends BaseSystem {

    ComponentMapper<CashItem> cashItems;        ComponentMapper<ItemOwner> itemOwners;
    ComponentMapper<Item> items;                ComponentMapper<ItemLevel> itemLevels;
    ComponentMapper<Equip> equips;              ComponentMapper<ItemFlag> flags;
    ComponentMapper<Pet> pets;                  ComponentMapper<CharacterInventory> charInventories;
    ComponentMapper<Ring> rings;                ComponentMapper<Inventory> inventories;
    ComponentMapper<Expiration> expirations;    ComponentMapper<DatabaseId> dbIds;
    ComponentMapper<CharacterStat> stats;
    EntityCreationSystem ecs;
    ItemInfoEncodingSystem itemInfoEncodingSystem;
    ItemCreationSystem itemCreationSystem;

    public void save(int playerEntityId) {
        CharacterInventory characterInventory = charInventories.get(playerEntityId);
        for (int inventoryEntityId : characterInventory.inventories) {
        }
    }

    public void retrieve(int playerEntityId) {

        CharacterInventory characterInventory = charInventories.create(playerEntityId);
        CharacterStat stat = stats.get(playerEntityId);

        for (int x = 0; x < CharacterInventory.Type.values().length - 1; x++) {
            characterInventory.inventories[x] = ecs.create();
            Inventory inv = inventories.create(characterInventory.inventories[x]);
            inv.itemIds = new Integer[stat.slotLimits[x]];
            inv.itemEntityIDs = new Integer[stat.slotLimits[x]];
        }

        DatabaseId dbId = dbIds.get(playerEntityId);
        StringBuilder b = new StringBuilder();
        b.append("SELECT * FROM `items` i1 left join equips on i1.id = equips.itemKey where i1.chrid = ? and i1.insertTime =")
        .append("(SELECT max(insertTime) FROM items i2 WHERE i2.chrid = ?)");
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(b.toString())) {
            ps.setInt(1, dbId.dbId);
            ps.setInt(2,dbId.dbId);
            try (ResultSet rs = ps.executeQuery()) {
                generate(rs, characterInventory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generate(ResultSet rs, CharacterInventory characterInventory) throws SQLException {
        if (rs.next()) {
            int itemEntityId = itemCreationSystem.generate(rs);
            if (itemEntityId != -1) {
                int itemId = rs.getInt("itemid");
                Equip equip = equips.get(itemEntityId);
                byte inventory;
                if (equip != null) {
                    inventory = (byte) (equip.position != -1 ? 0 : 1);
                } else {
                    inventory = Item.getInventoryType(itemId).getType();
                }
                Inventory inv = inventories.get(characterInventory.inventories[inventory]);
                int slot = inv.getFreeSlot();
                inv.itemEntityIDs[slot] = itemEntityId;
                inv.itemIds[slot] = itemId;
            }
            generate(rs, characterInventory);
        }
    }

    public void expandInventory(int playerEntityId, CharacterInventory.Type type, byte delta) {
        if (delta < 0) return;

        CharacterInventory characterInventory = charInventories.get(playerEntityId);
        Inventory inventory = inventories.get(characterInventory.inventories[type.getType()]);

        byte newSize = (byte) (inventory.itemEntityIDs.length + delta);
        // New expanded size must be larger than before
        if (newSize < inventory.itemEntityIDs.length) return;

        Integer[] newEntityArray = new Integer[newSize];
        Integer[] newItemIdArray = new Integer[newSize];

        inventory.lock.writeLock().lock();

        try {
            System.arraycopy(inventory.itemEntityIDs, 0, newEntityArray, 0, inventory.itemEntityIDs.length);
            System.arraycopy(inventory.itemIds, 0, newItemIdArray, 0, inventory.itemIds.length);
        } finally {
            inventory.lock.writeLock().unlock();
        }

    }

    public void addInventoryInfo(final OutboundPacket mplew, int entityId) {
//        MapleInventory iv = chr.getInventory(MapleInventoryType.EQUIPPED);
//        Collection<Item> equippedC = iv.list();
//        List<Item> equipped = new ArrayList<>(equippedC.size());
//        List<Item> equippedCash = new ArrayList<>(equippedC.size());
//        for (Item item : equippedC) {
//            if (item.getPosition() <= -100) {
//                equippedCash.add((Item) item);
//            } else {
//                equipped.add((Item) item);
//            }
//        }
//        Collections.sort(equipped);
        CharacterInventory characterInventory = charInventories.get(entityId);
        int[] inventoryIds = characterInventory.inventories;
        Inventory equipped = inventories.get(inventoryIds[CharacterInventory.Type.EQUIPPED.getType()]);
        for (short slot = 0; slot < equipped.itemEntityIDs.length; slot++) {
            Integer itemEntityId = equipped.itemEntityIDs[slot];
            if (itemEntityId != null)
                itemInfoEncodingSystem.addItemInfo(mplew, itemEntityId, slot, false);
        }
        mplew.writeShort(0); // start of equip cash TODO: equip cash
//        for (Item item : equippedCash) {
//            addItemInfo(mplew, item);
//        }
        mplew.writeShort(0); // start of equip inventory
//        for (Item item : chr.getInventory(MapleInventoryType.EQUIP).list()) {
//            addItemInfo(mplew, item);
//        }
//
        mplew.writeShort(0); //Evan Dragon inventory
//        for (Item item : equipped) {
//            if (item.getPosition() > -1004 && item.getPosition() <= -1000) {
//                addItemInfo(mplew, item);
//            }
//        }
        mplew.writeShort(0);
//        for (Item item : chr.getInventory(MapleInventoryType.USE).list()) {
//            addItemInfo(mplew, item);
//        }
        mplew.write(0);
//        for (Item item : chr.getInventory(MapleInventoryType.SETUP).list()) {
//            addItemInfo(mplew, item);
//        }
        mplew.write(0);
//        for (Item item : chr.getInventory(MapleInventoryType.ETC).list()) {
//            addItemInfo(mplew, item);
//        }
        mplew.write(0);
//        for (Item item : chr.getInventory(MapleInventoryType.CASH).list()) {
//            addItemInfo(mplew, item);
//        }
        mplew.write(0);
    }

    @Override
    protected void processSystem() {

    }
}
