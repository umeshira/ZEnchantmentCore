package com.hoodiecoder.enchantmentcore;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Holder responsible for holding pending custom enchantments for registration.</p>
 * <p>The holder must be passed to each <code>CustomEnch</code> constructor in order for the enchantments to be added to the holder. For example:</p>
 * <pre>
 * EnchantmentHolder holder = new EnchantmentHolder(plugin);
 * CustomEnch myEnch = new MyCustomEnch(holder);
 * </pre>
 */
public class EnchantmentHolder {
    private final List<CustomEnch> holder;
    private final Plugin plugin;

    /**
     * Creates a new <code>EnchantmentHolder</code> object with a plugin owner.
     *
     * @param plugin The plugin responsible for the holder
     */
    public EnchantmentHolder(Plugin plugin) {
        this.plugin = plugin;
        holder = new ArrayList<>();
    }

    boolean addEnchant(CustomEnch ench) {
        if (!holder.contains(ench)) {
            holder.add(ench);
            return true;
        } else {
            return false;
        }
    }

    boolean removeEnchant(CustomEnch ench) {
        if (holder.contains(ench)) {
            holder.remove(ench);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets a list of all enchants in the holder.
     *
     * @return List of all held enchants
     */
    public List<CustomEnch> getEnchants() {
        return Collections.unmodifiableList(holder);
    }

    /**
     * Registers all currently held enchants as pending registration.
     */
    public void registerPendingEnchants() {
        for (CustomEnch cew : holder) {
            cew.addToPending();
        }
    }

    /**
     * Gets the owner plugin of this holder.
     *
     * @return The plugin responsible for the holder
     */
    public Plugin getOwnerPlugin() {
        return plugin;
    }
}
