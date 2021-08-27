package io.zivoric.enchantmentcore.utils.lore;

import io.zivoric.enchantmentcore.CustomEnch;
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
     * @return the remaining enchantment map, contains enchantments that can not be added to the lore of the meta
     */
    Map<CustomEnch, Integer> updateItemLore(ItemMeta meta, Map<CustomEnch, Integer> currentEnchantMap);
}
