package com.hoodiecoder.enchantmentcore.enchant;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An extension to {@link com.hoodiecoder.enchantmentcore.CustomEnch} to deal with potion effect event
 */
public interface PotionEffectHandler {
    /**
     * <p>Event method automatically called when an entity using this enchantment receives a potion.</p>
     *
     * @param entity The entity involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the entity is involved with
     */
    void onPotionReceived(LivingEntity entity, List<Integer> levels, List<ItemStack> items, EntityPotionEffectEvent event);
}
