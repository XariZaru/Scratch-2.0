package components.library;

import com.artemis.Component;

import java.util.HashMap;
import java.util.Map;

public class EquipStat extends Component {
    public Map<String, Integer> properties = new HashMap<>();
    public int cash, upgradesPossible, cursed, success, fs;
    public boolean expireOnLogout, tradeBlock, accountSharable;
    public int level;
    public int only, quest;
}