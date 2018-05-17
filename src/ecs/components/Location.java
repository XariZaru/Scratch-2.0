package ecs.components;

import com.artemis.Component;
import net.packets.OutboundPacket;

import java.awt.*;

public class Location extends Component {
	public int mapId;
	public byte portal;

	public int fh, rx0, rx1, cy, f;
	public Point pos;
	public boolean hidden;

	public void encode(OutboundPacket mplew) {
		mplew.writeInt(mapId); // current map id
		mplew.write(portal); // spawnpoint
	}

}
