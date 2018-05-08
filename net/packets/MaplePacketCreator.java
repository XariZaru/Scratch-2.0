package net.packets;

import constants.ScratchConstants;
import net.components.Client;
import net.components.DatabaseId;
import net.components.Location;
import net.components.Name;
import net.components.character.CharacterJob;
import net.components.character.CharacterLook;
import net.components.character.CharacterStat;
import net.opcodes.SendOpcode;

import java.net.InetAddress;

public class MaplePacketCreator {

    private final static long FT_UT_OFFSET = 116444592000000000L; // EDT
    private final static long DEFAULT_TIME = 150842304000000000L;//00 80 05 BB 46 E6 17 02
    public final static long ZERO_TIME = 94354848000000000L;//00 40 E0 FD 3B 37 4F 01
    private final static long PERMANENT = 150841440000000000L; // 00 C0 9B 90 7D E5 17 02

    /**
     * Sends a hello packet.
     *
     * @param sendIv the IV used by the server for sending
     * @param recvIv the IV used by the server for receiving
     * @return
     */
    public static OutboundPacket handshake(byte[] sendIv, byte[] recvIv) {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(0x0E);
        mplew.writeShort(ScratchConstants.VERSION);
        mplew.writeString(ScratchConstants.PATCH);
//        mplew.write(49);
        mplew.write(recvIv);
        mplew.write(sendIv);
        mplew.write(8);
        return mplew;
    }

    /**
     * Gets a login failed packet.
     *
     * Possible values for <code>reason</code>:<br> 3: ID deleted or blocked<br>
     * 4: Incorrect password<br> 5: Not a registered id<br> 6: System error<br>
     * 7: Already logged in<br> 8: System error<br> 9: System error<br> 10:
     * Cannot process so many connections<br> 11: Only users older than 20 can
     * use this channel<br> 13: Unable to log on as master at this ip<br> 14:
     * Wrong gateway or personal info and weird korean button<br> 15: Processing
     * request with that korean button!<br> 16: Please verify your account
     * through email...<br> 17: Wrong gateway or personal info<br> 21: Please
     * verify your account through email...<br> 23: License agreement<br> 25:
     * Maple Europe notice =[ FUCK YOU NEXON<br> 27: Some weird full client
     * notice, probably for trial versions<br>
     *
     * @param reason The reason logging in failed.
     *
     * @return The login failed packet.
     */
    public static OutboundPacket getLoginFailed(int reason) {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(SendOpcode.LOGIN_STATUS.getValue());
        mplew.write(reason);
        mplew.write(0);
        mplew.writeInt(0);
        return mplew;
    }

    /**
     * Gets a successful authentication and PIN Request packet.
     *
     * @param c
     *
     * @return The PIN request packet.
     */
    public static OutboundPacket getAuthSuccess(Client c) {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(SendOpcode.LOGIN_STATUS.getValue());
        mplew.writeInt(0);
        mplew.writeShort(0);
        mplew.writeInt(c.accountId); //user id
        mplew.write(c.gender);
        mplew.writeByte(c.gmLevel); //admin byte
        short toWrite = (short) (1 * 64);
        mplew.write(toWrite >= 0x80 ? 0x80 : 0);//0x80 is admin, 0x20 and 0x40 = subgm
        mplew.writeByte(c.gmLevel);
        mplew.writeMapleAsciiString(c.accountName);
        mplew.write(0);
        mplew.write(0); //isquietbanned
        mplew.writeLong(0);//isquietban time
        mplew.writeLong(0); //creation time
        mplew.writeInt(0);
        mplew.writeShort(2);//PIN
        return mplew;
    }

    /**
     * Gets a packet saying that the server list is over.
     *
     * @return The end of server list packet.
     */
    public static OutboundPacket getEndOfServerList() {
        final OutboundPacket mplew = new OutboundPacket(3);
        mplew.writeShort(SendOpcode.SERVERLIST.getValue());
        mplew.write(0xFF);
        return mplew;
    }

    public static long getTime(long realTimestamp) {
        if (realTimestamp == -1) {
            return DEFAULT_TIME;//high number ll
        } else if (realTimestamp == -2) {
            return ZERO_TIME;
        } else if (realTimestamp == -3) {
            return PERMANENT;
        }
        return realTimestamp * 10000 + FT_UT_OFFSET;
    }

    public static OutboundPacket selectWorld(int world) {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(SendOpcode.LAST_CONNECTED_WORLD.getValue());
        mplew.writeInt(world);//According to GMS, it should be the world that contains the most characters (most active)
        return mplew;
    }

    public static OutboundPacket sendRecommended() {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(SendOpcode.RECOMMENDED_WORLD_MESSAGE.getValue());
        mplew.write(0);//size
//        for (Iterator<Pair<Integer, String>> it = worlds.iterator(); it.hasNext();) {
//            Pair<Integer, String> world = it.next();
//            mplew.writeInt(world.getLeft());
//            mplew.writeMapleAsciiString(world.getRight());
//        }
        return mplew;
    }

