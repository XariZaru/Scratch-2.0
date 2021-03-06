/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.opcodes;

public enum RecvOpcode {
	CUSTOM_PACKET(14099),
	LOGIN_PASSWORD(1),
	GUEST_LOGIN(2),
	ACCOUNT_INFO_REQUEST(3),
	SERVERLIST_REREQUEST(4),
	CHARLIST_REQUEST(5),
	SERVERSTATUS_REQUEST(6),
	ACCEPT_TOS(7),
	SET_GENDER(8),
	AFTER_LOGIN(9),
	REGISTER_PIN(10),
	SERVERLIST_REQUEST(11),
	PLAYER_DC(12),
	VIEW_ALL_CHAR(13),
	PICK_ALL_CHAR(14),
	VAC_FLAG_SET(15),
	CHECK_NAME_CHANGE_POSSIBLE(16),
	REGISTER_NEW_CHARACTER(17),
	CHECK_TRANSFER_WORLD_POSSIBLE(18),
	CHAR_SELECT(19),
	PLAYER_LOGGEDIN(20),
	CHECK_CHAR_NAME(21),
	CREATE_CHAR(22),
	DELETE_CHAR(23),
	PONG(24),
	CLIENT_START_ERROR(25),
	CLIENT_ERROR(26),
	STRANGE_DATA(27),
	RELOG(28),
	REGISTER_PIC(29),
	CHAR_SELECT_WITH_PIC(30),
	VIEW_ALL_PIC_REGISTER(31),
	VIEW_ALL_WITH_PIC(32),
	CHECK_OTP_REQUEST(33),
	CHECK_DELETE_CHARACTER_OTP(34),
	CREATE_SECURITY_HANDLE(35),
	END_SOCKET(36),
	BEGIN_USER(37),
	CHANGE_MAP(38),
	CHANGE_CHANNEL(39),
	ENTER_CASHSHOP(40),
	MOVE_PLAYER(41),
	CANCEL_CHAIR(42),
	USE_CHAIR(43),
	CLOSE_RANGE_ATTACK(44),
	RANGED_ATTACK(45),
	MAGIC_ATTACK(46),
	TOUCH_MONSTER_ATTACK(47),
	TAKE_DAMAGE(48),
	GENERAL_CHAT(49),
	CLOSE_CHALKBOARD(50),
	FACE_EXPRESSION(51),
	USE_ITEMEFFECT(52),
	USE_DEATHITEM(53),
	USER_HP(54),
	PREMIUM(55),
	USER_BAN_MAP_BY_MOB(56),
	MONSTER_BOOK_COVER(57),
	NPC_TALK(58),
	REMOTE_STORE(59),
	NPC_TALK_MORE(60),
	NPC_SHOP(61),
	STORAGE(62),
	HIRED_MERCHANT_REQUEST(63),
	FREDRICK_ACTION(64),
	DUEY_ACTION(65),
	USER_SHOP_SCANNER_REQUEST(66),
	USER_SHOP_LINK_REQUEST(67),
	ADMIN_SHOP(68),
	MERGE_ITEM(69),
	SORT_ITEM(70),
	ITEM_MOVE(71),
	USE_ITEM(72),
	CANCEL_ITEM_EFFECT(73),
	USER_STAT_CHANGE_BY_PORTABLE_CHAIR_REQUEST(74),
	USE_SUMMON_BAG(75),
	PET_FOOD(76),
	USE_MOUNT_FOOD(77),
	SCRIPTED_ITEM(78),
	USE_CASH_ITEM(79),
	USER_DESTROY_PET_ITEM_REQUEST(80),
	USE_CATCH_ITEM(81),
	USE_SKILL_BOOK(82),
	USE_TELEPORT_ROCK(84),
	USE_RETURN_SCROLL(85),
	USE_UPGRADE_SCROLL(86),
	DISTRIBUTE_AP(87),
	AUTO_DISTRIBUTE_AP(88),
	HEAL_OVER_TIME(89),
	DISTRIBUTE_SP(90),
	SPECIAL_MOVE(91),
	CANCEL_BUFF(92),
	SKILL_EFFECT(93),
	MESO_DROP(94),
	GIVE_FAME(95),
	CHAR_INFO_REQUEST(97),
	SPAWN_PET(98),
	CANCEL_DEBUFF(99),
	CHANGE_MAP_SPECIAL(100),
	USE_INNER_PORTAL(101),
	TROCK_ADD_MAP(102),
	USER_ANTI_MACRO_ITEM_USE_REQUEST(103),
	USER_ANTI_MACRO_SKILL_USE_REQUEST(104),
	USER_ANTI_MACRO_QUEST_RESULT(105),
	REPORT(106),
	QUEST_ACTION(107),
	USER_CALC_DAMAGE_STAT_SET_REQUEST(108),
	GRENADE_EFFECT(109),
	SKILL_MACRO(110),
	SPOUSE_CHAT(111),
	USE_ITEM_REWARD(112),
	MAKER_SKILL(113),
	USER_USE_GACHAPON_BOX_REQUEST(114),
	USE_REMOTE(116),
	USER_USE_WATER_OF_LIFE(117),
	ADMIN_CHAT(118),
	PARTYCHAT(119),
	WHISPER(120),
	COUPLE_MESSAGE(121),
	MESSENGER(122),
	PLAYER_INTERACTION(123),
	PARTY_OPERATION(124),
	DENY_PARTY_REQUEST(125),
	GUILD_OPERATION(126),
	DENY_GUILD_REQUEST(127),
	ADMIN_COMMAND(128),
	ADMIN_LOG(129),
	BUDDYLIST_MODIFY(130),
	NOTE_ACTION(131),
	MEMO_FLAG_REQUEST(132),
	USE_DOOR(133),
	SLIDE_REQUEST(134),
	CHANGE_KEYMAP(135),
	RPS_ACTION(136),
	RING_ACTION(137),
	WEDDING_ACTION(138),
	WEDDING_PROGRESS(139),
	GUEST_BLESS(140),
	ITEM_VAC_ALERT(141),
	STALK_BEGIN(142),
	ALIANCE_REQUEST(143),
	ALLIANCE_OPERATION(144),
	FAMILY_CHART_REQUEST(145),
	OPEN_FAMILY(146),
	ADD_FAMILY(147),
	FAMILY_UNREGISTER_JUNIOR(148),
	FAMILY_UNREGISTER_PARENT(149),
	ACCEPT_FAMILY(150),
	USE_FAMILY(151),
	FAMILY_SET_PRECEPT(152),
	FAMILY_SUMMON_REQUEST(153),
	CHAT_BLOCK_USER_REQ(154),
	BBS_OPERATION(155),
	ENTER_MTS(156),
	USE_SOLOMON_ITEM(157),
	USE_GACHA_EXP(158),
	NEW_YEAR_CARD_REQUEST(159),
	RANDOM_MORPH_REQUEST(160),
	CASH_ITEM_GACHAPON_REQUEST(161),
	CLICK_GUIDE(162),
	ARAN_COMBO_COUNTER(163),
	MOB_CRC_KEY_CHANGED_REPLY(164),
	REQUEST_SESSION_VALUE(165),
	BEGIN_PET(166),
	MOVE_PET(167),
	PET_CHAT(168),
	PET_COMMAND(169),
	PET_LOOT(170),
	PET_AUTO_POT(171),
	PET_EXCLUDE_ITEMS(172),
	END_PET(173),
	BEGIN_SUMMONED(174),
	MOVE_SUMMON(175),
	SUMMON_ATTACK(176),
	DAMAGE_SUMMON(177),
	SUMMON_SKILL(178),
	END_SUMMONED(179),
	BEGIN_DRAGON(180),
	MOVE_DRAGON(181),
	END_DRAGON(182),
	QUICKSLOT_CHANGE(183),
	END_USER(184),
	BEGIN_FIELD(185),
	BEGIN_LIFEPOOL(186),
	BEGIN_MOB(187),
	MOVE_LIFE(188),
	AUTO_AGGRO(189),
	MOB_DROP_PICKUP_REQUEST(190),
	MOB_HIT_BY_OBSTACLE(191),
	MOB_DAMAGE_MOB_FRIENDLY(192),
	MONSTER_BOMB(193),
	MOB_DAMAGE_MOB(194),
	END_MOB(195),
	BEGIN_NPC(196),
	NPC_ACTION(197),
	NPC_SPECIAL_ACTION(198),
	END_NPC(199),
	END_LIFEPOOL(200),
	BEGIN_DROPPOOL(201),
	ITEM_PICKUP(202),
	END_DROPPOOL(203),
	BEGIN_REACTORPOOL(204),
	DAMAGE_REACTOR(205),
	TOUCHING_REACTOR(206),
	REQUIRE_FIELD_OBSTACLE_STATUS(207),
	END_REACTORPOOL(208),
	BEGIN_EVENTPOOL(209),
	EVENT_START(210),
	SNOWBALL(211),
	LEFT_KNOCKBACK(212),
	COCONUT(213),
	MATCH_TABLE(214),
	PULLEY_HIT(215),
	END_EVENTPOOL(216),
	BEGIN_MONSTER_CARNIVAL_FIELD(217),
	MONSTER_CARNIVAL(218),
	END_MONSTER_CARNIVAL_FIELD(219),
	PARTY_SEARCH_REGISTER(220),
	BEGIN_PARTY_MATCH(221),
	PARTY_SEARCH_START(222),
	CANCEL_INVITE_PARTY_MATCH(223),
	END_PARTY_MATCH(224),
	END_FIELD(225),
	BEGIN_CASHSHOP(226),
	CASH_SHOP_CHARGE_PARAM_REQUEST(227),
	CHECK_CASH(228),
	CASHSHOP_OPERATION(229),
	COUPON_CODE(230),
	CASH_SHOP_GIFT_MATE_INFO_REQUEST(231),
	END_CASHSHOP(232),
	BEGIN_RAISE(235),
	RAISE_REFRESH(236),
	RAISE_UI_STATE(237),
	RAISE_INC_EXP(238),
	RAISE_ADD_PIECE(239),
	END_RAISE(240),
	SEND_MATE_MAIL(241),
	REQUEST_GUILD_BOARD_AUTH_KEY(242),
	REQUEST_CONSULT_AUTH_KEY(243),
	REQUEST_CLASS_COMPETITION_AUTH_KEY(244),
	REQUEST_WEB_BOARD_AUTH_KEY(245),
	BEGIN_MAPLETV(246),
	MAPLETV_SEND_MESSAGE_REQUEST(247),
	MAPLETV_UPDATE_VIEW_COUNT(248),
	END_MAPLETV(249),
	BEGIN_ITC(250),
	ITC_CHARGE_PARAM_REQUEST(251),
	ITC_QUERY_CASH_REQUEST(252),
	MTS_OPERATION(253),
	USE_MAPLELIFE(254),
	BEGIN_CHARACTERSALE(255),
	CHECK_DUPLICATED_ID_IN_CS(256),
	END_CHARACTERSALE(257),
	BEGIN_ITEMUPGRADE(259),
	USE_HAMMER(260),
	END_ITEMUPGRADE(261);

    private int code = -2;

    private RecvOpcode(int code) {
        this.code = code;
    }

    public int getValue() {
        return code;
    }
    
    public static RecvOpcode getOpcode(int value) {
    	for (RecvOpcode op : RecvOpcode.values())
    		if (op.getValue() == value)
    			return op;
    	return null;
    }
    
    @Override
    public String toString() {
    	return this.name() + "(" + this.getValue() + ")";
    }
    
    public static void listAllInString() {
    	StringBuilder sb = new StringBuilder();
    	for (RecvOpcode op : RecvOpcode.values())
    		sb.append(op.toString()).append(",\n");
    	System.out.println(sb.toString());
    }
}
