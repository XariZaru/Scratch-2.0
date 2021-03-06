package ecs.components.item;

import com.artemis.Component;
import net.packets.OutboundPacket;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Item extends Component implements Comparable<Item> {

	public int itemId;
	public short quantity = 1;

	public void encode(OutboundPacket mplew, ItemOwner itemOwner, ItemFlag itemFlag) {
		mplew.writeShort(quantity);
		mplew.writeMapleAsciiString(itemOwner != null ? itemOwner.owner : "");
		mplew.writeShort(itemFlag.flag); // flag

		if (Item.isRechargable(itemId)) {
			mplew.writeInt(2);
			mplew.write(new byte[]{(byte) 0x54, 0, 0, (byte) 0x34});
		}
	}

	/**
	 * Populates Item component
	 * @param rs
	 * @param item
	 * @return
	 */
	public static void generate(ResultSet rs, Item item) throws SQLException {
		item.itemId = rs.getInt("itemid");
		item.quantity = rs.getShort("quantity");
	}

	public static boolean isRechargable(int itemId) {
		return isThrowingStar(itemId) || isBullet(itemId);
	}

	public static boolean isThrowingStar(int itemId) { return itemId / 10000 == 207; }

	public static boolean isBullet(int itemId) {
		return itemId / 10000 == 233;
	}

	public static CharacterInventory.Type getInventoryType(int itemId) {
		byte cat = (byte) (itemId / 1000000);
		return CharacterInventory.Type.getByType(cat);
	}

	@Override
	public int compareTo(Item other) {
		if (this.itemId < other.itemId) {
			return -1;
		} else if (this.itemId > other.itemId) {
			return 1;
		}
		return quantity - other.quantity;
	}

	public enum Type {
		EQUIP(1), BUNDLE(2), PET(3);

		byte type;
		Type(int type) {
			this.type = (byte) type;
		}

		public byte getValue() {
			return type;
		}

	}

}
