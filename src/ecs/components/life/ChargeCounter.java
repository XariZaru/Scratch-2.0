package ecs.components.life;

import com.artemis.Component;

public class ChargeCounter extends Component {
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int counter;
}
