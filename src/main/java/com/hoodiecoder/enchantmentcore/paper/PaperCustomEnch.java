package com.hoodiecoder.enchantmentcore.paper;

import com.hoodiecoder.enchantmentcore.CustomEnch;
import com.hoodiecoder.enchantmentcore.EnchantmentHolder;
import com.hoodiecoder.enchantmentcore.utils.EnchEnums.Rarity;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Extension of <code>CustomEnch</code> exclusively compatible with paper.
 */
public abstract class PaperCustomEnch extends CustomEnch {

    /**
     * <p>Creates a new instance of a <code>PaperCustomEnch</code> with an {@link EnchantmentHolder} and the identifier of the enchantment.</p>
     * <p>The identifier must not already be registered in minecraft as an enchantment, or it will be replaced with a placeholder identifier.</p>
     *
     * @param holder     the <code>EnchantmentHolder</code> for the enchantment
     * @param identifier the internal ID of the enchantment
     */
    public PaperCustomEnch(EnchantmentHolder holder, String identifier) {
        super(holder, identifier);
    }

    /**
     * Get the display name of the enchantment
     *
     * @return the component of the display name
     */
    public @NotNull Component displayName() {
        return Component.text(getDisplayName());
    }

    @Override
    public final @NotNull Component displayName(int level) {
        Component component = displayName();
        if (level != 1 || this.getMaxLevel() != 1) {
            component = component.append(Component.space()).append(Component.translatable("enchantment.level." + level));
        }
        component = component.color(this.isCursed() ? NamedTextColor.RED : NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
        return component;
    }

    @Override
    public boolean isTradeable() {
        return isDiscoverable();
    }

    @Override
    public final boolean isDiscoverable() {
        return getEnchantmentRarity() == Rarity.UNFINDABLE;
    }

    @Override
    public final EnchantmentRarity getRarity() {
        Rarity rarity = getEnchantmentRarity();
        return rarity == Rarity.UNFINDABLE ? EnchantmentRarity.VERY_RARE : EnchantmentRarity.valueOf(rarity.name());
    }

    @Override
    public float getDamageIncrease(int i, EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public final Set<EquipmentSlot> getActiveSlots() {
        return getEquipmentSlot();
    }

    /**
     * Get the translation key. Default is <code>enchantment.custom.%_plugin_%.%_key_%</code>
     *
     * @return the translation key
     */
    @Override
    public @NotNull String translationKey() {
        NamespacedKey namespacedKey = this.getKey();
        return "enchantment.custom." + namespacedKey.getNamespace() + "." + namespacedKey.getKey();
    }
}
