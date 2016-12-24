/*
 	OrpheusMS: MapleStory Private Server based on OdinMS
    Copyright (C) 2012 Aaron Weiss <aaron@deviant-core.net>
    				Patrick Huy <patrick.huy@frz.cc>
					Matthias Butz <matze@odinms.de>
					Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import client.Equip;
import client.IEquip;
import client.IItem;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventory;
import client.MapleInventoryType;
import client.MapleJob;
import client.MapleWeaponType;
import client.SkillFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import constants.ItemConstants;
import java.util.Collection;
import java.util.LinkedList;
import tools.Randomizer;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.DatabaseConnection;
import tools.Pair;

/**
 * 
 * @author Matze
 * 
 */
public class MapleItemInformationProvider {
	private static MapleItemInformationProvider instance = null;
	protected MapleDataProvider itemData;
	protected MapleDataProvider equipData;
	protected MapleDataProvider stringData;
	protected MapleData cashStringData;
	protected MapleData consumeStringData;
	protected MapleData eqpStringData;
	protected MapleData etcStringData;
	protected MapleData insStringData;
	protected MapleData petStringData;
	protected Map<Integer, MapleInventoryType> inventoryTypeCache = new HashMap<Integer, MapleInventoryType>();
	protected Map<Integer, Short> slotMaxCache = new HashMap<Integer, Short>();
	protected Map<Integer, MapleStatEffect> itemEffects = new HashMap<Integer, MapleStatEffect>();
	protected Map<Integer, Map<String, Integer>> equipStatsCache = new HashMap<Integer, Map<String, Integer>>();
	protected Map<Integer, Equip> equipCache = new HashMap<Integer, Equip>();
	protected Map<Integer, Double> priceCache = new HashMap<Integer, Double>();
	protected Map<Integer, Integer> wholePriceCache = new HashMap<Integer, Integer>();
	protected Map<Integer, Integer> projectileWatkCache = new HashMap<Integer, Integer>();
	protected Map<Integer, String> nameCache = new HashMap<Integer, String>();
	protected Map<Integer, String> descCache = new HashMap<Integer, String>();
	protected Map<Integer, String> msgCache = new HashMap<Integer, String>();
	protected Map<Integer, Boolean> dropRestrictionCache = new HashMap<Integer, Boolean>();
	protected Map<Integer, Boolean> pickupRestrictionCache = new HashMap<Integer, Boolean>();
	protected Map<Integer, Integer> getMesoCache = new HashMap<Integer, Integer>();
	protected Map<Integer, Integer> monsterBookID = new HashMap<Integer, Integer>();
	protected Map<Integer, Boolean> onEquipUntradableCache = new HashMap<Integer, Boolean>();
	protected Map<Integer, scriptedItem> scriptedItemCache = new HashMap<Integer, scriptedItem>();
	protected Map<Integer, Boolean> karmaCache = new HashMap<Integer, Boolean>();
	protected Map<Integer, Integer> triggerItemCache = new HashMap<Integer, Integer>();
	protected Map<Integer, Integer> expCache = new HashMap<Integer, Integer>();
	protected Map<Integer, Integer> levelCache = new HashMap<Integer, Integer>();
	protected Map<Integer, Pair<Integer, List<RewardItem>>> rewardCache = new HashMap<Integer, Pair<Integer, List<RewardItem>>>();
	protected List<ItemNameEntry> itemNameCache = new ArrayList<ItemNameEntry>();
	protected Map<Integer, Boolean> consumeOnPickupCache = new HashMap<Integer, Boolean>();
	protected Map<Integer, Boolean> isQuestItemCache = new HashMap<Integer, Boolean>();

	private MapleItemInformationProvider() {
		loadCardIdData();
		itemData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Item.wz"));
		equipData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Character.wz"));
		stringData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz"));
		cashStringData = stringData.getData("Cash.img");
		consumeStringData = stringData.getData("Consume.img");
		eqpStringData = stringData.getData("Eqp.img");
		etcStringData = stringData.getData("Etc.img");
		insStringData = stringData.getData("Ins.img");
		petStringData = stringData.getData("Pet.img");
	}

	public static MapleItemInformationProvider getInstance() {
		if (instance == null) {
			instance = new MapleItemInformationProvider();
		}
		return instance;
	}

