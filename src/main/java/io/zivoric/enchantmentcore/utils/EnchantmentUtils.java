package io.zivoric.enchantmentcore.utils;

import io.zivoric.enchantmentcore.CustomEnch;
import io.zivoric.enchantmentcore.EnchantmentCore;
import io.zivoric.enchantmentcore.EnchantmentGenerator;
import io.zivoric.enchantmentcore.utils.lore.DefaultLoreHandler;
import io.zivoric.enchantmentcore.utils.lore.LoreHandler;
import io.zivoric.enchantmentcore.utils.lore.PaperLoreHandler;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class containing several useful methods for using the plugin, as well as manipulating and working with enchantments.
 */
public class EnchantmentUtils {
    /**
     * The list of registered lore handlers. Can be modified
     */
    public static final LinkedList<LoreHandler> LORE_HANDLER_LIST = new LinkedList<>();

    private static final List<Enchantment> COMMON;
    private static final List<Enchantment> UNCOMMON;
    private static final List<Enchantment> RARE;
    private static final List<Enchantment> VERY_RARE;

    static {
        int mcVersion = VersionUtils.SERVER_VERSION;
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

        LORE_HANDLER_LIST.addFirst(new DefaultLoreHandler());
        if (VersionUtils.BUKKIT_TYPE == BukkitType.PAPER) {
            LORE_HANDLER_LIST.addFirst(new PaperLoreHandler());
        }
    }

    /**
     * Update the lore of the item meta
     *
     * @param meta the item meta
     */
    public static void updateItemLore(ItemMeta meta) {
        if (meta == null) {
            return;
        }

        // Load enchants
        Map<Enchantment, Integer> enchs;
        if (meta instanceof EnchantmentStorageMeta) {
            enchs = ((EnchantmentStorageMeta) meta).getStoredEnchants();
        } else {
            enchs = meta.getEnchants();
        }

        // Filter the custom enchantments
        Map<CustomEnch, Integer> customEnchs = new HashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : enchs.entrySet()) {
            if (entry.getKey() instanceof CustomEnch) {
                customEnchs.put((CustomEnch) entry.getKey(), entry.getValue());
            }
        }

