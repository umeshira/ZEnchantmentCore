package com.hoodiecoder.enchantmentcore.utils.lore;

import com.hoodiecoder.enchantmentcore.CustomEnch;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The default {@link LoreHandler} that works on Spigot and its forks
 */
public class DefaultLoreHandler implements LoreHandler {
    private static final String[] numerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    private static final String ENCH_CODE = ChatColor.GRAY + "" + ChatColor.MAGIC + " ";

    /**
     * Returns the roman numeral for the specified integer.
     *
     * @param num The number to convert
     * @return Roman numeral for numbers 1-10; otherwise, it will return the integer as a String.
     */
    public static String getRomanNumeral(int num) {
        if (num > numerals.length || num < 1) return Integer.toString(num);
        return numerals[num - 1];
    }

    /**
     * Creates a single line of lore for a custom enchantment.
     *
     * @param ce  The custom enchantment
     * @param lvl The level of the enchantment
     * @return Lore line for the enchantment
     */
    public static String createLoreLine(CustomEnch ce, int lvl) {
        String endCode = ENCH_CODE;
        String loreName = (ce.isCursed() ? ChatColor.RED : ChatColor.GRAY) + ce.getDisplayName();
        if (ce.getMaxLevel() > 1) {
            return (loreName + " " + getRomanNumeral(lvl) + endCode);
        } else {
            return (loreName + endCode);
        }
    }

    @Override
    public Map<CustomEnch, Integer> updateItemLore(ItemMeta meta, Map<CustomEnch, Integer> currentEnchantMap) {
        // Create enchant lore
        List<String> createdLore = new ArrayList<>();
        if (!meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) { // Only add lore if the item doesn't hide enchants
            currentEnchantMap.forEach((ench, level) -> createdLore.add(createLoreLine(ench, level)));
        }

        // Add current lore
        List<String> currentLore = meta.getLore();
        if (currentLore != null) {
            for (String str : currentLore) {
                if (!str.endsWith(ENCH_CODE)) { // Should not contain enchant lore
                    createdLore.add(str);
                }
            }
        }

        // Set lore to item meta
        if (!createdLore.equals(currentLore)) {
            meta.setLore(createdLore);
        }

        return Collections.emptyMap();
    }
}
