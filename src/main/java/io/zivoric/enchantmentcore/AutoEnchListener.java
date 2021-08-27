package io.zivoric.enchantmentcore;

import io.zivoric.enchantmentcore.utils.EnchantmentUtils;
import io.zivoric.enchantmentcore.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Event listener responsible for automatically applying enchantment lore.
 */
public class AutoEnchListener implements Listener {
    private final EnchantmentCore core;

    public AutoEnchListener(EnchantmentCore c) {
        core = c;
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

    private void addAllLore(Inventory inv) {
        addLoreLoop(inv.getContents());
        if (VersionUtils.SERVER_VERSION >= 14 && inv.getType() == InventoryType.GRINDSTONE) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(core, () -> {
                GrindstoneInventory gInv = (GrindstoneInventory) inv;
                ItemStack[] grindstoneItems = new ItemStack[]{gInv.getItem(2)};
                addLoreLoop(grindstoneItems);
                gInv.setItem(2, grindstoneItems[0]);
            }); // schedule needed or Minecraft will override the set item
        }
    }

    private void addLoreLoop(ItemStack[] items) {
        for (ItemStack i : items) {
            if (i == null) continue;
            ItemMeta meta = i.getItemMeta();
            EnchantmentUtils.updateItemLore(meta);
            i.setItemMeta(meta);
        }
    }
}
