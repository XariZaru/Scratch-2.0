package systems;

import com.artemis.ComponentMapper;
import components.DatabaseId;
import components.Location;
import components.Name;
import components.character.CharacterJob;
import components.character.CharacterLook;
import components.character.CharacterStat;
import components.item.Inventory;
import components.requests.CharListRequest;
import database.DatabaseConnection;
import io.netty.channel.Channel;
import net.Key;
import net.MaplePacketCreator;
import net.PacketHandler;
import net.components.Client;
import net.components.Pipeline;
import net.opcodes.SendOpcode;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;
import tools.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CharListRequestSystemHandler extends PacketHandler {

    ComponentMapper<Pipeline> pipes;
    ComponentMapper<CharListRequest> requests;

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
     * @throws SQLException
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
            Inventory inventory = new Inventory();


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

            MaplePacketCreator.addCharEntry(packet, job, look, client, name, stat, location, dbId,false, false);
            return loadCharacters(rs, client, packet, ++count);
        } else {
            return count;
        }
    }

    public static void encodeEquips(List<Pair<Short, Integer>>[] inventory) {

        // Equips
        for (Pair<Short, Integer> equip : inventory[0]) {
            
        }

        // Masked Equips
        for (Pair<Short, Integer> maskedEquip : inventory[1]) {

        }

    }
}
