package systems;

import com.artemis.ComponentMapper;
import components.*;
import components.character.CharacterJob;
import components.character.CharacterLook;
import components.character.CharacterStat;
import components.item.CharacterInventory;
import components.requests.CharListRequest;
import database.DatabaseConnection;
import io.netty.channel.Channel;
import net.Key;
import net.PacketHandler;
import net.opcodes.SendOpcode;
import net.packets.InboundPacket;
import net.packets.MaplePacketCreator;
import net.packets.OutboundPacket;
import tools.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CharListRequestSystemHandler extends PacketHandler {

    private ComponentMapper<Pipeline> pipes;
    private ComponentMapper<CharListRequest> requests;

    public CharListRequestSystemHandler() {
        super(CharListRequest.class);
    }

    @Override
    public void receive(Channel channel, InboundPacket msg, OutboundPacket outBound) {
        byte value = msg.readByte(); // 2
        byte world = msg.readByte(); // 0
        byte server = msg.readByte(); // 0

        int e = channel.attr(Key.ENTITY).get();
        Pipeline pipe = pipes.get(e);
        pipe.world = world;
        pipe.server = server;
        requests.create(e).ch = channel;
    }

    @Override
    protected void process(int e) {
        try {
            OutboundPacket mplew = new OutboundPacket();
            CharListRequest request = requests.get(e);
            mplew.writeShort(SendOpcode.CHARLIST.getValue());
            mplew.write(0);

            generate(request.ch, mplew);

            mplew.write(2); // TODO: PIC handling
            mplew.writeInt(6); // TODO: char slot count

            request.ch.writeAndFlush(mplew);
        } finally {
            requests.remove(e);
        }
    }

    private void generate(Channel ch, OutboundPacket packet) {
        // Did * because everything in character table should be written here anyways
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(""
                     + "SELECT * FROM characters WHERE accountId = ? AND world = ?")) {
            con.setAutoCommit(true);
            Client client = ch.attr(Key.CLIENT).get();
            byte world = ch.attr(Key.PIPELINE).get().world;
            ps.setInt(1, client.accountId);
            ps.setInt(2, world);

            OutboundPacket packet2 = new OutboundPacket();
            byte count;
            try (ResultSet rs = ps.executeQuery()) {
                count = loadCharacters(rs, client, packet2, (byte) 0);
            }
            packet.write(count);
            packet.getPacket().writeBytes(packet2.getPacket());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Recursive function that loads over entire ResultSet to process character entries to populate CharList packet
     * @param rs ResultSet to recurse
     * @param count Counter to write to packet to show how many characters exist
     * @return Returns an integer to write number of characters
     * @throws SQLException Throws SQL exception if wrong columns to fetch are specified
     */
    private byte loadCharacters(ResultSet rs, Client client, OutboundPacket packet, byte count) throws SQLException {
        if (rs.next()) {
            CharacterLook look = new CharacterLook();
            look.face = rs.getInt("face");
            look.gender = rs.getByte("gender");
            look.skin = CharacterLook.SkinColor.getById(rs.getInt("skin"));
            look.hair = rs.getInt("hair");

            CharacterJob job = new CharacterJob();
            CharacterStat stat = new CharacterStat();

            String[] splitSp = rs.getString("sp").split(",");
            for (int x = 0; x < splitSp.length; x++)
                try {
                    job.remainingSp[x] = Short.parseShort(splitSp[x]);
                } catch (NumberFormatException e) {

                }
            job.type = CharacterJob.Type.getById(rs.getInt("job"));
            stat.remainingAp = rs.getShort("ap");
            stat.dex = rs.getShort("dex");
            stat.luk = rs.getShort("luk");
            stat.intel = rs.getShort("int");
            stat.str = rs.getShort("str");
            stat.level = rs.getByte("level");
            stat.hp = rs.getShort("hp");
            stat.mp = rs.getShort("mp");
            stat.maxHp = rs.getShort("maxHp");
            stat.maxMp = rs.getShort("maxMp");

            DatabaseId dbId = new DatabaseId();
            dbId.dbId = rs.getInt("id");

            Name name = new Name();
            name.name = rs.getString("name");

            Location location = new Location();
            location.mapId = rs.getInt("map");
            location.portal = rs.getByte("portal");

            List<Pair<Integer, Integer>>[] equipped = generateItems(dbId.dbId);

            CharListRequestSystemHandler.addCharEntry(packet, job, look, client, name, stat, location, dbId, equipped,false, false);
            return loadCharacters(rs, client, packet, ++count);
        } else {
            return count;
        }
    }

    public static void addCharEntry(final OutboundPacket mplew
            , CharacterJob job, CharacterLook look, Client client, Name name, CharacterStat stat
            , Location location, DatabaseId dbId, List<Pair<Integer, Integer>>[] inventory, boolean viewall, boolean aprilFools) {

//        if (aprilFools) {
//            addCharStatsAprilFools(mplew, location, job, stat, name, look);
//            mplew.write(look.gender);
//            mplew.write(look.skinColor.getId()); // skin color
//            mplew.writeInt(20000); // face
//            mplew.write(0);
//            mplew.writeInt(30030); // hair
//            mplew.write(0xFF);
//            mplew.write(0xFF);
//            for (int x = 0; x < 4; x++) mplew.writeInt(0);
////            mplew.skip(16);
//            if (!viewall) {
//                mplew.write(0);
//            }
//            mplew.write(0);
//            return;
//        }

        MaplePacketCreator.addCharStats(mplew, name, look, stat, job, location, dbId);
        MaplePacketCreator.addCharLook(mplew, look, false);
        CharListRequestSystemHandler.addCharEquips(mplew, inventory);

        if (!viewall) {
            mplew.write(0);
        }

        mplew.write(0);
        return; // TODO: this return is early and is meant to load GMs in
//        if (chr.isGM()) {
//            mplew.write(0);
//            return;
//        }
//
//        mplew.write(1); // world rank enabled (next 4 ints are not sent if disabled) Short??
//        mplew.writeInt(1);
//        mplew.writeInt(1);
//        mplew.writeInt(1);
//        mplew.writeInt(1);

//        mplew.writeInt(chr.getRank()); // world rank
//        mplew.writeInt(chr.getRankMove()); // move (negative is downwards)
//        mplew.writeInt(chr.getJobRank()); // job rank
//        mplew.writeInt(chr.getJobRankMove()); // move (negative is downwards)
    }
    private List<Pair<Integer, Integer>>[] generateItems(int dbId) throws SQLException {
        List<Pair<Integer, Integer>>[] equipped = new List[] {new ArrayList(), new ArrayList()};
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT itemid, pos FROM items WHERE chrid = ? AND type = ?")) {
            ps.setInt(1, dbId);
            ps.setByte(2, CharacterInventory.Type.EQUIPPED.getType());
            try (ResultSet rs = ps.executeQuery()) {
                int equippedWeapon = 0;
                while (rs.next()) {
                    int pos = rs.getInt("pos");
                    int itemId = rs.getInt("itemid");

                    if (pos == 11)
                        equippedWeapon = itemId;

                    else
                        // TODO: handle masked equipment
                        equipped[0].add(new Pair(pos, itemId));
                }
                equipped[0].add(new Pair(11, equippedWeapon));
            }
        }
        return equipped;
    }


    public static void addCharEquips(OutboundPacket mplew, List<Pair<Integer, Integer>>[] inventory) {

        Pair<Integer, Integer> equippedWeapon = inventory[0].get(inventory[0].size() - 1);

        // Equips
        for (Pair<Integer, Integer> equip : inventory[0]) {
            mplew.write(equip.left);
            mplew.writeInt(equip.right);
        }

        mplew.write(0xFF);

        // Masked Equips
        for (Pair<Integer, Integer> maskedEquip : inventory[1]) {
            mplew.write(maskedEquip.left);
            mplew.writeInt(maskedEquip.right);
        }

        mplew.write(0xFF);
        mplew.writeInt(equippedWeapon.right);

        if (equippedWeapon.right != 0) {
            System.out.println("Writing " + equippedWeapon.right);
        }

        for (int i = 0; i < 3; i++)
            mplew.writeInt(0);

    }
}
