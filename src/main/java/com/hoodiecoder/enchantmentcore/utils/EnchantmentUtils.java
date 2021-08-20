package com.hoodiecoder.enchantmentcore.utils;

import com.hoodiecoder.enchantmentcore.CustomEnch;
import com.hoodiecoder.enchantmentcore.EnchantmentCore;
import com.hoodiecoder.enchantmentcore.EnchantmentGenerator;
import com.hoodiecoder.enchantmentcore.utils.EnchEnums.EnchConstants;
import com.hoodiecoder.enchantmentcore.utils.EnchEnums.MaterialType;
import com.hoodiecoder.enchantmentcore.utils.EnchEnums.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.Map.Entry;

/**
 * Utility class containing several useful methods for using the plugin, as well as manipulating and working with enchantments.
 */
public class EnchantmentUtils {
    private static final String[] numerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String enchCode = ChatColor.GRAY + "" + ChatColor.MAGIC + " ";

    public static String getNMSVersion() {
        return version;
    }

    public static Class<?> getCraftBukkitClass(String cl) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + cl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the roman numeral for the specified integer.
     *
     * @param num The number to convert
     * @return Roman numeral for numbers 1-10; otherwise, it will return the integer as a String.
     */
    public static String getRomanNumeral(int num) {
        if (num > numerals.length || num < 1) return Integer.toString(num);
        return numerals[num - 1];
    }

