package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import components.Expiration;
import components.item.*;
import net.packets.OutboundPacket;

public class InventorySystem extends BaseSystem {

    ComponentMapper<CashItem> cashItems;        ComponentMapper<ItemOwner> itemOwners;
    ComponentMapper<Item> items;                ComponentMapper<ItemLevel> itemLevels;
    ComponentMapper<Equip> equips;              ComponentMapper<ItemFlag> flags;
    ComponentMapper<Pet> pets;                  ComponentMapper<CharacterInventory> charInventories;
    ComponentMapper<Ring> rings;                ComponentMapper<Inventory> inventories;
    ComponentMapper<Expiration> expirations;

    public void save(int playerEntityId) {
        CharacterInventory characterInventory = charInventories.get(playerEntityId);
        for (int inventoryEntityId : characterInventory.inventories) {
        }
    }

    public static void addInventoryInfo(final OutboundPacket mplew, int entityId) {
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
//        for (Item item : equipped) {
//            addItemInfo(mplew, item);
//        }
        mplew.writeShort(0); // start of equip cash
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
