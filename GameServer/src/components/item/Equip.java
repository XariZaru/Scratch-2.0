package components.item;

import com.artemis.Component;
import net.packets.OutboundPacket;

public class Equip extends Component {
	public short str, dex, luk, intel;
	public short hp, mp;
	public short watk, matk, wdef, mdef;
	public short acc, avoid, hands, speed, jump, vicious;
	public byte upgradeSlots, successfulUpgrades;
	public byte position;

	public void encode(OutboundPacket mplew, ItemOwner itemOwner, ItemLevel itemLevel, ItemFlag flag) {
		mplew.write(upgradeSlots); // upgrade slots
		mplew.write(successfulUpgrades); // scroll successes
		mplew.writeShort(str); // str
		mplew.writeShort(dex); // dex
		mplew.writeShort(intel); // int
		mplew.writeShort(luk); // luk
		mplew.writeShort(hp); // hp
		mplew.writeShort(mp); // mp
		mplew.writeShort(watk); // watk
		mplew.writeShort(matk); // matk
		mplew.writeShort(wdef); // wdef
		mplew.writeShort(mdef); // mdef
		mplew.writeShort(acc); // accuracy
		mplew.writeShort(avoid); // avoid
		mplew.writeShort(hands); // hands
		mplew.writeShort(speed); // speed
		mplew.writeShort(jump); // jump
		mplew.writeMapleAsciiString(itemOwner.owner); // itemOwner name
		mplew.writeShort(flag.flag); //Item Flags
		mplew.writeBool(false); //nLevelUpType TODO: hasSkill ????
		mplew.write(itemLevel.level); //Item Level
		mplew.writeInt(itemLevel.exp  * 100000); // TODO: why is this multiplied by an int??
		mplew.writeInt(vicious);
	}

}
