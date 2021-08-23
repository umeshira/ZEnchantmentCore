package com.hoodiecoder.enchantmentcore.utils.lore;

import org.bukkit.inventory.meta.ItemMeta;

/**
 * The lore handler. Used to update the lore of the item meta.
 */
public interface LoreHandler {
    /**
     * Update the lore of the item meta
     *
     * @param meta the item meta
     */
    void updateItemLore(ItemMeta meta);
}
