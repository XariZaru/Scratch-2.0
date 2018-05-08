package systems;

import com.artemis.BaseSystem;
import net.packets.OutboundPacket;

public class CooldownSystem extends BaseSystem {
    @Override
    protected void processSystem() {

    }

    public static void addCooldownInfo(final OutboundPacket mplew, int entityId) {
        mplew.writeShort(0);
//        mplew.writeShort(chr.getAllCooldowns().size());
//        for (PlayerCoolDownValueHolder cooling : chr.getAllCooldowns()) {
//            mplew.writeInt(cooling.skillId);
//            int timeLeft = (int) (cooling.length + cooling.startTime - System.currentTimeMillis());
//            mplew.writeShort(timeLeft / 1000);
//        }
    }

}
