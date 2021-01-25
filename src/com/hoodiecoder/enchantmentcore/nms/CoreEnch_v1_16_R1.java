package com.hoodiecoder.enchantmentcore.nms;

import java.lang.reflect.Field;

import com.hoodiecoder.enchantmentcore.EnchantmentCore;

import net.minecraft.server.v1_16_R1.Enchantment;
import net.minecraft.server.v1_16_R1.EnchantmentSlotType;
import net.minecraft.server.v1_16_R1.EnumItemSlot;
import net.minecraft.server.v1_16_R1.IRegistry;
import net.minecraft.server.v1_16_R1.MinecraftKey;

public class CoreEnch_v1_16_R1 extends Enchantment implements CoreEnchParent {
	private static int nextID = 0;
	private final int coreID, maxLevel;
	private final String intName;
	private final String displayName;
	private final EnchantmentSlotType slotType;
	private boolean disabled = false;
	
	CoreEnch_v1_16_R1(Rarity var0, EnchantmentSlotType var1, EnumItemSlot[] var2, String name, String displayName, int maxLevel) {
		super(var0, var1, var2);
		if (maxLevel > 10) {
			maxLevel = 10;
		}
		slotType = var1;
		c = displayName;
		coreID = nextID;
		nextID++;
		this.maxLevel = maxLevel;
		intName = name;
		this.displayName = displayName;
		if (!EnchantmentCore.enchList.contains(this)) EnchantmentCore.enchList.add(this);
	}
	@Override
	public int getMaxLevel() {
		return maxLevel;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void checkRegisterEnch(boolean resetting, int id) {
		if (IRegistry.ENCHANTMENT.a(this) == -1) {
			try {
				/* Tests for int fields, in case the name of the parameter is different
				for (Field f : IRegistry.ENCHANTMENT.getClass().getDeclaredFields()) {
					f.setAccessible(true);
					if (f.getGenericType().toString() == "int") {
					System.out.println("Field: " + f.getGenericType().toString() + " " + f.getName() + ", " + f.get(IRegistry.ENCHANTMENT));
					}
				}*/
				Field f = IRegistry.ENCHANTMENT.getClass().getDeclaredField("bd");
				Field f1 = org.bukkit.enchantments.Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f1.setAccessible(true);
				f1.set(null, true);
				if (resetting) f.set(IRegistry.ENCHANTMENT, id);
				IRegistry.a(IRegistry.ENCHANTMENT, new MinecraftKey(intName), this);
				try {
				org.bukkit.enchantments.Enchantment.registerEnchantment(new NameableCraftEnchantment_v1_16_R1(this, intName));
				} catch (Exception e) {
					EnchantmentCore.getInstance().getLogger().info("Skipping Registry for Enchantment " + new MinecraftKey(intName) + ".");
				}
			} catch (IllegalArgumentException|NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	public int getCoreID() {
		return coreID;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean d) {
		disabled = d;
	}
	public String getInternalName() {
		return intName;
	}
	public EnchantmentSlotType getSlotType() {
		return slotType;
	}
	public org.bukkit.enchantments.Enchantment getCraftEnchant() {
		if (disabled) return null;
		return (org.bukkit.enchantments.Enchantment) new NameableCraftEnchantment_v1_16_R1(this, intName);
	}
	@Override
	public String toString() {
		return "minecraft:" + intName;
	}
}
