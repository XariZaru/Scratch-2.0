package ecs.components.life;

import com.artemis.Component;
import tools.Pair;

import java.util.ArrayList;
import java.util.List;

public class MonsterSkill extends Component {
    public List<Pair<Integer, Integer>> skills = new ArrayList();
}
