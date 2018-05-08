package systems;

import com.artemis.BaseSystem;
import net.packets.OutboundPacket;

public class TeleportRockSystem extends BaseSystem {
    @Override
    protected void processSystem() {

    }

    public static void addTeleportInfo(final OutboundPacket mplew, int entityId) {
//        final List<Integer> tele = chr.getTrockMaps();
//        final List<Integer> viptele = chr.getVipTrockMaps();
        for (int i = 0; i < 5; i++) {
            mplew.writeInt(999999999);
//            mplew.writeInt(tele.get(i));
        }
        for (int i = 0; i < 10; i++) {
            mplew.writeInt(999999999);
//            mplew.writeInt(viptele.get(i));
        }
    }

}
