package io.zivoric.enchantmentcore.paper;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of <code>PaperCustomEnch</code> that uses a translatable key, for use with resource packs.
 *
 * @see PaperCustomEnch
 */
public abstract class TranslatableCustomEnch extends PaperCustomEnch {
    /**
     * <p>Creates a new instance of a <code>PaperCustomEnch</code> with an {@link Plugin} and the identifier of the enchantment.</p>
     *
     * @param plugin     the <code>Plugin</code> for the enchantment
     * @param identifier the internal ID of the enchantment
     */
    public TranslatableCustomEnch(Plugin plugin, String identifier) {
        super(plugin, identifier);
    }

    @Override
    public final @NotNull Component displayName() {
        return Component.translatable(translationKey());
    }

}
