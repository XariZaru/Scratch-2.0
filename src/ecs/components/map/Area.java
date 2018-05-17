package ecs.components.map;

import com.artemis.Component;
import provider.MapleData;
import provider.MapleDataTool;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Area extends Component {
    public List<Rectangle> areas = new ArrayList<Rectangle>();

    public void add(MapleData a) {
        int x1 = MapleDataTool.getInt(a.getChildByPath("x1"));
        int y1 = MapleDataTool.getInt(a.getChildByPath("y1"));
        int x2 = MapleDataTool.getInt(a.getChildByPath("x2"));
        int y2 = MapleDataTool.getInt(a.getChildByPath("y2"));
        areas.add(new Rectangle(x1, y1, (x2 - x2), (y2 - y1)));
    }

}
