package com.hoodiecoder.enchantmentcore.enchant;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An extension to {@link com.hoodiecoder.enchantmentcore.CustomEnch} to deal with fish event
 */
public interface FishHandler {
    /**
     * <p>Event method automatically called when a player using this enchantment is fishing.</p>
     *
     * @param player The player involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the player is involved with
     */
    void onFish(Player player, List<Integer> levels, List<ItemStack> items, PlayerFishEvent event);
}
