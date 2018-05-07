package net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import net.coders.PacketDecoder;
import net.coders.PacketEncoder;

public class GameServerTrafficHandler extends ChannelInitializer<SocketChannel> {

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
	 */
	
	@Override
	protected void initChannel(SocketChannel chan) throws Exception {
		ChannelPipeline pipeline = chan.pipeline();
		pipeline.addLast(new PacketDecoder(), new GameServerHandler(), new PacketEncoder());
	}
	
}
