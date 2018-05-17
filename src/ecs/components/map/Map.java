package ecs.components.map;

import com.artemis.Component;
import provider.MapleData;

public class Map extends Component {
    public MapleData data;
    public String bgm;
    public FootholdTree footholdTree;
    public int returnedMap, fieldLimit;
    public int mapId;
    public boolean respawns = true;
}
