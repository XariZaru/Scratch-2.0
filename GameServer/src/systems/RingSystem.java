package systems;

import com.artemis.BaseSystem;
import net.packets.OutboundPacket;

public class RingSystem extends BaseSystem {
    @Override
    protected void processSystem() {

    }

    public static void addRingInfo(final OutboundPacket mplew, int entityId) {
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.writeShort(0);
//        mplew.writeShort(chr.getCrushRings().size());
//        for (MapleRing ring : chr.getCrushRings()) {
//            mplew.writeInt(ring.getPartnerId());
//            mplew.writeAsciiString(getRightPaddedStr(ring.getPartnerName(), '\0', 13));
//            mplew.writeLong(ring.getRingId());
//            mplew.writeLong(ring.getPartnerRingId());
//        }
//        mplew.writeShort(chr.getFriendshipRings().size());
//        for (MapleRing ring : chr.getFriendshipRings()) {
//            mplew.writeInt(ring.getPartnerId());
//            mplew.writeAsciiString(getRightPaddedStr(ring.getPartnerName(), '\0', 13));
//            mplew.writeLong(ring.getRingId());
//            mplew.writeLong(ring.getPartnerRingId());
//            mplew.writeInt(ring.getItemId());
//        }
//        mplew.writeShort(chr.getWeddingRing() != null ? 1 : 0);
//        int marriageId = 30000; // TODO: update with actual marriage id
//        if (chr.getWeddingRing() != null) {
//            mplew.writeInt(marriageId);//dwMarriageN
//            mplew.writeInt(chr.getGender() == 0 ? chr.getId() : chr.getWeddingRing().getPartnerId());//dwGroomID
//            mplew.writeInt(chr.getGender() == 1 ? chr.getId() : chr.getWeddingRing().getPartnerId());//dwBrideID
//            mplew.writeShort(3); // 3 for married 1 for engaged
//            mplew.writeInt(chr.getGender() == 0 ? chr.getWeddingRing().getRingId() : chr.getWeddingRing().getPartnerRingId()); // nGroomItemID
//            mplew.writeInt(chr.getGender() == 1 ? chr.getWeddingRing().getRingId() : chr.getWeddingRing().getPartnerRingId()); //nBrideItemID
//            mplew.writeAsciiString(getRightPaddedStr(chr.getGender() == 0 ? chr.getName() : chr.getWeddingRing().getPartnerName(), '\0', 13)); //sGroomName
//            mplew.writeAsciiString(getRightPaddedStr(chr.getGender() == 0 ? chr.getWeddingRing().getPartnerName() : chr.getName(), '\0', 13)); //sBrideName
//        }
    }

}
