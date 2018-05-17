package ecs.components.map;

import com.artemis.Component;
import io.netty.util.internal.ConcurrentSet;

public class CharacterPool extends Component {
    public ConcurrentSet<Integer> characters = new ConcurrentSet();

    public void addPlayer(int entity) {
        characters.add(entity);
    }
}
