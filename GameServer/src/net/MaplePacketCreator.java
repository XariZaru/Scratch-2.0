package net;

import constants.ScratchConstants;
import net.components.Client;
import net.opcodes.SendOpcode;
import net.packets.OutboundPacket;

public class MaplePacketCreator {

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

}
