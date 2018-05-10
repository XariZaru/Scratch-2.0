package net;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import io.netty.channel.Channel;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;
import net.packets.PacketTask;

public abstract class PacketHandler extends IteratingSystem {

	protected ComponentMapper<PacketTask> packetTasks;

	public PacketHandler() { super(Aspect.all(PacketTask.class)); }
	public PacketHandler(Class<? extends Component> conditions) {
		super(Aspect.all(conditions));
	}

	public abstract void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound);

	@Override
	protected void process(int arg0) {
		System.out.println("Processing for " + this);
		packetTasks.remove(arg0);
	}

	public void create(Channel ch, InboundPacket packet, OutboundPacket outboundPacket) {
		int value = ch.attr(Key.ENTITY).get();
		PacketTask task = packetTasks.create(value);
		task.inPacket = packet;
		task.ch = ch;
		task.outPacket = outboundPacket;
	}
	
}
