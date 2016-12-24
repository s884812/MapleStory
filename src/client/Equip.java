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
package client;

import constants.ExpTable;
import java.util.LinkedList;
import tools.MaplePacketCreator;
import tools.Randomizer;
import server.MapleInventoryManipulator;
import net.channel.ChannelServer;
import net.channel.PlayerStorage;
import server.MapleItemInformationProvider;
import scripting.npc.NPCScriptManager;
import constants.ServerConstants;
import server.life.MapleMonster;

public class Equip extends Item implements IEquip {
    private byte upgradeSlots;
    private byte level, flag, itemLevel;
    private short str, dex, _int, luk, hp, mp, watk, matk, wdef, mdef, acc, avoid, hands, speed, jump, vicious;
    private int ringid;
    private int itemExp;
    private boolean wear = false;

    public Equip(int id, byte position) {
        super(id, position, (short) 1);
        this.itemExp = 0;
        this.itemLevel = 1;
        this.ringid = -1;
    }

    public Equip(int id, byte position, int slots, int ringid) {
        super(id, position, (short) 1);
        this.upgradeSlots = (byte) slots;
        this.itemExp = 0;
        this.itemLevel = 1;
        this.ringid = ringid;
    }

    public Equip(int id, byte position, int ringid) {
        super(id, position, (short) 1);
        this.ringid = ringid;
        this.itemExp = 0;
        this.itemLevel = 1;
    }

    @Override
    public IItem copy() {
        Equip ret = new Equip(getItemId(), getPosition(), getUpgradeSlots(), ringid);
        ret.str = str;
        ret.dex = dex;
        ret._int = _int;
        ret.luk = luk;
        ret.hp = hp;
        ret.mp = mp;
        ret.matk = matk;
        ret.mdef = mdef;
        ret.watk = watk;
        ret.wdef = wdef;
        ret.acc = acc;
        ret.avoid = avoid;
        ret.hands = hands;
        ret.speed = speed;
        ret.jump = jump;
        ret.flag = flag;
        ret.vicious = vicious;
        ret.upgradeSlots = upgradeSlots;
        ret.level = level;
        ret.itemExp = itemExp;
        ret.itemLevel = itemLevel;
        ret.log = new LinkedList<String>(log);
        ret.setOwner(getOwner());
        ret.setQuantity(getQuantity());
	ret.setExpiration(getExpiration());
	ret.setGiftFrom(getGiftFrom());
        return ret;
    }

    @Override
    public byte getFlag() {
        return flag;
    }

    @Override
    public byte getType() {
        return IItem.EQUIP;
    }

    public byte getUpgradeSlots() {
        return upgradeSlots;
    }

    public int getRingId() {
        return ringid;
    }

    public short getStr() {
        return str;
    }

    public short getDex() {
        return dex;
    }

    public short getInt() {
        return _int;
    }

    public short getLuk() {
        return luk;
    }

    public short getHp() {
        return hp;
    }

    public short getMp() {
        return mp;
    }

    public short getWatk() {
        return watk;
    }

    public short getMatk() {
        return matk;
    }

    public short getWdef() {
        return wdef;
    }

    public short getMdef() {
        return mdef;
    }

    public short getAcc() {
        return acc;
    }

    public short getAvoid() {
        return avoid;
    }

    public short getHands() {
        return hands;
    }

    public short getSpeed() {
        return speed;
    }

    public short getJump() {
        return jump;
    }

    public short getVicious() {
        return vicious;
    }

    @Override
    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public void setStr(short str) {
        this.str = str;
    }

    public void setDex(short dex) {
        this.dex = dex;
    }

    public void setInt(short _int) {
        this._int = _int;
    }

    public void setLuk(short luk) {
        this.luk = luk;
    }

    public void setHp(short hp) {
        this.hp = hp;
    }

    public void setMp(short mp) {
        this.mp = mp;
    }

