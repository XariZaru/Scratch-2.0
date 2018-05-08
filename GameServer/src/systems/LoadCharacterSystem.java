package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import database.DatabaseConnection;
import io.netty.channel.Channel;
import net.Key;
import net.components.Client;
import net.components.DatabaseId;
import net.components.Location;
import net.components.Name;
import net.components.character.CharacterJob;
import net.components.character.CharacterLook;
import net.components.character.CharacterStat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadCharacterSystem extends BaseSystem {

    ComponentMapper<Client> clients;
    ComponentMapper<CharacterLook> looks;
    ComponentMapper<CharacterStat> stats;
    ComponentMapper<CharacterJob> jobs;
    ComponentMapper<Name> names;
    ComponentMapper<Location> locations;
    ComponentMapper<DatabaseId> dbIds;

    @Override
    protected void processSystem() {

    }

    public void loadCharacter(int dbId, Channel ch) {
        final int entityId = ch.attr(Key.ENTITY).get();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE id = ? LIMIT 1")) {
            ps.setInt(1, dbId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client client = clients.create(entityId);
                    ch.attr(Key.CLIENT).set(client);
                    generate(rs, entityId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generate(ResultSet rs, int entityId) throws SQLException {
        CharacterLook look = looks.create(entityId);
        look.face = rs.getInt("face");
        look.gender = rs.getByte("gender");
        look.skin = CharacterLook.SkinColor.getById(rs.getInt("skin"));
        look.hair = rs.getInt("hair");

        CharacterJob job = jobs.create(entityId);
        CharacterStat stat = stats.create(entityId);

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

        DatabaseId dbId = dbIds.create(entityId);
        dbId.dbId = rs.getInt("id");

        Name name = names.create(entityId);
        name.name = rs.getString("name");

        Location location = locations.create(entityId);
        location.mapId = rs.getInt("map");
        location.portal = rs.getByte("portal");
    }

}
