package com.hoodiecoder.enchantmentcore.utils;

import org.bukkit.Bukkit;

public class VersionUtils {
    public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    public static final int SERVER_VERSION;

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
    }
}