    /**
     * Returns the integer value of the specified roman numeral 1-10.
     *
     * @param num The roman numeral to convert
     * @return Integer value of roman numeral for numbers 1-10. If <code>num</code>
     * is instead a numeric String, it will return the integer value of that
     * String; otherwise, it will return <code>-1</code>.
     */
    public static int parseRomanNumeral(String num) {
        int index = Arrays.asList(numerals).indexOf(num);
        if (index > -1) return index + 1;
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Gets the enchantment code applied at the end of every enchantment lore line.
     *
     * @return Enchantment lore code
     */
    public static String getEnchCode() {
        return enchCode;
    }

    /**
     * Parses a list containing item lore into its respective enchantments and levels.
     *
     * @param lore The lore to parse
     * @return Map of enchantments and their levels based on the lore
     */
    public static Map<Enchantment, Integer> parseLore(List<String> lore) {
        Map<Enchantment, Integer> enchMap = new HashMap<>();
        if (lore == null || lore.isEmpty()) return null;
        for (String s : lore) {
            Entry<Enchantment, Integer> parsedLine = parseLoreLine(s);
            if (parsedLine != null) {
                enchMap.put(parsedLine.getKey(), parsedLine.getValue());
            }
        }
        return enchMap;
    }

    /**
     * Creates item lore for enchantments and their levels based on current item lore.
     *
     * @param enchs       The enchantments and levels to add
     * @param currentLore Current item lore to combine with
     * @return Lore created from the given enchantments
     */
    public static List<String> createLore(Map<Enchantment, Integer> enchs, List<String> currentLore) {
        List<String> lore = new LinkedList<>();
        for (Entry<Enchantment, Integer> e : enchs.entrySet()) {
            if (e.getKey() instanceof CustomEnch) {
                lore.add(createLoreLine((CustomEnch) e.getKey(), e.getValue()));
            }
        }
        if (currentLore != null) {
            for (String str : currentLore) {
                if (!str.endsWith(enchCode) && parseLoreLine(str) == null) {
                    lore.add(str);
                }
            }
        }
        return lore;
    }

    /**
     * Creates a single line of lore for a custom enchantment.
     *
     * @param ce  The custom enchantment
     * @param lvl The level of the enchantment
     * @return Lore line for the enchantment
     */
    public static String createLoreLine(CustomEnch ce, int lvl) {
        String endCode = enchCode;
        if (ce.getMaxLevel() > 1) {
            return (ce.getLoreName() + " " + getRomanNumeral(lvl) + endCode);
        } else {
            return (ce.getLoreName() + endCode);
        }
    }

    /**
     * Parses a single line of lore into an enchantment and its level.
     *
     * @param s The string to parse
     * @return Parsed enchantment and its level; <code>null</code> if no enchantment could be extracted.
     */
    public static Entry<Enchantment, Integer> parseLoreLine(String s) {
        String endCode = enchCode;
        int power;
        String[] split = s.split(" ");
        if (s.endsWith(endCode)) {
            String lastWord = split[split.length - 1];
            lastWord = lastWord.substring(0, lastWord.length() - endCode.length());
            power = parseRomanNumeral(lastWord);
            if (power == -1) power = 1;
            for (CustomEnch ce : CustomEnch.values()) {
                if (s.startsWith(ce.getLoreName())) {
                    return new AbstractMap.SimpleEntry<>(ce, power);
                }
            }
        }
        return null;
    }

    /**
     * Gets the rarity of the specified enchantment.
     *
     * @param ench The enchantment to test
     * @return Rarity of the enchantment; <code>null</code> if the enchantment is unknown.
     */
    public static Rarity getRarity(Enchantment ench) {
        int mcVersion = EnchantmentCore.getInstance().getGenerator().getMinecraftVersion();
        final List<Enchantment> COMMON;
        final List<Enchantment> UNCOMMON;
        final List<Enchantment> RARE;
        final List<Enchantment> VERY_RARE;
        if (mcVersion >= 16) {
            COMMON = Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DAMAGE_ALL, Enchantment.DIG_SPEED, Enchantment.ARROW_DAMAGE, Enchantment.PIERCING);
            UNCOMMON = Arrays.asList(Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_FALL, Enchantment.PROTECTION_PROJECTILE, Enchantment.DAMAGE_UNDEAD, Enchantment.DAMAGE_ARTHROPODS, Enchantment.KNOCKBACK, Enchantment.DURABILITY, Enchantment.LOYALTY, Enchantment.QUICK_CHARGE);
            RARE = Arrays.asList(Enchantment.PROTECTION_EXPLOSIONS, Enchantment.OXYGEN, Enchantment.WATER_WORKER, Enchantment.DEPTH_STRIDER, Enchantment.FROST_WALKER, Enchantment.FIRE_ASPECT, Enchantment.LOOT_BONUS_MOBS, Enchantment.SWEEPING_EDGE, Enchantment.LOOT_BONUS_BLOCKS, Enchantment.ARROW_KNOCKBACK, Enchantment.ARROW_FIRE, Enchantment.LUCK, Enchantment.LURE, Enchantment.IMPALING, Enchantment.RIPTIDE, Enchantment.MULTISHOT, Enchantment.MENDING);
            VERY_RARE = Arrays.asList(Enchantment.THORNS, Enchantment.BINDING_CURSE, Enchantment.SILK_TOUCH, Enchantment.ARROW_INFINITE, Enchantment.CHANNELING, Enchantment.VANISHING_CURSE, Enchantment.SOUL_SPEED);
        } else if (mcVersion >= 14) {
            COMMON = Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DAMAGE_ALL, Enchantment.DIG_SPEED, Enchantment.ARROW_DAMAGE, Enchantment.PIERCING);
            UNCOMMON = Arrays.asList(Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_FALL, Enchantment.PROTECTION_PROJECTILE, Enchantment.DAMAGE_UNDEAD, Enchantment.DAMAGE_ARTHROPODS, Enchantment.KNOCKBACK, Enchantment.DURABILITY, Enchantment.LOYALTY, Enchantment.QUICK_CHARGE);
            RARE = Arrays.asList(Enchantment.PROTECTION_EXPLOSIONS, Enchantment.OXYGEN, Enchantment.WATER_WORKER, Enchantment.DEPTH_STRIDER, Enchantment.FROST_WALKER, Enchantment.FIRE_ASPECT, Enchantment.LOOT_BONUS_MOBS, Enchantment.SWEEPING_EDGE, Enchantment.LOOT_BONUS_BLOCKS, Enchantment.ARROW_KNOCKBACK, Enchantment.ARROW_FIRE, Enchantment.LUCK, Enchantment.LURE, Enchantment.IMPALING, Enchantment.RIPTIDE, Enchantment.MULTISHOT, Enchantment.MENDING);
            VERY_RARE = Arrays.asList(Enchantment.THORNS, Enchantment.BINDING_CURSE, Enchantment.SILK_TOUCH, Enchantment.ARROW_INFINITE, Enchantment.CHANNELING, Enchantment.VANISHING_CURSE);
        } else {
            COMMON = Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DAMAGE_ALL, Enchantment.DIG_SPEED, Enchantment.ARROW_DAMAGE);
            UNCOMMON = Arrays.asList(Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_FALL, Enchantment.PROTECTION_PROJECTILE, Enchantment.DAMAGE_UNDEAD, Enchantment.DAMAGE_ARTHROPODS, Enchantment.KNOCKBACK, Enchantment.DURABILITY, Enchantment.LOYALTY);
            RARE = Arrays.asList(Enchantment.PROTECTION_EXPLOSIONS, Enchantment.OXYGEN, Enchantment.WATER_WORKER, Enchantment.DEPTH_STRIDER, Enchantment.FROST_WALKER, Enchantment.FIRE_ASPECT, Enchantment.LOOT_BONUS_MOBS, Enchantment.SWEEPING_EDGE, Enchantment.LOOT_BONUS_BLOCKS, Enchantment.ARROW_KNOCKBACK, Enchantment.ARROW_FIRE, Enchantment.LUCK, Enchantment.LURE, Enchantment.IMPALING, Enchantment.RIPTIDE, Enchantment.MENDING);
            VERY_RARE = Arrays.asList(Enchantment.THORNS, Enchantment.BINDING_CURSE, Enchantment.SILK_TOUCH, Enchantment.ARROW_INFINITE, Enchantment.CHANNELING, Enchantment.VANISHING_CURSE);
        }
        if (ench instanceof CustomEnch) {
            Rarity r = ((CustomEnch) ench).getRarity();
            return r == null ? Rarity.UNFINDABLE : r;
        } else if (COMMON.contains(ench)) {
            return Rarity.COMMON;
        } else if (UNCOMMON.contains(ench)) {
            return Rarity.UNCOMMON;
        } else if (RARE.contains(ench)) {
            return Rarity.RARE;
        } else if (VERY_RARE.contains(ench)) {
            return Rarity.VERY_RARE;
        } else {
            return null;
        }
    }

    public static int getAnvilItemMultiplier(Enchantment ench) {
        return (int) Math.pow(2, getRarity(ench).ordinal());
    }

    public static int getAnvilBookMultiplier(Enchantment ench) {
        return (int) Math.ceil(Math.pow(2, getRarity(ench).ordinal() - 1));
    }

    /**
     * <p>Gets the modified minimum enchantment level that can produce the specified enchantment at a given power.</p>
     *
     * @param ench the enchantment to test
     * @param lvl  The power of the enchantment
     * @return The modified minimum enchantment level for the given power
     */
    public static int getMinModifiedLevel(Enchantment ench, int lvl) {
        if (ench instanceof CustomEnch)
            return ((CustomEnch) ench).getModifiedMin(lvl);
        EnchConstants ec = EnchConstants.fromEnch(ench);
        return ec.minB() + (lvl - 1) * ec.minM();
    }

    /**
     * <p>Gets the modified maximum enchantment level that can produce the specified enchantment at a given power.</p>
     *
     * @param ench The enchantment to test
     * @param lvl  The power of the enchantment
     * @return The modified maximum enchantment level for the given power
     */
    public static int getMaxModifiedLevel(Enchantment ench, int lvl) {
        if (ench instanceof CustomEnch)
            return ((CustomEnch) ench).getModifiedMax(lvl);
        EnchConstants ec = EnchConstants.fromEnch(ench);
        return ec.maxB() + (lvl - 1) * ec.maxM();
    }

    /**
     * Gets a map of all custom enchantments on the specified item.
     *
     * @param item The item to test
     * @return Map of all custom enchantments on the item
     */
    public static Map<Enchantment, Integer> getCustomEnchants(ItemStack item) {
        Map<Enchantment, Integer> result = new HashMap<>();
        Map<Enchantment, Integer> current = item.getEnchantments();
        for (Entry<Enchantment, Integer> ench : current.entrySet()) {
            if (ench.getKey() instanceof CustomEnch) {
                result.put(ench.getKey(), ench.getValue());
            }
        }
        return result;
    }

    /**
     * Gets a map of all vanilla (non-custom) enchantments on the specified item.
     *
     * @param item The item to test
     * @return Map of all non-custom enchantments on the item
     */
    public static Map<Enchantment, Integer> getVanillaEnchants(ItemStack item) {
        Map<Enchantment, Integer> result = new HashMap<>();
        Map<Enchantment, Integer> current = item.getEnchantments();
        for (Entry<Enchantment, Integer> ench : current.entrySet()) {
            if (!(ench.getKey() instanceof CustomEnch)) {
                result.put(ench.getKey(), ench.getValue());
            }
        }
        return result;
    }

    /**
     * Removes all enchantments from the specified item meta.
     *
     * @param meta The meta to remove from
     */
    public static void removeAllEnchantments(ItemMeta meta) {
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta eMeta = (EnchantmentStorageMeta) meta;
            for (Entry<Enchantment, Integer> e : eMeta.getStoredEnchants().entrySet())
                eMeta.removeStoredEnchant(e.getKey());
        } else {
            for (Entry<Enchantment, Integer> e : meta.getEnchants().entrySet())
                meta.removeEnchant(e.getKey());
        }
    }

    /**
     * Adds the specified map of enchantments to the specified item meta.
     *
     * @param meta                   The meta to add to
     * @param enchs                  The map of enchantments to add
     * @param ignoreLevelRestriction Indicates that level restriction on enchantments should be ignored
     */
    public static void addEnchantments(ItemMeta meta, Map<Enchantment, Integer> enchs, boolean ignoreLevelRestriction) {
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta eMeta = (EnchantmentStorageMeta) meta;
            for (Entry<Enchantment, Integer> e : enchs.entrySet()) {
                if (!ignoreLevelRestriction && e.getValue() > e.getKey().getMaxLevel())
                    eMeta.addStoredEnchant(e.getKey(), e.getKey().getMaxLevel(), false);
                else
                    eMeta.addStoredEnchant(e.getKey(), e.getValue(), ignoreLevelRestriction);
            }
        } else {
            for (Entry<Enchantment, Integer> e : enchs.entrySet()) {
                if (!ignoreLevelRestriction && e.getValue() > e.getKey().getMaxLevel())
                    meta.addEnchant(e.getKey(), e.getKey().getMaxLevel(), false);
                else
                    meta.addEnchant(e.getKey(), e.getValue(), ignoreLevelRestriction);
            }
        }
    }

    /**
     * Retrieves the <code>EnchantmentGenerator</code> for the current plugin instance.
     *
     * @return The current <code>EnchantmentGenerator</code>
     */
    public static EnchantmentGenerator getGenerator() {
        return EnchantmentCore.getInstance().getGenerator();

    }

    /**
     * Combines multiple maps of enchantments for the specified item.
     *
     * @param itemToEnchant The item to combine enchantments for
     * @param allowIllegal  Indicates that incompatible or conflicting enchantments can be combined and added
     * @param maps          Array of multiple maps to combine
     * @return Combined map of enchantments
     */
    @SafeVarargs
    public static Map<Enchantment, Integer> combineEnchants(ItemStack itemToEnchant, boolean allowIllegal, Map<Enchantment, Integer>... maps) {
        Map<Enchantment, Integer> combined = new HashMap<>();
        for (Map<Enchantment, Integer> map : maps) {
            initialEntryLoop:
            for (Entry<Enchantment, Integer> entry : map.entrySet()) {
                for (Entry<Enchantment, Integer> combinedEntry : combined.entrySet()) {
                    if (!allowIllegal && combinedEntry.getKey().conflictsWith(entry.getKey()))
                        continue initialEntryLoop;
                }
                if (!(itemToEnchant.getItemMeta() instanceof EnchantmentStorageMeta) && (!allowIllegal && !entry.getKey().canEnchantItem(itemToEnchant)))
                    continue initialEntryLoop;
                else if (!combined.containsKey(entry.getKey()) || combined.get(entry.getKey()) < entry.getValue()) {
                    combined.put(entry.getKey(), entry.getValue());
                } else if (combined.get(entry.getKey()).equals(entry.getValue()) && entry.getValue() < entry.getKey().getMaxLevel()) {
                    combined.put(entry.getKey(), entry.getValue() + 1);
                }
            }
        }
        return combined;
    }

    /**
     * Returns all valid sacrifice enchantments for anvil combinations for the given item.
     *
     * @param itemToEnchant The result item of the anvil
     * @param allowIllegal  Indicates that incompatible or illegal enchantments are valid
     * @param target        The map of all enchantments on the first item in the anvil
     * @param sacrifice     The map of all enchantments on the second item in the anvil
     * @return Map of all valid enchantments on the second item in the anvil
     */
    public static Map<Enchantment, Integer> getValidSacrificeEnchants(ItemStack itemToEnchant, boolean allowIllegal, Map<Enchantment, Integer> target, Map<Enchantment, Integer> sacrifice) {
        Map<Enchantment, Integer> result = new HashMap<>();
        sacrificeLoop:
        for (Entry<Enchantment, Integer> sacrificeEntry : sacrifice.entrySet()) {
            if (!allowIllegal && (!(itemToEnchant.getItemMeta() instanceof EnchantmentStorageMeta) && !sacrificeEntry.getKey().canEnchantItem(itemToEnchant)))
                continue sacrificeLoop;
            for (Entry<Enchantment, Integer> targetEntry : target.entrySet()) {
                if (!allowIllegal && (targetEntry.getKey().conflictsWith(sacrificeEntry.getKey())))
                    continue sacrificeLoop;
            }
            result.put(sacrificeEntry.getKey(), sacrificeEntry.getValue());
        }
        return result;
    }

    /**
     * <p>Gets the durability amount that can be repaired given a tiered item and a stack of its repair material.</p>
     * <p>For example, the tiered item type <code>IRON_PICKAXE</code> has the repair material <code>IRON_INGOT</code>.</p>
     *
     * @param target The tiered item to be tested
     * @param item   The stack of the target's repair material
     * @return The amount of durability that can be repaired with the given repair material
     */
    public static int getRepairAmount(ItemStack target, ItemStack item) {
        Material targetType = target.getType();
        Material itemType = item.getType();
        if (!(target.getItemMeta() instanceof Damageable)) return 0;
        if (targetType == itemType) {
            Damageable itemMeta = (Damageable) item.getItemMeta();
            return (int) Math.floor(targetType.getMaxDurability() * 1.12) - itemMeta.getDamage(); // repairs by second item amount + 12% of max durability
        } else if (MaterialType.fromMaterial(targetType).canRepairWith(itemType)) {
            return (int) Math.floor(targetType.getMaxDurability() * 0.25 * item.getAmount()); // repairs by 25% per valid repair item
        }
        return 0;
    }

    /**
     * Gets the number of times a tiered item can be repaired before its durability is full.
     *
     * @param target The tiered item to be tested
     * @param item   The stack of the target's repair material
     * @return The number of times the durability can be repaired with the given repair material
     */
    public static int getUnitRepairNumber(ItemStack target, ItemStack item) {
        Material targetType = target.getType();
        Material itemType = item.getType();
        if (MaterialType.fromMaterial(targetType).canRepairWith(itemType)) {
            int damage = ((Damageable) target.getItemMeta()).getDamage();
            int i = 0;
            while (damage > 0) {
                damage -= targetType.getMaxDurability() * 0.25;
                i++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Gets the anvil uses of the specified item.
     *
     * @param item The item to test
     * @return The anvil use number of the item
     */
    public static int getAnvilUses(ItemStack item) {
        try {
            Object nmsItem = getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            Object nbtTag = nmsItem.getClass().getMethod("getTag").invoke(nmsItem);
            if (nbtTag != null && (boolean) nbtTag.getClass().getMethod("hasKey", String.class).invoke(nbtTag, "AnvilUses")) {
                return (int) nbtTag.getClass().getMethod("getInt", String.class).invoke(nbtTag, "AnvilUses");
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Gets the anvil cost of the specified item.
     *
     * @param item The item to test
     * @return The anvil cost of the item
     */
    public static int getAnvilCost(ItemStack item) {
        return (int) Math.pow(2, getAnvilUses(item)) - 1;
    }

    /**
     * Sets the number of anvil uses to the specified number on a copy of the given item.
     *
     * @param item The item to test
     * @param num  Number to set to
     * @return A copy of the item with the modified data.
     */
    public static ItemStack setAnvilUses(ItemStack item, int num) {
        try {
            Object nmsItem = getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            Object nbtTag = nmsItem.getClass().getMethod("getTag").invoke(nmsItem);
            if (nbtTag != null) {
                nbtTag.getClass().getMethod("setInt", String.class, int.class).invoke(nbtTag, "AnvilUses", num);
                nmsItem.getClass().getMethod("setTag", nbtTag.getClass()).invoke(nmsItem, nbtTag);
                return (ItemStack) getCraftBukkitClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItem.getClass()).invoke(null, nmsItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item.clone();
    }

    /**
     * Gets the section of configuration containing enchantment generator settings.
     *
     * @return The enchantment generator configuration section
     */
    public static ConfigurationSection generatorSettings() {
        return EnchantmentCore.getInstance().getConfig().getConfigurationSection("enchantment-generator");
    }
}
