package net.packets;

import ecs.components.Pipeline;
import net.ClientType;
import net.opcodes.MasterServerOpcode;

public class MasterServerPacketCreator {	
	
	public static OutboundPacket loginResult(boolean success) {
		OutboundPacket packet = new OutboundPacket();
		packet.writeShort(230);
		return packet;
	}
	
	public static OutboundPacket serverStatusRequest(int e, short world) {
		OutboundPacket packet = new OutboundPacket();
		packet.writeShort(MasterServerOpcode.GAME_SERVER_STATUS_REQUEST.getValue());
		packet.writeInt(e);
		packet.writeByte(world);
		return packet;
	}
	
	public static OutboundPacket clientType(ClientType clientType) {
		OutboundPacket buf = new OutboundPacket();
		buf.writeShort(MasterServerOpcode.HANDSHAKE.getValue());
		buf.writeByte(clientType.getValue());
		return buf;
	}
	
	/**
	 * Sent by the LoginServer to MasterServer to request information on all currently connected game servers
	 * @param entity ID of entity requesting this information
	 * @return
	 */
	public static OutboundPacket masterServerListRequest(int entity) {
		OutboundPacket buf = new OutboundPacket();
		buf.writeShort(MasterServerOpcode.SERVER_LIST_REQUEST.getValue());
		buf.writeInt(entity);
		return buf;
	}

	public static OutboundPacket connectClientToServer(int clientEntityId, Pipeline pipe, int dbId) {
        OutboundPacket buf = new OutboundPacket();
        buf.writeShort(MasterServerOpcode.CONNECT_CLIENT_TO_SERVER.getValue());
        buf.writeInt(dbId);
        buf.writeInt(clientEntityId);
        buf.writeByte(pipe.world);
        buf.writeByte(pipe.server);
        return buf;
	}

}