    public void setWatk(short watk) {
        this.watk = watk;
    }

    public void setMatk(short matk) {
        this.matk = matk;
    }

    public void setWdef(short wdef) {
        this.wdef = wdef;
    }

    public void setMdef(short mdef) {
        this.mdef = mdef;
    }

    public void setAcc(short acc) {
        this.acc = acc;
    }

    public void setAvoid(short avoid) {
        this.avoid = avoid;
    }

    public void setHands(short hands) {
        this.hands = hands;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public void setJump(short jump) {
        this.jump = jump;
    }

    public void setVicious(short vicious) {
        this.vicious = vicious;
    }

    public void setUpgradeSlots(byte upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }
    public void expGainLevelUpCheck(MapleClient c, final MapleMonster monster) {
    this.itemExp++;
    if (this.getItemExp() >= ServerConstants.max_weapon_exp && this.getDex() < 32000 && this.getInt() < 32000 && this.getLuk() < 32000 && this.getStr() < 32000) {
    if  (monster.isBoss() == false) {
    monster.killBy(c.getPlayer());
    }
    this.newGainLevel(c);   
    
   // Equip nEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
    }
    }
    public void addStats(MapleClient c, String typeofstat, int amount) {
    Equip nEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((byte) 1);
    if (typeofstat.equals("str")) {
    nEquip.str += amount;
    }
    if (typeofstat.equals("dex")) {
    nEquip.dex += amount;
    }
    if (typeofstat.equals("int")) {
    nEquip._int += amount;
    }
    if (typeofstat.equals("luk")) {
    nEquip.luk += amount;
    }
    if (typeofstat.equals("speed")) {
    nEquip.speed += amount;
    }
    if (typeofstat.equals("accuracy")) {
    nEquip.acc += amount;
    }
    if (typeofstat.equals("watk")) {
    nEquip.watk += amount;
    }
    if (typeofstat.equals("matk")) {
    nEquip.matk += amount;
    }

    }
    public void donator5000(MapleCharacter player, byte slot) {
     MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
    Equip eu = (Equip) equip.getItem(slot);
    Equip nEquip = (Equip) player.getInventory(MapleInventoryType.EQUIP).getItem((byte) slot);
    nEquip.setStr((short) 5000); // STR
    nEquip.setDex((short) 5000); // DEX
    nEquip.setInt((short) 5000); // INT
    nEquip.setLuk((short) 5000); //LUK
    nEquip.setHp((short) 5000); // HP
    nEquip.setMp((short) 5000); // MP
    nEquip.setWatk((short) 5000); //WATK
    nEquip.setMatk((short) 5000); //MATK
    nEquip.setWdef((short) 5000); //WDEF
    nEquip.setMdef((short) 5000); //MDEF
    nEquip.setAcc((short) 5000); // ACC
    nEquip.setAvoid((short) 5000); // AVOID
    nEquip.setSpeed((short) 40); // SPEED ( 140% is max speed )
    nEquip.setJump((short) 30); //JUMP ( 130% is max jump )
    MapleInventoryManipulator.removeFromSlot(player.getClient(), MapleInventoryType.EQUIP, slot, (short) 1, true);
    MapleInventoryManipulator.addFromDrop(player.getClient(), nEquip, false);
    }
    public void newGainLevel(MapleClient c) {
      this.level++;
      this.setItemExp(0);
      if (c.getPlayer().getJob().isA(MapleJob.MAGICIAN) || c.getPlayer().getJob().isA(MapleJob.BLAZEWIZARD1)) {
      int addedmatk=(int)(4*Math.random());
      int added_int=(int)(3*Math.random());
      int lukcheck=(int)(2*Math.random());
      this.matk += addedmatk;
      this._int += added_int;
      this.luk += lukcheck;
      }
      if (c.getPlayer().getJob().isA(MapleJob.WARRIOR) || c.getPlayer().getJob().isA(MapleJob.DAWNWARRIOR1) || c.getPlayer().getJob().isA(MapleJob.ARAN1)) {
      int addedwatk=(int)(4*Math.random());
      int added_str=(int)(3*Math.random());
      int dexcheck=(int)(2*Math.random());
      this.watk += addedwatk;
      this.str += added_str;
      this.dex += dexcheck;
      }
      if (c.getPlayer().getJob().isA(MapleJob.THIEF) || c.getPlayer().getJob().isA(MapleJob.NIGHTWALKER1)) {
      int addedwatk=(int)(4*Math.random());
      int added_luk=(int)(3*Math.random());
      int dexcheck=(int)(2*Math.random());
      this.watk += addedwatk;
      this.luk += added_luk;
      this.dex += dexcheck;
      }
      if (c.getPlayer().getJob().isA(MapleJob.BOWMAN) || c.getPlayer().getJob().isA(MapleJob.WINDARCHER1)) {
      int addedwatk=(int)(4*Math.random());
      int added_dex=(int)(2*Math.random());
      int strcheck=(int)(2*Math.random());
      this.watk += addedwatk;
      this.dex += added_dex;
      this.str += strcheck;
      }
      if (c.getPlayer().getJob().isA(MapleJob.PIRATE) || c.getPlayer().getJob().isA(MapleJob.THUNDERBREAKER1)) {
      int addedwatk=(int)(4*Math.random());
      int added_str=(int)(3*Math.random());
      int dexcheck=(int)(2*Math.random());
      this.watk += addedwatk;
      this.str += added_str;
      this.dex += dexcheck;
      }
      int addedavoid=(int)(4*Math.random());
      int addedacc=(int)(4*Math.random());
      int addedspeed=(int)(4*Math.random());
      int addedjump=(int)(4*Math.random());

      if (addedavoid == 1) {
      int addedavoid1=(int)(4*Math.random());
      this.avoid += addedavoid1;
      }
      if (addedacc == 1) {
      int addedacc1=(int)(4*Math.random());
      this.acc += addedacc1;
      }
      if (addedspeed == 1) {
      int addedspeed1=(int)(4*Math.random());
      this.speed += addedspeed1;
      }
      if (addedjump == 1) {
      int addedjump1=(int)(4*Math.random());
      this.jump += addedjump1;
      }
      c.getPlayer().dropOverheadMessage("#eYour weapon has just leveled up to #r" + this.getLevel() + "#k! Congrats!#k Enequip and Re-equip your item to see the stat change.");
      c.getSession().write(MaplePacketCreator.showItemLevelup());
      c.announce(MaplePacketCreator.updateInventorySlot(MapleInventoryType.EQUIPPED, this));
    }
    public void newGainLevel(MapleClient c, int amt) {
       for (int i = 0; i < amt;) {
        i++;
        this.level++;
      this.setItemExp(0);
      if (c.getPlayer().getJob().isA(MapleJob.MAGICIAN) || c.getPlayer().getJob().isA(MapleJob.BLAZEWIZARD1)) {
      int addedmatk=(int)(4*Math.random());
      int added_int=(int)(3*Math.random());
      int lukcheck=(int)(2*Math.random());
      this.matk += addedmatk;
      this._int += added_int;
      this.luk += lukcheck;
      }
      if (c.getPlayer().getJob().isA(MapleJob.WARRIOR) || c.getPlayer().getJob().isA(MapleJob.DAWNWARRIOR1) || c.getPlayer().getJob().isA(MapleJob.ARAN1)) {
      int addedwatk=(int)(4*Math.random());
      int added_str=(int)(3*Math.random());
      int dexcheck=(int)(2*Math.random());
      this.watk += addedwatk;
      this.str += added_str;
      this.dex += dexcheck;
      }
      if (c.getPlayer().getJob().isA(MapleJob.THIEF) || c.getPlayer().getJob().isA(MapleJob.NIGHTWALKER1)) {
      int addedwatk=(int)(4*Math.random());
      int added_luk=(int)(3*Math.random());
      int dexcheck=(int)(2*Math.random());
      this.watk += addedwatk;
      this.luk += added_luk;
      this.dex += dexcheck;
      }
      if (c.getPlayer().getJob().isA(MapleJob.BOWMAN) || c.getPlayer().getJob().isA(MapleJob.WINDARCHER1)) {
      int addedwatk=(int)(4*Math.random());
      int added_dex=(int)(2*Math.random());
      int strcheck=(int)(2*Math.random());
      this.watk += addedwatk;
      this.dex += added_dex;
      this.str += strcheck;
      }
      if (c.getPlayer().getJob().isA(MapleJob.PIRATE) || c.getPlayer().getJob().isA(MapleJob.THUNDERBREAKER1)) {
      int addedwatk=(int)(4*Math.random());
      int added_str=(int)(3*Math.random());
      int dexcheck=(int)(2*Math.random());
      this.watk += addedwatk;
      this.str += added_str;
      this.dex += dexcheck;
      }
      int addedavoid=(int)(8*Math.random());
      int addedacc=(int)(8*Math.random());
      int addedspeed=(int)(8*Math.random());
      int addedjump=(int)(8*Math.random());
      if (addedavoid == 1) {
      int addedavoid1=(int)(4*Math.random());
      this.avoid += addedavoid1;
      }
      if (addedacc == 1) {
      int addedacc1=(int)(4*Math.random());
      this.acc += addedacc1;
      }
      if (addedspeed == 1) {
      int addedspeed1=(int)(4*Math.random());
      this.speed += addedspeed1;
      }
      if (addedjump == 1) {
      int addedjump1=(int)(4*Math.random());
      this.jump += addedjump1;
      }
      c.getPlayer().dropOverheadMessage("#eYour weapon has just leveled up to #r" + this.getLevel() + "#k! Congrats!#k Enequip and Re-equip your item to see the stat change.");
      c.getSession().write(MaplePacketCreator.showItemLevelup());
      }
}
    public void gainLevel(MapleClient c, boolean timeless) { //GEY METHOD IS GEY
        if (level < 6) {
            if (c.getPlayer().getJob().isA(MapleJob.MAGICIAN)) {
                this.matk += Randomizer.nextInt(5);
                this._int += Randomizer.nextInt(1) + 1;
                this.luk += Randomizer.nextInt(1);
            } else {
                this.watk += Randomizer.nextInt(3);
            }
        }
        this.level++;
    }

    public int getItemExp() {
        return itemExp;
    }

    public void gainItemExp(MapleClient c, int gain, boolean timeless) {
        itemExp += gain;
        int expNeeded = 0;
        if (timeless) {
            expNeeded = ExpTable.getTimelessItemExpNeededForLevel(itemLevel + 1);
        } else {
            expNeeded = ExpTable.getReverseItemExpNeededForLevel(itemLevel + 1);
        }
        if (itemExp >= expNeeded) {
            gainLevel(c, timeless);
            c.getSession().write(MaplePacketCreator.showItemLevelup());
        }
    }
    public void realExpGain(MapleClient c, int gain) {

    }

    public void setItemExp(int itemExp) {
        this.itemExp = itemExp;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    @Override
    public void setQuantity(short quantity) {
        if (quantity < 0 || quantity > 1) {
            throw new RuntimeException("Setting the quantity to " + quantity + " on an equip (itemid: " + getItemId() + ")");
        }
        super.setQuantity(quantity);
    }

    public void setUpgradeSlots(int i) {
        this.upgradeSlots = (byte) i;
    }

    public void setVicious(int i) {
        this.vicious = (short) i;
    }

    public boolean isWearing() {
        return wear;
    }

    public void wear(boolean yes) {
        wear = yes;
    }
}