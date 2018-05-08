package net.components.character;

import com.artemis.Component;

public class CharacterStat extends Component {
	public byte level = 1;
	public short str = 12, dex = 4, luk = 4, intel = 4;
	public short mp = 5, hp = 50, maxHp = 50, maxMp = 5, fame;
	public short remainingAp;	
	public int exp, gachaExp;
	
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
	        for (Type stat : Type.values()) {
	            if (stat.getValue() == value) {
	                return stat;
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
