package ecs.components.item;

import com.artemis.Component;
import com.artemis.World;
import ecs.system.ItemInfoEncodingSystem;
import net.packets.OutboundPacket;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Inventory extends Component {
	public final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	// Contains reference IDs to entities with the Item Component
	public Integer[] itemEntityIDs;
	public Integer[] itemIds;
	
	public int getFreeSlot() {

		lock.readLock().lock();

		try {

			if (itemIds == null) return -1;
			for (int x = 0; x < itemIds.length; x++)
				if (itemIds[x] == null) return x;
			return -1;

		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Called when first created to instantiate with a specific size. Should only be called from one location. (LoadCharacterSystem atm).
	 * @param size Size of inventory
	 */
	public void instantiate(byte size) {
		itemEntityIDs = new Integer[size];
		itemIds = new Integer[size];
	}

	public void encode(OutboundPacket mplew, ItemInfoEncodingSystem encodingSystem) {
		for (short slot = 0; slot < itemEntityIDs.length; slot++) {
			Integer itemEntityId = itemEntityIDs[slot];
			if (itemEntityId != null)
				encodingSystem.addItemInfo(mplew, itemEntityId, slot, false);
		}
	}

	public void destroy(World world) {
		for (Integer itemEntityId : itemEntityIDs) {
			if (itemEntityId != null)
				world.delete(itemEntityId);
		}
	}

}
