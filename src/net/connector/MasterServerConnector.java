package net.connector;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import constants.ScratchConstants;
import net.ClientType;
import net.Server;

public class MasterServerConnector extends net.connector.Connector {

	public Server owner;

	public MasterServerConnector(ClientType clientType, ChannelInitializer<SocketChannel> handler) {
		super(clientType, ScratchConstants.MASTER_SERVER_IP, ScratchConstants.MASTER_SERVER_PORT, handler);
		this.owner = owner;
	}

	
	
}
