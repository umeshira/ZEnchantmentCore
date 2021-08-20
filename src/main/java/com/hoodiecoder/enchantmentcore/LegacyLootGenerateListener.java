package com.hoodiecoder.enchantmentcore;

import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.loot.LootTable;

/**
 * Event listener responsible for generating loot tables for versions 1.14 and below.
 */
public class LegacyLootGenerateListener implements Listener {
    private final EnchantmentGenerator generator;

    public LegacyLootGenerateListener(EnchantmentGenerator generator) {
        this.generator = generator;
    }

    @EventHandler
    public void onInteractChest(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getClickedBlock().getState();
            if (chest.getLootTable() != null) {
                Entity looter = event.getPlayer();
                LootTable table = chest.getLootTable();
                chest.getInventory().forEach(item -> {
                    if (item != null)
                        LootGenerateListener.addEnchantToItem(generator, looter, table, item);
                });
            }
        }
    }

    @EventHandler
    public void onInteractMinecartChest(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof StorageMinecart) {
            StorageMinecart minecart = (StorageMinecart) event.getRightClicked();
            if (minecart.getLootTable() != null) {
                Entity looter = event.getPlayer();
                LootTable table = minecart.getLootTable();
                minecart.getInventory().forEach(item -> {
                    if (item != null)
                        LootGenerateListener.addEnchantToItem(generator, looter, table, item);
                });
            }
        }
    }
}
