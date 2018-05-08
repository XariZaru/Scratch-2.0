package components.item;

import com.artemis.Component;
import components.Expiration;
import net.packets.OutboundPacket;
import tools.StringUtil;

import static systems.ItemInfoEncodingSystem.addExpirationTime;

public class Pet extends Component {
    public int dbId;
    public byte level;
    public String name;
    public byte fullness;
    public short closeness;

    public void encode(OutboundPacket mplew, Expiration expiration) {
        mplew.writeAsciiString(StringUtil.getRightPaddedStr(name, '\0', 13));
        mplew.write(level);
        mplew.writeShort(closeness);
        mplew.write(fullness);
        addExpirationTime(mplew, expiration);
        mplew.writeShort(0); //nPetAttribute
        mplew.writeShort(0);//usPetSkill
        mplew.writeInt(0); //nRemainLife
        mplew.writeShort(0);//nAttribute
    }
}
