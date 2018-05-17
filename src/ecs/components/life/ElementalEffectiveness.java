package ecs.components.life;

import com.artemis.Component;

import java.util.HashMap;
import java.util.Map;

public class ElementalEffectiveness extends Component {

    public Map<Element, Effectiveness> resistance = new HashMap<>();

    public enum Effectiveness {
        NORMAL, IMMUNE, STRONG, WEAK, NEUTRAL;

        public static Effectiveness getByNumber(int num) {
            switch (num) {
                case 1:
                    return IMMUNE;
                case 2:
                    return STRONG;
                case 3:
                    return WEAK;
                case 4:
                    return NEUTRAL;
                default:
                    throw new IllegalArgumentException("Unkown effectiveness: " + num);
            }
        }
    }

}
