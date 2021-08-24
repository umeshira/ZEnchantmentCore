package com.hoodiecoder.enchantmentcore.utils.lore;

import com.hoodiecoder.enchantmentcore.CustomEnch;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

/**
 * The lore handler. Used to update the lore of the item meta.
 */
public interface LoreHandler {
    /**
     * Update the lore of the item meta
     *
     * @param meta              the item meta
     * @param currentEnchantMap the current enchantment map
     * @return the remaining enchantment map, contains enchantments that can be added to the lore of the meta
     */
    Map<CustomEnch, Integer> updateItemLore(ItemMeta meta, Map<CustomEnch, Integer> currentEnchantMap);
}
