package io.zivoric.enchantmentcore.paper;

import io.papermc.paper.enchantments.EnchantmentRarity;
import io.zivoric.enchantmentcore.CustomEnch;
import io.zivoric.enchantmentcore.utils.EnchEnums.Rarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Extension of <code>CustomEnch</code> exclusively compatible with paper.
 */
public abstract class PaperCustomEnch extends CustomEnch {

    /**
     * <p>Creates a new instance of a <code>PaperCustomEnch</code> with an {@link Plugin} and the identifier of the enchantment.</p>
     *
     * @param plugin     the <code>Plugin</code> for the enchantment
     * @param identifier the internal ID of the enchantment
     */
    public PaperCustomEnch(Plugin plugin, String identifier) {
        super(plugin, identifier);
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
        NamedTextColor color = this.isCursed() ? NamedTextColor.RED : NamedTextColor.GRAY;
        if (level != 1 || this.getMaxLevel() != 1) {
            Component levelComp = Component.translatable("enchantment.level." + level).color(color);
            component = component.append(Component.space()).append(levelComp);
        }
        component = component.color(color);
        return component;
    }

    @Override
    public boolean isTradeable() {
        return isDiscoverable();
    }

    @Override
    public final boolean isDiscoverable() {
        return getEnchantmentRarity() != Rarity.UNFINDABLE;
    }

    @Override
    public final @NotNull EnchantmentRarity getRarity() {
        Rarity rarity = getEnchantmentRarity();
        return rarity == Rarity.UNFINDABLE ? EnchantmentRarity.VERY_RARE : EnchantmentRarity.valueOf(rarity.name());
    }

    @Override
    public float getDamageIncrease(int i, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public final @NotNull Set<EquipmentSlot> getActiveSlots() {
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