    public static OutboundPacket getServerStatus(int status) {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(SendOpcode.SERVERSTATUS.getValue());
        mplew.writeShort(status);
        return mplew;
    }
//
//    public static void addCharEntry(final OutboundPacket mplew
//            , CharacterJob job, CharacterLook look, Client client, Name name, CharacterStat stat
//            , Location location, DatabaseId dbId, boolean viewall, boolean aprilFools) {
//
//        addCharStats(mplew, name, look, stat, job, location, dbId);
//        addCharLook(mplew, look, false);
//        addCharEquips(mplew, null);
//        if (!viewall) {
//            mplew.write(0);
//        }
//
//        mplew.write(0);
//        return;
////        if (chr.isGM()) {
////            mplew.write(0);
////            return;
////        }
////
////        mplew.write(1); // world rank enabled (next 4 ints are not sent if disabled) Short??
////        mplew.writeInt(1);
////        mplew.writeInt(1);
////        mplew.writeInt(1);
////        mplew.writeInt(1);
//
////        mplew.writeInt(chr.getRank()); // world rank
////        mplew.writeInt(chr.getRankMove()); // move (negative is downwards)
////        mplew.writeInt(chr.getJobRank()); // job rank
////        mplew.writeInt(chr.getJobRankMove()); // move (negative is downwards)
//    }

//    private static void addCharEquips(OutboundPacket mplew, List<Pair<Short, Integer>>[] inventory) {
//        // TODO: finish non-char select equips loading including pets
////        Collection<Item> ii = MapleItemInformationProvider.getInstance().canWearEquipment(chr, equip.list());
////        Map<Short, Integer> myEquip = new LinkedHashMap<>();
////        Map<Short, Integer> maskedEquip = new LinkedHashMap<>();
////        for (Item item : ii) {
////            short pos = (byte) (item.getPosition() * -1);
////            if (pos < 100 && myEquip.get(pos) == null) {
////                myEquip.put(pos, item.getItemId());
////            } else if (pos > 100 && pos != 111) { // don't ask. o.o
////                pos -= 100;
////                if (myEquip.get(pos) != null) {
////                    maskedEquip.put(pos, myEquip.get(pos));
////                }
////                myEquip.put(pos, item.getItemId());
////            } else if (myEquip.get(pos) != null) {
////                maskedEquip.put(pos, item.getItemId());
////            }
////        }
////        for (Entry<Short, Integer> entry : myEquip.entrySet()) {
////            mplew.write(entry.getKey());
////            mplew.writeInt(entry.getValue());
////        }
//        mplew.write(0xFF);
////        for (Entry<Short, Integer> entry : maskedEquip.entrySet()) {
////            mplew.write(entry.getKey());
////            mplew.writeInt(entry.getValue());
////        }
//        mplew.write(0xFF);
////        Item cWeapon = equip.getItem((short) -111);
////        mplew.writeInt(cWeapon != null ? cWeapon.getItemId() : 0);
//        mplew.writeInt(0);
//        for (int i = 0; i < 3; i++) {
////            if (chr.getPet(i) != null) {
////                mplew.writeInt(chr.getPet(i).getItemId());
////            } else {
//            mplew.writeInt(0);
////            }
//        }
//    }

    public static OutboundPacket connect(InetAddress ip, int port, int dbId) {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(SendOpcode.SERVER_IP.getValue());
        mplew.writeShort(0);
        mplew.write(ip.getAddress());
        mplew.writeShort(port);
        mplew.writeInt(dbId);
        mplew.write(new byte[] {0, 0, 0, 0, 0});
        return mplew;
    }

    public static void addCharLook(final OutboundPacket mplew, CharacterLook look, boolean mega) {
        mplew.write(look.gender);
        mplew.write(look.skin.getId()); // skin color
        mplew.writeInt(look.face); // face
        mplew.write(mega ? 0 : 1);
        mplew.writeInt(look.hair); // hair
    }

    public static void addCharStats(final OutboundPacket mplew
            , Name name, CharacterLook look, CharacterStat stat, CharacterJob job
            , Location location, DatabaseId dbId) {
        mplew.writeInt(dbId.dbId); // character id
        mplew.writeAsciiString(tools.StringUtil.getRightPaddedStr(name.name, '\0', 13));
        mplew.write(look.gender); // gender (0 = male, 1 = female)
        mplew.write(look.skin.getId()); // skin color
        mplew.writeInt(look.face); // face
        mplew.writeInt(look.hair);// hair
        for (int i = 0; i < 3; i++) {
            // TODO: write in pets
//            if (chr.getPet(i) != null) {//Checked GMS.. and your pets stay when going into the cash shop.
//                mplew.writeLong(chr.getPet(i).getUniqueId());
//            } else {
            mplew.writeLong(0);
//            }
        }
        mplew.write(stat.level); // level
        mplew.writeShort(job.type.getId()); // job
        mplew.writeShort(stat.str);// str
        mplew.writeShort(stat.dex); // dex
        mplew.writeShort(stat.intel); // int
        mplew.writeShort(stat.luk); // luk
        mplew.writeShort(stat.hp); // hp (?)
        mplew.writeShort(stat.maxHp); // maxhp
        mplew.writeShort(stat.mp); // mp (?)
        mplew.writeShort(stat.maxMp); // maxmp
        mplew.writeShort(stat.remainingAp); // remaining ap
//        if (GameConstants.hasSPTable(job.job)) {
//            mplew.write(chr.getRemainingSpSize());
//            for (int i = 0; i < chr.getRemainingSps().length; i++) {
//                if (chr.getRemainingSpBySkill(i) > 0) {
//                    mplew.write(i + 1);
//                    mplew.write(chr.getRemainingSpBySkill(i));
//                }
//            }
//        } else {
        // TODO: SP table belongs to Evan if you ever want to write it in
        mplew.writeShort(job.getRemainingSp()); // remaining sp
//        }
        mplew.writeInt(stat.exp); // current exp
        mplew.writeShort(stat.fame); // fame
        mplew.writeInt(stat.gachaExp); //Gacha Exp
        mplew.writeInt(location.mapId); // current map id
        mplew.write(location.portal); // spawnpoint
        mplew.writeInt(0);// playtime in seconds?
    }

}
