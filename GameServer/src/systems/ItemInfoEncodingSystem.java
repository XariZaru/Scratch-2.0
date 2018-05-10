package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import components.Expiration;
import components.item.*;
import net.packets.OutboundPacket;

import static net.packets.MaplePacketCreator.getTime;

public class ItemInfoEncodingSystem extends BaseSystem {

    ComponentMapper<CashItem> cashItems;        ComponentMapper<ItemOwner> itemOwners;
    ComponentMapper<Item> items;                ComponentMapper<ItemLevel> itemLevels;
    ComponentMapper<Equip> equips;              ComponentMapper<ItemFlag> flags;
    ComponentMapper<Pet> pets;
    ComponentMapper<Ring> rings;
    ComponentMapper<Expiration> expirations;

    public static void main(String[] args) {
        try {
            ItemInfo itemInfo = new ItemInfo();
            ItemInfo itemInfo1 = new ItemInfo();
            System.out.println(itemInfo + " " +itemInfo1);
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    public void addItemInfo(final OutboundPacket mplew, int itemEntityId, short pos, boolean zeroPosition) {
        CashItem cashItem = cashItems.get(itemEntityId);        ItemOwner itemOwner = itemOwners.get(itemEntityId);
        Item item = items.get(itemEntityId);                    ItemLevel itemLevel = itemLevels.get(itemEntityId);
        Equip equip = equips.get(itemEntityId);                 ItemFlag flag = flags.get(itemEntityId);
        Pet pet = pets.get(itemEntityId);
        Ring ring = rings.get(itemEntityId);
        Expiration expiration = expirations.get(itemEntityId);
//        ItemLibrarySystem ii = ItemLibrarySystem.getInstance();
//        boolean isCash = ii.isCash(item.getItemId());
//        boolean isPet = item.getPetId() > -1;
//        boolean isRing = false;
//        if (item.getType() == 1) {
//            equip = (Equip) item;
//            isRing = equip.getRingId() > -1;
//        }
        if (!zeroPosition) {
            if (equip != null) {
                mplew.writeShort(pos);
            } else {
                mplew.write(pos);
            }
        }

        mplew.write(pet != null ? 3 : 2);
        mplew.writeInt(item.itemId);
        mplew.writeBool(cashItem != null);

        if (pet != null) {
            mplew.writeLong(pet.dbId);
        } else if (ring != null) {
            mplew.writeLong(ring.dbId);
        } else if (cashItem != null) {
            mplew.writeLong(cashItem.sn);
        }

        ItemInfoEncodingSystem.addExpirationTime(mplew, expiration);

        if (pet != null) {
            pet.encode(mplew, expiration);
        } else if (equip == null) {
            item.encode(mplew, itemOwner, flag);
        } else {
            equip.encode(mplew, itemOwner, itemLevel, flag);
            if (cashItem != null) {
                mplew.writeLong(0);
            }
            mplew.writeLong(getTime(-2));
            mplew.writeInt(cashItem.bonusExp); // this is nPrevBonusExpRate TODO: is bonus exp only in cash items?
        }

    }

    public static void addExpirationTime(final OutboundPacket mplew, Expiration expiration) {
        long time = expiration != null ? expiration.expiration : -1;
        mplew.writeLong(getTime(time));
    }

    @Override
    protected void processSystem() {

    }

    public static class ItemInfo {

    }

}
