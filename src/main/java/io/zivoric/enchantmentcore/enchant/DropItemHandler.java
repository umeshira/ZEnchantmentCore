package io.zivoric.enchantmentcore.enchant;

import io.zivoric.enchantmentcore.CustomEnch;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An extension to {@link CustomEnch} to deal with drop item event
 */
public interface DropItemHandler {
    /**
     * <p>Event method automatically called when a entity using this enchantment drops an item.</p>
     *
     * @param entity The entity involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the entity is involved with
     */
    void onDropItem(LivingEntity entity, List<Integer> levels, List<ItemStack> items, EntityDropItemEvent event);
}
