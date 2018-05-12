package ecs.components.item;

import com.artemis.Component;
import net.packets.OutboundPacket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Equip extends Component {
    public Map<String, Short> properties = new HashMap<>();
    public boolean tradeBlock;
	public short hp, mp;
	public short acc, avoid, hands, speed, jump, vicious;
	public byte upgradeSlots, successfulUpgrades;
	public byte position;

	public void encode(OutboundPacket mplew, ItemOwner itemOwner, ItemLevel itemLevel, ItemFlag flag) {
		mplew.write(upgradeSlots); // upgrade slots
		mplew.write(successfulUpgrades); // scroll successes
		mplew.writeShort(getProperty("STR")); // str
		mplew.writeShort(getProperty("DEX")); // dex
		mplew.writeShort(getProperty("INT")); // int
		mplew.writeShort(getProperty("LUK")); // luk
		mplew.writeShort(hp); // hp
		mplew.writeShort(mp); // mp
		mplew.writeShort(getProperty("PAD")); // watk
		mplew.writeShort(getProperty("MAD")); // matk
		mplew.writeShort(getProperty("PDD")); // wdef
		mplew.writeShort(getProperty("MDD")); // mdef
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
        equip.setProperty("MAD", rs.getShort("mAtk"));
        equip.setProperty("PAD", rs.getShort("wAtk"));
        equip.setProperty("PDD", rs.getShort("wDef"));
        equip.setProperty("MDD", rs.getShort("mDef"));
        equip.hp = rs.getShort("hp");
        equip.mp = rs.getShort("mp");
        equip.setProperty("STR", rs.getShort("str"));
        equip.setProperty("DEX", rs.getShort("dex"));
        equip.setProperty("INT", rs.getShort("int"));
        equip.setProperty("LUK", rs.getShort("luk"));
        equip.successfulUpgrades = rs.getByte("successfulUpgrades");
        equip.upgradeSlots = rs.getByte("slots");
        equip.position = rs.getByte("equipPos");
    }

    public short getProperty(String property) {
	    Short value = properties.get(property);
	    return value == null ? 0 : value;
    }

    public void setProperty(String property, short value) {
	    properties.put(property, value);
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

    public Map<String, Short> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Short> properties) {
	    this.properties.clear();
	    this.properties.putAll(properties);
    }

    public boolean isTradeBlock() {
        return tradeBlock;
    }

    public void setTradeBlock(boolean tradeBlock) {
        this.tradeBlock = tradeBlock;
    }

    public short getHp() {
        return hp;
    }

    public void setHp(short hp) {
        this.hp = hp;
    }

    public short getMp() {
        return mp;
    }

    public void setMp(short mp) {
        this.mp = mp;
    }

    public short getAcc() {
        return acc;
    }

    public void setAcc(short acc) {
        this.acc = acc;
    }

    public short getAvoid() {
        return avoid;
    }

    public void setAvoid(short avoid) {
        this.avoid = avoid;
    }

    public short getHands() {
        return hands;
    }

    public void setHands(short hands) {
        this.hands = hands;
    }

    public short getSpeed() {
        return speed;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public short getJump() {
        return jump;
    }

    public void setJump(short jump) {
        this.jump = jump;
    }

    public short getVicious() {
        return vicious;
    }

    public void setVicious(short vicious) {
        this.vicious = vicious;
    }

    public byte getUpgradeSlots() {
        return upgradeSlots;
    }

    public void setUpgradeSlots(byte upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
    }

    public byte getSuccessfulUpgrades() {
        return successfulUpgrades;
    }

    public void setSuccessfulUpgrades(byte successfulUpgrades) {
        this.successfulUpgrades = successfulUpgrades;
    }

    public byte getPosition() {
        return position;
    }

    public void setPosition(byte position) {
        this.position = position;
    }
}
