package com.hoodiecoder.enchantmentcore.nms;

import org.bukkit.enchantments.Enchantment;

public interface CoreEnchParent {
	public String getDisplayName();
	public void checkRegisterEnch(boolean resetting, int id);
	public int getCoreID();
	public boolean isDisabled();
	public void setDisabled(boolean d);
	public String getInternalName();
	public Enchantment getCraftEnchant();
	public int getMaxLevel();
}
