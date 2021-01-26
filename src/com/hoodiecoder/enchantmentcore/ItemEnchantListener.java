package com.hoodiecoder.enchantmentcore;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemEnchantListener implements Listener {
	@SuppressWarnings("unused")
	private EnchantmentCore core;
	
	public ItemEnchantListener(EnchantmentCore c) {
		core = c;
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent event) {
		Map<Enchantment, Integer> map = event.getEnchantsToAdd();
		ItemMeta meta = event.getItem().getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new LinkedList<String>();
		}
		List<String> createdLore = EnchantmentCore.createLore(map, lore);
		if (lore != null) createdLore.addAll(lore);
		meta.setLore(createdLore);
		event.getItem().setItemMeta(meta);
	}
	@EventHandler
	public void onAnvil(PrepareAnvilEvent event) {
		ItemStack result = event.getResult();
		if (result == null || result.getType().equals(Material.AIR)) return;
		Map<Enchantment, Integer> map = result.getEnchantments();
		ItemMeta meta = result.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new LinkedList<String>();
		}
		List<String> createdLore = EnchantmentCore.createLore(map, lore);
		if (lore != null) createdLore.addAll(lore);
		meta.setLore(createdLore);
		result.setItemMeta(meta);
	}
}
