package io.zivoric.enchantmentcore.enchant;

import io.zivoric.enchantmentcore.CustomEnch;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An extension to {@link CustomEnch} to deal with air change event
 */
public interface AirChangeHandler {
    /**
     * <p>Event method automatically called when an entity using this enchantment has their air level changed.</p>
     *
     * @param entity The entity involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the entity is involved with
     */
    void onAir(LivingEntity entity, List<Integer> levels, List<ItemStack> items, EntityAirChangeEvent event);
}
