package ecs.components.life;

import com.artemis.Component;

public class Banish extends Component {

    public BanishInfo banishInfo;

    public static class BanishInfo {

        private int type = 0;
        private int map;
        private String portal, msg;

        public BanishInfo(String msg, int map, String portal) {
            this.msg = msg;
            this.map = map;
            this.portal = portal;
        }

        public BanishInfo(int type, String msg, int map, String portal) {
            this.type = type;
            this.msg = msg;
            this.map = map;
            this.portal = portal;
        }

        public int getType() {
            return type;
        }

        public int getMap() {
            return map;
        }

        public String getPortal() {
            return portal;
        }

        public String getMsg() {
            return msg;
        }
    }

}
