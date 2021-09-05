package io.zivoric.enchantmentcore.utils.lore;

import io.zivoric.enchantmentcore.CustomEnch;
import io.zivoric.enchantmentcore.paper.PaperCustomEnch;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link LoreHandler} that works on Paper-specific enchantments
 */
public class PaperLoreHandler implements LoreHandler {
    private static final Component ENCH_CODE = Component.empty().style(Style.style().color(TextColor.color(0x7a6563))
            .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.OBFUSCATED, false)
            .decoration(TextDecoration.BOLD, false).decoration(TextDecoration.STRIKETHROUGH, false)
            .decoration(TextDecoration.UNDERLINED, false));

    @Override
    public Map<CustomEnch, Integer> updateItemLore(ItemMeta meta, Map<CustomEnch, Integer> currentEnchantMap) {
        Map<CustomEnch, Integer> remaining = new HashMap<>();
        // Create enchant lore
        List<Component> createdLore = new ArrayList<>();
        boolean showEnchants = !meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS); // Only add lore if the item doesn't hide enchants
        currentEnchantMap.forEach((ench, level) -> {
            if (ench instanceof PaperCustomEnch) {
                if (showEnchants) {
                    createdLore.add(ench.displayName(level).decoration(TextDecoration.ITALIC, false).append(ENCH_CODE));
                }
            } else {
                remaining.put(ench, level);
            }
        });

        // Add current lore
        List<Component> currentLore = meta.lore();
        if (currentLore != null) {
            for (Component comp : currentLore) {
                if (!comp.children().contains(ENCH_CODE)) { // Should not contain enchant lore
                    createdLore.add(comp);
                }
            }
        }

        // Set lore to item meta
        if (!createdLore.equals(currentLore)) {
            meta.lore(createdLore.isEmpty() ? null : createdLore);
        }

        return remaining;
    }
}
