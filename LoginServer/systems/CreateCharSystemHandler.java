package systems;

import com.artemis.ComponentMapper;
import components.*;
import components.character.CharacterJob;
import components.character.CharacterLook;
import components.character.CharacterStat;
import database.DatabaseConnection;
import io.netty.channel.Channel;
import net.Key;
import net.PacketHandler;
import net.opcodes.SendOpcode;
import net.packets.InboundPacket;
import net.packets.OutboundPacket;
import requests.CreateCharRequest;
import tools.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CreateCharSystemHandler extends PacketHandler {

    private ComponentMapper<CreateCharRequest> requests;

    // TODO: Look at consequences of setting auto commit true always. Might have issues when only char is inserted but not equipStats

    private static Set<Integer> IDs = new HashSet<>(Arrays.asList(
            1302000, 1312004, 1322005, 1442079,// weapons
            1040002, 1040006, 1040010, 1041002, 1041006, 1041010, 1041011, 1042167,// bottom
            1060002, 1060006, 1061002, 1061008, 1062115, // top
            1072001, 1072005, 1072037, 1072038, 1072383,// shoes
            30000, 30010, 30020, 30030, 31000, 31040, 31050,// hair
            20000, 20001, 20002, 21000, 21001, 21002, 21201, 20401, 20402, 21700, 20100 //face
            //#NeverTrustStevenCode
    ));

    public CreateCharSystemHandler() {
        super(CreateCharRequest.class);
    }

    @Override
    public void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound) {
        String name = inBound.readMapleAsciiString();
        if (CheckCharNameSystemHandler.charNameInUse(name)) return;
        CreateCharRequest request = requests.create(channel.attr(Key.ENTITY).get());
        request.name = name;
        request.ch = channel;
        request.packet = inBound;
    }

    @Override
    protected void process(int e) {
        try {
            CreateCharRequest request = requests.get(e);
            Channel ch = request.ch;
            InboundPacket packet = request.packet;

            if (CheckCharNameSystemHandler.charNameInUse(request.name)) {
                ch.writeAndFlush(CheckCharNameSystemHandler.charNameResponse(request.name, true));
                return;
            }

            Client client = ch.attr(Key.CLIENT).get();
            Pipeline pipe = ch.attr(Key.PIPELINE).get();
            Name name = new Name();
            name.name = request.name;

            CharacterJob job = new CharacterJob();
            CharacterStat stat = new CharacterStat();
            CharacterLook look = new CharacterLook();
            Location location = new Location();

            job.type = setJobDefaults(job, location, packet.readInt());
            look.face = packet.readInt();
            look.hair = packet.readInt();
            look.hairColor = packet.readInt();
            look.skin = CharacterLook.SkinColor.getById(packet.readInt());

            // Process incoming default equipStats. Can be packet edited, so check for legal items. Disconnect if packet editing.

            List<Pair<Integer, Integer>>[] equipped = new ArrayList[] {new ArrayList(), new ArrayList()};
            Pair<Integer, Integer> items[] =  new Pair[] {
                            /* top */   new Pair(5, packet.readInt()), new Pair(6, packet.readInt()), /* bottom */
                            /* shoes */ new Pair(7, packet.readInt()), new Pair(11, packet.readInt())}; // weapon

            for (Pair<Integer, Integer> item : items) {
                if (!CreateCharSystemHandler.isLegal(item.right)) {
                    ch.disconnect();
                    return;
                }
            }

            equipped[0].addAll(Arrays.asList(items));

            look.gender = packet.readByte();
            Integer id = insertChar(client, pipe, job, look, stat, location, name);

            DatabaseId dbId = new DatabaseId();
            dbId.dbId = id;

            insertItems(items, dbId, client);

            request.ch.writeAndFlush(CreateCharSystemHandler.addNewCharEntry(job, look, client, name, stat, location, dbId, equipped));
         } finally {
            requests.remove(e);
        }
    }

    private void insertItems(Pair<Integer, Integer>[] items, DatabaseId dbId, Client client) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO items (chrid, accountid, type, pos, itemid) VALUES (?,?,?,?,?)")) {
            con.setAutoCommit(true);
            for (Pair<Integer, Integer> item : items) {
                ps.setInt(1, dbId.dbId);
                ps.setInt(2, client.accountId);
                ps.setInt(3, 0);
                ps.setInt(4, item.left);
                ps.setInt(5, item.right);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private CharacterJob.Type setJobDefaults(CharacterJob job, Location location, int jobId) {
        if (jobId == 0) { // Cygnus
            job.type = CharacterJob.Type.NOBLESSE;
            location.mapId = 130030000;
//            Pair<Integer, Item> item = itemCreator.createItem(4161047);
//            inventories.addSingle(etcInventory, item.left);
        } else if (jobId == 1) { // Explorer
            job.type = CharacterJob.Type.BEGINNER;
            location.mapId = 10000;
//            Pair<Integer, Item> item = itemCreator.createItem(4161001);
//            inventories.addSingle(etcInventory, item.left);
        } else if (jobId == 2) { // Aran
            job.type = CharacterJob.Type.LEGEND;
            location.mapId = 914000000;
//            Pair<Integer, Item> item = itemCreator.createItem(4161048);
//            inventories.addSingle(etcInventory, item.left);
        }
        return job.type;
    }

    private Integer insertChar(Client client, Pipeline pipe, CharacterJob job, CharacterLook look,
                               CharacterStat stat, Location location, Name name) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO characters ("
                             + "str, dex, luk, `int`, "
                             + "hp, mp, maxHp, maxMp,"
                             + "job,"
                             + "skin, gender, hair, face, "
                             + "map, accountId, name, world) "
                             + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                     , PreparedStatement.RETURN_GENERATED_KEYS)) {
            con.setAutoCommit(true);
            ps.setInt(1, 12);
            ps.setInt(2, 5);
            ps.setInt(3, 4);
            ps.setInt(4, 4);
            ps.setShort(5, stat.hp);
            ps.setShort(6, stat.mp);
            ps.setShort(7, stat.maxHp);
            ps.setShort(8, stat.maxMp);
            ps.setInt(9, job.type.getId());
            ps.setInt(10, look.skin.getId());
            ps.setByte(11, look.gender);
            ps.setInt(12, look.hair);
            ps.setInt(13, look.face);
            ps.setInt(14, location.mapId);
            ps.setInt(15, client.accountId);
            ps.setString(16, name.name);
            ps.setByte(17, pipe.world);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isLegal(int toCompare) {
        return IDs.contains(toCompare);
    }

    private static OutboundPacket addNewCharEntry(CharacterJob job, CharacterLook look, Client client, Name name, CharacterStat stat
            , Location location, DatabaseId dbId, List<Pair<Integer, Integer>>[] equipped) {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(SendOpcode.ADD_NEW_CHAR_ENTRY.getValue());
        mplew.write(0);
        CharListRequestSystemHandler.addCharEntry(mplew, job, look, client, name, stat, location, dbId, equipped, false, false);
        return mplew;
    }

}