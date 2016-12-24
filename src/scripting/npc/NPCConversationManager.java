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
package scripting.npc;

import client.Equip;
import client.ISkill;
import client.ItemFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import constants.ExpTable;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleJob;
import client.MaplePet;
import client.MapleSkinColor;
import client.MapleStat;
import client.SkillFactory;
import tools.Randomizer;
import java.io.File;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import net.channel.ChannelServer;
import tools.DatabaseConnection;
import net.world.MapleParty;
import net.world.guild.MapleAlliance;
import server.MapleShopFactory;
import net.world.guild.MapleGuild;
import net.world.remote.WorldChannelInterface;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import scripting.AbstractPlayerInteraction;
import scripting.event.EventManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleSquad;
import server.MapleSquadType;
import server.MapleStatEffect;
import server.events.MapleEvent;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import client.MapleInventory;
import client.IItem;
import client.MapleCQuests;

/**
 *
 * @author Matze
 */
public class NPCConversationManager extends AbstractPlayerInteraction {

    private int npc;
    private String getText;

    public NPCConversationManager(MapleClient c, int npc) {
        super(c);
        this.npc = npc;
    }

    public int getNpc() {
        return npc;
    }

    public static int editEquipById(MapleCharacter chr, int max, int itemid, String stat, int newval) {
        return editEquipById(chr,max,itemid,stat,(short)newval);
    }
    
    public void gainEpicItem (byte slot, MapleCharacter player, short str, short dex, short Int, short luk, short wa, short ma) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        int item = equip.getItem(slot).getItemId();
        short hand = eu.getHands();
        byte level = eu.getLevel();
        Equip nItem = new Equip(item, equip.getNextFreeSlot());
        nItem.setStr(str); // STR
        nItem.setDex(dex); // DEX
        nItem.setInt(Int); // INT
        nItem.setLuk(luk); //LUK
        nItem.setWatk(wa);
        nItem.setMatk(ma);
        nItem.setUpgradeSlots(nItem.getUpgradeSlots()); // Can Upgrade amount
        nItem.setHands(hand);
        nItem.setLevel(level);
        player.getInventory(MapleInventoryType.EQUIP).removeItem(slot);
        player.getInventory(MapleInventoryType.EQUIP).addItem(nItem);
}
public void gainEpicItem (MapleCharacter player, short str, short dex, short Int, short luk, short wa, short ma) {
        byte slot =  player.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot();
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        int item = equip.getItem(slot).getItemId();
        short hand = eu.getHands();
        byte level = eu.getLevel();
        Equip nItem = new Equip(item, equip.getNextFreeSlot());
        nItem.setStr(str); // STR
        nItem.setDex(dex); // DEX
        nItem.setInt(Int); // INT
        nItem.setLuk(luk); //LUK
        nItem.setWatk(wa);
        nItem.setMatk(ma);
        nItem.setUpgradeSlots(nItem.getUpgradeSlots()); // Can Upgrade amount
        nItem.setHands(hand);
        nItem.setLevel(level);
        player.getInventory(MapleInventoryType.EQUIP).removeItem(slot);
        player.getInventory(MapleInventoryType.EQUIP).addItem(nItem);
}

    public void MakeGMItem (byte slot, MapleCharacter player) {
      MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
      Equip eu = (Equip) equip.getItem(slot);
      int item = equip.getItem(slot).getItemId();
      short hand = eu.getHands();
      byte level = eu.getLevel();
      Equip nItem = new Equip(item, equip.getNextFreeSlot());
      nItem.setStr((short) 30000); // STR
      nItem.setDex((short) 30000); // DEX
      nItem.setInt((short) 30000); // INT
      nItem.setLuk((short) 30000); //LUK
      nItem.setUpgradeSlots((byte) 0);
      nItem.setHands(hand);
      nItem.setLevel(level);
        player.getInventory(MapleInventoryType.EQUIP).removeItem(slot);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
    }

    public String EquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
           MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
                List<String> stra = new LinkedList<String>();
                    for (IItem item : equip.list()) {
                        stra.add("#L"+item.getPosition()+"##v"+item.getItemId()+"##l");
                        }
                    for (String strb : stra) {
                str.append(strb);
            }
        return str.toString();
    }

    public void reloadChar() {
        getPlayer().getClient().announce(MaplePacketCreator.getCharInfo(getPlayer()));
        getPlayer().getMap().removePlayer(getPlayer());
        getPlayer().getMap().addPlayer(getPlayer());
    }

