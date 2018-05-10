package components.item;

import com.artemis.Component;
import net.packets.OutboundPacket;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Item extends Component {

	public int itemId;
	public short quantity = 1;

	public void encode(OutboundPacket mplew, ItemOwner itemOwner, ItemFlag itemFlag) {
		mplew.writeShort(quantity);
		mplew.writeMapleAsciiString(itemOwner.owner);
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
	
}