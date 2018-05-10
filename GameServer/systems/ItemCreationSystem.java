package systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import components.item.Equip;
import components.item.Item;
import ecs.EntityCreationSystem;
import tools.Pair;

public class ItemCreationSystem extends BaseEntitySystem {

	EntityCreationSystem entities;
	public ComponentMapper<Item> items;
	public ComponentMapper<Equip> equips;
	
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
		return new Pair<Integer, Equip>(e, equip);
	}
	
	public Item getItem(int e) {
		return items.get(e);
	}
	
	public Equip getEquip(int e) {
		return equips.get(e);
	}

}
