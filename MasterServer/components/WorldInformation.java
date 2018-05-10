package components;

import com.artemis.Component;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.Key;
import net.opcodes.MasterServerOpcode;
import net.packets.OutboundPacket;

public class WorldInformation extends Component {

	public byte world;
	public int flag = 3;
	public String msg = "";
	public final ChannelGroup channels = 
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public void encode(OutboundPacket mplew) {
	  String worldName = constants.ScratchConstants.WORLD_NAMES[world];
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
	  encodeChannels(mplew);
	}

	private void encodeChannels(OutboundPacket mplew) {
		byte x = 0;
		for (Channel ch : channels) {
			Integer connected = ch.attr(Key.CONNECTED_AMOUNT).get();
			connected = connected == null ? 0 : connected;
			mplew.writeMapleAsciiString((constants.ScratchConstants.WORLD_NAMES[world] + "-" + (x + 1)));
			mplew.writeInt((connected * 1200) / constants.ScratchConstants.GAME_SERVER_LOAD);
			mplew.write(1);
			mplew.writeShort(x);
			x++;
		}
	}

	public OutboundPacket encodeConnectionInformation(byte channel) {
		OutboundPacket packet = new OutboundPacket();
		Channel ch = getChannel(channel);
		packet.writeShort(MasterServerOpcode.CONNECT_CLIENT_TO_SERVER.getValue());
		packet.writeMapleAsciiString(ch.remoteAddress().toString());
		return packet;
	}

	public OutboundPacket encodeStatus(int e) {
		int status = populationStatus();
		OutboundPacket packet = new OutboundPacket();
		packet.writeShort(MasterServerOpcode.GAME_SERVER_STATUS_RESPONSE.getValue());
		packet.writeInt(e);
		packet.writeShort(status);
		return packet;
	}

	public int populationStatus() {
		int connected = 0;
		for (Channel ch : channels) {
			Integer num = ch.attr(Key.CONNECTED_AMOUNT).get();
			connected += num == null ? 0 : num;
		}

		if (connected >= constants.ScratchConstants.GAME_SERVER_LOAD)
			return 2;
		else if (connected >= constants.ScratchConstants.GAME_SERVER_LOAD * .8)
			return 1;
		return 0;
	}

	public synchronized boolean addChannel(Channel ch) {
		//TODO: remove channel limit cap to 20
		if (channels.size() == 1)
			return false;
		ch.attr(Key.CHANNEL_NUMBER).set(channels.size());
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
