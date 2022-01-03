package io.zivoric.enchantmentcore.enchant;

import io.zivoric.enchantmentcore.CustomEnch;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An extension to {@link CustomEnch} to deal with projectile event
 */
public interface ProjectileHandler {
    /**
     * <p>Event method automatically called when an entity using this enchantment is hit by a projectile.</p>
     *
     * @param entity The entity involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the entity is involved with
     */
    default void onHit(LivingEntity entity, List<Integer> levels, List<ItemStack> items, ProjectileHitEvent event) {
        // EMPTY
    }

    /**
     * <p>Event method automatically called when an entity using this enchantment hits something with a projectile.</p>
     *
     * @param entity The entity involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the entity is involved with
     */
    default void onTargetHit(LivingEntity entity, List<Integer> levels, List<ItemStack> items, ProjectileHitEvent event) {
        // EMPTY
    }
}
