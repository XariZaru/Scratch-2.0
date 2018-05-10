package net.packets;

import com.artemis.Component;
import io.netty.channel.Channel;

public class PacketTask extends Component {
	public InboundPacket inPacket;
	public OutboundPacket outPacket;
	public Component component;
	public Channel ch;
}
