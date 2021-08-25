package com.hoodiecoder.enchantmentcore.paper;

import com.hoodiecoder.enchantmentcore.EnchantmentHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of <code>PaperCustomEnch</code> that uses a translatable key, for use with resource packs.
 *
 * @see PaperCustomEnch
 */
public abstract class TranslatableCustomEnch extends PaperCustomEnch {
    /**
     * <p>Creates a new instance of a <code>PaperCustomEnch</code> with an {@link EnchantmentHolder} and the identifier of the enchantment.</p>
     *
     * @param holder     the <code>EnchantmentHolder</code> for the enchantment
     * @param identifier the internal ID of the enchantment
     */
    public TranslatableCustomEnch(EnchantmentHolder holder, String identifier) {
        super(holder, identifier);
    }

    @Override
    public final @NotNull Component displayName() {
        return Component.translatable(translationKey());
    }

}
