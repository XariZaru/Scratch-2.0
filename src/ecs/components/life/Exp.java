package ecs.components.life;

import com.artemis.Component;

public class Exp extends Component {
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int amount;
}
