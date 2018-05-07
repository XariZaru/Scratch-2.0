package net.opcodes;

public enum MasterServerOpcode {
	
	HANDSHAKE(0),
	SERVER_LIST_REQUEST(1),
	GAME_SERVER_LIST_REQUEST(2),
	GAME_SERVER_LIST_RESPONSE(3),
	GAME_SERVER_STATUS_REQUEST(4),
	GAME_SERVER_STATUS_RESPONSE(5),
	CONNECT_CLIENT_TO_SERVER(6);
	
	
	private int value;

	private MasterServerOpcode(int value) {
		this.value = value;		
	}

	public int getValue() {
		return value;
	}
	
	public static MasterServerOpcode getOpcode(int value) {
		try {
			return MasterServerOpcode.values()[value];
		} catch (Exception e) {
			return null;
		}
	}

}
