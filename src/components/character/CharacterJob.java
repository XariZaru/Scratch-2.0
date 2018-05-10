package components.character;

import com.artemis.Component;
import constants.GameConstants;

public class CharacterJob extends Component {

	public Type type;
	public short[] remainingSp = new short[10];
	
	public short getRemainingSp() {
		return remainingSp[GameConstants.getSkillBook(type.getId())];
	}
	
	public enum Type {
		BEGINNER(0),

		WARRIOR(100),
		FIGHTER(110), CRUSADER(111), HERO(112),
		PAGE(120), WHITEKNIGHT(121), PALADIN(122),
		SPEARMAN(130), DRAGONKNIGHT(131), DARKKNIGHT(132),

		MAGICIAN(200),
		FP_WIZARD(210), FP_MAGE(211), FP_ARCHMAGE(212),
		IL_WIZARD(220), IL_MAGE(221), IL_ARCHMAGE(222),
		CLERIC(230), PRIEST(231), BISHOP(232),

		BOWMAN(300),
		HUNTER(310), RANGER(311), BOWMASTER(312),
		CROSSBOWMAN(320), SNIPER(321), MARKSMAN(322),

		THIEF(400),
		ASSASSIN(410), HERMIT(411), NIGHTLORD(412),
		BANDIT(420), CHIEFBANDIT(421), SHADOWER(422),

		PIRATE(500),
		BRAWLER(510), MARAUDER(511), BUCCANEER(512),
		GUNSLINGER(520), OUTLAW(521), CORSAIR(522),

		UNKOWN(800),
		GM(900), SUPERGM(910), MAPLE_WATCH_LEAF_BRIGADIE(920),

		NOBLESSE(1000),
		DAWNWARRIOR1(1100), DAWNWARRIOR2(1110), DAWNWARRIOR3(1111), DAWNWARRIOR4(1112),
		BLAZEWIZARD1(1200), BLAZEWIZARD2(1210), BLAZEWIZARD3(1211), BLAZEWIZARD4(1212),
		WINDARCHER1(1300), WINDARCHER2(1310), WINDARCHER3(1311), WINDARCHER4(1312),
		NIGHTWALKER1(1400), NIGHTWALKER2(1410), NIGHTWALKER3(1411), NIGHTWALKER4(1412),
		THUNDERBREAKER1(1500), THUNDERBREAKER2(1510), THUNDERBREAKER3(1511), THUNDERBREAKER4(1512),

		LEGEND(2000), EVAN(2001),
		ARAN1(2100),ARAN2(2110), ARAN3(2111), ARAN4(2112),

		EVAN1(2200), EVAN2(2210), EVAN3(2211), EVAN4(2212), EVAN5(2213), EVAN6(2214),
		EVAN7(2215), EVAN8(2216), EVAN9(2217), EVAN10(2218);

		final int jobid;

		private Type(int id) {
		    jobid = id;
		}

		public int getId() {
		    return jobid;
		}

		public static String getName(Type mjob) {
		    return mjob.name();
		}

		public static Type getById(int id) {
		    for (Type l : Type.values()) {
		        if (l.getId() == id) {
		            return l;
		        }
		    }
		    return null;
		}

		public static boolean checkJobMask(int mask, Type toCheck) {
			int maskToCheck = getBy5ByteEncoding(toCheck);
			return (mask & maskToCheck) == maskToCheck;
		}

		public static int getBy5ByteEncoding(Type job) {
		    switch (job) {
		        case WARRIOR:
				case FIGHTER:
				case CRUSADER:
				case HERO:
				case PAGE:
				case WHITEKNIGHT:
				case PALADIN:
				case SPEARMAN:
				case DRAGONKNIGHT:
				case DARKKNIGHT:
		            return 2;
		        case MAGICIAN:
				case FP_WIZARD:
				case FP_MAGE:
				case FP_ARCHMAGE:
				case IL_WIZARD:
				case IL_MAGE:
				case IL_ARCHMAGE:
				case CLERIC:
				case PRIEST:
				case BISHOP:
		            return 4;
		        case BOWMAN:
				case HUNTER:
				case RANGER:
				case BOWMASTER:
				case CROSSBOWMAN:
				case SNIPER:
				case MARKSMAN:
		            return 8;
		        case THIEF:
				case ASSASSIN:
				case HERMIT:
				case NIGHTLORD:
				case BANDIT:
				case CHIEFBANDIT:
				case SHADOWER:
		            return 16;
		        case PIRATE:
				case BRAWLER:
				case MARAUDER:
				case BUCCANEER:
				case GUNSLINGER:
				case OUTLAW:
				case CORSAIR:
		            return 32;
		        case NOBLESSE:
		            return 1024;
		        case DAWNWARRIOR1:
				case DAWNWARRIOR2:
				case DAWNWARRIOR3:
				case DAWNWARRIOR4:
		            return 2048;
		        case BLAZEWIZARD1:
				case BLAZEWIZARD2:
				case BLAZEWIZARD3:
				case BLAZEWIZARD4:
		            return 4096;
		        case WINDARCHER1:
				case WINDARCHER2:
				case WINDARCHER3:
				case WINDARCHER4:
		            return 8192;
		        case NIGHTWALKER1:
				case NIGHTWALKER2:
				case NIGHTWALKER3:
				case NIGHTWALKER4:
		            return 16348;
		        case THUNDERBREAKER1:
				case THUNDERBREAKER2:
				case THUNDERBREAKER3:
				case THUNDERBREAKER4:
		            return 32768;
		        default:
		            return 1;
		    }
		}

		public boolean isBeginner(Type beginners) {
			return MAGICIAN == beginners || WARRIOR == beginners || THIEF == beginners || PIRATE == beginners || BOWMAN == beginners || ARAN1 == beginners || THUNDERBREAKER1 == beginners
					|| DAWNWARRIOR1 == beginners || NIGHTWALKER1 == beginners || BLAZEWIZARD1 == beginners;
		 }

		public boolean isA(Type basejob) {        
		    return getId() >= basejob.getId() && getId() / 100 == basejob.getId() / 100;
		}

		public int getBeginnerJob() {
			return (int) Math.floor(getId() / 1000);
		}
	}

}