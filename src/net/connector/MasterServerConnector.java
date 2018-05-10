package net.connector;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import src.constants.ScratchConstants;
import src.net.ClientType;
import src.net.Server;

public class MasterServerConnector extends net.connector.Connector {

	public Server owner;

	public MasterServerConnector(ClientType clientType, ChannelInitializer<SocketChannel> handler) {
		super(clientType, ScratchConstants.MASTER_SERVER_IP, ScratchConstants.MASTER_SERVER_PORT, handler);
		this.owner = owner;
	}

	
	
}