public void MakeDonatorItem5000 (byte slot, MapleCharacter player) {
Equip nEquip = (Equip) getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
nEquip.donator5000(getPlayer(), slot);
}


public boolean makeProItem(int id, int hardcore) {
MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
IItem item = ii.getEquipById(id);
MapleInventoryType type = ii.getInventoryType(id);
if (type.equals(MapleInventoryType.EQUIP)) {
MapleInventoryManipulator.addFromDrop(c, ii.hardcoreItem((Equip) item, (short) hardcore), true);
return true;
} else {
return false;
}
}

    public void dispose() {
        NPCScriptManager.getInstance().dispose(this);
    }

    public void sendNext(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", (byte) 0));
    }

    public void sendPrev(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", (byte) 0));
    }

    public void sendNextPrev(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", (byte) 0));
    }

    public void sendOk(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
    }

    public void sendYesNo(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", (byte) 0));
    }

    public void sendAcceptDecline(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", (byte) 0));
    }

    public void sendSimple(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) 0));
    }

    public void sendNext(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", speaker));
    }

    public void sendPrev(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", speaker));
    }

    public void sendNextPrev(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", speaker));
    }

    public void sendOk(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", speaker));
    }

    public void sendYesNo(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", speaker));
    }

    public void sendAcceptDecline(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", speaker));
    }

    public void sendSimple(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", speaker));
    }

    public void sendStyle(String text, int styles[]) {
        getClient().announce(MaplePacketCreator.getNPCTalkStyle(npc, text, styles));
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        getClient().announce(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
    }

    public void sendGetText(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalkText(npc, text));
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return this.getText;
    }

    public int getJobId() {
        return getPlayer().getJob().getId();
    }

    public void startQuest(int id) { 
        getPlayer().getCQuest().loadQuest(id); 
        getPlayer().setQuestId(id); 
        getPlayer().setQuestKills(1, 0); 
        getPlayer().setQuestKills(2, 0); 
        if (id == 0) { 
            getPlayer().sendHint("#eQuest Canceled!"); 
        } else {     
            getPlayer().sendHint("#eQuest Start: " + getPlayer().getCQuest().getTitle()); 
        } 
    } 

    public void completeQuest(int id) {
        try {
            MapleQuest.getInstance(id).forceComplete(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public int getMeso() {
        return getPlayer().getMeso();
    }

    public void gainMeso(int gain) {
        getPlayer().gainMeso(gain, true, false, true);
    }

    public void gainExp(int gain) {
        getPlayer().gainExp(gain, true, true);
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public EventManager getEventManager(String event) {
        return getClient().getChannelServer().getEventSM().getEventManager(event);
    }

    public void showEffect(String effect) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(effect, 3));
    }

    public void playSound(String sound) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(sound, 4));
    }

    public void setHair(int hair) {
        getPlayer().setHair(hair);
        getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        getPlayer().equipChanged();
    }

    public void setFace(int face) {
        getPlayer().setFace(face);
        getPlayer().updateSingleStat(MapleStat.FACE, face);
        getPlayer().equipChanged();
    }
    public void openShop(int shop) {
    MapleShopFactory.getInstance().getShop(shop).sendShop(c);
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor(MapleSkinColor.getById(color));
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public int itemQuantity(int itemid) {
        return getPlayer().getInventory(MapleItemInformationProvider.getInstance().getInventoryType(itemid)).countById(itemid);
    }

    public void displayGuildRanks() {
        MapleGuild.displayGuildRanks(getClient(), npc);
    }

    @Override
    public MapleParty getParty() {
        return getPlayer().getParty();
    }

    @Override
    public void resetMap(int mapid) {
        getClient().getChannelServer().getMapFactory().getMap(mapid).resetReactors();
    }

    public void environmentChange(String env, int mode) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(env, mode));
    }

    public void gainCloseness(int closeness) {
        for (MaplePet pet : getPlayer().getPets()) {
            if (pet.getCloseness() > 30000) {
                pet.setCloseness(30000);
                return;
            }
            pet.gainCloseness(closeness);
            while (pet.getCloseness() > ExpTable.getClosenessNeededForLevel(pet.getLevel())) {
                pet.setLevel(pet.getLevel() + 1);
                getClient().announce(MaplePacketCreator.showOwnPetLevelUp(getPlayer().getPetIndex(pet)));
            }
            getPlayer().getClient().announce(MaplePacketCreator.updatePet(pet));
        }
    }

    public String getName() {
        return getPlayer().getName();
    }

    public int getGender() {
        return getPlayer().getGender();
    }

    public void changeJobById(int a) {
        getPlayer().changeJob(MapleJob.getById(a));
    }

    public void addRandomItem(int id) {
        MapleItemInformationProvider i = MapleItemInformationProvider.getInstance();
        MapleInventoryManipulator.addFromDrop(getClient(), i.randomizeStats((Equip) i.getEquipById(id)), true);
    }

    public MapleJob getJobName(int id) {
        return MapleJob.getById(id);
    }

    public MapleStatEffect getItemEffect(int itemId) {
        return MapleItemInformationProvider.getInstance().getItemEffect(itemId);
    }

    public void resetStats() {
        getPlayer().resetStats();
    }

    public void maxMastery() {
        for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
            try {
                ISkill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                if ((skill.getId() / 10000 % 10 == 2 || (getClient().getPlayer().isCygnus() && skill.getId() / 10000 % 10 == 1) || (getClient().getPlayer().isAran() && skill.getId() / 10000 % 10 == 2)) && getPlayer().getSkillLevel(skill) < 1) {
                    getPlayer().changeSkillLevel(skill, 0, skill.getMaxLevel(), -1);
                }
            } catch (NumberFormatException nfe) {
                break;
            } catch (NullPointerException npe) {
                continue;
            }
        }
    }

    public void processGachapon(int[] id, boolean remote) {
        int[] gacMap = {100000000, 101000000, 102000000, 103000000, 105040300, 800000000, 809000101, 809000201, 600000000, 120000000};
        int itemid = id[Randomizer.nextInt(id.length)];
        addRandomItem(itemid);
        if (!remote) {
            gainItem(5220000, (short) -1);
        }
        sendNext("You have obtained a #b#t" + itemid + "##k.");
        getClient().getChannelServer().broadcastPacket(MaplePacketCreator.gachaponMessage(getPlayer().getInventory(MapleInventoryType.getByType((byte) (itemid / 1000000))).findById(itemid), c.getPlayer().getMapName(gacMap[(getNpc() != 9100117 && getNpc() != 9100109) ? (getNpc() - 9100100) : getNpc() == 9100109 ? 8 : 9]), getPlayer()));
    }

    public void disbandAlliance(MapleClient c, int allianceId) {
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM `alliance` WHERE id = ?");
            ps.setInt(1, allianceId);
            ps.executeUpdate();
            ps.close();
            c.getChannelServer().getWorldInterface().allianceMessage(c.getPlayer().getGuild().getAllianceId(), MaplePacketCreator.disbandAlliance(allianceId), -1, -1);
            c.getChannelServer().getWorldInterface().disbandAlliance(allianceId);
        } catch (RemoteException r) {
            c.getChannelServer().reconnectWorld();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public boolean canBeUsedAllianceName(String name) {
        if (name.contains(" ") || name.length() > 12) {
            return false;
        }
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT name FROM alliance WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                rs.close();
                return false;
            }
            ps.close();
            rs.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static MapleAlliance createAlliance(MapleCharacter chr1, MapleCharacter chr2, String name) {
        int id = 0;
        int guild1 = chr1.getGuildId();
        int guild2 = chr2.getGuildId();
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `alliance` (`name`, `guild1`, `guild2`) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, guild1);
            ps.setInt(3, guild2);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        MapleAlliance alliance = new MapleAlliance(name, id, guild1, guild2);
        try {
            WorldChannelInterface wci = chr1.getClient().getChannelServer().getWorldInterface();
            wci.setGuildAllianceId(guild1, id);
            wci.setGuildAllianceId(guild2, id);
            chr1.setAllianceRank(1);
            chr1.saveGuildStatus();
            chr2.setAllianceRank(2);
            chr2.saveGuildStatus();
            wci.addAlliance(id, alliance);
            wci.allianceMessage(id, MaplePacketCreator.makeNewAlliance(alliance, chr1.getClient()), -1, -1);
        } catch (RemoteException e) {
            chr1.getClient().getChannelServer().reconnectWorld();
            chr2.getClient().getChannelServer().reconnectWorld();
            return null;
        }
        return alliance;
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<MapleCharacter>();
        for (ChannelServer channel : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : channel.getPartyMembers(getPlayer().getParty())) {
                if (chr != null) {
                    chars.add(chr);
                }
            }
        }
        return chars;
    }

    public void warpParty(int id) {
        for (MapleCharacter mc : getPartyMembers()) {
            if (id == 925020100) {
                mc.setDojoParty(true);
            }
            mc.changeMap(getWarpMap(id));
        }
    }

    public boolean hasMerchant() {
        return getPlayer().hasMerchant();
    }

    public boolean hasMerchantItems() {
        try {
            if (!ItemFactory.MERCHANT.loadItems(getPlayer().getId(), false).isEmpty()) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        if (getPlayer().getMerchantMeso() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void showFredrick() {
        c.announce(MaplePacketCreator.getFredrick(getPlayer()));
    }

    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharacters()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public MapleSquad createMapleSquad(MapleSquadType type) {
        MapleSquad squad = new MapleSquad(c.getChannel(), getPlayer());
        if (getSquadState(type) == 0) {
            c.getChannelServer().addMapleSquad(squad, type);
        } else {
            return null;
        }
        return squad;
    }

    public MapleCharacter getSquadMember(MapleSquadType type, int index) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        MapleCharacter ret = null;
        if (squad != null) {
            ret = squad.getMembers().get(index);
        }
        return ret;
    }

    public int getSquadState(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.getStatus();
        }
        return 0;
    }

    public void setSquadState(MapleSquadType type, int state) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.setStatus(state);
        }
    }

    public boolean checkSquadLeader(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.getLeader().getId() == getPlayer().getId();
        }
        return false;
    }

    public int numSquadMembers(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        int ret = 0;
        if (squad != null) {
            ret = squad.getSquadSize();
        }
        return ret;
    }

    public boolean isSquadMember(MapleSquadType type) {
        return c.getChannelServer().getMapleSquad(type).containsMember(getPlayer());
    }

    public void addSquadMember(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.addMember(getPlayer());
        }
    }

    public void removeSquadMember(MapleSquadType type, int index, boolean ban) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(squad.getMembers().get(index), ban);
        }
    }

    public boolean canAddSquadMember(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return !squad.isBanned(getPlayer());
        }
        return false;
    }

    public void warpSquadMembers(MapleSquadType type, int mapId) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapId);
        if (squad != null) {
            if (checkSquadLeader(type)) {
                for (MapleCharacter chr : squad.getMembers()) {
                    chr.changeMap(map, map.getPortal(0));
                }
            }
        }
    }

    public MapleEvent getEvent() {
        return c.getChannelServer().getEvent();
    }

    public void divideTeams() {
        if (getEvent() != null) {
            getPlayer().setTeam(getEvent().getLimit() % 2); //muhaha :D
        }
    }
    
    public int getStory() { 
        return getPlayer().getStory(); 
    } 
     
    public boolean onQuest() { 
        return getPlayer().getQuestId() > 0; 
    } 
     
    public boolean onQuest(int questid) { 
        return getPlayer().getQuestId() == questid; 
    } 
     
    public boolean canComplete() { 
        return getPlayer().canComplete(); 
    } 
     
    public String selectQuest(int questid, String msg) { 
        String intro = msg + "\r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n#L0#"; 
        String selection = "#k[" + (getPlayer().getQuestId() == 0 ? "#rAvailable" : (getPlayer().getQuestId() == questid && !canComplete()) ? "#dIn Prog." : "#gComplete") + "#k]"; 
        return intro + selection + " #e" + getPlayer().getCQuest().loadTitle(questid); 
    } 
         
    public String showReward(String msg) { 
        StringBuilder sb = new StringBuilder(); 
        sb.append(msg); 
        sb.append("\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n\r\n"); 
        sb.append("#fUI/UIWindow.img/QuestIcon/8/0#  ").append(getPlayer().makeNumberReadable(getPlayer().getCQuest().getReward(1))).append("\r\n"); 
        sb.append("#fUI/UIWindow.img/QuestIcon/7/0#  ").append(getPlayer().makeNumberReadable(getPlayer().getCQuest().getReward(2))).append("\r\n");     
        if (getPlayer().getCQuest().getReward(3) > 0) { 
            sb.append("\r\n#i").append(getPlayer().getCQuest().getReward(3)).append("#  ").append(getPlayer().makeNumberReadable(getPlayer().getCQuest().getItemRewardAmount())); 
        } 
        return sb.toString(); 
    } 
     
    public void rewardPlayer(int story, int storypoints) { 
        MapleCQuests q = getPlayer().getCQuest(); 
        getPlayer().addStory(story); 
        getPlayer().addStoryPoints(storypoints); 
        gainExp(q.getReward(1)); 
        gainMeso(q.getReward(2)); 
        if (q.getReward(3) > 0) { 
            gainItem(q.getReward(3), (short) (q.getItemRewardAmount() > 0 ? q.getItemRewardAmount() : 1)); 
        } 
        if (q.getItemId(1) > 0) { 
            gainItem(q.getItemId(1), (short) q.getToCollect(1)); 
        } 
        if (q.getItemId(2) > 0) { 
            gainItem(q.getItemId(2), (short) q.getToCollect(2)); 
        } 
        c.announce(MaplePacketCreator.playSound("Dojan/clear")); 
        c.announce(MaplePacketCreator.showEffect("dojang/end/clear")); 
        startQuest(0); 
    } 
     
    public void rewardPlayer() { 
        rewardPlayer(1, 1); 
    } 
     
    public String showQuestProgress() { 
        MapleCQuests q = getPlayer().getCQuest(); 
        boolean mob1 = q.getTargetId(1) > 0; 
        boolean mob2 = q.getTargetId(2) > 0; 
        boolean item1 = q.getItemId(1) > 0; 
        boolean item2 = q.getItemId(2) > 0; 
        boolean killed1 = getPlayer().getQuestKills(1) >= q.getToKill(1); 
        boolean killed2 = getPlayer().getQuestKills(2) >= q.getToKill(2); 
        boolean collected1 = false; 
        boolean collected2 = false; 
        if (item1) { 
            if (itemQuantity(q.getItemId(1)) >= q.getToCollect(1)) { 
                collected1 = true; 
            } 
        } 
        if (item2) { 
            if (itemQuantity(q.getItemId(2)) >= q.getToCollect(2)) { 
                collected2 = true; 
            } 
        } 
        StringBuilder sb = new StringBuilder(); 
        sb.append("Your Quest Info for:\r\n#e#r").append(q.getTitle()).append("#n#k\r\n\r\n"); 
        if (mob1 || mob2) { 
            sb.append("#eMonster Targets: #n\r\n"); 
            if (mob1) { 
                sb.append(q.getTargetName(1)).append(": ").append(killed1 ? "#g" : "#r").append(getPlayer().getQuestKills(1)).append(" #k/ ").append(q.getToKill(1)).append("\r\n"); 
            } 
            if (mob2) { 
                sb.append(q.getTargetName(2)).append(": ").append(killed2 ? "#g" : "#r").append(getPlayer().getQuestKills(2)).append(" #k/ ").append(q.getToKill(2)).append("\r\n"); 
            } 
            sb.append("\r\n"); 
        } 
        if (item1 || item2) { 
            sb.append("#eItems To Collect: #n\r\n"); 
            if (item1) { 
                sb.append(q.getItemName(1)).append(": ").append(collected1 ? "#g" : "#r").append(itemQuantity(q.getItemId(1))).append(" #k/ ").append(q.getToCollect(1)).append("\r\n"); 
            } 
            if (item2) { 
                sb.append(q.getItemName(2)).append(": ").append(collected2 ? "#g" : "#r").append(itemQuantity(q.getItemId(2))).append(" #k/ ").append(q.getToCollect(2)).append("\r\n"); 
            } 
            sb.append("\r\n"); 
        } 
        sb.append("#eQuest NPC: #n\r\n#d").append(q.getNPC()).append("\r\n"); 
        sb.append("#eQuest Info: #n\r\n").append(q.getInfo()); 
        return sb.toString(); 
    } 
     
    public String randomText(int type) { 
        switch (type) { 
            case 1: 
                return "Hey, what's up?"; 
            case 2: 
                return "Well, done"; 
            case 3: 
                return "What a shame"; 
            case 4:  
                return "You already accepted: "; 
            case 5: 
                return ". Do you want to cancel it?"; 
            case 6: 
                return "Quest Complete!"; 
            case 7: 
                return "You didn't complete your task yet. You can look it up by typing @questinfo in the chat.\r\nDo you want to cancel this Quest?"; 
        } 
        return ""; 
    }  
}