        // Update the lore
        for (LoreHandler loreHandler : LORE_HANDLER_LIST) {
            customEnchs = loreHandler.updateItemLore(meta, customEnchs);
        }
    }

    /**
     * Gets the rarity of the specified enchantment.
     *
     * @param ench The enchantment to test
     * @return Rarity of the enchantment; <code>null</code> if the enchantment is unknown.
     */
    public static EnchEnums.Rarity getRarity(Enchantment ench) {
        if (ench instanceof CustomEnch) {
            EnchEnums.Rarity r = ((CustomEnch) ench).getEnchantmentRarity();
            return r == null ? EnchEnums.Rarity.UNFINDABLE : r;
        } else if (COMMON.contains(ench)) {
            return EnchEnums.Rarity.COMMON;
        } else if (UNCOMMON.contains(ench)) {
            return EnchEnums.Rarity.UNCOMMON;
        } else if (RARE.contains(ench)) {
            return EnchEnums.Rarity.RARE;
        } else if (VERY_RARE.contains(ench)) {
            return EnchEnums.Rarity.VERY_RARE;
        } else {
            return EnchEnums.Rarity.UNFINDABLE;
        }
    }

    public static int getAnvilItemMultiplier(Enchantment ench) {
        try {
            return (int) Math.pow(2, getRarity(ench).ordinal());
        } catch (NullPointerException e) {
            return 1;
        }
    }

    public static int getAnvilBookMultiplier(Enchantment ench) {
        try {
            return (int) Math.ceil(Math.pow(2, getRarity(ench).ordinal() - 1D));
        } catch (NullPointerException e) {
            return 1;
        }
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
        EnchEnums.EnchConstants ec = EnchEnums.EnchConstants.fromEnch(ench);
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
        EnchEnums.EnchConstants ec = EnchEnums.EnchConstants.fromEnch(ench);
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
                if (!ignoreLevelRestriction)
                    eMeta.addStoredEnchant(e.getKey(), Math.min(e.getKey().getMaxLevel(), e.getValue()), false);
                else
                    eMeta.addStoredEnchant(e.getKey(), e.getValue(), true);
            }
        } else {
            for (Entry<Enchantment, Integer> e : enchs.entrySet()) {
                if (!ignoreLevelRestriction)
                    meta.addEnchant(e.getKey(), Math.min(e.getKey().getMaxLevel(), e.getValue()), false);
                else
                    meta.addEnchant(e.getKey(), e.getValue(), true);
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
            map.forEach((enchant, level) -> {
                for (Entry<Enchantment, Integer> combinedEntry : combined.entrySet()) {
                    if (!allowIllegal && combinedEntry.getKey().conflictsWith(enchant))
                        return;
                }
                if (!(itemToEnchant.getItemMeta() instanceof EnchantmentStorageMeta) && (!allowIllegal && !enchant.canEnchantItem(itemToEnchant))) {
                    return;
                }
                if (!combined.containsKey(enchant) || combined.get(enchant) < level) {
                    combined.put(enchant, level);
                } else if (combined.get(enchant).equals(level) && level < enchant.getMaxLevel()) {
                    combined.put(enchant, level + 1);
                }
            });
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
        sacrifice.forEach((enchant, level) -> {
            if (!allowIllegal && (!(itemToEnchant.getItemMeta() instanceof EnchantmentStorageMeta) && !enchant.canEnchantItem(itemToEnchant)))
                return;
            for (Entry<Enchantment, Integer> targetEntry : target.entrySet()) {
                if (!allowIllegal && targetEntry.getKey().conflictsWith(enchant))
                    return;
            }
            result.put(enchant, level);
        });
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
        } else if (EnchEnums.MaterialType.fromMaterial(targetType).canRepairWith(itemType)) {
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
        if (EnchEnums.MaterialType.fromMaterial(targetType).canRepairWith(itemType)) {
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
        return (int) (Math.log(getAnvilCost(item) + 1) / Math.log(2));
    }

    /**
     * Gets the anvil cost of the specified item.
     *
     * @param item The item to test
     * @return The anvil cost of the item
     */
    public static int getAnvilCost(ItemStack item) {
        try {
            Object nmsItem = VersionUtils.getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            return (int) nmsItem.getClass().getMethod("getRepairCost").invoke(nmsItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Sets the number of anvil uses to the specified number on a copy of the given item.
     *
     * @param item The item to test
     * @param num  Number to set to
     * @return A copy of the item with the modified data.
     */
    public static ItemStack setAnvilUses(ItemStack item, int num) {
        return setAnvilCost(item, (int) Math.pow(2, num) - 1);
    }

    /**
     * Sets the anvil cost to the specified number on a copy of the given item.
     *
     * @param item The item to test
     * @param num  Cost to set to
     * @return A copy of the item with the modified data.
     */
    public static ItemStack setAnvilCost(ItemStack item, int num) {
        try {
            Object nmsItem = VersionUtils.getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            nmsItem.getClass().getMethod("setRepairCost", int.class).invoke(nmsItem, num);
            return (ItemStack) VersionUtils.getCraftBukkitClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItem.getClass()).invoke(null, nmsItem);
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

    /**
     * Execute the enchantment event handler from the equipment of the entity
     *
     * @param livingEntity    the entity
     * @param handlerClass    the handler class
     * @param handlerConsumer the consumer contains an instance of the handler, a list of levels and a list of applicable items
     * @param <T>             the type of the handler
     */
    public static <T> void executeEnchantEvent(LivingEntity livingEntity, Class<T> handlerClass, TriConsumer<T, List<Integer>, List<ItemStack>> handlerConsumer) {
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        if (entityEquipment == null) {
            return;
        }
        executeEnchantEvent(customEnch -> {
            ItemStack[] applicableItems = new ItemStack[0];
            if (customEnch.isDisabled()) return applicableItems;
            switch (customEnch.getItemTarget()) {
                case ARMOR_FEET:
                    ItemStack feet = entityEquipment.getBoots();
                    if (feet != null && feet.getItemMeta() != null && customEnch.canEnchantItem(feet) && feet.getItemMeta().hasEnchant(customEnch)) {
                        applicableItems = new ItemStack[]{feet};
                    }
                    break;
                case ARMOR_HEAD:
                    ItemStack head = entityEquipment.getHelmet();
                    if (head != null && head.getItemMeta() != null && customEnch.canEnchantItem(head) && head.getItemMeta().hasEnchant(customEnch)) {
                        applicableItems = new ItemStack[]{head};
                    }
                    break;
                case ARMOR_LEGS:
                    ItemStack legs = entityEquipment.getLeggings();
                    if (legs != null && legs.getItemMeta() != null && customEnch.canEnchantItem(legs) && legs.getItemMeta().hasEnchant(customEnch)) {
                        applicableItems = new ItemStack[]{legs};
                    }
                    break;
                case ARMOR_TORSO:
                    ItemStack chest = entityEquipment.getChestplate();
                    if (chest != null && chest.getItemMeta() != null && customEnch.canEnchantItem(chest) && chest.getItemMeta().hasEnchant(customEnch)) {
                        applicableItems = new ItemStack[]{chest};
                    }
                    break;
                case ARMOR:
                case WEARABLE:
                    ItemStack[] armorArr = entityEquipment.getArmorContents();
                    List<ItemStack> armor = new ArrayList<>();
                    for (ItemStack a : armorArr) {
                        if (a != null && a.getItemMeta() != null && a.getItemMeta().hasEnchant(customEnch) && customEnch.canEnchantItem(a)) {
                            armor.add(a);
                        }
                    }
                    if (!armor.isEmpty()) {
                        applicableItems = armor.toArray(new ItemStack[0]);
                    }
                    break;
                case BOW:
                case CROSSBOW:
                case FISHING_ROD:
                case TOOL:
                case TRIDENT:
                case WEAPON:
                case BREAKABLE:
                    List<ItemStack> itemS = new ArrayList<>();
                    ItemStack itemMain = entityEquipment.getItemInMainHand();
                    ItemStack itemOff = entityEquipment.getItemInOffHand();
                    if (itemMain.getItemMeta() != null && itemMain.getItemMeta().hasEnchant(customEnch) && customEnch.canEnchantItem(itemMain)) {
                        itemS.add(itemMain);
                    }
                    if (itemOff.getItemMeta() != null && itemOff.getItemMeta().hasEnchant(customEnch) && customEnch.canEnchantItem(itemOff)) {
                        itemS.add(itemOff);
                    }
                    if (!itemS.isEmpty()) {
                        applicableItems = itemS.toArray(new ItemStack[0]);
                    }
                    break;
                default:
                    break;
            }
            return applicableItems;
        }, handlerClass, handlerConsumer);
    }

    /**
     * Execute the enchantment event handler, given a list of the enchanted items
     *
     * @param itemStacks      the list of the items
     * @param handlerClass    the handler class
     * @param handlerConsumer the consumer contains an instance of the handler, a list of levels and a list of applicable items
     * @param <T>             the type of the handler
     */
    public static <T> void executeEnchantEvent(List<ItemStack> itemStacks, Class<T> handlerClass, TriConsumer<T, List<Integer>, List<ItemStack>> handlerConsumer) {
        executeEnchantEvent(
                customEnch -> itemStacks.stream()
                        .filter(Objects::nonNull)
                        .filter(itemStack -> itemStack.getItemMeta() != null)
                        .filter(customEnch::canEnchantItem)
                        .filter(itemStack -> itemStack.getItemMeta().hasEnchant(customEnch))
                        .toArray(ItemStack[]::new),
                handlerClass, handlerConsumer
        );
    }

    /**
     * Execute the enchantment event handler, given the applicable item supplier
     *
     * @param applicableItemSupplier the applicable item supplier
     * @param handlerClass           the handler class
     * @param handlerConsumer        the consumer contains an instance of the handler, a list of levels and a list of applicable items
     * @param <T>                    the type of the handler
     */
    private static <T> void executeEnchantEvent(Function<CustomEnch, ItemStack[]> applicableItemSupplier, Class<T> handlerClass, TriConsumer<T, List<Integer>, List<ItemStack>> handlerConsumer) {
        for (CustomEnch customEnch : CustomEnch.values()) {
            if (!handlerClass.isInstance(customEnch)) {
                continue;
            }
            T handler = handlerClass.cast(customEnch);
            ItemStack[] applicableItems = applicableItemSupplier.apply(customEnch);
            if (applicableItems.length > 0) {
                List<ItemStack> items = Arrays.asList(applicableItems);
                List<Integer> levels = items.stream().map(item -> item.getItemMeta().getEnchantLevel(customEnch)).collect(Collectors.toList());
                handlerConsumer.accept(handler, levels, items);
            }
        }
    }
}
