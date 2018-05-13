package ecs.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import ecs.EntityCreationSystem;
import ecs.WorldManager;
import ecs.components.item.*;
import tools.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemCreationSystem extends BaseEntitySystem {

	EntityCreationSystem entities;
	public WorldManager libraryWorldManager = null;
	ComponentMapper<Item> items;			ComponentMapper<Expiration> expirations;
	ComponentMapper<Equip> equips;			ComponentMapper<ItemFlag> flags;
	ComponentMapper<ItemLevel> itemLevels;
	
	public ItemCreationSystem() {
		super(Aspect.all());
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void processSystem() {
		// TODO Auto-generated method stub

	}
	
	public Pair<Integer, Item> createItem(int itemId) {
		int e = entities.create();
		Item item = items.create(e);
		item.itemId = itemId;
		return new Pair<Integer, Item>(e, item);
	}
	
	public Pair<Integer, Equip> createEquip(int itemId) {
		Pair<Integer, Item> itemInfo = createItem(itemId);
		int e = itemInfo.left;
		Equip equip = equips.create(e);
        ItemLibrarySystem librarySystem = null;
        if ((librarySystem = world.getSystem(ItemLibrarySystem.class)) == null)
			librarySystem = libraryWorldManager.getSystem(ItemLibrarySystem.class);
        librarySystem.populateEquip(itemId, e, world);
		return new Pair<Integer, Equip>(e, equip);
	}

	public int generate(ResultSet rs) throws SQLException {
		int itemId = rs.getInt("itemid");
		ItemLibrarySystem librarySystem;
		if ((librarySystem = libraryWorldManager.getSystem(ItemLibrarySystem.class)) == null)
			librarySystem = world.getSystem(ItemLibrarySystem.class);

		if (librarySystem.exists(itemId)) {
			int itemEntityId = entities.create();

			// Create Item
			Item item = items.create(itemEntityId);
			Item.generate(rs, item);

			// Create Equip if it is an equip
			if (Equip.isEquip(itemId)) {
				Equip equip = equips.create(itemEntityId);
				Equip.generate(rs, equip);
			}

			// Create Expiration if exists
			long expiration = rs.getLong("expiration"); // TODO: do expirations, owner name, etc.
			if (expiration != -1) {
				Expiration expiration1 = expirations.create(itemEntityId);
				Expiration.generate(rs, expiration1);
			}

			short flagValue = rs.getShort("flag");
			ItemFlag flag = flags.create(itemEntityId);
			flag.flag = flagValue;

			byte level = rs.getByte("itemlevel");
			if (level != 0) {
				itemLevels.create(itemEntityId).level = level;
			}

			return itemEntityId;
		}
		return -1;
	}
	
	public Item getItem(int e) {
		return items.get(e);
	}
	
	public Equip getEquip(int e) {
		return equips.get(e);
	}

}
