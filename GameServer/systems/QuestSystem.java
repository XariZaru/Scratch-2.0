package systems;

import com.artemis.BaseSystem;
import net.packets.OutboundPacket;

public class QuestSystem extends BaseSystem {
    @Override
    protected void processSystem() {

    }

    public static void addQuestInfo(final OutboundPacket mplew, int entityId) {
        mplew.writeShort(0);
//        mplew.writeShort(chr.getStartedQuestsSize());
//        for (MapleQuestStatus q : chr.getStartedQuests()) {
//            mplew.writeShort(q.getQuest().getId());
//            mplew.writeMapleAsciiString(q.getQuestData());
//            if (q.getQuest().getInfoNumber() > 0) {
//                mplew.writeShort(q.getQuest().getInfoNumber());
//                mplew.writeMapleAsciiString(q.getQuestData());
//            }
//        }
    }

    public static void addCompletedQuestInfo(final OutboundPacket mplew, int entityId) {
        mplew.writeShort(0);
//        List<MapleQuestStatus> completed = chr.getCompletedQuests();
//        mplew.writeShort(completed.size());
//        for (MapleQuestStatus q : completed) {
//            mplew.writeShort(q.getQuest().getId());
//            mplew.writeLong(getTime(q.getCompletionTime()));
//        }
    }


}
