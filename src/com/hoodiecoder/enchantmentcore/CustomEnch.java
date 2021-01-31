package com.hoodiecoder.enchantmentcore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.hoodiecoder.enchantmentcore.utils.EnchEnums.*;
import com.hoodiecoder.enchantmentcore.utils.EnchantmentUtils;


public abstract class CustomEnch {
	List<String> l;
	private static int enchLimit = 37;
	private final CoreEnch coreEnch;
	public CustomEnch(EnchantmentHolder holder) {
		if (EnchantmentUtils.getNMSVersion().equals("v1_16_R3")) {
			enchLimit = 37;
			net.minecraft.server.v1_16_R3.EnchantmentSlotType slotType;
			net.minecraft.server.v1_16_R3.Enchantment.Rarity rarity;
			net.minecraft.server.v1_16_R3.EnumItemSlot[] enumSlot = null;
			switch (getEnchantmentSlot()) {
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
			switch (getRarity()) {
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
			List<net.minecraft.server.v1_16_R3.EnumItemSlot> eList = new ArrayList<>();
			for (ItemSlotEnum e : getItemSlot()) {
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
			this.coreEnch = new CoreEnch_v1_16_R3(rarity, slotType, enumSlot, getName(), getDisplayName(), getMaxLevel());
		}
		else if (EnchantmentUtils.getNMSVersion().equals("v1_16_R2")) {
			enchLimit = 37;
			net.minecraft.server.v1_16_R2.EnchantmentSlotType slotType;
			net.minecraft.server.v1_16_R2.Enchantment.Rarity rarity;
			net.minecraft.server.v1_16_R2.EnumItemSlot[] enumSlot = null;
			switch (getEnchantmentSlot()) {
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
			switch (getRarity()) {
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
			List<net.minecraft.server.v1_16_R2.EnumItemSlot> eList = new ArrayList<>();
			for (ItemSlotEnum e : getItemSlot()) {
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
			this.coreEnch = new CoreEnch_v1_16_R2(rarity, slotType, enumSlot, getName(), getDisplayName(), getMaxLevel());
		}
		else if (EnchantmentUtils.getNMSVersion().equals("v1_16_R1")) {
			enchLimit = 37;
			net.minecraft.server.v1_16_R1.EnchantmentSlotType slotType;
			net.minecraft.server.v1_16_R1.Enchantment.Rarity rarity;
			net.minecraft.server.v1_16_R1.EnumItemSlot[] enumSlot = null;
			switch (getEnchantmentSlot()) {
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
			switch (getRarity()) {
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
			List<net.minecraft.server.v1_16_R1.EnumItemSlot> eList = new ArrayList<>();
			for (ItemSlotEnum e : getItemSlot()) {
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
			this.coreEnch = new CoreEnch_v1_16_R1(rarity, slotType, enumSlot, getName(), getDisplayName(), getMaxLevel());
		}
		else coreEnch = null;
		holder.addEnchant(this);
	}
	public CoreEnch getCoreEnch() {
		return coreEnch;
	}
	public static int getEnchLimit() {
		return enchLimit;
	}
	void setDisabled(boolean d) {
		if (EnchantmentUtils.getNMSVersion().equals("v1_16_R3")) {
			((CoreEnch_v1_16_R3) coreEnch).setDisabled(d);
		} else if (EnchantmentUtils.getNMSVersion().equals("v1_16_R2")) {
			((CoreEnch_v1_16_R2) coreEnch).setDisabled(d);
		} else if (EnchantmentUtils.getNMSVersion().equals("v1_16_R1")) {
			((CoreEnch_v1_16_R1) coreEnch).setDisabled(d);
		}
	}
	void checkRegisterEnch(boolean resetting, int id) {
		if (EnchantmentUtils.getNMSVersion().equals("v1_16_R3")) {
			((CoreEnch_v1_16_R3) coreEnch).checkRegisterEnch(resetting, id);
		} else if (EnchantmentUtils.getNMSVersion().equals("v1_16_R2")) {
			((CoreEnch_v1_16_R2) coreEnch).checkRegisterEnch(resetting, id);
		} else if (EnchantmentUtils.getNMSVersion().equals("v1_16_R1")) {
			((CoreEnch_v1_16_R1) coreEnch).checkRegisterEnch(resetting, id);
		}
	}
	public abstract RarityEnum getRarity();
	public abstract EnchantmentSlotEnum getEnchantmentSlot();
	public ItemSlotEnum[] getItemSlot() {
		ItemSlotEnum[] armorSlot = new ItemSlotEnum[] {ItemSlotEnum.CHEST, ItemSlotEnum.FEET, ItemSlotEnum.LEGS, ItemSlotEnum.HEAD};
		switch (getEnchantmentSlot()) {
		case ARMOR:
			return armorSlot;
		case ARMOR_CHEST:
			return new ItemSlotEnum[] {ItemSlotEnum.CHEST};
		case ARMOR_FEET:
			return new ItemSlotEnum[] {ItemSlotEnum.FEET};
		case ARMOR_HEAD:
			return new ItemSlotEnum[] {ItemSlotEnum.HEAD};
		case ARMOR_LEGS:
			return new ItemSlotEnum[] {ItemSlotEnum.LEGS};
		case BOW:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		case BREAKABLE:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		case CROSSBOW:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		case DIGGER:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		case FISHING_ROD:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		case TRIDENT:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		case VANISHABLE:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		case WEAPON:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		case WEARABLE:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		default:
			return new ItemSlotEnum[] {ItemSlotEnum.MAINHAND};
		}
	}
	public abstract String getName();
	public abstract String getDisplayName();
	public abstract int getMaxLevel();
	public void onTakeDamage(EntityDamageByEntityEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onGainExp(PlayerExpChangeEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onFish(PlayerFishEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onAir(EntityAirChangeEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onTargeted(EntityTargetEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onBreakBlock(BlockBreakEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onDealDamage(EntityDamageByEntityEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onDeath(EntityDeathEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onDropItem(EntityDropItemEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onHit(ProjectileHitEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onInteract(EntityInteractEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onPlaceBlock(BlockPlaceEvent event, List<Integer> levels, List<ItemStack> items) {
	}
	public void onPotionReceived(EntityPotionEffectEvent event, List<Integer> levels, List<ItemStack> items) {
		
	}
	public void onRegainHealth(EntityRegainHealthEvent event, List<Integer> levels, List<ItemStack> items) {
		
	}
	public void onShootBow(EntityShootBowEvent event, List<Integer> levels, List<ItemStack> items) {
		
	}
}
