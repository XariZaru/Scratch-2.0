package ecs.components.library;

import com.artemis.Component;

import java.util.HashMap;
import java.util.Map;

public class EquipStaticProperties extends Component implements Cloneable {
    public Map<String, Integer> properties = new HashMap<>();
    public int cursed, success, fs;
    public boolean cash, expireOnLogout, accountSharable;
    public int only, quest;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
