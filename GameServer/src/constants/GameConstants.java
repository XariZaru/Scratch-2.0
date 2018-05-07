package constants;

import components.character.CharacterJob;

public class GameConstants {

	public static int getSkillBook(final int job) {
		if (job >= 2210 && job <= 2218) {
			return job - 2209;
		}
		return 0;
	}
	
	public static boolean hasSPTable(CharacterJob.Type type) {
		switch (type) {
		case EVAN:
		case EVAN1:
		case EVAN2:
		case EVAN3:
		case EVAN4:
		case EVAN5:
		case EVAN6:
		case EVAN7:
		case EVAN8:
		case EVAN9:
		case EVAN10:
			return true;
		default:
			return false;
		}
	}
	
}
