package com.hoodiecoder.enchantmentcore.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

import com.hoodiecoder.enchantmentcore.CoreEnch;
import com.hoodiecoder.enchantmentcore.CustomEnch;
import com.hoodiecoder.enchantmentcore.EnchantmentCore;

public class EnchantmentUtils {
private static final String[] numerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
private static final int enchLimit = CustomEnch.getEnchLimit();
private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
public static int getEnchantLimit() {
	return enchLimit;
}
public static String getNMSVersion() {
	return version;
}
public static String[] numeralsArray() {
	return numerals;
}
public static Map<Enchantment, Integer> parseLore(List<String> lore) {
	String enchCode = ChatColor.GRAY.toString();
	String endCode = ChatColor.GRAY + "" + ChatColor.MAGIC + " ";
	Map<Enchantment, Integer> enchMap = new HashMap<>();
	if (lore == null || lore.isEmpty()) return null;
	for (String s : lore) {
		String display = s.substring(enchCode.length());
		int power = 1;
		for (String n : numerals) {
			if (display.indexOf(" " + n + endCode) != -1) {
				display = display.substring(0, display.length()-n.length()-endCode.length()-1);
				power = ArrayUtils.indexOf(numerals, n)+1;
			}
		}
		if (s.startsWith(enchCode)) {
			for (CustomEnch cew : EnchantmentCore.getInstance().getEnchList()) {
				CoreEnch ne = cew.getCoreEnch();
				if (EnchantmentCore.getInstance().getEnchList().get(ne.getCoreID()) != null && s.startsWith(enchCode + ne.getDisplayName()) &&  ne.equals(EnchantmentCore.getInstance().getEnchList().get(ne.getCoreID()).getCoreEnch())) {
					enchMap.put(ne.getCraftEnchant(), power);
				}
			}
		}
	}
	return enchMap;
}
public static List<String> createLore(Map<Enchantment, Integer> enchs, List<String> currentLore) {
	String enchCode = ChatColor.GRAY.toString();
	String endCode = ChatColor.GRAY + "" + ChatColor.MAGIC + " ";
	List<String> lore = new LinkedList<String>();
	for (Entry<Enchantment, Integer> e : enchs.entrySet()) {
		for (CustomEnch cew : EnchantmentCore.getInstance().getEnchList()) {
			CoreEnch ne = cew.getCoreEnch();
			if (!ne.isDisabled() && ne.getCraftEnchant() != null && e.getKey().equals(ne.getCraftEnchant())) {
				if (currentLore == null || (ne.getMaxLevel() > 1 && !currentLore.contains(enchCode + ne.getDisplayName() + " " + numerals[e.getValue()-1] + endCode)) || (ne.getMaxLevel() <= 1 && !currentLore.contains(enchCode + ne.getDisplayName() + endCode))) {
					if (ne.getMaxLevel() > 1) {
						lore.add(enchCode + ne.getDisplayName() + " " + numerals[e.getValue()-1] + endCode);
					} else {
						lore.add(enchCode + ne.getDisplayName() + endCode);
					}
				}
			}
		}
	}
	return lore;
}
}
