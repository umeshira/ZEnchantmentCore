package io.zivoric.enchantmentcore.plugin;

import io.zivoric.enchantmentcore.CustomEnch;

import java.util.List;

/**
 * This interface will be implemented by plugins to add new enchants.
 * Developers need to implement this interface to their main plugin class.
 * This core will detect all plugins implementing this interface and add their enchants to the core.
 */
public interface EnchantmentPlugin {
    /**
     * Get the list of enchantments from this plugin
     *
     * @return List of enchantments
     */
    List<CustomEnch> getEnchants();
}
