package ecs.components.life;

import com.artemis.Component;

public class SelfDestruction extends Component {
    public byte action;
    public int removeAfter = -1;
    public int hp = -1;
}
