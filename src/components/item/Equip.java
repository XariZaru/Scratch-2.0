package components.item;

import com.artemis.Component;
import net.packets.OutboundPacket;

import java.sql.ResultSet;
import java.sql.SQLException;

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

	public static void generate(ResultSet rs, Equip equip) throws SQLException {
        equip.acc = rs.getShort("acc");
        equip.avoid = rs.getShort("avoid");
        equip.hands = rs.getShort("hands");
        equip.speed = rs.getShort("speed");
        equip.jump = rs.getShort("jump");
        equip.vicious = rs.getShort("vicious");
        equip.matk = rs.getShort("mAtk");
        equip.watk = rs.getShort("wAtk");
        equip.wdef = rs.getShort("wDef");
        equip.mdef = rs.getShort("mDef");
        equip.hp = rs.getShort("hp");
        equip.mp = rs.getShort("mp");
        equip.str = rs.getShort("str");
        equip.dex = rs.getShort("dex");
        equip.intel = rs.getShort("int");
        equip.luk = rs.getShort("luk");
        equip.successfulUpgrades = rs.getByte("successfulUpgrades");
        equip.upgradeSlots = rs.getByte("slots");
        equip.position = rs.getByte("equipPos");
    }

	public static boolean isEquip(int itemId) {
	    return itemId / 1000000 == 1;
    }
	
	public static WeaponType getWeaponType(int itemId) {
        int cat = (itemId / 10000) % 100;
        WeaponType[] type
            = {WeaponType.SWORD1H,
            WeaponType.GENERAL1H_SWING,
            WeaponType.GENERAL1H_SWING,
            WeaponType.DAGGER_OTHER,
            WeaponType.NOT_A_WEAPON,
            WeaponType.NOT_A_WEAPON,
            WeaponType.NOT_A_WEAPON,
            WeaponType.WAND,
            WeaponType.STAFF,
            WeaponType.NOT_A_WEAPON,
            WeaponType.SWORD2H,
            WeaponType.GENERAL2H_SWING,
            WeaponType.GENERAL2H_SWING,
            WeaponType.SPEAR_STAB,
            WeaponType.POLE_ARM_SWING,
            WeaponType.BOW,
            WeaponType.CROSSBOW,
            WeaponType.CLAW,
            WeaponType.KNUCKLE,
            WeaponType.GUN};
        if (cat < 30 || cat > 49) {
            return WeaponType.NOT_A_WEAPON;
        }
        return type[cat - 30];
    }

    public enum WeaponType {
        NOT_A_WEAPON(0),
        GENERAL1H_SWING(4.4),
        GENERAL1H_STAB(3.2),
        GENERAL2H_SWING(4.8),
        GENERAL2H_STAB(3.4),
        BOW(3.4),
        CLAW(3.6),
        CROSSBOW(3.6),
        DAGGER_THIEVES(3.6),
        DAGGER_OTHER(4),
        GUN(3.6),
        KNUCKLE(4.8),
        POLE_ARM_SWING(5.0),
        POLE_ARM_STAB(3.0),
        SPEAR_STAB(5.0),
        SPEAR_SWING(3.0),
        STAFF(3.6),
        SWORD1H(4.0),
        SWORD2H(4.6),
        WAND(3.6);
        private double damageMultiplier;

        WeaponType(double maxDamageMultiplier) {
            this.damageMultiplier = maxDamageMultiplier;
        }

        public double getMaxDamageMultiplier() {
            return damageMultiplier;
        }
    }

}
