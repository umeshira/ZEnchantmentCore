package io.zivoric.enchantmentcore.utils;

import org.bukkit.Bukkit;

/**
 * Utility class for server version information
 */
public class VersionUtils {
    /**
     * <p>The NMS version. Mainly used to get CB and NMS classes</p>
     */
    public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    /**
     * <p>Gets the numeric Minecraft version.</p>
     * <p>The Minecraft version number is based on the major release of Minecraft. For example:</p>
     * <ul>
     * <li>In version <code>"1.13.2"</code>, it will return <code>13</code></li>
     * <li>In version <code>"1.16"</code>, will return <code>16</code></li>
     * </ul>
     */
    public static final int SERVER_VERSION;

    /**
     * <p>The Bukkit type that the server is running.</p>
     * <p>Can either be <code>SPIGOT</code>, <code>PAPER</code>, or <code>GENERIC</code>.</p>
     */
    public static final BukkitType BUKKIT_TYPE;

    static {
        String bukkitVers = Bukkit.getVersion();
        if (bukkitVers.contains("1.17")) {
            SERVER_VERSION = 17;
        } else if (bukkitVers.contains("1.16")) {
            SERVER_VERSION = 16;
        } else if (bukkitVers.contains("1.15")) {
            SERVER_VERSION = 15;
        } else if (bukkitVers.contains("1.14")) {
            SERVER_VERSION = 14;
        } else if (bukkitVers.contains("1.13")) {
            SERVER_VERSION = 13;
        } else {
            SERVER_VERSION = -1;
        }
        BukkitType bukkitType;
        try {
            Class.forName("io.papermc.paper.enchantments.EnchantmentRarity");
            bukkitType = BukkitType.PAPER;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("org.bukkit.Server$Spigot");
                bukkitType = BukkitType.SPIGOT;
            } catch (ClassNotFoundException e2) {
                bukkitType = BukkitType.GENERIC;
            }
        }
        BUKKIT_TYPE = bukkitType;
    }

    /**
     * Get the CraftBukkit class if it exists
     *
     * @param cl the class name
     * @return the existing CraftBukkit class
     * @throws IllegalArgumentException If the class is not found
     */
    public static Class<?> getCraftBukkitClass(String cl) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + "." + cl);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot find the CraftBukkit class", e);
        }
    }
}
