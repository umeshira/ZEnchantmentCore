package com.hoodiecoder.enchantmentcore.enchant;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An extension to {@link com.hoodiecoder.enchantmentcore.CustomEnch} to deal with target event
 */
public interface TargetHandler {
    /**
     * <p>Event method automatically called when an entity using this enchantment is targeted.</p>
     *
     * @param entity The entity involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the entity is involved with
     */
    void onTargeted(LivingEntity entity, List<Integer> levels, List<ItemStack> items, EntityTargetEvent event);
}
