package com.hoodiecoder.enchantmentcore;

import org.bukkit.enchantments.Enchantment;

public interface CoreEnch {
	String getDisplayName();
	int getCoreID();
	boolean isDisabled();
	String getInternalName();
	Enchantment getCraftEnchant();
	int getMaxLevel();
}
