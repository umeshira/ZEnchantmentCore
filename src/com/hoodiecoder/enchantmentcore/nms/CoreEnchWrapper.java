package com.hoodiecoder.enchantmentcore.nms;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;


public class CoreEnchWrapper {
	private static int enchLimit = 37;
	private final CoreEnchParent coreEnch;
	private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	public CoreEnchWrapper(RarityEnum var0, EnchantmentSlotEnum var1, ItemSlotEnum[] var2, String name, String displayName, int maxLevel) {
		if (version.equals("v1_16_R3")) {
			enchLimit = 37;
			net.minecraft.server.v1_16_R3.EnchantmentSlotType slotType;
			net.minecraft.server.v1_16_R3.Enchantment.Rarity rarity;
			net.minecraft.server.v1_16_R3.EnumItemSlot[] enumSlot = null;
			switch (var1) {
			case ARMOR:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.ARMOR;
				break;
			case ARMOR_CHEST:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.ARMOR_CHEST;
				break;
			case ARMOR_FEET:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.ARMOR_FEET;
				break;
			case ARMOR_HEAD:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.ARMOR_HEAD;
				break;
			case ARMOR_LEGS:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.ARMOR_LEGS;
				break;
			case BOW:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.BOW;
				break;
			case BREAKABLE:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.BREAKABLE;
				break;
			case CROSSBOW:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.CROSSBOW;
				break;
			case DIGGER:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.DIGGER;
				break;
			case FISHING_ROD:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.FISHING_ROD;
				break;
			case TRIDENT:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.TRIDENT;
				break;
			case VANISHABLE:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.VANISHABLE;
				break;
			case WEAPON:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.WEAPON;
				break;
			case WEARABLE:
				slotType = net.minecraft.server.v1_16_R3.EnchantmentSlotType.WEARABLE;
				break;
			default:
				slotType = null;
				break;
			
			}
			switch (var0) {
			case COMMON:
				rarity = net.minecraft.server.v1_16_R3.Enchantment.Rarity.COMMON;
				break;
			case RARE:
				rarity = net.minecraft.server.v1_16_R3.Enchantment.Rarity.RARE;
				break;
			case UNCOMMON:
				rarity = net.minecraft.server.v1_16_R3.Enchantment.Rarity.UNCOMMON;
				break;
			case VERY_RARE:
				rarity = net.minecraft.server.v1_16_R3.Enchantment.Rarity.VERY_RARE;
				break;
			default:
				rarity = null;
				break;
			
			}
			List<net.minecraft.server.v1_16_R3.EnumItemSlot> eList = new LinkedList<>();
			for (ItemSlotEnum e : var2) {
				switch (e) {
				case CHEST:
					eList.add(net.minecraft.server.v1_16_R3.EnumItemSlot.CHEST);
					break;
				case FEET:
					eList.add(net.minecraft.server.v1_16_R3.EnumItemSlot.FEET);
					break;
				case HEAD:
					eList.add(net.minecraft.server.v1_16_R3.EnumItemSlot.HEAD);
					break;
				case LEGS:
					eList.add(net.minecraft.server.v1_16_R3.EnumItemSlot.LEGS);
					break;
				case MAINHAND:
					eList.add(net.minecraft.server.v1_16_R3.EnumItemSlot.MAINHAND);
					break;
				case OFFHAND:
					eList.add(net.minecraft.server.v1_16_R3.EnumItemSlot.OFFHAND);
					break;
				default:
					eList.add(null);
					break;
				
				}
				enumSlot = eList.toArray(new net.minecraft.server.v1_16_R3.EnumItemSlot[0]);
			}
			this.coreEnch = new CoreEnch_v1_16_R3(rarity, slotType, enumSlot, name, displayName, maxLevel);
		}
		else if (version.equals("v1_16_R2")) {
			enchLimit = 37;
			net.minecraft.server.v1_16_R2.EnchantmentSlotType slotType;
			net.minecraft.server.v1_16_R2.Enchantment.Rarity rarity;
			net.minecraft.server.v1_16_R2.EnumItemSlot[] enumSlot = null;
			switch (var1) {
			case ARMOR:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.ARMOR;
				break;
			case ARMOR_CHEST:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.ARMOR_CHEST;
				break;
			case ARMOR_FEET:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.ARMOR_FEET;
				break;
			case ARMOR_HEAD:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.ARMOR_HEAD;
				break;
			case ARMOR_LEGS:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.ARMOR_LEGS;
				break;
			case BOW:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.BOW;
				break;
			case BREAKABLE:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.BREAKABLE;
				break;
			case CROSSBOW:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.CROSSBOW;
				break;
			case DIGGER:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.DIGGER;
				break;
			case FISHING_ROD:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.FISHING_ROD;
				break;
			case TRIDENT:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.TRIDENT;
				break;
			case VANISHABLE:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.VANISHABLE;
				break;
			case WEAPON:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.WEAPON;
				break;
			case WEARABLE:
				slotType = net.minecraft.server.v1_16_R2.EnchantmentSlotType.WEARABLE;
				break;
			default:
				slotType = null;
				break;
			
			}
			switch (var0) {
			case COMMON:
				rarity = net.minecraft.server.v1_16_R2.Enchantment.Rarity.COMMON;
				break;
			case RARE:
				rarity = net.minecraft.server.v1_16_R2.Enchantment.Rarity.RARE;
				break;
			case UNCOMMON:
				rarity = net.minecraft.server.v1_16_R2.Enchantment.Rarity.UNCOMMON;
				break;
			case VERY_RARE:
				rarity = net.minecraft.server.v1_16_R2.Enchantment.Rarity.VERY_RARE;
				break;
			default:
				rarity = null;
				break;
			
			}
			List<net.minecraft.server.v1_16_R2.EnumItemSlot> eList = new LinkedList<>();
			for (ItemSlotEnum e : var2) {
				switch (e) {
				case CHEST:
					eList.add(net.minecraft.server.v1_16_R2.EnumItemSlot.CHEST);
					break;
				case FEET:
					eList.add(net.minecraft.server.v1_16_R2.EnumItemSlot.FEET);
					break;
				case HEAD:
					eList.add(net.minecraft.server.v1_16_R2.EnumItemSlot.HEAD);
					break;
				case LEGS:
					eList.add(net.minecraft.server.v1_16_R2.EnumItemSlot.LEGS);
					break;
				case MAINHAND:
					eList.add(net.minecraft.server.v1_16_R2.EnumItemSlot.MAINHAND);
					break;
				case OFFHAND:
					eList.add(net.minecraft.server.v1_16_R2.EnumItemSlot.OFFHAND);
					break;
				default:
					eList.add(null);
					break;
				
				}
				enumSlot = eList.toArray(new net.minecraft.server.v1_16_R2.EnumItemSlot[0]);
			}
			this.coreEnch = new CoreEnch_v1_16_R2(rarity, slotType, enumSlot, name, displayName, maxLevel);
		}
		else if (version.equals("v1_16_R1")) {
			enchLimit = 37;
			net.minecraft.server.v1_16_R1.EnchantmentSlotType slotType;
			net.minecraft.server.v1_16_R1.Enchantment.Rarity rarity;
			net.minecraft.server.v1_16_R1.EnumItemSlot[] enumSlot = null;
			switch (var1) {
			case ARMOR:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.ARMOR;
				break;
			case ARMOR_CHEST:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.ARMOR_CHEST;
				break;
			case ARMOR_FEET:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.ARMOR_FEET;
				break;
			case ARMOR_HEAD:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.ARMOR_HEAD;
				break;
			case ARMOR_LEGS:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.ARMOR_LEGS;
				break;
			case BOW:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.BOW;
				break;
			case BREAKABLE:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.BREAKABLE;
				break;
			case CROSSBOW:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.CROSSBOW;
				break;
			case DIGGER:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.DIGGER;
				break;
			case FISHING_ROD:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.FISHING_ROD;
				break;
			case TRIDENT:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.TRIDENT;
				break;
			case VANISHABLE:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.VANISHABLE;
				break;
			case WEAPON:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.WEAPON;
				break;
			case WEARABLE:
				slotType = net.minecraft.server.v1_16_R1.EnchantmentSlotType.WEARABLE;
				break;
			default:
				slotType = null;
				break;
			
			}
			switch (var0) {
			case COMMON:
				rarity = net.minecraft.server.v1_16_R1.Enchantment.Rarity.COMMON;
				break;
			case RARE:
				rarity = net.minecraft.server.v1_16_R1.Enchantment.Rarity.RARE;
				break;
			case UNCOMMON:
				rarity = net.minecraft.server.v1_16_R1.Enchantment.Rarity.UNCOMMON;
				break;
			case VERY_RARE:
				rarity = net.minecraft.server.v1_16_R1.Enchantment.Rarity.VERY_RARE;
				break;
			default:
				rarity = null;
				break;
			
			}
			List<net.minecraft.server.v1_16_R1.EnumItemSlot> eList = new LinkedList<>();
			for (ItemSlotEnum e : var2) {
				switch (e) {
				case CHEST:
					eList.add(net.minecraft.server.v1_16_R1.EnumItemSlot.CHEST);
					break;
				case FEET:
					eList.add(net.minecraft.server.v1_16_R1.EnumItemSlot.FEET);
					break;
				case HEAD:
					eList.add(net.minecraft.server.v1_16_R1.EnumItemSlot.HEAD);
					break;
				case LEGS:
					eList.add(net.minecraft.server.v1_16_R1.EnumItemSlot.LEGS);
					break;
				case MAINHAND:
					eList.add(net.minecraft.server.v1_16_R1.EnumItemSlot.MAINHAND);
					break;
				case OFFHAND:
					eList.add(net.minecraft.server.v1_16_R1.EnumItemSlot.OFFHAND);
					break;
				default:
					eList.add(null);
					break;
				
				}
				enumSlot = eList.toArray(new net.minecraft.server.v1_16_R1.EnumItemSlot[0]);
			}
			this.coreEnch = new CoreEnch_v1_16_R1(rarity, slotType, enumSlot, name, displayName, maxLevel);
		}
		else coreEnch = null;
	}
	public CoreEnchParent getCoreEnch() {
		return coreEnch;
	}
	public static int getEnchLimit() {
		return enchLimit;
	}
}
