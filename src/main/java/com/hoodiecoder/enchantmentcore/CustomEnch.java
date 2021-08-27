package com.hoodiecoder.enchantmentcore;

import com.hoodiecoder.enchantmentcore.utils.EnchEnums.Rarity;
import com.hoodiecoder.enchantmentcore.utils.EnchantmentUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>Represents a custom enchantment handled by ZEnchantmentCore.</p>
 * <p>This class can be extended to create a new custom enchantment to be handled by ZEnchantmentCore.
 * The superconstructor must be passed an {@link EnchantmentHolder}, which holds the enchantment before
 * it is registered in minecraft, and a <code>String</code> representing the internal ID of the enchantment.</p>
 *
 * @see EnchantmentHolder
 */
public abstract class CustomEnch extends Enchantment {
    private static final Field byKeyField;
    private static final Field byNameField;

    private static final List<CustomEnch> pendingEnchants = new ArrayList<>();
    private static final Map<NamespacedKey, CustomEnch> byKey = new LinkedHashMap<>();
    private static final Map<String, CustomEnch> byName = new LinkedHashMap<>();
    private static int nextID = 0;

    static {
        Field acceptingNew;
        Field keyField;
        Field nameField;

        try {
            acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);
            keyField = Enchantment.class.getDeclaredField("byKey");
            keyField.setAccessible(true);
            nameField = Enchantment.class.getDeclaredField("byName");
            nameField.setAccessible(true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

        byKeyField = keyField;
        byNameField = nameField;
    }

    private final int coreID;
    private final Plugin ownerPlugin;
    private boolean disabled = false;

    /**
     * <p>Creates a new instance of a <code>CustomEnch</code> with an {@link EnchantmentHolder} and the identifier of the enchantment.</p>
     *
     * @param holder     the <code>EnchantmentHolder</code> for the enchantment
     * @param identifier the internal ID of the enchantment
     */
    public CustomEnch(EnchantmentHolder holder, String identifier) {
        super(new NamespacedKey(holder.getOwnerPlugin(), identifier));
        coreID = nextID;
        nextID++;
        ownerPlugin = holder.getOwnerPlugin();
        holder.addEnchant(this);
    }

    static void batchRegister() {
        pendingEnchants.sort(Comparator.comparingInt(CustomEnch::getPriority));
        Collections.reverse(pendingEnchants);
        for (CustomEnch ce : pendingEnchants) {
            ce.registerEnchantment();
        }
    }

    /**
     * Gets all currently disabled custom enchantment names.
     *
     * @return List containing disabled enchantment names
     */
    public static List<String> getDisabledEnchants() {
        return EnchantmentCore.getInstance().getConfig().getStringList("disabled-enchantments");
    }

    /**
     * <p>Gets the registered <code>CustomEnch</code> with the specified key.</p>
     *
     * @param key The key assigned to the enchantment
     * @return The <code>CustomEnch</code> object with the given key, or <code>null</code> if none are registered with that key.
     * @see #getKey()
     */
    public static CustomEnch getByKey(NamespacedKey key) {
        return byKey.get(key);
    }

    /**
     * <p>Gets the registered <code>CustomEnch</code> with the specified internal name.</p>
     *
     * @param name The internal name assigned to the enchantment
     * @return The <code>CustomEnch</code> object with the given name, or <code>null</code> if none are registered with that name.
     */
    @Deprecated
    public static CustomEnch getByName(String name) {
        return byName.get(name);
    }

    /**
     * <p>Returns all registered custom enchantments as an array.</p>
     *
     * @return Array of all registered custom enchantments
     */
    public static CustomEnch[] values() {
        return byKey.values().toArray(new CustomEnch[0]);
    }

    /**
     * <p>Returns all registered and unregistered custom enchantments as an array.</p>
     *
     * @return Array of all custom enchantments
     */
    public static CustomEnch[] allValues() {
        return pendingEnchants.toArray(new CustomEnch[0]);
    }

    void registerEnchantment() {
        setDisabled(!getDisabledEnchants().isEmpty() && getDisabledEnchants().contains(getKey().toString().toLowerCase()));
        if (!isDisabled() && !byKey.containsKey(super.getKey())) {
            byKey.put(super.getKey(), this);
            byName.put(getName(), this);
            try {
                Enchantment.registerEnchantment(this);
            } catch (IllegalArgumentException e) {
                EnchantmentCore.getInstance().getLogger().warning("Enchantment " + getKey() + " skipped because it has already been registered by a third-party plugin.");
            }
        }
    }

    @SuppressWarnings("unchecked")
    void unregisterEnchantment() {
        if (byKey.containsKey(getKey())) {
            byKey.remove(getKey());
            byName.remove(getName());
            try {
                Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);
                Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
                byKey.remove(getKey());
                byName.remove(getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void addToPending() {
        pendingEnchants.add(this);
    }

    /**
     * Gets the plugin that is responsible for this enchantment.
     *
     * @return Owner plugin
     */
    public Plugin getOwnerPlugin() {
        return ownerPlugin;
    }

    /**
     * <p>Returns the priority of the enchantment, starting at zero (higher is more important).</p>
     * <p>Enchantments with higher priorities will be loaded before enchantments with lower priorities.</p>
     *
     * @return The priority of the enchantment
     */
    public int getPriority() {
        return 0;
    }

    /**
     * <p>Gets the numeric ID of the custom enchantment, starting at 0. <br>The order of the IDs depend on the order of instantiation of the enchantments.</p>
     *
     * @return The numeric ID of the enchantment
     */
    public int getCoreID() {
        return coreID;
    }

    /**
     * <p>Checks if the enchantment is disabled.</p>
     *
     * @return <code>true</code> if disabled; <code>false</code> otherwise.
     */
    public boolean isDisabled() {
        return disabled;
    }

    private void setDisabled(boolean d) {
        disabled = d;
    }

    /**
     * <p>Gets the rarity of the enchantment for use in enchantment generation.</p>
     *
     * @return The rarity of the enchantment
     * @see Rarity
     */
    public abstract Rarity getEnchantmentRarity();

    /**
     * <p>Gets the list of applicable equipment slots for this enchantment.</p>
     * <p>The equipment slot is dependent on the <code>EnchantmentTarget</code> of the enchantment.</p>
     *
     * @return List containing all applicable equipment slots
     * @see #getItemTarget()
     */
    public final Set<EquipmentSlot> getEquipmentSlot() {
        EquipmentSlot[] slots;
        switch (getItemTarget()) {
            case ARMOR:
            case WEARABLE:
                slots = new EquipmentSlot[]{EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.HEAD};
                break;
            case ARMOR_TORSO:
                slots = new EquipmentSlot[]{EquipmentSlot.CHEST};
                break;
            case ARMOR_FEET:
                slots = new EquipmentSlot[]{EquipmentSlot.FEET};
                break;
            case ARMOR_HEAD:
                slots = new EquipmentSlot[]{EquipmentSlot.HEAD};
                break;
            case ARMOR_LEGS:
                slots = new EquipmentSlot[]{EquipmentSlot.LEGS};
                break;
            case BOW:
            case BREAKABLE:
            case CROSSBOW:
            case TOOL:
            case FISHING_ROD:
            case TRIDENT:
            case VANISHABLE:
            case WEAPON:
            default:
                slots = new EquipmentSlot[]{EquipmentSlot.HAND};
                break;
        }
        return new HashSet<>(Arrays.asList(slots));
    }

    /**
     * <p>Gets the modified minimum enchantment level that can produce this enchantment at a given power, for use in the enchantment algorithm.</p>
     *
     * @param enchLevel The power of the enchantment
     * @return The modified minimum enchantment level for the given power
     */
    public int getModifiedMin(int enchLevel) {
        return 1 + enchLevel * 10;
    }

    /**
     * <p>Gets the modified maximum enchantment level that can produce this enchantment at a given power, for use in the enchantment algorithm.</p>
     *
     * @param enchLevel The power of the enchantment
     * @return The modified maximum enchantment level for the given power
     */
    public int getModifiedMax(int enchLevel) {
        return getModifiedMin(enchLevel) + 5;
    }

    /**
     * <p>Gets the internal name of this enchantment in upper case.</p>
     * <p>The name of the enchantment in normal case is equal to <code>{@link #getKey()}.getKey()</code>.</p>
     *
     * @return The internal name of the enchantment
     * @see #getKey()
     */
    @Override
    public String getName() {
        return getKey().getKey().toUpperCase();
    }

    /**
     * <p>Gets the starting level of this enchantment. Should always return <code>0</code>.</p>
     *
     * @return The starting level of the enchantment
     */
    @Override
    public final int getStartLevel() {
        return 0;
    }

    /**
     * <p>Determines whether or not the enchantment is lenient in its enchanting capability.</p>
     * <p>This affects the following conditions:</p>
     * <ul>
     * <li>If <code>{@link #getItemTarget()} == WEAPON</code>, whether it will enchant axes by default or not</li>
     * </ul>
     *
     * @return <code>true</code> if the enchantment conditions are lenient; <code>false</code> otherwise.
     * @see #getItemTarget()
     * @see #canEnchantItem(ItemStack)
     */
    public boolean isLenient() {
        return true;
    }

    /**
     * <p>Determines whether or not this enchantment can be applied to the given item.</p>
     *
     * @param stack The item to be tested
     * @return <code>true</code> if the enchantment can be applied to the item; <code>false</code> otherwise.
     */
    @Override
    public boolean canEnchantItem(ItemStack stack) {
        if (getItemTarget() == EnchantmentTarget.WEAPON && (isLenient() || EnchantmentUtils.generatorSettings().getBoolean("treat-axes-as-weapons"))) {
            return getItemTarget().includes(stack) || stack.getType().toString().endsWith("_AXE");
        }
        return getItemTarget().includes(stack);
    }

    /**
     * <p>Checks if this enchantment conflicts with another specified enchantment.</p>
     * <p>This method is used in enchantment algorithms and cannot be overridden, unlike {@link #isCompatibleWith(Enchantment)}.</p>
     *
     * @param other The enchantment to check against
     * @return <code>true</code> if there is a conflict; <code>false</code> otherwise.
     * @see #isCompatibleWith(Enchantment)
     */
    @Override
    public final boolean conflictsWith(Enchantment other) {
        if (other instanceof CustomEnch)
            return !isCompatibleWith(other) || !((CustomEnch) other).isCompatibleWith(this);
        else
            return !isCompatibleWith(other);
    }

    /**
     * <p>Checks if this enchantment is compatible with another specified enchantment.</p>
     *
     * @param other The enchantment to check against
     * @return <code>true</code> if the enchantments are compatible; <code>false</code> otherwise.
     */
    public boolean isCompatibleWith(Enchantment other) {
        return true;
    }

    /**
     * <p>Checks if this enchantment is a cursed enchantment.</p>
     * <p>A cursed enchantment will show up as red in the enchantment list rather than gray.</p>
     *
     * @return <code>true</code> if the enchantment is cursed; <code>false</code> otherwise.
     */
    @Override
    public boolean isCursed() {
        return false;
    }

    /**
     * <p>Checks if this enchantment is a treasure enchantment.</p>
     * <p>A treasure enchantment will only generate in loot tables and won't appear in enchantment tables by default.</p>
     *
     * @return <code>true</code> if the enchantment is a treasure enchantment; <code>false</code> otherwise.
     */
    @Override
    public boolean isTreasure() {
        return false;
    }

    /**
     * <p>Gets the plain display name of this enchantment.</p>
     *
     * @return The display name of the enchantment
     * @see #getDisplayName()
     */
    public abstract String getDisplayName();
}
