package ecs.components.character;

import com.artemis.Component;
import net.packets.OutboundPacket;

public class CharacterStat extends Component {
	public byte level = 1;
	public short str = 12, dex = 4, luk = 4, intel = 4;
	public short mp = 5, hp = 50, maxHp = 50, maxMp = 5, fame;
	public short remainingAp;	
	public int exp, gachaExp;
	public byte[] slotLimits;
	
	public void encode(OutboundPacket mplew, ecs.components.character.CharacterJob job) {
		mplew.write(level); // level
		mplew.writeShort(job.type.getId()); // job
		mplew.writeShort(str);// str
		mplew.writeShort(dex); // dex
		mplew.writeShort(intel); // int
		mplew.writeShort(luk); // luk
		mplew.writeShort(hp); // hp (?)
		mplew.writeShort(maxHp); // maxhp
		mplew.writeShort(mp); // mp (?)
		mplew.writeShort(maxMp); // maxmp
		mplew.writeShort(remainingAp); // remaining ap
        //        if (GameConstants.hasSPTable(job.job)) {
//            mplew.write(chr.getRemainingSpSize());
//            for (int i = 0; i < chr.getRemainingSps().length; i++) {
//                if (chr.getRemainingSpBySkill(i) > 0) {
//                    mplew.write(i + 1);
//                    mplew.write(chr.getRemainingSpBySkill(i));
//                }
//            }
//        } else {
        // TODO: SP table belongs to Evan if you ever want to write it in
        mplew.writeShort(job.getRemainingSp()); // remaining sp
//        }
        mplew.writeInt(exp); // current exp
        mplew.writeShort(fame); // fame
        mplew.writeInt(gachaExp); //Gacha Exp
	}
	
	public enum Type {

	    SKIN(0x1),
	    FACE(0x2),
	    HAIR(0x4),
	    LEVEL(0x10),
	    JOB(0x20),
	    STR(0x40),
	    DEX(0x80),
	    INT(0x100),
	    LUK(0x200),
	    HP(0x400),
	    MAXHP(0x800),
	    MP(0x1000),
	    MAXMP(0x2000),
	    AVAILABLEAP(0x4000),
	    AVAILABLESP(0x8000),
	    EXP(0x10000),
	    FAME(0x20000),
	    MESO(0x40000),
	    PET(0x180008),
	    GACHAEXP(0x200000);
	    private final int i;

	    Type(int i) {
	        this.i = i;
	    }

	    public int getValue() {
	        return i;
	    }

	    public static Type getByValue(int value) {
	        for (Type statType : Type.values()) {
	            if (statType.getValue() == value) {
	                return statType;
	            }
	        }
	        return null;
	    }

	    public static Type getBy5ByteEncoding(int encoded) {
	        switch (encoded) {
	            case 64:
	                return STR;
	            case 128:
	                return DEX;
	            case 256:
	                return INT;
	            case 512:
	                return LUK;
	        }
	        return null;
	    }

	    public static Type getByString(String type) {
	        switch (type) {
	            case "SKIN":
	                return SKIN;
	            case "FACE":
	                return FACE;
	            case "HAIR":
	                return HAIR;
	            case "LEVEL":
	                return LEVEL;
	            case "JOB":
	                return JOB;
	            case "STR":
	                return STR;
	            case "DEX":
	                return DEX;
	            case "INT":
	                return INT;
	            case "LUK":
	                return LUK;
	            case "HP":
	                return HP;
	            case "MAXHP":
	                return MAXHP;
	            case "MP":
	                return MP;
	            case "MAXMP":
	                return MAXMP;
	            case "AVAILABLEAP":
	                return AVAILABLEAP;
	            case "AVAILABLESP":
	                return AVAILABLESP;
	            case "EXP":
	                return EXP;
	            case "FAME":
	                return FAME;
	            case "MESO":
	                return MESO;
	            case "PET":
	                return PET;
	        }
	        return null;
	    }
	}

}
