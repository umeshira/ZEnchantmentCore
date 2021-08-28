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
     * <p>Event method automatically called when an entity using this enchantment hits something.</p>
     *
     * @param entity The entity involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the entity is involved with
     */
    void onHit(LivingEntity entity, List<Integer> levels, List<ItemStack> items, ProjectileHitEvent event);

    /**
     * <p>Event method automatically called when an entity using this enchantment have its target hit by the entity's projectile.</p>
     *
     * @param entity The entity involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the entity is involved with
     */
    void onTargetHit(LivingEntity entity, List<Integer> levels, List<ItemStack> items, ProjectileHitEvent event);
}
