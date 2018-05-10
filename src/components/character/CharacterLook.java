package components.character;

import com.artemis.Component;

public class CharacterLook extends Component {

	public byte gender;
	public int face; 
	public int hair, hairColor;
	public SkinColor skin;
	
	public enum SkinColor {
		NORMAL(0), DARK(1), BLACK(2), PALE(3), BLUE(4), GREEN(5), WHITE(9), PINK(10), ARAN(11);
	    final int id;

	    SkinColor(int id) {
	        this.id = id;
	    }

	    public int getId() {
	        return id;
	    }

	    public static SkinColor getById(int id) {
	        try {
	        	return SkinColor.values()[id];
	        } catch (Exception e) {
	        	return null;
	        }
	    }
	}
	
}


