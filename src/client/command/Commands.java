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
    License.te

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package client.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import client.IItem;
import client.ISkill;
import client.Item;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import tools.StringUtil;
import client.MapleJob;
import client.MaplePet;
import client.MapleStat;
import client.SkillFactory;
import constants.ItemConstants;
import constants.ServerConstants;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import tools.DatabaseConnection;
import net.channel.ChannelServer;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.npc.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleShopFactory;
import server.events.MapleEvent;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleNPC;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.Pair;
import server.maps.MapleMap;
import constants.skills.SuperGM;
import server.TimerManager;
import server.MapleStatEffect;
import client.Equip;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import client.MapleInventory;
import client.MapleInventoryType;
import java.util.HashMap;

public class Commands {
    public static boolean executePlayerCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter chr = c.getPlayer();
        if (heading == '!' && chr.gmLevel() == 0 || ServerConstants.playercommands == false || chr.getMapId() == 970000101) {
            if (chr.getMapId() > 970000101 || chr.getMapId() < 970000101) {
            chr.yellowMessage("You may not use that prefiix to your commands. If the problem persists, the servre owner ("+ ServerConstants.SERVEROWNER +") may have disabled player commands.");
            }
            if (chr.getMapId() == 970000101) {
            chr.dropMessage("You can't use commands in this map");
            }   
            return false;
        }
        ChannelServer cserv = c.getChannelServer();
        if (sub[0].equals("commands") || sub[0].equals("help")) {
        chr.dropMessage("____________________________");
        chr.dropMessage("       " + ServerConstants.SERVERNAME + " Commands       ");
        chr.dropMessage("____________________________");
        chr.dropMessage("@commands - | - Displays this page");
        chr.dropMessage("@dispose - | - Unstucks you");
        chr.dropMessage("@expfix - | - Sets your exp back to 1 if its negative or won't move");
        chr.dropMessage("@str [number] - | - Adds ap into Strength");
        chr.dropMessage("@dex [number] - | - Adds ap into Dexterity");
        chr.dropMessage("@int [number] - | - Adds ap into Intelligence");
        chr.dropMessage("@luk [number] - | - Adds ap into Luck");
        chr.dropMessage("@rev - | - Displays the current revision of "+ ServerConstants.SERVERNAME + "");
        chr.dropMessage("@save - | - Saves your progress");
        chr.dropMessage("@monsterrider - | - Maxes your monsterrider skill");
        chr.dropMessage("@donorshop - | - opens up the donor shop");
        if (ServerConstants.npc_commands == true) {
         chr.dropMessage("@npchelp - | - Displays all of the NPC's");
        }
        if (ServerConstants.legends == true) {
        chr.dropMessage("@legendnpc - | - Opens the legend NPC");
        }
        if (ServerConstants.exp_bonuses == true) {
        chr.dropMessage("@bonuses - | - Tells you about Exp Bonuses");
        chr.dropMessage("@expbonus - | - Uses an Exp Bonus");
        }
        chr.dropMessage("@reset [stat] - | - Resets a stat");
        chr.dropMessage("@jobadvance [job] - | - Job advances you if you meet requirements");
        chr.dropMessage("@jobs - | - Shows you what jobs there are for '@jobadvance'");
        chr.dropMessage("@charinfo [character] - | - Displays info about a character");
        if (ServerConstants.weapons_level_up == true) {
        chr.dropMessage("@weaponlevel - | - Displays your weapon level");
        }
        if (ServerConstants.reborns == true) {
        chr.dropMessage("@reborncommands - | - Resets your level to 10");
        }
        if (ServerConstants.max_skills == true) {
        chr.dropMessage("@maxskills - | - Maxes your skills");
        }
        }
         if (sub[0].equals("save")) {
            chr.saveToDB(true);
            chr.dropMessage("Save complete!");
         }
          if (sub[0].equals("monsterrider")) {
            if (chr.getSkillLevel(1004) > 0 || chr.getSkillLevel(10001004) > 0 || chr.getLevel() < 70) {
                chr.dropMessage("You already have Monster Rider!");
                }
                chr.changeSkillLevel(SkillFactory.getSkill(1004), 1, 1, 1);
                chr.changeSkillLevel(SkillFactory.getSkill(10001004), 1, 1, 1);
                chr.dropMessage("It is completed.");
}
          if (sub[0].equals("npchelp") && ServerConstants.npc_commands == true) {
          chr.dropMessage("____________________________");
          chr.dropMessage("       " + ServerConstants.SERVERNAME + " NPC's      ");
          chr.dropMessage("____________________________");
          chr.dropMessage("@cody - | - Opens up cody");
          chr.dropMessage("@fmnpc - | - Opens the all-in-one NPC");
          chr.dropMessage("@styler - | - Opens the Styler NPC");
          chr.dropMessage("@boyhair - | - Opens the boy hair styler");
          chr.dropMessage("@girlhair - | - Opens the girl hair styler");
          chr.dropMessage("@buynx - | - Opens the BuyNX NPC");
          chr.dropMessage("@shop - | - Opens the shop NPC");
          chr.dropMessage("@spinel - | - Opens spinel");
          chr.dropMessage("@jobchanger - | - opens the jobchanger");
          }
         if (sub[0].equals("cody") && ServerConstants.npc_commands == true) {
             NPCScriptManager.getInstance().start(chr.getClient(), 9200000, null, null);

         } if (sub[0].equals("fmnpc") && ServerConstants.npc_commands == true) {
            NPCScriptManager.getInstance().start(chr.getClient(), 9110000, null, null);

         } if (sub[0].equals("styler") && ServerConstants.npc_commands == true) {
            NPCScriptManager.getInstance().start(chr.getClient(), 9000037, null, null);

         } if (sub[0].equals("boyhair") && ServerConstants.npc_commands == true) {
            NPCScriptManager.getInstance().start(chr.getClient(), 9900000, null, null);

         } if (sub[0].equals("girlhair") && ServerConstants.npc_commands == true) {
            NPCScriptManager.getInstance().start(chr.getClient(), 9900001, null, null);

         } if (sub[0].equals("maxskills") && ServerConstants.max_skills == true) {
           if (ServerConstants.max_skills == true) {
            for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
                try {
                    ISkill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                    chr.changeSkillLevel(skill, skill.getMaxLevel(), skill.getMaxLevel(), -1);
                } catch (NumberFormatException nfe) {
                    break;
                } catch (NullPointerException npe) {
                    continue;
                }
            }
            }
           chr.dropMessage("Your skills have been maxed, good luck training in "+ ServerConstants.SERVERNAME +"!");


            } if (sub[0].equals("buynx") && ServerConstants.npc_commands == true) {
            NPCScriptManager.getInstance().start(chr.getClient(), 9000034, null, null);

        } if(sub[0].equals("spinel") && ServerConstants.npc_commands == true) {
            NPCScriptManager.getInstance().start(chr.getClient(), 9000020, null, null);

        } if (sub[0].equals("jobchanger") && ServerConstants.npc_commands == true) {
            NPCScriptManager.getInstance().start(chr.getClient(), 9000019, null, null);
        }

       /* if (sub[0].equals("addstat") && ServerConstants.addstat == true) {
        if (chr.getStr() >= 32000 || chr.getDex() >= 32000 || chr.getLuk() >= 32000 || chr.getInt() >= 32000) {
        Equip nEquip = (Equip) chr.getInventory(MapleInventoryType.EQUIP).getItem((byte) 1);
        int amount = Integer.parseInt(sub[2]);
        if (nEquip == null) {
        } else {
        if (sub[1].equals("str")) {
        if (amount+nEquip.getStr() > 32767 && chr.getStr() >= amount && chr.getStr()-amount >= 4) {
        nEquip.addStats(c, "str", amount);
        chr.setStr(chr.getStr()-amount);
        }
        }
        if (sub[1].equals("dex")) {
        if (amount+nEquip.getDex() > 32767 && chr.getDex() >= amount && chr.getDex()-amount >= 4) {
        nEquip.addStats(c, "dex", amount);
        chr.setDex(chr.getDex()-amount);
        }
        }
        if (sub[1].equals("luk")) {
        if (amount+nEquip.getLuk() > 32767 && chr.getLuk() >= amount && chr.getLuk()-amount >= 4) {
        nEquip.addStats(c, "luk", amount);
        chr.setLuk(chr.getLuk()-amount);
        }
        }
        if (sub[1].equals("int")) {
        if (amount+nEquip.getInt() > 32767 && chr.getInt() >= amount && chr.getInt()-amount >= 4) {
        nEquip.addStats(c, "int", amount);
        chr.setInt(chr.getInt()-amount);
        }
        }
        }
        }
        } */
        if (sub[0].equals("weaponlevel") && ServerConstants.weapons_level_up == true) {
        Equip nEquip = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
        chr.dropMessage("Your current weapon level is " + nEquip.getLevel() + ".");
        }
        if (sub[0].equals("legendnpc") && ServerConstants.legends == true) {
        NPCScriptManager.getInstance().start(chr.getClient(), 9110000, null, null);
        }
        if (sub[0].equals("charinfo")) {
        if (!sub[1].equals(null)) {
        MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
        victim.dropMessage("________________");
        victim.dropMessage("Character Stats");
        victim.dropMessage("________________");
        victim.dropMessage("Str:" + victim.getStr());
        victim.dropMessage("Dex:" + victim.getDex());
        victim.dropMessage("Int:" + victim.getInt());
        victim.dropMessage("Luk:" + victim.getLuk());
        if (ServerConstants.exp_bonuses == true) {
        victim.dropMessage("ExpBonuses:" + victim.getExpBonuses());
        }
        if (ServerConstants.weapons_level_up == true) {
        Equip nEquip = (Equip) victim.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
        victim.dropMessage("CurrentWeaponLevel:" + nEquip.getLevel());
        }
        victim.dropMessage("NxCash:" + victim.getCashShop().getCash(1));
        if (ServerConstants.reborns == true) {
        victim.dropMessage("Reborns:" + victim.getReborns());
        }
        victim.dropMessage("Mesos:" + victim.getMeso());
        victim.dropMessage("VotePoints:" + victim.getVotePoints());
        victim.dropMessage("DonorPoints:" + victim.getDonatorPoints());
        }
        }
        if (sub[0].equals("jobs")) {
            if (chr.getLevel() < 30) {
            chr.dropMessage("Jobs(Adventurer):|M=Mage|W=Warrior|B=Bowman|T=Thief|P=Pirate|");
            chr.dropMessage("Jobs(Cygnus):|M=BlazeWiz.|W=DawnWarr.|B=WindArch.|T=NightWalk.|P=ThunderBreak.|");
            chr.dropMessage("Jobs(ETC):|A=Aran|");    
            chr.dropMessage("Syntax:'@jobadvance b'");
            }
            if (chr.getLevel() >= 30) {
            chr.dropMessage("Jobs(Mage):|C=Cleric|F=FireWiz.|I=IceWiz.|");
            chr.dropMessage("Jobs(Warrior):|P=Page|F=Fighter.|S=SpearMan|");
            chr.dropMessage("Jobs(Thief):|A=Assassin|B=Bandit.|");
            chr.dropMessage("Jobs(Bowman):|C=CrossBowman|H=Hunter|");
            chr.dropMessage("Jobs(Pirate):|B=Brawler|G=Gunslinger|");
            chr.dropMessage("Syntax:'@jobadvance b'");
            }
        }
        if (sub[0].equals("jobadvance")) {
 /*       if (sub[1].equals("noblesse") && chr.getLevel() <= 15 && chr.getReborns() > 0) {
        chr.changeJob(MapleJob.NOBLESSE);
        
        }
        if (sub[1].equals("beginner") && chr.getLevel() <= 15 && chr.getReborns() > 0) {
        chr.changeJob(MapleJob.BEGINNER);
        
        }
        if (sub[1].equals("aran") && chr.getLevel() <= 15 && chr.getReborns() > 0) {
        chr.changeJob(MapleJob.ARAN1);
        
        } */
        if (sub[1].equals("m") && chr.getLevel() >= 8 && chr.getJob().getId() == 0) {
        chr.changeJob(MapleJob.MAGICIAN);
        chr.setRemainingSp(1);
        chr.updateSingleStat(MapleStat.AVAILABLESP, chr.getRemainingSp());
        chr.resetStats();
        }
        if (sub[1].equals("w") && chr.getLevel() >= 10 && chr.getJob().getId() == 0) {
        chr.changeJob(MapleJob.WARRIOR);
        chr.resetStats();
        }
        if (sub[1].equals("b") && chr.getLevel() >= 10 && chr.getJob().getId() == 0) {
        chr.changeJob(MapleJob.BOWMAN);
        chr.resetStats();
        }
        if (sub[1].equals("p") && chr.getLevel() >= 10 && chr.getJob().getId() == 0) {
        chr.changeJob(MapleJob.PIRATE);
        chr.resetStats();
        }
        if (sub[1].equals("t") && chr.getLevel() >= 10 && chr.getJob().getId() == 0) {
        chr.changeJob(MapleJob.THIEF);
        chr.resetStats();
        }
        if (sub[1].equals("m") && chr.getLevel() >= 8 && chr.getJob().getId() == 1000) {
        chr.changeJob(MapleJob.BLAZEWIZARD1);
        chr.resetStats();
        }
        if (sub[1].equals("w") && chr.getLevel() >= 10 && chr.getJob().getId() == 1000) {
        chr.changeJob(MapleJob.DAWNWARRIOR1);
        chr.resetStats();
        }
        if (sub[1].equals("b") && chr.getLevel() >= 10 && chr.getJob().getId() == 1000) {
        chr.changeJob(MapleJob.WINDARCHER1);
        chr.resetStats();
        }
        if (sub[1].equals("p") && chr.getLevel() >= 10 && chr.getJob().getId() == 1000) {
        chr.changeJob(MapleJob.THUNDERBREAKER1);
        chr.resetStats();
        }
        if (sub[1].equals("t") && chr.getLevel() >= 10 && chr.getJob().getId() == 1000) {
        chr.changeJob(MapleJob.NIGHTWALKER1);
        chr.resetStats();
        }
        if (sub[1].equals("a") && chr.getLevel() >= 10 && chr.getJob().getId() == 2000) {
        chr.changeJob(MapleJob.ARAN1);
        chr.resetStats();
        }
        //second job starts here!
        if (sub[1].equals("c") && chr.getLevel() >= 30 && chr.getJob().getId() == 200) {
        chr.changeJob(MapleJob.CLERIC);
        
        }
        if (sub[1].equals("f") && chr.getLevel() >= 30 && chr.getJob().getId() == 200) {
        chr.changeJob(MapleJob.FP_WIZARD);
        
        }
        if (sub[1].equals("i") && chr.getLevel() >= 30 && chr.getJob().getId() == 200) {
        chr.changeJob(MapleJob.IL_WIZARD);
        
        }
        if (sub[1].equals("f") && chr.getLevel() >= 30 && chr.getJob().getId() == 100) {
        chr.changeJob(MapleJob.FIGHTER);
        
        }
        if (sub[1].equals("p") && chr.getLevel() >= 30 && chr.getJob().getId() == 100) {
        chr.changeJob(MapleJob.PAGE);
        
        }
        if (sub[1].equals("s") && chr.getLevel() >= 30 && chr.getJob().getId() == 100) {
        chr.changeJob(MapleJob.SPEARMAN);
        
        }
        if (sub[1].equals("h") && chr.getLevel() >= 30 && chr.getJob().getId() == 300) {
        chr.changeJob(MapleJob.HUNTER);
        
        }
        if (sub[1].equals("c") && chr.getLevel() >= 30 && chr.getJob().getId() == 300) {
        chr.changeJob(MapleJob.CROSSBOWMAN);
        
        }
        if (sub[1].equals("b") && chr.getLevel() >= 30 && chr.getJob().getId() == 500) {
        chr.changeJob(MapleJob.BRAWLER);
        
        }
        if (sub[1].equals("g") && chr.getLevel() >= 30 && chr.getJob().getId() == 500) {
        chr.changeJob(MapleJob.GUNSLINGER);
        
        }
        if (sub[1].equals("a") && chr.getLevel() >= 30 && chr.getJob().getId() == 400) {
        chr.changeJob(MapleJob.ASSASSIN);
        
        }
        if (sub[1].equals("b") && chr.getLevel() >= 30 && chr.getJob().getId() == 400) {
        chr.changeJob(MapleJob.BANDIT);
        
        }
        }
         if (sub[0].equals("reborn") || sub[0].equals("rebirth")) {
         if (chr.getLevel() >= 200 && ServerConstants.reborns == true) {
         chr.doReborn();
         }
         }
         if (sub[0].equals("reborncyg") || sub[0].equals("rebirthcyg")) {
         if (chr.isCygnus() && chr.getLevel() >= 120 && ServerConstants.reborns == true) {
         chr.doReborn();
         }
         }
        if (sub[0].equals("rebirthcommands") && ServerConstants.reborns == true || sub[0].equals("reborncommands") && ServerConstants.reborns == true) {
        chr.dropMessage("_________________________________________");
        chr.dropMessage("           Rebirth Commands              ");
        chr.dropMessage("_________________________________________");
        chr.dropMessage("@rebirth - | - Rebirths you if you aren't cygnus");
        chr.dropMessage("@rebirthcyg - | - Rebirths you if you're cygnus");
        }
        if (sub[0].equals("bonuses") && ServerConstants.exp_bonuses == true) {
        chr.dropMessage("_________________________________________");
        chr.dropMessage("              Exp Bonuses                ");
        chr.dropMessage("_________________________________________");
        chr.dropMessage("You currently have " + chr.getExpBonuses() + " ExpBonuses. ExpBonuses are most effective in a party, where they affect all members, nearly tripling the exp you gain as a team. You have to be in the party for the members to gain the bonus.");
        chr.dropMessage("You can also use them by yourself and you will gain 150% experience. This lasts for thirty minutes, and ExpBonuses are awarded randomly by the server.");
        chr.dropMessage("To use one right now, type '@expbonus'");
        chr.dropMessage("_________________________________________");
        chr.dropOverheadMessage("Please extend your chatlog and read from the top about exp bonuses.");
        }
        /*if (sub[0].equals("gatchapon")) {
        if (NPCScriptManager.getInstance().getCM(c).haveItem(4001126)) {
        NPCScriptManager.getInstance().getCM(c).gainItem(4001126,(short) -1);
        if (!chr.getParty().getMembers().isEmpty()) {
        int randomPartyExp=(int)(10*Math.random());
        NPCScriptManager.getInstance().getCM(c).givePartyExp(1000*randomPartyExp, chr.getClient().getChannelServer().getPartyMembers(chr.getParty()));
        chr.dropMessage("You and your party have just gained " + randomPartyExp*1000 + " exp!");
        }
        int randomInt=(int)(3*Math.random());
        if (randomInt == 1) {
        int randomExp=(int)(100*Math.random());
        chr.gainExp(randomExp*1000, true, true);
        chr.dropMessage("You just gained " + randomExp*1000 + " exp!");
        }
        if (randomInt == 2) {
        int randomMeso=(int)(300*Math.random());
        chr.gainMeso(randomMeso*1000, true);
        chr.dropMessage("You have gained " + randomMeso*1000 + " Mesos!");
        }
        if (randomInt == 3) {
        int cashAmount=(int)(5*Math.random());
        chr.getClient().getChannelServer().getPlayerStorage().getCharacterByName(chr.getName()).getCashShop().gainCash(1, cashAmount*1000);
        chr.dropMessage("You have gained " + cashAmount*1000 + " Nx Cash!");
        }
        chr.dropOverheadMessage("#eCheck your chatbox to see the results of your #bgatchapon#k.");
        }
        }*/
        if (sub[0].equals("reset")) {
        if (sub[1].equals("str") && chr.getStr() > 4) {
        chr.setRemainingAp(chr.getRemainingAp()+(chr.getStr()-4));
        chr.setStr(4);
        chr.updateSingleStat(MapleStat.STR, chr.getStr());
        chr.updateSingleStat(MapleStat.AVAILABLEAP, chr.getRemainingAp());
        }
        if (sub[1].equals("dex" ) && chr.getDex() > 4) {
        chr.setRemainingAp(chr.getRemainingAp()+(chr.getDex()-4));
        chr.setDex(4);
        chr.updateSingleStat(MapleStat.DEX, chr.getDex());
        chr.updateSingleStat(MapleStat.AVAILABLEAP, chr.getRemainingAp());
        }
        if (sub[1].equals("int") && chr.getInt() > 4) {
        chr.setRemainingAp(chr.getRemainingAp()+(chr.getInt()-4));
        chr.setInt(4);
        chr.updateSingleStat(MapleStat.INT, chr.getInt());
        chr.updateSingleStat(MapleStat.AVAILABLEAP, chr.getRemainingAp());
        }
        if (sub[1].equals("luk") && chr.getLuk() > 4) {
        chr.setRemainingAp(chr.getRemainingAp()+(chr.getLuk()-4));
        chr.setLuk(4);
        chr.updateSingleStat(MapleStat.LUK, chr.getLuk());
        chr.updateSingleStat(MapleStat.AVAILABLEAP, chr.getRemainingAp());
        }
        }
        if (sub[0].equals("expbonus")) {
           if (chr.getExpBonuses() >= 1) {
            if (chr.getParty() == null) {
          SkillFactory.getSkill(SuperGM.HOLY_SYMBOL).getEffect(SkillFactory.getSkill(SuperGM.HOLY_SYMBOL).getMaxLevel()).applyTo(chr);     
          chr.setExpBonuses(chr.getExpBonuses()-1);
          chr.dropOverheadMessage("#eYou have used up one 30 Minute 150% exp session. " + chr.getExpBonuses() + " remain. (Check the buff at the top of your screen)");
            } else {
              chr.setExpBonuses(chr.getExpBonuses()-1);
              chr.dropOverheadMessage("#eYou have used up one 30 Minute 150% exp session. " + chr.getExpBonuses() + " remain. (Check the buff at the top of your screen)");
              SkillFactory.getSkill(SuperGM.HOLY_SYMBOL).getEffect(SkillFactory.getSkill(SuperGM.HOLY_SYMBOL).getMaxLevel()).applyTo(chr);
            }
          } else {
          chr.dropOverheadMessage("You don't have any ExpBonuses to use.");
          }
        }
        if (sub[0].equals("rev")) {
        chr.dropMessage(ServerConstants.SERVERNAME + " rev 11");
        NPCScriptManager.getInstance().getCM(c).getPlayer().setlegend(heading);
        }
       if (sub[0].equals("str") || sub[0].equals("int") || sub[0].equals("luk") || sub[0].equals("dex")) {
           int amount = Integer.parseInt(sub[1]);
            if (amount > 0 && amount <= chr.getRemainingAp() && amount < 31997) {
                if (sub[0].equals("str") && amount + chr.getStr() < 32001) {
                    chr.setStr(chr.getStr() + amount);
                    chr.updateSingleStat(MapleStat.STR, chr.getStr());
                    chr.dropMessage("Your Strength stat has been increased.");
                } else if (sub[0].equals("int") && amount + chr.getInt() < 32001) {
                    chr.setInt(chr.getInt() + amount);
                    chr.updateSingleStat(MapleStat.INT, chr.getInt());
                    chr.dropMessage("Your Intelligence stat has been increased.");
                } else if (sub[0].equals("luk") && amount + chr.getLuk() < 32001) {
                    chr.setLuk(chr.getLuk() + amount);
                    chr.updateSingleStat(MapleStat.LUK, chr.getLuk());
                    chr.dropMessage("Your Luck stat has been increased.");
                } else if (sub[0].equals("dex") && amount + chr.getDex() < 32001) {
                    chr.setDex(chr.getDex() + amount);
                    chr.updateSingleStat(MapleStat.DEX, chr.getDex());
                    chr.dropMessage("Your Dexterity stat has been increased.");
                } else {
                    chr.dropMessage("Please make sure the stat you are trying to raise is not over 32000.");
                }
                chr.setRemainingAp(chr.getRemainingAp() - amount);
                chr.updateSingleStat(MapleStat.AVAILABLEAP, chr.getRemainingAp());
            } else {
                chr.dropMessage("Please make sure your AP is not over 32000 and you have enough to distribute.");
            }
       }
        if (sub[0].equals("expfix")) {
        chr.setExp(0);
        chr.dropMessage("Your exp isn't negative or zero.");     
        }
        if (sub[0].equals("dispose")) {
            NPCScriptManager.getInstance().dispose(c);
            c.announce(MaplePacketCreator.enableActions());
            chr.message("Done.");
        } else {
            if (chr.gmLevel() == 0) {

            }
            return false;
        }
        return true;
    }
    public static boolean executeDonatorCommand(MapleClient c, String[] sub, char heading) {
         MapleCharacter player = c.getPlayer();
         ChannelServer cserv = c.getChannelServer();
         if (sub[0].equals("donline")) {
            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                String s = "Characters online (Channel " + ch.getChannel() + " Online: " + ch.getPlayerStorage().getAllCharacters().size() + ") : ";
                if (ch.getPlayerStorage().getAllCharacters().size() < 50) {
                    for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters()) {
                        s += MapleCharacter.makeMapleReadable(chr.getName()) + ", ";
                    }
                    player.dropMessage(s.substring(0, s.length() - 2));
                }
            }
          } else if (sub[0].equals("dspawn")) {
            HashMap<String, Integer> mobs = new HashMap<String, Integer>();
                mobs.put("anego", 9400121);
                mobs.put("balrog", 9400536);
                mobs.put("mushmom", 6130101);
                mobs.put("mushma", 6300005);
                mobs.put("mushdad", 9400205);
                mobs.put("pap", 8500001);
                mobs.put("pianus", 8510000);
                mobs.put("theboss", 9400300);
                mobs.put("manon", 8180000);
                mobs.put("griffey", 8180001);
                mobs.put("bigfoot", 9400575);
                mobs.put("ergoth", 9300028);
                mobs.put("crow ", 9400014);
                mobs.put("phoenix", 9300089);
                mobs.put("freezer", 9300090);
                mobs.put("kyrin", 9300158);
                mobs.put("ikyrin", 9300159);
                mobs.put("leviathan", 8220003);
                mobs.put("franken", 9500335);
                mobs.put("pirate", 9500334);
                mobs.put("centipede", 9600010);
                mobs.put("alishar", 9500330);
                mobs.put("papa", 9500329);
                mobs.put("snowman", 9500319);
                mobs.put("skiman", 8220001);
                mobs.put("elliza", 8220000);
                mobs.put("priestcat", 7220002);
                mobs.put("taeroon", 7220000);
                mobs.put("ninetails", 7220001);
                mobs.put("dyle", 6220000);
                mobs.put("zeno", 6220001);
                mobs.put("timer", 5220003);
                mobs.put("faust", 5220002);
                mobs.put("stumpy", 3220000);
                mobs.put("mano", 2220000);
                mobs.put("latanica", 9420513);
                mobs.put("snackbar", 9410014);
                mobs.put("biker", 9410004);
                mobs.put("bigpuff", 9400569);
                mobs.put("loki", 9400566);
                mobs.put("bob", 9400551);
                mobs.put("kimera", 8220002);
            if (sub.length != 2) {
                StringBuilder builder = new StringBuilder("Syntax: !aspawn [monster]:");
                    for (String allMobs : mobs.keySet()) {
                        if (1 % 10 == 0) {
                            player.message(builder.toString());
                        } else {
                            builder.append(allMobs + ", ");
                        }
                    }
                    player.message(builder.toString());
            } else if (mobs.containsKey(sub[1])) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(mobs.get(sub[1])), player.getPosition());
            } else {
                player.message("Invalid mob, try something else.");
            }
         }
         if (sub[0].equals("donorshop")) {
            NPCScriptManager.getInstance().start(player.getClient(), 2120003, null, null);
         }
         if (sub[0].equals("donornpc")) {
            NPCScriptManager.getInstance().start(player.getClient(), 2040048, null, null);
         }
   if (sub[0].equals("dgoto")) {
            HashMap<String, Integer> maps = new HashMap<String, Integer>();
            maps.put("southperry", 60000);
            maps.put("amherst", 1010000);
            maps.put("henesys", 100000000);
            maps.put("ellinia", 101000000);
            maps.put("perion", 102000000);
            maps.put("kerning", 103000000);
            maps.put("lith", 104000000);
            maps.put("sleepywood", 105040300);
            maps.put("florina", 110000000);
            maps.put("orbis", 200000000);
            maps.put("happy", 209000000);
            maps.put("elnath", 211000000);
            maps.put("ludi", 220000000);
            maps.put("aqua", 230000000);
            maps.put("leafre", 240000000);
            maps.put("mulung", 250000000);
            maps.put("herb", 251000000);
            maps.put("omega", 221000000);
            maps.put("korean", 222000000);
            maps.put("nlc", 600000000);
            maps.put("excavation", 990000000);
            maps.put("pianus", 230040420);
            maps.put("horntail", 240060200);
            maps.put("mushmom", 100000005);
            maps.put("griffey", 240020101);
            maps.put("manon", 240020401);
            maps.put("headless", 682000001);
            maps.put("balrog", 105090900);
            maps.put("zakum", 280030000);
            maps.put("papu", 220080001);
            maps.put("showa", 801000000);
            maps.put("guild", 200000301);
            maps.put("shrine", 800000000);
            maps.put("fm", 910000000);
            maps.put("skelegon", 240040511);
            maps.put("ariant", 260000100);
            maps.put("dmap", 100000203);
            if (sub.length < 2) {
                player.message("Syntax: !goto <mapname> <optional_target>, where target is char name and mapname is one of:");
                StringBuilder builder = new StringBuilder();
                int i = 0;
                for (String mapss : maps.keySet()) {
                    if (1 % 10 == 0) {
                        player.message(builder.toString());
                    } else {
                        builder.append(mapss + ", ");
                    }
                }
                player.message(builder.toString());
            } else {
                String message = sub[1];
                if (maps.containsKey(message)) {
                    if (sub.length == 2) {
                        player.changeMap(maps.get(message));
                    } else if (sub.length == 3) {
                        MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[2]);
                        victim.changeMap(maps.get(message));
                    }
                } else {
                    player.message("Could not find map");
                }
            }
            maps.clear();
        player.getMap().clearDrops();
}
         if (sub[0].equals("dcleardrops")) {
             player.getMap().clearDrops(player);
             player.dropMessage("Drops Cleared.");
         }
         if (sub[0].equals("dhelp")) {
                player.dropMessage("============================================================");
                player.dropMessage("                     "+ ServerConstants.SERVERNAME +" Donator Commands");
                player.dropMessage("============================================================");
                player.dropMessage("@dgoto [mapname] - | - Takes you to a map of your choice");
                player.dropMessage("@dsay - | - Says your message with a [Donator] tag plus your legend");
                player.dropMessage("@dcleardrops - | - Clears all the drops in the map");
                player.dropMessage("@dspawn [mobname] - | - Spawns a monsters of your choice");
                player.dropMessage("@donorshop - | - Opens the donator shop");
                player.dropMessage("@donline - | - Displays users online");
                player.dropMessage("@dbuffme - | - Applys buffs to your character");
         }
          if (sub[0].equals("dbuffme")) {
            final int[] array = {9001000, 9101002, 9101003, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 1111002, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (int i : array) {
                SkillFactory.getSkill(i).getEffect(SkillFactory.getSkill(i).getMaxLevel()).applyTo(player);
            }
          }
            if (sub[0].equals("dsay")) {
            try {
                cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, player.getLegend() + "[Donator]" + player.getName() + ": " + StringUtil.joinStringFrom(sub, 1)).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            }
         if (player.getLevel() > 0) {
            return false;
        }
        return true;
    }


    public static boolean executeGMCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();


        if (sub[0].equals("ap")) {
            player.setRemainingAp(Integer.parseInt(sub[1]));
            player.equipChanged();
             } else if (sub[0].equals("say")) {
           try {
                cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, player.getLegend() + "[GameMaster]" + player.getName() + " : " + StringUtil.joinStringFrom(sub, 1)).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
        } else if (sub[0].equals("buffme")) {
            final int[] array = {9001000, 9101002, 9101003, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 1111002, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (int i : array) {
                SkillFactory.getSkill(i).getEffect(SkillFactory.getSkill(i).getMaxLevel()).applyTo(player);
            }
        } else if (sub[0].equals("spawn")) {
            int subasdf = Integer.parseInt(sub[1]);
            int sfdasd = Integer.parseInt(sub[2]);
            if (Integer.parseInt(sub[2]) == 0) {
            sfdasd = 1;
            }
            for (int i = 0; i < Math.min(subasdf, sfdasd); i++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(Integer.parseInt(sub[1])), player.getPosition());
            }
        } else if (sub[0].equals("giftdonatorpoints")) {
        MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
        int amt = Integer.parseInt(sub[2]);
        victim.gainDonatorPoints(amt);
        victim.dropMessage("You have gained " + amt + " Donator points. You can use them by typing @donorshop. You also have donor status now if you didn't already. '@dhelp' will display your new commands.");
        if (victim.gmLevel() > 1) {
        victim.setGM(1);
        }
        } else if (sub[0].equals("chattype")) {
            player.toggleGMChat();
            player.message("You now chat in " + (player.getGMChat() ? "white." : "black."));
        } else if (sub[0].equals("cleardrops")) {
            player.getMap().clearDrops(player);
        } else if (sub[0].equals("cody")) {
            NPCScriptManager.getInstance().start(player.getClient(), 9200000, null, null);
        } else if (sub[0].equals("dc")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).getClient().disconnect();
        } else if (sub[0].equals("exprate")) {
            ServerConstants.EXP_RATE = (byte) (Integer.parseInt(sub[1]) % 128);
            cserv.broadcastPacket(MaplePacketCreator.serverNotice(6, "Exp Rate has been temporarily changed to " + Integer.parseInt(sub[1]) + "x."));
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                for (MapleCharacter mc : cs.getPlayerStorage().getAllCharacters()) {
                    mc.setRates();
                }
            }
        } else if (sub[0].equals("levelupweapon")) {
         Equip nEquip = (Equip) player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
         nEquip.newGainLevel(c);
         player.equipChanged(); // refreshes EQ i think..
        } else if (sub[0].equals("giftvotepoints")) {
          cserv.getPlayerStorage().getCharacterByName(sub[1]).gainVotePoints(Integer.parseInt(sub[2]));
          cserv.getPlayerStorage().getCharacterByName(sub[1]).dropMessage("You have been gifted " + sub[2] + " VotePoints from " + player.getName() + ". You now have " + cserv.getPlayerStorage().getCharacterByName(sub[1]).getVotePoints() + " VotePoints total.");
        } else if (sub[0].equals("whereami")) {
          player.dropMessage(player.getMapName(player.getMapId()) + " [" + player.getMapId() + "] ");
        } else if (sub[0].equals("warp")) {
          MapleCharacter victim =  cserv.getPlayerStorage().getCharacterByName(sub[1]);  
          player.changeMap(victim.getMapId(), victim.getMap().findClosestSpawnpoint(victim.getPosition()));
        } else if (sub[0].equals("fame")) {
          MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
          victim.setFame(Integer.parseInt(sub[2]));
          victim.updateSingleStat(MapleStat.FAME, victim.getFame());
        } else if (sub[0].equals("map")) {
          player.changeMap(Integer.parseInt(sub[1]), 0);
        } else if (sub[0].equals("warpplayer")) {
          MapleCharacter victim =  cserv.getPlayerStorage().getCharacterByName(sub[1]);
          victim.changeMap(Integer.parseInt(sub[1]), 0);
        } else if (sub[0].equals("gainexpbonus")) {
          player.addExpBonus(); 
        } else if (sub[0].equals("giftnx")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).getCashShop().gainCash(1, Integer.parseInt(sub[2]));
            player.message("Done.");
        } else if (sub[0].equals("gmshop")) {
            MapleShopFactory.getInstance().getShop(1337).sendShop(c);
        } else if (sub[0].equals("openshop")) {
            if (sub[0].equals("")) {
            MapleShopFactory.getInstance().getShop(1).sendShop(c);
            } else {
            MapleShopFactory.getInstance().getShop(Integer.parseInt(sub[1])).sendShop(c);
            NPCScriptManager.getInstance().dispose(c);
            }

        } else if (sub[0].equals("heal")) {
            player.setHpMp(30000);
		} else if (sub[0].equals("healch")) {
	    for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
		if (mch != null) {
		    mch.setHp(mch.getMaxHp());
		    mch.updateSingleStat(MapleStat.HP, mch.getMaxHp());
		    mch.setMp(mch.getMaxMp());
		    mch.updateSingleStat(MapleStat.MP, mch.getMaxMp());
		}
	    }	
		} else if (sub[0].equals("healmap")) {
	    for (MapleCharacter mch : c.getPlayer().getMap().getCharacters()) {
		if (mch != null) {
		    mch.setHp(mch.getMaxHp());
		    mch.updateSingleStat(MapleStat.HP, mch.getMaxHp());
		    mch.setMp(mch.getMaxMp());
		    mch.updateSingleStat(MapleStat.MP, mch.getMaxMp());
		}
	    }
        } else if (sub[0].equals("id")) {
            try {
                BufferedReader dis = new BufferedReader(new InputStreamReader(new URL("http://www.mapletip.com/search_java.php?search_value=" + sub[1] + "&check=true").openConnection().getInputStream()));
                String s;
                while ((s = dis.readLine()) != null) {
                    player.dropMessage(s);
                }
                dis.close();
            } catch (Exception e) {
            }
        } else if (sub[0].equals("item") || sub[0].equals("drop")) {
            int itemId = Integer.parseInt(sub[1]);
            short quantity = 1;
            try {
                quantity = Short.parseShort(sub[2]);
            } catch (Exception e) {
            }
            if (sub[0].equals("item")) {
                int petid = -1;
                if (ItemConstants.isPet(itemId)) {
                    petid = MaplePet.createPet(itemId);
                } 
                MapleInventoryManipulator.addById(c, itemId, quantity, player.getName(), petid, -1);
            } else {
                IItem toDrop;
                if (MapleItemInformationProvider.getInstance().getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    toDrop = MapleItemInformationProvider.getInstance().getEquipById(itemId);
                } else {
                    toDrop = new Item(itemId, (byte) 0, quantity);
                }
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
            }
        } else if (sub[0].equals("job")) {
            player.changeJob(MapleJob.getById(Integer.parseInt(sub[1])));
            player.equipChanged();
            player.dropMessage("Job changed.");
        } else if (sub[0].equals("kill")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).setHpMp(0);
        } else if (sub[0].equals("killall")) {
            List<MapleMapObject> monsters = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                player.getMap().killMonster(monster, player, true);
                monster.giveExpToCharacter(player, monster.getExp() * c.getPlayer().getExpRate(), true, 1);
            }
            player.dropMessage("Killed " + monsters.size() + " monsters.");
		} else if (sub[0].equals("killch")) {
	    for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
		if (mch != null && mch.isGM() == false) {
		    mch.setHpMp(0);
			}
			}	
        } else if (sub[0].equals("unbug")) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.enableActions());
        } else if (sub[0].equals("level")) {
            player.setLevel(Integer.parseInt(sub[1]));
            player.gainExp(-player.getExp(), false, false);
            player.updateSingleStat(MapleStat.LEVEL, player.getLevel());
        } else if (sub[0].equals("levelperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            victim.setLevel(Integer.parseInt(sub[2]));
            victim.gainExp(-victim.getExp(), false, false);
            victim.updateSingleStat(MapleStat.LEVEL, victim.getLevel());
        } else if (sub[0].equals("levelpro")) {
            while (player.getLevel() < Math.min(255, Integer.parseInt(sub[1]))) {
                player.levelUp(false);
            }
        } else if (sub[0].equals("occupation")) {
         player.setOccuption(Integer.parseInt(sub[1]));
         player.dropMessage("You are now a " + player.getOccupationName());
        } else if (sub[0].equals("levelup")) {
            player.levelUp(false);
        } else if (sub[0].equals("maxstat")) {
            final String[] s = {"setall", String.valueOf(Short.MAX_VALUE)};
            executeGMCommand(c, s, heading);
            player.setLevel(255);
            player.setFame(13337);
            player.setMaxHp(30000);
            player.setMaxMp(30000);
            player.updateSingleStat(MapleStat.LEVEL, 255);
            player.updateSingleStat(MapleStat.FAME, 13337);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);
        } else if (sub[0].equals("maxskills")) {
            for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
                try {
                    ISkill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                    player.changeSkillLevel(skill, skill.getMaxLevel(), skill.getMaxLevel(), -1);
                } catch (NumberFormatException nfe) {
                    break;
                } catch (NullPointerException npe) {
                    continue;
                }
            }
        } else if (sub[0].equals("mesoperson")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).gainMeso(Integer.parseInt(sub[2]), true);
        } else if (sub[0].equals("mesos")) {
            player.gainMeso(Integer.parseInt(sub[1]), true);
        } else if (sub[0].equals("notice")) {
            try {
                cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "[Notice] " + joinStringFrom(sub, 1)).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
        } else if (sub[0].equals("openportal")) {
           c.getPlayer().getMap().getPortal(sub[1]).setPortalState(true);
        } else if (sub[0].equals("closeportal")) {
           c.getPlayer().getMap().getPortal(sub[1]).setPortalState(false);
        } else if (sub[0].equals("startevent")) {
            for (MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
                 c.getPlayer().getMap().startEvent(chr);
            }
            c.getChannelServer().setEvent(null);
        } else if (sub[0].equals("scheduleevent")) {
           if (c.getPlayer().getMap().hasEventNPC()) {
            if (sub[1].equals("treasure")) {
                c.getChannelServer().setEvent(new MapleEvent(109010000, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (sub[1].equals("ox")) {
                c.getChannelServer().setEvent(new MapleEvent(109020001, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (sub[1].equals("ola")) {
                c.getChannelServer().setEvent(new MapleEvent(109030101, 50)); // Wrong map but still Ola Ola
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (sub[1].equals("fitness")) {
                c.getChannelServer().setEvent(new MapleEvent(109040000, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (sub[1].equals("snowball")) {
                c.getChannelServer().setEvent(new MapleEvent(109060001, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (sub[1].equals("coconut")) {
                c.getChannelServer().setEvent(new MapleEvent(109080000, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else {
                player.message("Wrong Syntax: /scheduleevent treasure, ox, ola, fitness, snowball or coconut");
            }
           } else {
               player.message("You can only use this command in the following maps: 60000, 104000000, 200000000, 220000000");
           }

        } else if (sub[0].equals("online")) {
            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                String s = "Characters online (Channel " + ch.getChannel() + " Online: " + ch.getPlayerStorage().getAllCharacters().size() + ") : ";
                if (ch.getPlayerStorage().getAllCharacters().size() < 50) {
                    for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters()) {
                        s += MapleCharacter.makeMapleReadable(chr.getName()) + ", ";
                    }
                    player.dropMessage(s.substring(0, s.length() - 2));
                }
            }
        } else if (sub[0].equals("pap")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8500001), player.getPosition());
        } else if (sub[0].equals("pianus")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8510000), player.getPosition());
        } else if (sub[0].equalsIgnoreCase("search")) {
            if (sub.length > 2) {
                String search = joinStringFrom(sub, 2);
                MapleData data = null;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz"));
                player.dropMessage("~Searching~ <<Type: " + sub[1] + " | Search: " + search + ">>");
                if (!sub[1].equalsIgnoreCase("ITEM")) {
                    if (sub[1].equalsIgnoreCase("NPC")) {
                        data = dataProvider.getData("Npc.img");
                    } else if (sub[1].equalsIgnoreCase("MOB")) {
                        List<String> retMobs = new LinkedList<String>();
                        data = dataProvider.getData("Mob.img");
                        List<Pair<Integer, String>> mobPairList = new LinkedList<Pair<Integer, String>>();
                        for (MapleData mobIdData : data.getChildren()) {
                            int mobIdFromData = Integer.parseInt(mobIdData.getName());
                            String mobNameFromData = MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME");
                            mobPairList.add(new Pair<Integer, String>(mobIdFromData, mobNameFromData));
                        }
                        for (Pair<Integer, String> mobPair : mobPairList) {
                            if (mobPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                                retMobs.add(mobPair.getLeft() + " - " + mobPair.getRight());
                            }
                        }
                        if (retMobs != null && retMobs.size() > 0) {
                            for (String singleRetMob : retMobs) {
                                player.dropMessage(singleRetMob);
                            }
                        } else {
                            player.dropMessage("No Mob's Found");
                        }
                    } else if (sub[1].equalsIgnoreCase("SKILL")) {
                        data = dataProvider.getData("Skill.img");
                    } else {
                        player.dropMessage("Invalid search.\nSyntax: '/search [type] [name]', where [type] is NPC, MAP, ITEM, MOB, or SKILL.");
                        return true;
                    }
                    List<Pair<Integer, String>> searchList = new LinkedList<Pair<Integer, String>>();
                    for (MapleData searchData : data.getChildren()) {
                        int searchFromData = Integer.parseInt(searchData.getName());
                        String infoFromData = sub[1].equalsIgnoreCase("MAP") ? MapleDataTool.getString(searchData.getChildByPath("streetName"), "NO-NAME") + " - " + MapleDataTool.getString(searchData.getChildByPath("mapName"), "NO-NAME") : MapleDataTool.getString(searchData.getChildByPath("name"), "NO-NAME");
                        searchList.add(new Pair<Integer, String>(searchFromData, infoFromData));
                    }
                    for (Pair<Integer, String> searched : searchList) {
                        if (searched.getRight().toLowerCase().contains(search.toLowerCase())) {
                            player.dropMessage(searched.getLeft() + " - " + searched.getRight());
                        }
                    }
                } else {
                    for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (itemPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            player.dropMessage(itemPair.getLeft() + " - " + itemPair.getRight());
                        }
                    }
                    player.dropMessage("Search Complete.");
                }
            } else {
                player.dropMessage("Invalid search.\nSyntax: '/search [type] [name]', where [type] is NPC, ITEM, MOB, or SKILL.");
            }
        
        } else if (sub[0].equals("warpsnowball")) {
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                 chr.changeMap(109060000, chr.getTeam());
            }
        } else if (sub[0].equals("team")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
            victim.setTeam(Integer.parseInt(sub[2]));
        } else if (sub[0].equals("setall")) {
            final int x = Short.parseShort(sub[1]);
            player.setStr(x);
            player.setDex(x);
            player.setInt(x);
            player.setLuk(x);
            player.updateSingleStat(MapleStat.STR, x);
            player.updateSingleStat(MapleStat.DEX, x);
            player.updateSingleStat(MapleStat.INT, x);
            player.updateSingleStat(MapleStat.LUK, x);
        } else if (sub[0].equals("doreborn")) {
            player.doReborn();
        } else if (sub[0].equals("jobperson")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
            victim.changeJob(MapleJob.getById(Integer.parseInt(sub[2])));
            player.equipChanged();
        } else if (sub[0].equals("sp")) {
            player.setRemainingSp(Integer.parseInt(sub[1]));
            player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
        } else if (sub[0].equals("giftmedal")) {
            player.giftMedal(Integer.parseInt(sub[1]));
        } else if (sub[0].equals("unban")) {
            try {
                PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET banned = -1 WHERE id = " + MapleCharacter.getIdByName(sub[1]));
                p.executeUpdate();
                p.close();
            } catch (Exception e) {
                player.message("Failed to unban " + sub[1]);
                return true;
            }
            player.message("Unbanned " + sub[1]);
        } else {
            if (player.gmLevel() == 1) {
                player.yellowMessage("GM Command " + heading + sub[0] + " does not exist");
            }
            return false;
        }
        return true;
    }

    public static void executeAdminCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        if (sub[0].equals("horntail")) {
            for (int i = 8810002; i < 8810010; i++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(i), player.getPosition());
            }
        } else if (sub[0].equals("mesorate")) {
            ServerConstants.MESO_RATE = (byte) (Integer.parseInt(sub[1]) % 128);
            cserv.broadcastPacket(MaplePacketCreator.serverNotice(6, "Meso Rate has been changed to " + Integer.parseInt(sub[1]) + "x."));
        } else if (sub[0].equals("servermessage")) {
            for (int i = 1; i <= ChannelServer.getAllInstances().size(); i++) {
                ChannelServer.getInstance(i).setServerMessage(joinStringFrom(sub, 1));
            }
        } else if (sub[0].equals("packet")) {
            player.getMap().broadcastMessage(MaplePacketCreator.customPacket(joinStringFrom(sub, 1)));
        } else if (sub[0].equals("npc")) {
            MapleNPC npc = MapleLifeFactory.getNPC(Integer.parseInt(sub[1]));
            if (npc != null) {
                npc.setPosition(player.getPosition());
                npc.setCy(player.getPosition().y);
                npc.setRx0(player.getPosition().x + 50);
                npc.setRx1(player.getPosition().x - 50);
                npc.setFh(player.getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            }
        
        
        } else if (sub[0].equals("pinkbean")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8820009), player.getPosition());
        } else if (sub[0].equals("playernpc")) {
            player.playerNPC(c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]), Integer.parseInt(sub[2]));
        } else if (sub[0].equals("setgmlevel")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
            victim.setGM(Integer.parseInt(sub[2]));
            player.message("Done.");
            victim.getClient().disconnect();
        } else if (sub[0].equals("shutdown") || sub[0].equals("shutdownnow")) {
            int time = 60000;
            if (sub[0].equals("shutdownnow")) {
                time = 1;
            } else if (sub.length > 1) {
                time *= Integer.parseInt(sub[1]);
            }
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.shutdown(time);
            }
        } else if (sub[0].equals("sql")) {
            final String query = Commands.joinStringFrom(sub, 1);
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
                ps.executeUpdate();
                ps.close();
                player.message("Done " + query);
            } catch (SQLException e) {
                player.message("Query Failed: " + query);
            }
        } else if (sub[0].equals("sqlwithresult")) {
            String name = sub[1];
            final String query = Commands.joinStringFrom(sub, 2);
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    player.dropMessage(String.valueOf(rs.getObject(name)));
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                player.message("Query Failed: " + query);
            }
        } else if (sub[0].equals("zakum")) {
            player.getMap().spawnFakeMonsterOnGroundBelow(MapleLifeFactory.getMonster(8800000), player.getPosition());
            for (int x = 8800003; x < 8800011; x++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(x), player.getPosition());
            }
        } else {
    
        }
    }

    private static String joinStringFrom(String arr[], int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i != arr.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
}