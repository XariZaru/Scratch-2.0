package net.components;

import com.artemis.Component;

public class Client extends Component {
	public Integer playerEntityId;
	public int accountId, gender, gmLevel;
	public String accountName;

	/**
	 * Maple will only display characters starting at ID 30000 and more. This poses a problem to ECS framework since entities begin at 0.
	 * Have to add 30000 to entity and send it to the client. However, this means when receiving the player's ID from client, must subtract 30000
	 * @return 30000 + playerEntityId
	 */
	public int getMapleEntityId() { return 30000 + playerEntityId; }
}
