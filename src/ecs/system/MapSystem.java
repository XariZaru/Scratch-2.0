package ecs.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import ecs.components.Location;
import ecs.components.Name;
import ecs.components.life.AddRespawn;
import ecs.components.life.Level;
import ecs.components.life.MonsterStat;
import ecs.components.life.Respawn;
import ecs.components.map.CharacterPool;
import ecs.components.map.FootholdTree;
import ecs.components.map.Map;
import ecs.components.map.MonsterPool;
import net.opcodes.SendOpcode;
import net.packets.MaplePacketCreator;
import net.packets.OutboundPacket;

import java.awt.*;

public class MapSystem extends IntervalIteratingSystem {

    ComponentMapper<Location> locations;    ComponentMapper<Map> maps;
    ComponentMapper<Respawn> respawns;      ComponentMapper<AddRespawn> addRespawns;
    MapLibrarySystem mapLibrarySystem;      LifeLibrarySystem lifeLibrarySystem;

    public MapSystem() {
        super(Aspect.all(Respawn.class), 500);
    }

    @Override
    protected void process(int entityId) {
        Respawn respawn = respawns.get(entityId);
        Location location = locations.get(entityId);
        Map map = getMap(location.mapId);
        MonsterPool monsterPool = mapLibrarySystem.getProperty(MonsterPool.class, map.mapId);

        if (map.respawns) {
            try {
                if (respawn.infinite)
                    addRespawns.create(entityId);
                Point newPos = calcPointBelow(location.pos, map.mapId);
                if (newPos == null) {
                    newPos = new Point(location.pos.x, location.pos.y - 1);
                } else {
                    newPos.y -= 1;
                }
                location.pos = newPos;
                monsterPool.addMobEntity(entityId);

                MonsterStat monsterStat = world.getMapper(MonsterStat.class).get(entityId);
//                System.out.println("Monster " + lifeLibrarySystem.getMonsterName(monsterStat.mobId)
//                        + " at " + location.pos + " - " + location.mapId + ".");
            } finally {
                respawns.remove(entityId);
            }
        }
    }

    public void addSpawn(int e, boolean infinite) {
        Respawn respawn = respawns.create(e);
        respawn.infinite = infinite;
    }

    public void addPlayer(int playerEntityId, int mapId) {
        CharacterPool characterPool = mapLibrarySystem.getProperty(CharacterPool.class, mapId);
        characterPool.addPlayer(playerEntityId);
    }

    public Map getMap(int mapId) {
        return mapLibrarySystem.getMap(mapId);
    }

    private Point calcPointBelow(Point initial, int mapId) {
        FootholdTree footholdTree = mapLibrarySystem.getProperty(FootholdTree.class, mapId);
        FootholdTree.Foothold fh = footholdTree.findBelow(initial);
        if (fh == null)
            return null;

        int dropY = fh.getY1();
        if (!fh.isWall() && fh.getY1() != fh.getY2()) {
            double s1 = Math.abs(fh.getY2() - fh.getY1());
            double s2 = Math.abs(fh.getX2() - fh.getX1());
            double s5 = Math.cos(Math.atan(s2 / s1))
                    * (Math.abs(initial.x - fh.getX1()) / Math.cos(Math.atan(s1 / s2)));
            if (fh.getY2() < fh.getY1()) {
                dropY = fh.getY1() - (int) s5;
            } else {
                dropY = fh.getY1() + (int) s5;
            }
        }
        return new Point(initial.x, dropY);
    }

