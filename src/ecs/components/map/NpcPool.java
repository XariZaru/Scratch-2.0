package ecs.components.map;

import com.artemis.Component;

import java.util.ArrayList;

public class NpcPool extends Component {
    public ArrayList<Integer> npcEntityIds = new ArrayList<>();
    public void addNpcEntity(int npcEntityId) {
        npcEntityIds.add(npcEntityId);
    }
}
