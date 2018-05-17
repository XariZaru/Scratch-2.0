package systems;

import com.artemis.ComponentMapper;
import ecs.components.Client;
import ecs.components.item.CharacterInventory;
import ecs.system.InventorySystem;
import io.netty.channel.Channel;
import net.Key;
import net.PacketHandler;
import net.packets.InboundPacket;
import net.packets.MaplePacketCreator;
import net.packets.OutboundPacket;
import tools.Randomizer;

import static systems.CooldownSystem.addCooldownInfo;
import static systems.MonsterBookSystem.addMonsterBookInfo;
import static systems.QuestSystem.addCompletedQuestInfo;
import static systems.QuestSystem.addQuestInfo;
import static systems.RingSystem.addRingInfo;
import static systems.SkillSystem.addSkillInfo;
import static systems.TeleportRockSystem.addTeleportInfo;

public class PlayerLoggedInSystemHandler extends PacketHandler {

    LoadCharacterSystem loadCharacterSystem;    InventorySystem inventorySystem;
    CharacterInfoEncodingSystem encodingSystem;
    ComponentMapper<Client> clients;
    private ServerIdentifier si;

    @Override
    public void receive(Channel channel, InboundPacket inBound, OutboundPacket outBound) {
        final int dbId = inBound.readInt();
        final int entityId = channel.attr(Key.ENTITY).get();

        /*
         * TODO: load character into map
         */

        // Load Character from database
        if (!loadCharacterSystem.retrieve(dbId, entityId))
            return;

        Client client = clients.create(entityId);
        channel.attr(Key.CLIENT).set(client);



        // Load Inventory
        inventorySystem.retrieve(entityId);

        // Send Character information to client
        channel.writeAndFlush(getCharInfo(entityId));
//        final Server server = Server.getInstance();
//        MapleCharacter player = c.getWorldServer().getPlayerStorage().getCharacterById(cid);
//        boolean newcomer = false;
//        if (player == null) {
//            try {
//                player = MapleCharacterFactory.loadCharFromDB(cid, c, true);
//                newcomer = true;
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else {
//            player.newClient(c);
//        }
//        if (player == null) { //If you are still getting null here then please just uninstall the game >.>, we dont need you fucking with the logs
//            c.disconnect(false, false);
//            return;
//        }
//
//        // Checks if the current sessionID matches the one generated on login
//        // For some reason getSessiuon
//     /*  if (c.getSessionID() != c.fetchSessionID(cid)) {
//            LogHelper.CONSOLE.get().info("The client's session ID {} does not match with: {}", c.getSessionID(), c.fetchSessionID(cid));
//            c.disconnect(false, false);
//            return;
//        } */
//        c.setPlayer(player);
//        c.setAccID(player.getAccountID());
//
//        int state = c.getLoginState();
//        boolean allowLogin = true;
//        Channel cserv = c.getChannelServer();
//
//        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.LOGIN_NOTLOGGEDIN) {
//            for (String charName : c.loadCharacterNames(c.getWorld())) {
//                for (Channel ch : c.getWorldServer().getChannels()) {
//                    if (ch.isConnected(charName)) {
//                        LogHelper.PACKET_HANDLER.get().info("The character {} is already connected to the server.", charName);
//                        allowLogin = false;
//                    }
//                }
//                break;
//            }
//        }
//       /* if (state != MapleClient.LOGIN_SERVER_TRANSITION || !allowLogin) {
//            LogHelper.PACKET_HANDLER.get().info("Setting character {} to null.", player.getName());
//            c.setPlayer(null);
//            c.announce(MaplePacketCreator.getAfterLoginError(7));
//            return;
//        } */
//        if (newcomer) {
//            MapleCharacterFactory.disconnectPlayersOfAccount(c.getAccID(), cserv.getWorld());
//        }
//
//        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN);
//
//        cserv.addPlayer(player);
//        //if (c.getPlayer().isDonor() < 1 && c.getChannel() == 6 && !player.isGM()) {
//        //    c.getPlayer().dropMessage(1, "You may not enter the donor channel!");
//        //    c.changeChannel(1);
//        //    c.announce(MaplePacketCreator.enableActions());
//        //}
//        List<PlayerBuffValueHolder> buffs = server.getPlayerBuffStorage().getBuffsFromStorage(cid);
//        if (buffs != null) {
//            player.silentGiveBuffs(buffs);
//        }
//        Connection con = DatabaseConnection.getConnection();
//        PreparedStatement ps = null;
//        PreparedStatement pss = null;
//        ResultSet rs = null;
//        try {
//            ps = con.prepareStatement("SELECT Mesos FROM dueypackages WHERE RecieverId = ? and Checked = 1");
//            ps.setInt(1, player.getId());
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                try {
//                    pss = DatabaseConnection.getConnection().prepareStatement(
//                            "UPDATE dueypackages SET Checked = 0 where RecieverId = ?");
//                    pss.setInt(1, player.getId());
//                    pss.executeUpdate();
//                    pss.close();
//                } catch (SQLException e) {
//                }
//                c.announce(MaplePacketCreator.sendDueyMSG((byte) 0x1B));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (pss != null) {
//                    pss.close();
//                }
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (SQLException ex) {
//                LogHelper.GENERAL_EXCEPTION.get().error("Failed to close sql query:", ex);
//            }
//        }
//        c.announce(MaplePacketCreator.getCharInfo(player));
//        if (!player.isHidden()) {
//            player.toggleHide(true);
//        }
//
//        player.getMap().addPlayer(player);
//        World world = server.getWorld(c.getWorld());
//        world.getPlayerStorage().addPlayer(player);
//
//
//        // keys and macros
//        loadKeyAndMacroStuff(player);
//
//        // update guild
//        loadGuildStuff(player);
//
//        // update family
//        loadFamilyStuff(player, world);
//
//        // update party
//        loadPartyStuff(player, world);
//
//        // update buddy
//        loadBuddyStuff(player, world);
//
//        // wedding
//        loadWeddingStuff(player, world);
//
//
//        // show any notes
//        player.showNote();
//
//        if (newcomer) {
//            for (MaplePet pet : player.getPets()) {
//                if (pet != null) {
//                    pet.startFullnessSchedule(player);
//                }
//            }
//        }
//        c.announce(MaplePacketCreator.updateGender(player));
//        player.checkMessenger();
//        c.announce(MaplePacketCreator.enableReport());
//        player.changeSkillLevel(SkillFactory.getSkill(10000000 * player.getJobType() + 12), (byte) (player.getLinkedLevel() / 10), 20, -1);
//        player.checkBerserk();
//        player.expirationTask();
//        player.setRates();
//        player.setLoginTime();
//
//        if (net.constants.GameConstants.hasSPTable(player.getJob()) && player.getJob().getId() != 2001) {
//            player.createDragon();
//        }
//
//
//        // Halloween Roll Call Event (ONE PER PLAYER!)
///*	        int month = LocalDateTime.now().getMonthValue();
//	        int date = LocalDateTime.now().getDayOfMonth();
//	        try {
//	            player.getClient().loadHWIDIfNescessary();
//	        } catch (SQLException ex) {
//	            Logger.getLogger(PlayerLoggedinHandler.class.getName()).log(Level.SEVERE, null, ex);
//	        }
//	        if (((month == 10 && date >= 21) || (month == 11 && date <= 7))
//	                && // Event dates
//	                player.getRollCallDate(player.getClient().getHWID()) != date) { // Not the same person on the same day
//
//	            int amount = player.getRollCallAmount(player.getClient().getHWID());
//	            player.setRollCall(player.getClient().getHWID(), amount, date);
//
//	            player.dropMessage(6, "[Roll Call Event] You have logged in " + ++amount + "/15 times during the event thus far.");
//	            if (amount == 15) {
//	                // Give them some Items
//	            }
//	        }*/
//
//
//        // Donor Status
//        //if (!c.hasClaimedDonor() && c.getPlayer().isDonor() == 2) {
//        //    MapleInventoryManipulator.addById(c, 5220000, (short) 5);
//        //    player.message("You have gained your 5 gachapon tickets, thanks for subscribing for VIP!");
//        // }
//
//        /*if (!c.hasVotedAlready()) {
//            player.announce(MaplePacketCreator.earnTitleMessage("You can vote now! Vote at " + c.getVotingSite() + " and earn " + c.getVotingNxValue() + " NX!"));
//        }*/
//        player.setHasMerchant(false);
//        for (Channel ch : world.getChannels())
//            if (ch.getHiredMerchants().get(player.getId()) != null) {
//                player.setHasMerchant(true);
//                break;
//            }
//
//
//        for (Integer npcid : MapleNPCFactory.npcs)
//            c.announce(MaplePacketCreator.setNPCScriptable(npcid));
//
//        if (newcomer) {
//            // Donor King medals being removed upon log in if you lost the title
//
//            if(!c.isLPActivated()) {
//                if(player.getLevel() >= 15)
//                    ApiFactory.getFactory().postUserLPActivated(c.getAuthID(), null);
//            }
//
//            for (int x = 1142014; x < 1142032; x++) {
//                if (player.haveItem(x, true) && DonorKing.getInstance().getRanking(player.getId(), DonorKing.getInstance().getLocationOfMedal(x)) != 0) {
//                    player.dropMessage("Because you are no longer the Donor King, you've lost the " + ItemLibrarySystem.getInstance().getName(x) + ".");
//                    if (player.haveItem(x, false))
//                        MapleInventoryManipulator.removeById(player.getClient(), MapleInventoryType.EQUIP, x, 1, true, false);
//                    if (player.haveItem(x, true))
//                        MapleInventoryManipulator.removeById(player.getClient(), MapleInventoryType.EQUIPPED, x, 1, true, false);
//                }
//            }
//            if (player.getPetTasks())
//                player.startPetTasks();
//
//            for (MaplePet pet : player.getPets())
//                if (pet != null)
//                    pet.createDonorFeature(player);
//
//            if (player.isGM()) {
//                Server.getInstance().broadcastGMMessage(MaplePacketCreator.earnTitleMessage("GM " + player.getName() + " has logged in."));
//            }
//            holidayAnnouncement(player);
//        }
    }

    public void processCharacter(Client client, int dbId, int entityId) {

    }

    /**
     * Gets character info for a character.
     *
     * @param entityId The entityId to get info about.
     *
     * @return The character info packet.
     */
    public OutboundPacket getCharInfo(int entityId) {
        final OutboundPacket mplew = new OutboundPacket();
        mplew.writeShort(net.opcodes.SendOpcode.SET_FIELD.getValue());
        mplew.writeInt(si.channel);
        mplew.write(1);
        mplew.write(1);
        mplew.writeShort(0);
        for (int i = 0; i < 3; i++) {
            mplew.writeInt(Randomizer.nextInt());
        }
        addCharacterInfo(mplew, entityId);
        mplew.writeLong(MaplePacketCreator.getTime(System.currentTimeMillis()));
        return mplew;
    }

    private void addCharacterInfo(final OutboundPacket mplew, int entityId) {
        long mask = -1;
        mplew.writeLong(mask);
        mplew.write(0); //true do below
        if ((mask & 1) == 1) {
            encodingSystem.encodeStats(mplew, entityId);
            mplew.write(10); // TODO: buddylist capacity
            mplew.writeBool(false); // TODO: linked name
//            if (chr.getLinkedName() != null) {
//                mplew.writeMapleAsciiString(chr.getLinkedName());
//            }
        }
        if ((mask & 2) == 2) {
            mplew.writeInt(0); // TODO: meso
        }
        if ((mask & 0x80) == 0x80) {
            // Inventory Sizes
            for (byte i = 1; i <= 5; i++) {
                mplew.write(inventorySystem.getInventory(entityId, CharacterInventory.Type.getByType(i)).itemEntityIDs.length);
            }
        }

        if ((mask & 0x100000) == 0x100000) {
            mplew.writeLong(MaplePacketCreator.getTime(-2)); // this is actually two decodes
        }
        if ((mask & 4) == 4) {
            inventorySystem.addInventoryInfo(mplew, entityId);
        }
        if ((mask & 0x100) == 0x100) {
            addSkillInfo(mplew, entityId);
        }
        if ((mask & 0x8000) == 0x8000) {
            addCooldownInfo(mplew, entityId);
        }
        if ((mask & 0x200) == 0x200) {
            addQuestInfo(mplew, entityId);
        }
        if ((mask & 0x4000) == 0x4000) {
            addCompletedQuestInfo(mplew, entityId);
        }
        if ((mask & 0x400) == 0x400) {
            mplew.writeShort(0); // TODO: minigame info
        }
        if ((mask & 0x800) == 0x800) {
            addRingInfo(mplew, entityId);
        }
        if ((mask & 0x1000) == 0x1000) {
            addTeleportInfo(mplew, entityId);
        }
        if ((mask & 0x20000) == 0x20000) {
            mplew.writeInt(0);
//            mplew.writeInt(chr.getMonsterBookCover()); // monster book cover
        }
        if ((mask & 0x10000) == 0x10000) {
            addMonsterBookInfo(mplew, entityId);
        }
        if ((mask & 0x40000) == 0x40000) {
            mplew.writeShort(0);
//            addNewYearInfo(mplew, chr);//have fun!
        }
        if ((mask & 0x80000) == 0x80000) {
            mplew.writeShort(0);
//            addAreaInfo(mplew, chr);//assuming it stayed here xd
        }
        if ((mask & 0x100000) == 0x100000) {
            mplew.writeShort(0);
//        	/*
//        	 *
//        	 * if ((dbcharFlag & DBChar.AdminShopCount) > 0) {//AdminShopNpcID = 2084001
//            oPacket.Encode2(mAdminShopCommodityCount.size());
//            for (Map.Entry<Integer, Integer> pAdminShop : mAdminShopCommodityCount.entrySet()) {
//                oPacket.Encode4(pAdminShop.getKey());
//                oPacket.Encode2(pAdminShop.getValue());
//            }
//        }
//        	 *
//        	 *
//        	     v141 = CInPacket::Decode2(a2); //the short above
//    if ( v141 > 0 )
//    {
//      v142 = v141;
//      do
//      {
//        v153 = CInPacket::Decode4(a2);
//        a3 = CInPacket::Decode2(a2);
//        ZMap_long_long_long_::Insert(v140, &v153, &a3);
//        --v142;
//      }
//      while ( v142 );
//    }
//  }
//
//        	 */
        }
    }

}