	public MapleInventoryType getInventoryType(int itemId) {
		if (inventoryTypeCache.containsKey(itemId)) {
			return inventoryTypeCache.get(itemId);
		}
		MapleInventoryType ret;
		String idStr = "0" + String.valueOf(itemId);
		MapleDataDirectoryEntry root = itemData.getRoot();
		for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
			for (MapleDataFileEntry iFile : topDir.getFiles()) {
				if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
					ret = MapleInventoryType.getByWZName(topDir.getName());
					inventoryTypeCache.put(itemId, ret);
					return ret;
				} else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
					ret = MapleInventoryType.getByWZName(topDir.getName());
					inventoryTypeCache.put(itemId, ret);
					return ret;
				}
			}
		}
		root = equipData.getRoot();
		for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
			for (MapleDataFileEntry iFile : topDir.getFiles()) {
				if (iFile.getName().equals(idStr + ".img")) {
					ret = MapleInventoryType.EQUIP;
					inventoryTypeCache.put(itemId, ret);
					return ret;
				}
			}
		}
		ret = MapleInventoryType.UNDEFINED;
		inventoryTypeCache.put(itemId, ret);
		return ret;
	}

	public List<ItemNameEntry> getAllItems() {
		// BUG: If you never add anything to this, it'll always be empty, duh?
		if (!itemNameCache.isEmpty()) {
			return itemNameCache; 
		}
		
		List<ItemNameEntry> entries = new ArrayList<ItemNameEntry>();
		MapleData itemsData;
		
		itemsData = stringData.getData("Cash.img");
		for (MapleData itemFolder : itemsData.getChildren()) {
			entries.add(getItemNameEntry(itemFolder));
		}
		
		itemsData = stringData.getData("Consume.img");
		for (MapleData itemFolder : itemsData.getChildren()) {
			entries.add(getItemNameEntry(itemFolder));
		}
		
		itemsData = stringData.getData("Eqp.img").getChildByPath("Eqp");
		for (MapleData eqpType : itemsData.getChildren()) {
			for (MapleData itemFolder : eqpType.getChildren()) {
				entries.add(getItemNameEntry(itemFolder));
			}
		}
		
		itemsData = stringData.getData("Etc.img").getChildByPath("Etc");
		for (MapleData itemFolder : itemsData.getChildren()) {
			entries.add(getItemNameEntry(itemFolder));
		}
		
		itemsData = stringData.getData("Ins.img");
		for (MapleData itemFolder : itemsData.getChildren()) {
			entries.add(getItemNameEntry(itemFolder));
		}
		
		itemsData = stringData.getData("Pet.img");
		for (MapleData itemFolder : itemsData.getChildren()) {
			entries.add(getItemNameEntry(itemFolder));
		}
		
		return entries;
	}

	private static ItemNameEntry getItemNameEntry(MapleData itemFolder) {
		return new ItemNameEntry(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME"));
	}

	private MapleData getStringData(int itemId) {
		String cat = "null";
		MapleData theData;
		if (itemId >= 5010000) {
            theData = cashStringData;
        } else if (itemId >= 2000000 && itemId < 3000000) {
            theData = consumeStringData;
        } else if ((itemId >= 1132000 && itemId < 1191001) || (itemId >= 1010000 && itemId < 1040000) || (itemId >= 1122000 && itemId < 1123000)) {
            theData = eqpStringData;
            cat = "Eqp/Accessory";
        } else if (itemId >= 1662000 && itemId < 1680000) {
            theData = eqpStringData;
            cat = "Eqp/Android";
        } else if (itemId >= 1000000 && itemId < 1010000) {
            theData = eqpStringData;
            cat = "Eqp/Cap";
        } else if (itemId >= 1102000 && itemId < 1103502) {
            theData = eqpStringData;
            cat = "Eqp/Cape";
        } else if (itemId >= 1040000 && itemId < 1103000) {
            theData = eqpStringData;
            cat = "Eqp/Coat";
        } else if (itemId >= 20000 && itemId < 29000) {
            theData = eqpStringData;
            cat = "Eqp/Face";
        } else if (itemId >= 1080000 && itemId < 1090000) {
            theData = eqpStringData;
            cat = "Eqp/Glove";
        } else if (itemId >= 30000 && itemId < 40000) {
            theData = eqpStringData;
            cat = "Eqp/Hair";
        } else if (itemId >= 1050000 && itemId < 1060000) {
            theData = eqpStringData;
            cat = "Eqp/Longcoat";
        } else if (itemId >= 1060000 && itemId < 1083000) {
            theData = eqpStringData;
            cat = "Eqp/Pants";
        } else if (itemId >= 1802000 && itemId < 1832000) {
            theData = eqpStringData;
            cat = "Eqp/PetEquip";
        } else if (itemId >= 1920000 && itemId < 2000000) {
            theData = eqpStringData;
            cat = "Eqp/Dragon";
        } else if (itemId >= 1112000 && itemId < 1120000) {
            theData = eqpStringData;
            cat = "Eqp/Ring";
        } else if (itemId >= 1090000 && itemId < 1100000) {
            theData = eqpStringData;
            cat = "Eqp/Shield";
        } else if (itemId >= 1070000 && itemId < 1080000) {
            theData = eqpStringData;
            cat = "Eqp/Shoes";
        } else if (itemId >= 1900000 && itemId < 1920000) {
            theData = eqpStringData;
            cat = "Eqp/Taming";
        } else if (itemId >= 1110000 && itemId < 1800000) {
            theData = eqpStringData;
            cat = "Eqp/Weapon";
        } else if (itemId >= 4000000 && itemId < 5000000) {
            theData = etcStringData;
            cat = "Etc";
        } else if (itemId >= 3000000 && itemId < 4000000) {
            theData = insStringData;
        } else if (itemId >= 5000000 && itemId < 5010000) {
            theData = petStringData;
		} else {
			return null;
		}
		if (cat.equalsIgnoreCase("null")) {
			return theData.getChildByPath(String.valueOf(itemId));
		} else {
			return theData.getChildByPath(cat + "/" + itemId);
		}
	}

	public boolean noCancelMouse(int itemId) {
		MapleData item = getItemData(itemId);
		if (item == null)
			return false;
		return MapleDataTool.getIntConvert("info/noCancelMouse", item, 0) == 1;
	}

	private MapleData getItemData(int itemId) {
		MapleData ret = null;
		String idStr = "0" + String.valueOf(itemId);
		MapleDataDirectoryEntry root = itemData.getRoot();
		for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
			for (MapleDataFileEntry iFile : topDir.getFiles()) {
				if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
					ret = itemData.getData(topDir.getName() + "/" + iFile.getName());
					if (ret == null) {
						return null;
					}
					ret = ret.getChildByPath(idStr);
					return ret;
				} else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
					return itemData.getData(topDir.getName() + "/" + iFile.getName());
				}
			}
		}
		root = equipData.getRoot();
		for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
			for (MapleDataFileEntry iFile : topDir.getFiles()) {
				if (iFile.getName().equals(idStr + ".img")) {
					return equipData.getData(topDir.getName() + "/" + iFile.getName());
				}
			}
		}
		return ret;
	}

	public short getSlotMax(MapleClient c, int itemId) {
		if (slotMaxCache.containsKey(itemId)) {
			return slotMaxCache.get(itemId);
		}
		short ret = 0;
		MapleData item = getItemData(itemId);
		if (item != null) {
			MapleData smEntry = item.getChildByPath("info/slotMax");
			if (smEntry == null) {
				if (getInventoryType(itemId).getType() == MapleInventoryType.EQUIP.getType()) {
					ret = 1;
				} else {
					ret = 100;
				}
			} else {
				if (ItemConstants.isRechargable(itemId) || (MapleDataTool.getInt(smEntry) == 0)) {
					ret = 1;
				}
				ret = (short) MapleDataTool.getInt(smEntry);
				if (ItemConstants.isThrowingStar(itemId)) {
					ret += c.getPlayer().getSkillLevel(SkillFactory.getSkill(4100000)) * 10;
				} else {
					ret += c.getPlayer().getSkillLevel(SkillFactory.getSkill(5200000)) * 10;
				}
			}
		}
		if (!ItemConstants.isRechargable(itemId)) {
			slotMaxCache.put(itemId, ret);
		}
		return ret;
	}

	public int getMeso(int itemId) {
		if (getMesoCache.containsKey(itemId)) {
			return getMesoCache.get(itemId);
		}
		MapleData item = getItemData(itemId);
		if (item == null) {
			return -1;
		}
		int pEntry = 0;
		MapleData pData = item.getChildByPath("info/meso");
		if (pData == null) {
			return -1;
		}
		pEntry = MapleDataTool.getInt(pData);
		getMesoCache.put(itemId, pEntry);
		return pEntry;
	}

	public int getWholePrice(int itemId) {
		if (wholePriceCache.containsKey(itemId)) {
			return wholePriceCache.get(itemId);
		}
		MapleData item = getItemData(itemId);
		if (item == null) {
			return -1;
		}
		int pEntry = 0;
		MapleData pData = item.getChildByPath("info/price");
		if (pData == null) {
			return -1;
		}
		pEntry = MapleDataTool.getInt(pData);
		wholePriceCache.put(itemId, pEntry);
		return pEntry;
	}

	public double getPrice(int itemId) {
		if (priceCache.containsKey(itemId)) {
			return priceCache.get(itemId);
		}
		MapleData item = getItemData(itemId);
		if (item == null) {
			return -1;
		}
		double pEntry = 0.0;
		MapleData pData = item.getChildByPath("info/unitPrice");
		if (pData != null) {
			try {
				pEntry = MapleDataTool.getDouble(pData);
			} catch (Exception e) {
				pEntry = (double) MapleDataTool.getInt(pData);
			}
		} else {
			pData = item.getChildByPath("info/price");
			if (pData == null) {
				return -1;
			}
			pEntry = (double) MapleDataTool.getInt(pData);
		}
		priceCache.put(itemId, pEntry);
		return pEntry;
	}

	protected Map<String, Integer> getEquipStats(int itemId) {
		if (equipStatsCache.containsKey(itemId)) {
			return equipStatsCache.get(itemId);
		}
		Map<String, Integer> ret = new LinkedHashMap<String, Integer>();
		MapleData item = getItemData(itemId);
		if (item == null) {
			return null;
		}
		MapleData info = item.getChildByPath("info");
		if (info == null) {
			return null;
		}
		for (MapleData theData : info.getChildren()) {
			if (theData.getName().startsWith("inc"))
				ret.put(theData.getName().substring(3), MapleDataTool.getIntConvert(theData));
			/*
			 * else if (theData.getName().startsWith("req"))
			 * ret.put(theData.getName(), MapleDataTool.getInt(theData.getName(),
			 * info, 0));
			 */
		}
		ret.put("reqJob", MapleDataTool.getInt("reqJob", info, 0));
		ret.put("reqLevel", MapleDataTool.getInt("reqLevel", info, 0));
		ret.put("reqDEX", MapleDataTool.getInt("reqDEX", info, 0));
		ret.put("reqSTR", MapleDataTool.getInt("reqSTR", info, 0));
		ret.put("reqINT", MapleDataTool.getInt("reqINT", info, 0));
		ret.put("reqLUK", MapleDataTool.getInt("reqLUK", info, 0));
		ret.put("reqPOP", MapleDataTool.getInt("reqPOP", info, 0));
		ret.put("cash", MapleDataTool.getInt("cash", info, 0));
		ret.put("tuc", MapleDataTool.getInt("tuc", info, 0));
		ret.put("cursed", MapleDataTool.getInt("cursed", info, 0));
		ret.put("success", MapleDataTool.getInt("success", info, 0));
		ret.put("fs", MapleDataTool.getInt("fs", info, 0));
		equipStatsCache.put(itemId, ret);
		return ret;
	}

	public List<Integer> getScrollReqs(int itemId) {
		List<Integer> ret = new ArrayList<Integer>();
		MapleData theData = getItemData(itemId);
		theData = theData.getChildByPath("req");
		if (theData == null) {
			return ret;
		}
		for (MapleData req : theData.getChildren()) {
			ret.add(MapleDataTool.getInt(req));
		}
		return ret;
	}

	public MapleWeaponType getWeaponType(int itemId) {
		int cat = (itemId / 10000) % 100;
		MapleWeaponType[] type = {MapleWeaponType.SWORD1H, MapleWeaponType.AXE1H, MapleWeaponType.BLUNT1H, MapleWeaponType.DAGGER, MapleWeaponType.NOT_A_WEAPON, MapleWeaponType.NOT_A_WEAPON, MapleWeaponType.NOT_A_WEAPON, MapleWeaponType.WAND, MapleWeaponType.STAFF, MapleWeaponType.NOT_A_WEAPON, MapleWeaponType.SWORD2H, MapleWeaponType.AXE2H, MapleWeaponType.BLUNT2H, MapleWeaponType.SPEAR, MapleWeaponType.POLE_ARM, MapleWeaponType.BOW, MapleWeaponType.CROSSBOW, MapleWeaponType.CLAW, MapleWeaponType.KNUCKLE, MapleWeaponType.GUN};
		if (cat < 30 || cat > 49) {
			return MapleWeaponType.NOT_A_WEAPON;
		}
		return type[cat - 30];
	}

	private boolean isCleanSlate(int scrollId) {
		return scrollId > 2048999 && scrollId < 2049004;
	}

	public IItem scrollEquipWithId(IItem equip, int scrollId, boolean usingWhiteScroll, boolean isGM) {
		if (equip instanceof Equip) {
			Equip nEquip = (Equip) equip;
			Map<String, Integer> stats = this.getEquipStats(scrollId);
			Map<String, Integer> eqstats = this.getEquipStats(equip.getItemId());
			if (((nEquip.getUpgradeSlots() > 0 || isCleanSlate(scrollId)) && Math.ceil(Math.random() * 100.0) <= stats.get("success")) || isGM) {
				short flag = nEquip.getFlag();
				switch (scrollId) {
					case 2040727:
						flag |= ItemConstants.SPIKES;
						nEquip.setFlag((byte) flag);
						return equip;
					case 2041058:
						flag |= ItemConstants.COLD;
						nEquip.setFlag((byte) flag);
						return equip;
					case 2049000:
					case 2049001:
					case 2049002:
					case 2049003:
						if (nEquip.getLevel() + nEquip.getUpgradeSlots() < eqstats.get("tuc")) {
							nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
						}
						break;
					case 2049100:
					case 2049101:
					case 2049102:
						int inc = 1;
						if (Randomizer.nextInt(2) == 0) {
							inc = -1;
						}
						if (nEquip.getStr() > 0) {
							nEquip.setStr((short) Math.max(0, (nEquip.getStr() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getDex() > 0) {
							nEquip.setDex((short) Math.max(0, (nEquip.getDex() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getInt() > 0) {
							nEquip.setInt((short) Math.max(0, (nEquip.getInt() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getLuk() > 0) {
							nEquip.setLuk((short) Math.max(0, (nEquip.getLuk() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getWatk() > 0) {
							nEquip.setWatk((short) Math.max(0, (nEquip.getWatk() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getWdef() > 0) {
							nEquip.setWdef((short) Math.max(0, (nEquip.getWdef() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getMatk() > 0) {
							nEquip.setMatk((short) Math.max(0, (nEquip.getMatk() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getMdef() > 0) {
							nEquip.setMdef((short) Math.max(0, (nEquip.getMdef() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getAcc() > 0) {
							nEquip.setAcc((short) Math.max(0, (nEquip.getAcc() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getAvoid() > 0) {
							nEquip.setAvoid((short) Math.max(0, (nEquip.getAvoid() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getSpeed() > 0) {
							nEquip.setSpeed((short) Math.max(0, (nEquip.getSpeed() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getJump() > 0) {
							nEquip.setJump((short) Math.max(0, (nEquip.getJump() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getHp() > 0) {
							nEquip.setHp((short) Math.max(0, (nEquip.getHp() + Randomizer.nextInt(6) * inc)));
						}
						if (nEquip.getMp() > 0) {
							nEquip.setMp((short) Math.max(0, (nEquip.getMp() + Randomizer.nextInt(6) * inc)));
						}
						break;
					default:
						for (Entry<String, Integer> stat : stats.entrySet()) {
							if (stat.getKey().equals("STR")) {
								nEquip.setStr((short) (nEquip.getStr() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("DEX")) {
								nEquip.setDex((short) (nEquip.getDex() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("INT")) {
								nEquip.setInt((short) (nEquip.getInt() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("LUK")) {
								nEquip.setLuk((short) (nEquip.getLuk() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("PAD")) {
								nEquip.setWatk((short) (nEquip.getWatk() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("PDD")) {
								nEquip.setWdef((short) (nEquip.getWdef() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("MAD")) {
								nEquip.setMatk((short) (nEquip.getMatk() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("MDD")) {
								nEquip.setMdef((short) (nEquip.getMdef() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("ACC")) {
								nEquip.setAcc((short) (nEquip.getAcc() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("EVA")) {
								nEquip.setAvoid((short) (nEquip.getAvoid() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("Speed")) {
								nEquip.setSpeed((short) (nEquip.getSpeed() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("Jump")) {
								nEquip.setJump((short) (nEquip.getJump() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("MHP")) {
								nEquip.setHp((short) (nEquip.getHp() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("MMP")) {
								nEquip.setMp((short) (nEquip.getMp() + stat.getValue().intValue()));
							} else if (stat.getKey().equals("afterImage")) {
							}
						}
						break;
				}
				if (!isCleanSlate(scrollId)) {
					if (!isGM) {
						nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
					}
					nEquip.setLevel((byte) (nEquip.getLevel() + 1));
				}
			} else {
				if (!usingWhiteScroll && !isCleanSlate(scrollId) && !isGM) {
					nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
				}
				if (Randomizer.nextInt(101) < stats.get("cursed")) {
					return null;
				}
			}
		}
		return equip;
	}

	public IItem getEquipById(int equipId) {
		return getEquipById(equipId, -1);
	}

	IItem getEquipById(int equipId, int ringId) {
		Equip nEquip;
		nEquip = new Equip(equipId, (byte) 0, ringId);
		nEquip.setQuantity((short) 1);
		Map<String, Integer> stats = this.getEquipStats(equipId);
		if (stats != null) {
			for (Entry<String, Integer> stat : stats.entrySet()) {
				if (stat.getKey().equals("STR")) {
					nEquip.setStr((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("DEX")) {
					nEquip.setDex((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("INT")) {
					nEquip.setInt((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("LUK")) {
					nEquip.setLuk((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("PAD")) {
					nEquip.setWatk((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("PDD")) {
					nEquip.setWdef((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("MAD")) {
					nEquip.setMatk((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("MDD")) {
					nEquip.setMdef((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("ACC")) {
					nEquip.setAcc((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("EVA")) {
					nEquip.setAvoid((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("Speed")) {
					nEquip.setSpeed((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("Jump")) {
					nEquip.setJump((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("MHP")) {
					nEquip.setHp((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("MMP")) {
					nEquip.setMp((short) stat.getValue().intValue());
				} else if (stat.getKey().equals("tuc")) {
					nEquip.setUpgradeSlots((byte) stat.getValue().intValue());
				} else if (isDropRestricted(equipId)) {
					byte flag = nEquip.getFlag();
					flag |= ItemConstants.UNTRADEABLE;
					nEquip.setFlag(flag);
				} else if (stats.get("fs") > 0) {
					byte flag = nEquip.getFlag();
					flag |= ItemConstants.SPIKES;
					nEquip.setFlag(flag);
					equipCache.put(equipId, nEquip);
				}
			}
		}
		return nEquip.copy();
	}

	private static short getRandStat(short defaultValue, int maxRange) {
		if (defaultValue == 0) {
			return 0;
		}
		int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1), maxRange);
		return (short) ((defaultValue - lMaxRange) + Math.floor(Randomizer.nextDouble() * (lMaxRange * 2 + 1)));
	}

	public Equip randomizeStats(Equip equip) {
		equip.setStr(getRandStat(equip.getStr(), 5));
		equip.setDex(getRandStat(equip.getDex(), 5));
		equip.setInt(getRandStat(equip.getInt(), 5));
		equip.setLuk(getRandStat(equip.getLuk(), 5));
		equip.setMatk(getRandStat(equip.getMatk(), 5));
		equip.setWatk(getRandStat(equip.getWatk(), 5));
		equip.setAcc(getRandStat(equip.getAcc(), 5));
		equip.setAvoid(getRandStat(equip.getAvoid(), 5));
		equip.setJump(getRandStat(equip.getJump(), 5));
		equip.setSpeed(getRandStat(equip.getSpeed(), 5));
		equip.setWdef(getRandStat(equip.getWdef(), 10));
		equip.setMdef(getRandStat(equip.getMdef(), 10));
		equip.setHp(getRandStat(equip.getHp(), 10));
		equip.setMp(getRandStat(equip.getMp(), 10));
		return equip;
	}

	public MapleStatEffect getItemEffect(int itemId) {
		MapleStatEffect ret = itemEffects.get(Integer.valueOf(itemId));
		if (ret == null) {
			MapleData item = getItemData(itemId);
			if (item == null) {
				return null;
			}
			MapleData spec = item.getChildByPath("spec");
			ret = MapleStatEffect.loadItemEffectFromData(spec, itemId);
			itemEffects.put(Integer.valueOf(itemId), ret);
		}
		return ret;
	}

	public int[][] getSummonMobs(int itemId) {
		MapleData theData = getItemData(itemId);
		int theInt = theData.getChildByPath("mob").getChildren().size();
		int[][] mobs2spawn = new int[theInt][2];
		for (int x = 0; x < theInt; x++) {
			mobs2spawn[x][0] = MapleDataTool.getIntConvert("mob/" + x + "/id", theData);
			mobs2spawn[x][1] = MapleDataTool.getIntConvert("mob/" + x + "/prob", theData);
		}
		return mobs2spawn;
	}

	public int getWatkForProjectile(int itemId) {
		Integer atk = projectileWatkCache.get(itemId);
		if (atk != null) {
			return atk.intValue();
		}
		MapleData theData = getItemData(itemId);
		atk = Integer.valueOf(MapleDataTool.getInt("info/incPAD", theData, 0));
		projectileWatkCache.put(itemId, atk);
		return atk.intValue();
	}

	public String getName(int itemId) {
		if (nameCache.containsKey(itemId)) {
			return nameCache.get(itemId);
		}
		MapleData strings = getStringData(itemId);
		if (strings == null) {
			return null;
		}
		String ret = MapleDataTool.getString("name", strings, null);
		nameCache.put(itemId, ret);
		return ret;
	}

	public String getMsg(int itemId) {
		if (msgCache.containsKey(itemId)) {
			return msgCache.get(itemId);
		}
		MapleData strings = getStringData(itemId);
		if (strings == null) {
			return null;
		}
		String ret = MapleDataTool.getString("msg", strings, null);
		msgCache.put(itemId, ret);
		return ret;
	}

	public boolean isDropRestricted(int itemId) {
		if (dropRestrictionCache.containsKey(itemId)) {
			return dropRestrictionCache.get(itemId);
		}
		MapleData theData = getItemData(itemId);
		boolean bRestricted = MapleDataTool.getIntConvert("info/tradeBlock", theData, 0) == 1;
		if (!bRestricted)
			bRestricted = MapleDataTool.getIntConvert("info/quest", theData, 0) == 1;
		dropRestrictionCache.put(itemId, bRestricted);
		return bRestricted;
	}

	public boolean isPickupRestricted(int itemId) {
		if (pickupRestrictionCache.containsKey(itemId)) {
			return pickupRestrictionCache.get(itemId);
		}
		MapleData theData = getItemData(itemId);
		boolean bRestricted = MapleDataTool.getIntConvert("info/only", theData, 0) == 1;
		pickupRestrictionCache.put(itemId, bRestricted);
		return bRestricted;
	}

	public Map<String, Integer> getSkillStats(int itemId, double playerJob) {
		Map<String, Integer> ret = new LinkedHashMap<String, Integer>();
		MapleData item = getItemData(itemId);
		if (item == null) {
			return null;
		}
		MapleData info = item.getChildByPath("info");
		if (info == null) {
			return null;
		}
		for (MapleData theData : info.getChildren()) {
			if (theData.getName().startsWith("inc")) {
				ret.put(theData.getName().substring(3), MapleDataTool.getIntConvert(theData));
			}
		}
		ret.put("masterLevel", MapleDataTool.getInt("masterLevel", info, 0));
		ret.put("reqSkillLevel", MapleDataTool.getInt("reqSkillLevel", info, 0));
		ret.put("success", MapleDataTool.getInt("success", info, 0));
		MapleData skill = info.getChildByPath("skill");
		int curskill = 1;
		for (int i = 0; i < skill.getChildren().size(); i++) {
			curskill = MapleDataTool.getInt(Integer.toString(i), skill, 0);
			if (curskill == 0) {
				break;
			}
			if (curskill / 10000 == playerJob) {
				ret.put("skillid", curskill);
				break;
			}
		}
		if (ret.get("skillid") == null) {
			ret.put("skillid", 0);
		}
		return ret;
	}

	public List<Integer> petsCanConsume(int itemId) {
		List<Integer> ret = new ArrayList<Integer>();
		MapleData theData = getItemData(itemId);
		int curPetId = 0;
		for (int i = 0; i < theData.getChildren().size(); i++) {
			curPetId = MapleDataTool.getInt("spec/" + Integer.toString(i), theData, 0);
			if (curPetId == 0) {
				break;
			}
			ret.add(Integer.valueOf(curPetId));
		}
		return ret;
	}

	public boolean isQuestItem(int itemId) {
		if (isQuestItemCache.containsKey(itemId)) {
			return isQuestItemCache.get(itemId);
		}
		MapleData theData = getItemData(itemId);
		boolean questItem = MapleDataTool.getIntConvert("info/quest", theData, 0) == 1;
		isQuestItemCache.put(itemId, questItem);
		return questItem;
	}

	public int getQuestIdFromItem(int itemId) {
		MapleData theData = getItemData(itemId);
		int questItem = MapleDataTool.getIntConvert("info/quest", theData, 0);
		return questItem;
	}

	private void loadCardIdData() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DatabaseConnection.getConnection().prepareStatement("SELECT cardid, mobid FROM monstercardtheData");
			rs = ps.executeQuery();
			while (rs.next()) {
				monsterBookID.put(rs.getInt(1), rs.getInt(2));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
			}
		}
	}

	public int getCardMobId(int id) {
		return monsterBookID.get(id);
	}

	public boolean isUntradeableOnEquip(int itemId) {
		if (onEquipUntradableCache.containsKey(itemId)) {
			return onEquipUntradableCache.get(itemId);
		}
		boolean untradableOnEquip = MapleDataTool.getIntConvert("info/equipTradeBlock", getItemData(itemId), 0) > 0;
		onEquipUntradableCache.put(itemId, untradableOnEquip);
		return untradableOnEquip;
	}

	public scriptedItem getScriptedItemInfo(int itemId) {
		if (scriptedItemCache.containsKey(itemId)) {
			return scriptedItemCache.get(itemId);
		}
		if ((itemId / 10000) != 243) {
			return null;
		}
		scriptedItem script = new scriptedItem(MapleDataTool.getInt("spec/npc", getItemData(itemId), 0), MapleDataTool.getString("spec/script", getItemData(itemId), ""), MapleDataTool.getInt("spec/runOnPickup", getItemData(itemId), 0) == 1);
		scriptedItemCache.put(itemId, script);
		return scriptedItemCache.get(itemId);
	}

	public boolean isKarmaAble(int itemId) {
		if (karmaCache.containsKey(itemId)) {
			return karmaCache.get(itemId);
		}
		boolean bRestricted = MapleDataTool.getIntConvert("info/tradeAvailable", getItemData(itemId), 0) > 0;
		karmaCache.put(itemId, bRestricted);
		return bRestricted;
	}

	public int getStateChangeItem(int itemId) {
		if (triggerItemCache.containsKey(itemId)) {
			return triggerItemCache.get(itemId);
		} else {
			int triggerItem = MapleDataTool.getIntConvert("info/stateChangeItem", getItemData(itemId), 0);
			triggerItemCache.put(itemId, triggerItem);
			return triggerItem;
		}
	}

	public int getExpById(int itemId) {
		if (expCache.containsKey(itemId)) {
			return expCache.get(itemId);
		} else {
			int exp = MapleDataTool.getIntConvert("spec/exp", getItemData(itemId), 0);
			expCache.put(itemId, exp);
			return exp;
		}
	}

	public int getMaxLevelById(int itemId) {
		if (levelCache.containsKey(itemId)) {
			return levelCache.get(itemId);
		} else {
			int level = MapleDataTool.getIntConvert("info/maxLevel", getItemData(itemId), 256);
			levelCache.put(itemId, level);
			return level;
		}
	}

	public Pair<Integer, List<RewardItem>> getItemReward(int itemId) {// Thanks
																		// Celino,
																		// used
																		// some
																		// stuffs
																		// :)
		if (rewardCache.containsKey(itemId)) {
			return rewardCache.get(itemId);
		}
		int totalprob = 0;
		List<RewardItem> rewards = new ArrayList<RewardItem>();
		for (MapleData child : getItemData(itemId).getChildByPath("reward").getChildren()) {
			RewardItem reward = new RewardItem();
			reward.itemid = MapleDataTool.getInt("item", child, 0);
			reward.prob = (byte) MapleDataTool.getInt("prob", child, 0);
			reward.quantity = (short) MapleDataTool.getInt("count", child, 0);
			reward.effect = MapleDataTool.getString("Effect", child, "");
			reward.worldmsg = MapleDataTool.getString("worldMsg", child, null);
			reward.period = MapleDataTool.getInt("period", child, -1);

			totalprob += reward.prob;

			rewards.add(reward);
		}
		Pair<Integer, List<RewardItem>> hmm = new Pair<Integer, List<RewardItem>>(totalprob, rewards);
		rewardCache.put(itemId, hmm);
		return hmm;
	}

	public boolean isConsumeOnPickup(int itemId) {
		if (consumeOnPickupCache.containsKey(itemId)) {
			return consumeOnPickupCache.get(itemId);
		}
		MapleData theData = getItemData(itemId);
		boolean consume = MapleDataTool.getIntConvert("spec/consumeOnPickup", theData, 0) == 1 || MapleDataTool.getIntConvert("specEx/consumeOnPickup", theData, 0) == 1;
		consumeOnPickupCache.put(itemId, consume);
		return consume;
	}

	public final boolean isTwoHanded(int itemId) {
		switch (getWeaponType(itemId)) {
			case AXE2H:
			case BLUNT2H:
			case BOW:
			case CLAW:
			case CROSSBOW:
			case POLE_ARM:
			case SPEAR:
			case SWORD2H:
			case GUN:
			case KNUCKLE:
				return true;
			default:
				return false;
		}
	}

	public boolean isCash(int itemId) {
		return itemId / 1000000 == 5 || getEquipStats(itemId).get("cash") == 1;
	}

	public Collection<IItem> canWearEquipment(MapleCharacter chr, Collection<IItem> items) {
		MapleInventory inv = chr.getInventory(MapleInventoryType.EQUIPPED);
		if (inv.checked())
			return items;
		Collection<IItem> itemz = new LinkedList<IItem>();
		if (chr.getJob() == MapleJob.SUPERGM || chr.getJob() == MapleJob.GM) {
			for (IItem item : items) {
				IEquip equip = (IEquip) item;
				equip.wear(true);
				itemz.add(item);
			}
			return itemz;
		}
		boolean highfivestamp = false;
		/*
		 * Removed because players shouldn't even get this, and gm's should just
		 * be gm job. try { for (Pair<IItem, MapleInventoryType> ii :
		 * ItemFactory.INVENTORY.loadItems(chr.getId(), false)) { if
		 * (ii.getRight() == MapleInventoryType.CASH) { if
		 * (ii.getLeft().getItemId() == 5590000) { highfivestamp = true; } } } }
		 * catch (SQLException ex) { }
		 */
		int tdex = chr.getDex(), tstr = chr.getStr(), tint = chr.getInt(), tluk = chr.getLuk(), fame = chr.getFame();
		if (chr.getJob() != MapleJob.SUPERGM || chr.getJob() != MapleJob.GM) {
			for (IItem item : inv.list()) {
				IEquip equip = (IEquip) item;
				tdex += equip.getDex();
				tstr += equip.getStr();
				tluk += equip.getLuk();
				tint += equip.getInt();
			}
		}
		for (IItem item : items) {
			IEquip equip = (IEquip) item;
			int reqLevel = getEquipStats(equip.getItemId()).get("reqLevel");
			if (highfivestamp) {
				reqLevel -= 5;
				if (reqLevel < 0)
					reqLevel = 0;
			}
			/*
			 * int reqJob = getEquipStats(equip.getItemId()).get("reqJob"); if
			 * (reqJob != 0) { Really hard check, and not really needed in this
			 * one Gm's should just be GM job, and players cannot change jobs. }
			 */
			if (reqLevel > chr.getLevel())
				continue;
			else if (getEquipStats(equip.getItemId()).get("reqDEX") > tdex)
				continue;
			else if (getEquipStats(equip.getItemId()).get("reqSTR") > tstr)
				continue;
			else if (getEquipStats(equip.getItemId()).get("reqLUK") > tluk)
				continue;
			else if (getEquipStats(equip.getItemId()).get("reqINT") > tint)
				continue;
			int reqPOP = getEquipStats(equip.getItemId()).get("reqPOP");
			if (reqPOP > 0) {
				if (getEquipStats(equip.getItemId()).get("reqPOP") > fame)
					continue;
			}
			equip.wear(true);
			itemz.add(equip);
		}
		inv.checked(true);
		return itemz;
	}

	public boolean canWearEquipment(MapleCharacter chr, Equip equip) {
		if (chr.getJob() == MapleJob.SUPERGM || chr.getJob() == MapleJob.GM) {
			equip.wear(true);
			return true;
		}
		boolean highfivestamp = false;
		/*
		 * Removed check above for message >< try { for (Pair<IItem,
		 * MapleInventoryType> ii : ItemFactory.INVENTORY.loadItems(chr.getId(),
		 * false)) { if (ii.getRight() == MapleInventoryType.CASH) { if
		 * (ii.getLeft().getItemId() == 5590000) { highfivestamp = true; } } } }
		 * catch (SQLException ex) { }
		 */
		int tdex = chr.getDex(), tstr = chr.getStr(), tint = chr.getInt(), tluk = chr.getLuk();
		for (IItem item : chr.getInventory(MapleInventoryType.EQUIPPED).list()) {
			IEquip eq = (IEquip) item;
			tdex += eq.getDex();
			tstr += eq.getStr();
			tluk += eq.getLuk();
			tint += eq.getInt();
		}
		int reqLevel = getEquipStats(equip.getItemId()).get("reqLevel");
		if (highfivestamp) {
			reqLevel -= 5;
		}
		int i = 0; // lol xD
		// Removed job check. Shouldn't really be needed.
		if (reqLevel > chr.getLevel())
			i++;
		else if (getEquipStats(equip.getItemId()).get("reqDEX") > tdex)
			i++;
		else if (getEquipStats(equip.getItemId()).get("reqSTR") > tstr)
			i++;
		else if (getEquipStats(equip.getItemId()).get("reqLUK") > tluk)
			i++;
		else if (getEquipStats(equip.getItemId()).get("reqINT") > tint)
			i++;
		int reqPOP = getEquipStats(equip.getItemId()).get("reqPOP");
		if (reqPOP > 0) {
			if (getEquipStats(equip.getItemId()).get("reqPOP") > chr.getFame())
				i++;
		}

		if (i > 0) {
			equip.wear(false);
			return false;
		}
		equip.wear(true);
		return true;
	}

	public List<EquipLevelUpStat> getItemLevelupStats(int itemId, int level, boolean timeless) {
		List<EquipLevelUpStat> list = new LinkedList<EquipLevelUpStat>();
		MapleData theData = getItemData(itemId);
		MapleData theData1 = theData.getChildByPath("info").getChildByPath("level");
		/*
		 * if ((timeless && level == 5) || (!timeless && level == 3)) {
		 * MapleData skilltheData =
		 * theData1.getChildByPath("case").getChildByPath("1")
		 * .getChildByPath(timeless ? "6" : "4"); if (skilltheData != null) {
		 * List<MapleData> skills =
		 * skilltheData.getChildByPath("Skill").getChildren(); for (int i = 0; i <
		 * skilltheData.getChildByPath("Skill").getChildren().size(); i++) {
		 * System.
		 * out.println(MapleDataTool.getInt(skills.get(i).getChildByPath("id"
		 * ))); if (Math.random() < 0.1) list.add(new Pair<String,
		 * Integer>("Skill" + 0,
		 * MapleDataTool.getInt(skills.get(i).getChildByPath("id")))); } } }
		 */
		if (theData1 != null) {
			MapleData theData2 = theData1.getChildByPath("info").getChildByPath(Integer.toString(level));
			if (theData2 != null) {
				for (MapleData da : theData2.getChildren()) {
					if (Math.random() < 0.9) {
						final String name = da.getName();
						final int lowerBound = MapleDataTool.getInt(da);
						if (name.startsWith("incDEXMin")) {
							list.add(new EquipLevelUpStat("incDEX", Randomizer.rand(lowerBound, MapleDataTool.getInt("incDEXMax", theData2))));
						} else if (name.startsWith("incSTRMin")) {
							list.add(new EquipLevelUpStat("incSTR", Randomizer.rand(lowerBound, MapleDataTool.getInt("incSTRMax", theData2))));
						} else if (name.startsWith("incINTMin")) {
							list.add(new EquipLevelUpStat("incINT", Randomizer.rand(lowerBound, MapleDataTool.getInt("incINTMax", theData2))));
						} else if (name.startsWith("incLUKMin")) {
							list.add(new EquipLevelUpStat("incLUK", Randomizer.rand(lowerBound, MapleDataTool.getInt("incLUKMax", theData2))));
						} else if (name.startsWith("incMHPMin")) {
							list.add(new EquipLevelUpStat("incMHP", Randomizer.rand(lowerBound, MapleDataTool.getInt("incMHPMax", theData2))));
						} else if (name.startsWith("incMMPMin")) {
							list.add(new EquipLevelUpStat("incMMP", Randomizer.rand(lowerBound, MapleDataTool.getInt("incMMPMax", theData2))));
						} else if (name.startsWith("incPADMin")) {
							list.add(new EquipLevelUpStat("incPAD", Randomizer.rand(lowerBound, MapleDataTool.getInt("incPADMax", theData2))));
						} else if (name.startsWith("incMADMin")) {
							list.add(new EquipLevelUpStat("incMAD", Randomizer.rand(lowerBound, MapleDataTool.getInt("incMADMax", theData2))));
						} else if (name.startsWith("incPDDMin")) {
							list.add(new EquipLevelUpStat("incPDD", Randomizer.rand(lowerBound, MapleDataTool.getInt("incPDDMax", theData2))));
						} else if (name.startsWith("incMDDMin")) {
							list.add(new EquipLevelUpStat("incMDD", Randomizer.rand(lowerBound, MapleDataTool.getInt("incMDDMax", theData2))));
						} else if (name.startsWith("incACCMin")) {
							list.add(new EquipLevelUpStat("incACC", Randomizer.rand(lowerBound, MapleDataTool.getInt("incACCMax", theData2))));
						} else if (name.startsWith("incEVAMin")) {
							list.add(new EquipLevelUpStat("incEVA", Randomizer.rand(lowerBound, MapleDataTool.getInt("incEVAMax", theData2))));
						} else if (name.startsWith("incSpeedMin")) {
							list.add(new EquipLevelUpStat("incSpeed", Randomizer.rand(lowerBound, MapleDataTool.getInt("incSpeedMax", theData2))));
						} else if (name.startsWith("incJumpMin")) {
							list.add(new EquipLevelUpStat("incJump", Randomizer.rand(lowerBound, MapleDataTool.getInt("incJumpMax", theData2))));
						}
					}
				}
			}
		}

		return list;
	}

	public class scriptedItem {
		private boolean runOnPickup;
		private int npc;
		private String script;

		public scriptedItem(int npc, String script, boolean rop) {
			this.npc = npc;
			this.script = script;
			this.runOnPickup = rop;
		}

		public int getNpc() {
			return npc;
		}

		public String getScript() {
			return script;
		}

		public boolean runOnPickup() {
			return runOnPickup;
		}
	}

	public static final class RewardItem {
		public int itemid, period;
		public short prob, quantity;
		public String effect, worldmsg;
	}
}