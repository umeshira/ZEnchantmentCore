package com.hoodiecoder.enchantmentcore;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.GrindstoneInventory;
//import org.bukkit.event.inventory.InventoryEvent;
//import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
//import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.hoodiecoder.enchantmentcore.utils.EnchantmentInformation;
import com.hoodiecoder.enchantmentcore.utils.EnchantmentUtils;

/**
 * Event listener responsible for automatically applying enchantment lore and generating loot tables.
 */
public class AutoEnchListener implements Listener {
	private final EnchantmentCore core;
	private final EnchantmentGenerator generator;
	public AutoEnchListener(EnchantmentCore c, EnchantmentGenerator generator) {
		core = c;
		this.generator = generator;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Inventory inv = e.getWhoClicked().getInventory();
		addAllLore(inv);
	}
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e) {
		Player player = (Player) e.getPlayer();
		Inventory playerInventory = player.getInventory();
		Inventory eventInventory = e.getInventory();
		addAllLore(playerInventory);
		addAllLore(eventInventory);
	}
	@EventHandler
	public void onInteract(InventoryClickEvent e) {
		addAllLore(e.getInventory());
	}
	@EventHandler
	public void onGenerate(LootGenerateEvent e) {
		List<ItemStack> loot = e.getLoot();
		for (ItemStack i : loot) {
			int index = loot.indexOf(i);
			ItemMeta iMeta = i.getItemMeta();
			if (!(iMeta instanceof EnchantmentStorageMeta) && iMeta.hasEnchants()) {
				Map<Enchantment, Integer> enchMap = iMeta.getEnchants();
				for (Entry<Enchantment, Integer> entry : enchMap.entrySet()) {
					iMeta.removeEnchant(entry.getKey());
				}
				EnchantmentInformation enchInfo = generator.getLootOffer(i, e.getEntity(), e.getLootTable(), enchMap.size(), true);
				for (Entry<Enchantment, Integer> ench : enchInfo.toMap().entrySet()) {
					iMeta.addEnchant(ench.getKey(), ench.getValue(), false);
				}
				i.setItemMeta(iMeta);
			} else if (iMeta instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta) iMeta).hasStoredEnchants()) {
				EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) iMeta;

				Map<Enchantment, Integer> enchMap = storageMeta.getStoredEnchants();
				for (Entry<Enchantment, Integer> entry : enchMap.entrySet()) {
					storageMeta.removeStoredEnchant(entry.getKey());	
				}
				EnchantmentInformation enchInfo = generator.getLootOffer(i, e.getEntity(), e.getLootTable(), enchMap.size(), true);
				for (Entry<Enchantment, Integer> ench : enchInfo.toMap().entrySet()) {
					storageMeta.addStoredEnchant(ench.getKey(), ench.getValue(), false);
				}
				i.setItemMeta(storageMeta);
			} else {
				continue;
			}
			loot.set(index, i);
		}
	}
	private void addAllLore(Inventory inv) {
		addLoreLoop(inv.getContents());
		if (inv.getType() == InventoryType.GRINDSTONE) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
				@Override
				public void run() {
					GrindstoneInventory gInv = (GrindstoneInventory) inv;
					ItemStack[] grindstoneItems = new ItemStack[] {gInv.getItem(2)};
					addLoreLoop(grindstoneItems);
					gInv.setItem(2, grindstoneItems[0]);
				} 
			}); // schedule needed or Minecraft will override the set item
		}
	}
	private void addLoreLoop(ItemStack[] items) {
		for (ItemStack i : items) {
			if (i == null) continue;
			ItemMeta meta = i.getItemMeta();
			Map<Enchantment,Integer> enchs;
			if (meta instanceof EnchantmentStorageMeta) {
				enchs = ((EnchantmentStorageMeta)meta).getStoredEnchants();
			} else if (meta != null) {
				enchs = meta.getEnchants();
			} else {
				return;
			}
			List<String> currentLore = meta.getLore();
			List<String> createdLore = EnchantmentUtils.createLore(enchs, currentLore);
			if (createdLore == null || !createdLore.equals(currentLore)) {
				meta.setLore(createdLore);
				i.setItemMeta(meta);
			}
		}
	}
}
