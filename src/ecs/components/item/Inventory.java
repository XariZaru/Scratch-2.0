package ecs.components.item;

import com.artemis.Component;

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
	
}
