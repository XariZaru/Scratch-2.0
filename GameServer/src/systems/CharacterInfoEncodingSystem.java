package systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import net.components.Client;
import net.components.DatabaseId;
import net.components.Location;
import net.components.Name;
import net.components.character.CharacterJob;
import net.components.character.CharacterLook;
import net.components.character.CharacterStat;
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
