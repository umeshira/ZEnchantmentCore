package io.zivoric.enchantmentcore.auto;

import io.zivoric.enchantmentcore.EnchantmentCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

/**
 * {@link AutoEnchListener} with Bukkit events
 */
public class SimpleAutoEnchListener extends AutoEnchListener implements Listener {
    public SimpleAutoEnchListener(EnchantmentCore core) {
        super(core);
    }

    @Override
    public void setup() {
        Bukkit.getPluginManager().registerEvents(this, getCore());
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
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
    public void onPickup(EntityPickupItemEvent e) {
        addLore(e.getItem().getItemStack());
    }
}
