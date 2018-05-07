package components;

import com.artemis.Component;
import constants.ScratchConstants;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.packets.OutboundPacket;

public class WorldInformation extends Component {

	public byte world;
	public int flag = 3;
	public String msg = "";
	public final ChannelGroup channels = 
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public void encode(OutboundPacket mplew) {
	  String worldName = ScratchConstants.WORLD_NAMES[world];
	  mplew.write(world);
	  mplew.writeMapleAsciiString(worldName);
	  mplew.write(flag);
	  mplew.writeMapleAsciiString(msg);
	  mplew.write(100); // rate modifier, don't ask O.O!
	  mplew.write(0); // event xp * 2.6 O.O!
	  mplew.write(100); // rate modifier, don't ask O.O!
	  mplew.write(0); // drop rate * 2.6
	  mplew.write(0);
	  mplew.write(channels.size());
	}
	
	public synchronized boolean addChannel(Channel ch) {
		//TODO: remove channel limit cap to 20
		if (channels.size() == 1)
			return false;
		return channels.add(ch);
	}

	public Channel getChannel(byte num) {
		byte counter = 0;
		for (Channel ch : channels) {
			if (counter == num) return ch;
			counter++;
		}
		return null;
	}
	
}
