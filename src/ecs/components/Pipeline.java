package ecs.components;

import com.artemis.Component;
import io.netty.channel.Channel;
import net.packets.OutboundPacket;

import java.util.Random;

public class Pipeline extends Component {
	
	public Channel channel;
	public int nextPacketSize, sessionId;
	public byte world, server;
	
	public Pipeline() {
		sessionId = new Random().nextInt();
	}
	
	public void write(OutboundPacket msg) {
		channel.writeAndFlush(msg);		
	}
	
}
