package ecs.components.map;

import com.artemis.Component;

import java.util.ArrayList;

public class MonsterPool extends Component {
    public ArrayList<Integer> mobEntityIds = new ArrayList<>();
    public void addMobEntity(int entity) {
        mobEntityIds.add(entity);
    }
}
