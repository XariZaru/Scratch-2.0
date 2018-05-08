package systems;

import com.artemis.BaseSystem;
import net.packets.OutboundPacket;

public class MonsterBookSystem extends BaseSystem {
    @Override
    protected void processSystem() {

    }

    public static void addMonsterBookInfo(final OutboundPacket mplew, int entityId) {
        mplew.write(0);
        mplew.writeShort(0);
//        mplew.write(0);
//        Map<Integer, Integer> cards = chr.getMonsterBook().getCards();
//        mplew.writeShort(cards.size());
//        for (Entry<Integer, Integer> all : cards.entrySet()) {
//            mplew.writeShort(all.getKey() % 10000); // Id
//            mplew.write(all.getValue()); // Level
//        }
//        /*
//         if the first byte above is set to 1
//          mplew.writeShort(0);
//          mplew.write(0);
//          decode buffer of the size of the byte above
//          deocde 1
//          decode buffer of the decode 1 above
//         */
    }

}
