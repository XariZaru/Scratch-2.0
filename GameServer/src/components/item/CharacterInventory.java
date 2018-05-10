package components.item;

import com.artemis.Component;

public class CharacterInventory extends Component {

	/* Each int references an entity ID that is associated with
	   another entity which contains the respective inventory component.
	   The reason why this exists is to allow re-usability of the
	   inventory component with merchants, shops, stores, etc.
	 */
	public int[] inventories = new int[Type.values().length - 1]; // Size 5 atm if ignoring UNKNOWN
	
	public enum Type {
		EQUIPPED(0), EQUIP(1), USE(2), SETUP(3), ETC(4), CASH(5), UNKNOWN(6);
		final byte type;
		Type(int type) {
			this.type = (byte) type;
		}
		
		public byte getType() {
			return type;
		}
		
		public short getBitfieldEncoding() {
	        return (short) (2 << type);
	    }
		
		public static Type getByType(byte type) {
	        for (Type l : Type.values()) {
	            if (l.getType() == type) {
	                return l;
	            }
	        }
	        return UNKNOWN;
	    }
		
		public static Type getByWZName(String name) {
	        if (name.equals("Install")) {
	            return SETUP;
	        } else if (name.equals("Consume")) {
	            return USE;
	        } else if (name.equals("Etc")) {
	            return ETC;
	        } else if (name.equals("Cash")) {
	            return CASH;
	        } else if (name.equals("Pet")) {
	            return CASH;
	        }
	        return UNKNOWN;
	    }
		
	}
}
