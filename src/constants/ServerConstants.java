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
package constants;

public class ServerConstants {
    public static short VERSION = 83;
    public static String SERVERNAME = "XsPro";
    public static String SERVEROWNER = "XsPro";
    // Rate Configuration
    public static byte EXP_RATE = (byte)50;
    public static byte MESO_RATE = 20;
    public static final byte DROP_RATE = 5;
    public static final byte BOSS_DROP_RATE = 2;
    public static final byte QUEST_EXP_RATE = 5;
    public static final byte QUEST_MESO_RATE = 3;
    public static final boolean GMS_LIKE = false;
    // Login Configuration
    public static final byte FLAG = 3;
    public static final int CHANNEL_NUMBER = 2;
    public static final int CHANNEL_LOAD = 50;
    public static final String EVENT_MESSAGE = "-#r" + SERVERNAME +"#k- Exp:#b"+ EXP_RATE + "x#k Meso:#r" +MESO_RATE+ "x#k Drop:#b"+ DROP_RATE +"x#k";
    public static final long RANKING_INTERVAL = 3600000;
    public static final boolean ENABLE_PIN = false;
    public static final boolean ENABLE_PIC = false;
    // Channel Configuration
    public static String SERVER_MESSAGE = "Welcome to "+ SERVERNAME +".Exp:"+ EXP_RATE + "x Meso:" +MESO_RATE+ "x Drop:"+ DROP_RATE +"x BossDropRate:"+ BOSS_DROP_RATE +"x QuestExpRate:"+ QUEST_EXP_RATE +"x QuestMesoRate:"+ QUEST_MESO_RATE +"x";
    public static String RECOMMEND_MESSAGE = "";
    public static final String EVENTS = "automsg CathedralWedding GuildQuest KerningPQ Boats Subway ZakumBattle ZakumPQ";
    // IP Configuration
    public static final String HOST = "localhost";
    //XSPro beast features
    public static final boolean exp_bonuses = true; //done
    public static final String exp_bonus_message = "You have just gained an 150% exp bonus! For now it will be stored somewhere safe, for more information type '@bonuses', or to use it now, type '@expbonus'";
    public static final String exp_bonus_overhead_message = "#eYou have gained an ExpBonus! Check your chatlog for more info.#k";
    public static final boolean login_announcing = true; //done
    public static final int exp_bonus_chance = 10; // 1 out of 20 chance default don't set it below 2 ever
    public static final boolean weapons_level_up = true; // weapons level up the more you use them
    public static final boolean gain_nx_for_killing_mobs = true; // you gain Nx for every mob you kill
    public static final int nx_gain = 30; //nx gain for killing a mob
    public static final int max_weapon_exp = 10; //how much exp a weapon needs to level
    public static final boolean gain_medals = true; // gain medals for various achievements
    public static final boolean levelup_bonuses = true; // gain various bonuses upon leveling up (fame, mesos etc)
    public static final boolean npc_commands = true; // toggles Npc commands ex. @cody
    public static final boolean playercommands = true;
    public static final boolean tim = true;
    // ========reborns=======
    public static final boolean reborns = true; // once you reach level 200 you can go back to 1
    public static final boolean resetstats = true; // reset stats upon reborning?
    public static final boolean gainmesos = true; // gain rewards for reborning?
    public static final int mesos = 10000000; // mesos you gain for reborning
    public static final boolean resetjob = true; // reset job upon reborning?
    public static final boolean gmjob = true; // get GM job after X reborns? (Not commands, just the job)
    public static final int rebirths_until_gm_job = 5; //how many reborns until gm job? set as 0 to disable
    public static final boolean drop_gm_message = true; // drop message about someone getting gm job
    public static final int weapons_level_upon_rb = 10; // how many times do weapons level upon reborning? put 0 to turn it off
    public static final boolean legends = true; //The legend in front of your name ex: (TheBeast)Redeemer34:hi
    public static final boolean max_skills = false; // want your skills to always be maxed?
    // Note: you can have weapons leveling up by killing monsters, and just have weapons level by RBing
}