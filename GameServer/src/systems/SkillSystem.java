package systems;

import com.artemis.BaseSystem;
import net.packets.OutboundPacket;

public class SkillSystem extends BaseSystem {

    public static void addSkillInfo(final OutboundPacket mplew, int entityId) {
//        Map<PlayerSkill, MapleCharacter.SkillEntry> skills = chr.getSkills();
//        int skillsSize = skills.size();
//        // We don't want to include any hidden skill in this, so subtract them from the size list and ignore them.
//        for (Iterator<Entry<PlayerSkill, SkillEntry>> it = skills.entrySet().iterator(); it.hasNext();) {
//            Entry<PlayerSkill, MapleCharacter.SkillEntry> skill = it.next();
//            if (GameConstants.isHiddenSkills(skill.getKey().getId())) {
//                skillsSize--;
//            }
//        }
        mplew.writeShort(0); // TODO: skillsSize
//        for (Iterator<Entry<PlayerSkill, SkillEntry>> it = skills.entrySet().iterator(); it.hasNext();) {
//            Entry<PlayerSkill, MapleCharacter.SkillEntry> skill = it.next();
//            if (GameConstants.isHiddenSkills(skill.getKey().getId())) {
//                continue;
//            }
//            mplew.writeInt(skill.getKey().getId());
//            mplew.writeInt(skill.getValue().skillevel);
//            addExpirationTime(mplew, skill.getValue().expiration);
//            if (skill.getKey().isFourthJob()) {
//                mplew.writeInt(skill.getValue().masterlevel);
//            }
//        }
    }


    @Override
    protected void processSystem() {

    }
}
