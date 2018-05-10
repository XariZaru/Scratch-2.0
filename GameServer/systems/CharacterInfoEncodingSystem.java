package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import components.Client;
import components.DatabaseId;
import components.Location;
import components.Name;
import components.character.CharacterJob;
import components.character.CharacterLook;
import components.character.CharacterStat;
import net.packets.MaplePacketCreator;
import net.packets.OutboundPacket;

public class CharacterInfoEncodingSystem extends BaseSystem {

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

    public void encodeStats(OutboundPacket packet, int entityId) {
        Name name = names.get(entityId);        CharacterLook look = looks.get(entityId);
        CharacterJob job = jobs.get(entityId);  CharacterStat stat = stats.get(entityId);
        DatabaseId dbId = dbIds.get(entityId);  Location location = locations.get(entityId);
        MaplePacketCreator.addCharStats(packet, name, look, stat, job, location, dbId);
    }

}
