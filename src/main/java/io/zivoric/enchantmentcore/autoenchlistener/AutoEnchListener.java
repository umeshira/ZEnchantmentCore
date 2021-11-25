package io.zivoric.enchantmentcore.autoenchlistener;

import io.zivoric.enchantmentcore.EnchantmentCore;
import io.zivoric.enchantmentcore.utils.EnchantmentUtils;
import io.zivoric.enchantmentcore.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collection;

/**
 * Event listener responsible for automatically applying enchantment lore.
 */
public abstract class AutoEnchListener {
    private final EnchantmentCore core;

    public AutoEnchListener(EnchantmentCore core) {
        this.core = core;
    }

    public EnchantmentCore getCore() {
        return core;
    }

    public abstract void setup();

    public abstract void unregister();

    protected final void addLore(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        EnchantmentUtils.updateItemLore(meta);
        item.setItemMeta(meta);
    }

    protected final void addLoreLoop(Collection<ItemStack> items) {
        for (ItemStack i : items) {
            addLore(i);
        }
    }

    protected final void addLoreLoop(ItemStack... items) {
        addLoreLoop(Arrays.asList(items));
    }

    protected final void addAllLore(Inventory inv) {
        addLoreLoop(inv.getContents());
        if (VersionUtils.SERVER_VERSION >= 14 && inv.getType() == InventoryType.GRINDSTONE) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getCore(), () -> {
                GrindstoneInventory gInv = (GrindstoneInventory) inv;
                ItemStack[] grindstoneItems = new ItemStack[]{gInv.getItem(2)};
                addLoreLoop(grindstoneItems);
                gInv.setItem(2, grindstoneItems[0]);
            }); // schedule needed or Minecraft will override the set item
        }
    }
}