    public OutboundPacket spawnPlayerMapObject(int playerEntityId) {
        final OutboundPacket mplew = new OutboundPacket();
        Level level = world.getMapper(Level.class).get(playerEntityId);
        Name name = world.getMapper(Name.class).get(playerEntityId);
        mplew.writeShort(SendOpcode.SPAWN_PLAYER.getValue());
        mplew.writeInt(playerEntityId);
        mplew.write(level.level); //v83
        mplew.writeMapleAsciiString(name.name);
//        if (chr.getGuildId() < 1) { TODO: Guild spawn player object info
            mplew.writeMapleAsciiString("");
            mplew.write(new byte[6]);
//        } else {
//            MapleGuildSummary gs = chr.getClient().getWorldServer().getGuildSummary(chr.getGuildId(), chr.getWorld());
//            if (gs != null) {
//                mplew.writeMapleAsciiString(gs.getName());
//                mplew.writeShort(gs.getLogoBG());
//                mplew.write(gs.getLogoBGColor());
//                mplew.writeShort(gs.getLogo());
//                mplew.write(gs.getLogoColor());
//            } else {
//                mplew.writeMapleAsciiString("");
//                mplew.write(new byte[6]);
//            }
//        }

        MaplePacketCreator.writeForeignBuffs(mplew, playerEntityId);

        mplew.writeShort(chr.getJob().getId());
        addCharLook(mplew, chr, false);
        mplew.writeInt(chr.getInventory(MapleInventoryType.CASH).countById(5110000));
        mplew.writeInt(chr.getItemEffect());
        mplew.writeInt(ItemConstants.getInventoryType(chr.getChair()) == MapleInventoryType.SETUP ? chr.getChair() : 0);
        mplew.writePos(chr.getPosition());
        mplew.write(chr.getStance());
        mplew.writeShort(chr.getFh());
        mplew.write(0);// Admin Byte - Just pulls effect from the WZ file if its there.
        for (MaplePet pet : chr.getPets()) {
            if (pet != null) {
                mplew.writeBool(pet.isSummoned());
                if (pet.isSummoned()) {
                    addPetInfo(mplew, chr, pet);
                }
            }
        }
        mplew.write(0); //end of pets, there is a while loop in the idb when going through the pet code
        MapleMount mount = chr.getMount();
        mplew.writeInt(mount != null ? chr.getMount().getLevel() : 1);
        mplew.writeInt(mount != null ? chr.getMount().getExp() : 0);
        mplew.writeInt(mount != null ? chr.getMount().getTiredness() : 0);

        if (chr.getPlayerShop() != null && chr.getPlayerShop().isOwner(chr)) {
            if (chr.getPlayerShop().hasFreeSlot()) {
                addAnnounceBox(mplew, chr.getPlayerShop(), chr.getPlayerShop().getVisitors().length);
            } else {
                addAnnounceBox(mplew, chr.getPlayerShop(), 1);
            }
        } else if (chr.getMiniGame() != null && chr.getMiniGame().isOwner(chr)) {
            if (chr.getMiniGame().hasFreeSlot()) {
                addAnnounceBox(mplew, chr.getMiniGame(), 1, 0, 1, 0);
            } else {
                addAnnounceBox(mplew, chr.getMiniGame(), 1, 0, 2, 1);
            }
        } else {
            mplew.write(0);
        }
        mplew.writeBool(chr.getChalkboard() != null);
        if (chr.getChalkboard() != null) {
            mplew.writeMapleAsciiString(chr.getChalkboard());
        }
        addRingLook(mplew, chr.getCrushRing());
        addRingLook(mplew, chr.getFriendshipRing());
        addMarriageRingLook(mplew, chr.getWeddingRing(), chr);
        mplew.write(0);
        /* above
           if ( CInPacket::Decode1(a2) ) {
		    v39 = CInPacket::Decode4(a2);
		    if ( v39 > 0 )
		    {
		      v40 = v39;
		      do
		      {
		        v41 = CInPacket::Decode4(a2);
		        CUserPool::OnNewYearCardRecordAdd(0, v5, v41);
		        --v40;
		      }
		      while ( v40 );
		    }
		  }
         */
        mplew.write(0); //should be the byte for berserk //CSkillInfo::GetSkill(0, 1320006);
        mplew.write(0);
        mplew.write(chr.getTeam());//only needed in specific fields
        return mplew.getPacket();
    }

}
