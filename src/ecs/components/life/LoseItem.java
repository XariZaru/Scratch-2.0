package ecs.components.life;

import com.artemis.Component;

import java.util.LinkedList;

public class LoseItem extends Component {

    public LinkedList<LostItem> items = new LinkedList<>();

    public static class LostItem {
        private final int id;
        private final byte prop;
        private final byte x;

        public LostItem(int id, byte prop, byte x) {
            this.id = id;
            this.prop = prop;
            this.x = x;
        }
    }

}
