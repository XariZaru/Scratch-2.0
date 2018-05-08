package net.components;

import com.artemis.Component;
import net.packets.OutboundPacket;

public class Location extends Component {
	public int mapId;
	public byte portal;

	public void encode(OutboundPacket mplew) {
		mplew.writeInt(mapId); // current map id
		mplew.write(portal); // spawnpoint
	}

}
