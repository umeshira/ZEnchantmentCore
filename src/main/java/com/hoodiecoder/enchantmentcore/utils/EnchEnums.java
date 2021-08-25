package com.hoodiecoder.enchantmentcore.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bukkit.Material.*;

/**
 * Class containing various enums that ZEnchantmentCore uses.
 */
public class EnchEnums {

    /**
     * Enum representing rarity of enchantments.
     */
    public enum Rarity {
        COMMON(10, "Common"), UNCOMMON(5, "Uncommon"), RARE(2, "Rare"), VERY_RARE(1, "Very rare"), UNFINDABLE(0, "Unfindable");
        private final int weight;
        private final String displayName;

        Rarity(int w, String d) {
            weight = w;
            displayName = d;
        }

        /**
         * Returns the weight of this rarity in the enchantment algorithm.
         *
         * @return Weight of the rarity
         */
        public int weight() {
            return weight;
        }

        /**
         * Returns the display name of this rarity.
         *
         * @return Display name of the rarity
         */
        public String displayName() {
            return displayName;
        }
    }

    /**
     * Enum representing constant numbers for vanilla enchantments for use in the enchantment algorithm.
     */
    public enum EnchConstants {
        PROTECTION(1, 11, 1 + 11, 11), SHARPNESS(1, 11, 1 + 20, 11), FIRE_PROTECTION(10, 8, 10 + 8, 8), FEATHER_FALLING(5, 6, 5 + 6, 6),
        BLAST_PROTECTION(5, 8, 5 + 8, 8), PROJECTILE_PROTECTION(3, 6, 3 + 6, 6), RESPIRATION(10, 10, 10 + 30, 10), AQUA_AFFINITY(1, 0, 1 + 40, 0),
        THORNS(10, 20, 10 + 50, 20), FIRE_ASPECT(10, 20, 10 + 50, 20), DEPTH_STRIDER(0, 10, 15, 10), FROST_WALKER(0, 10, 15, 10), SOUL_SPEED(0, 10, 15, 10),
        BINDING_CURSE(25, 0, 50, 0), SMITE(5, 8, 5 + 20, 8), BANE_OF_ARTHROPODS(5, 8, 5 + 20, 8), KNOCKBACK(5, 20, 5 + 50, 20), LOOTING(15, 9, 15 + 50, 9),
        FORTUNE(15, 9, 15 + 50, 9), SWEEPING(5, 9, 5 + 15, 9), EFFICIENCY(1, 10, 1 + 50, 10), SILK_TOUCH(15, 0, 65, 0), UNBREAKING(5, 8, 55, 8),
        POWER(1, 10, 1 + 15, 10), PUNCH(12, 20, 12 + 25, 20), FLAME(20, 0, 50, 0), INFINITY(20, 0, 50, 0),
        LUCK_OF_THE_SEA(15, 9, 15 + 50, 9), LURE(15, 9, 15 + 50, 9), LOYALTY(5, 7, 5 + 50, 7), IMPALING(1, 8, 1 + 20, 8), RIPTIDE(10, 7, 10 + 50, 7), CHANNELING(25, 0, 50, 0),
        MULTISHOT(20, 0, 50, 0), QUICK_CHARGE(12, 20, 12 + 50, 20), PIERCING(1, 10, 50, 0), MENDING(0, 25, 50, 25), VANISHING_CURSE(25, 0, 50, 0),
        PLACEHOLDER(0, 0, 0, 0);

        private final int minM;
        private final int minB;
        private final int maxM;
        private final int maxB;

        EnchConstants(int minB, int minM, int maxB, int maxM) {
            this.minM = minM;
            this.minB = minB;
            this.maxM = maxM;
            this.maxB = maxB;
        }

        /**
         * Gets the <code>EnchConstants</code> enum from the specified vanilla <code>Enchantment</code> object.
         *
         * @param ench The enchantment to get the enum for
         * @return <code>EnchConstants</code> for the vanilla enchantment; if the enchantment is not a vanilla
         * enchantment, it returns <code>PLACEHOLDER</code>.
         */
        public static EnchConstants fromEnch(Enchantment ench) {
            try {
                return EnchConstants.valueOf(ench.getKey().getKey().toUpperCase());
            } catch (Exception e) {
                return PLACEHOLDER;
            }
        }

        /**
         * Gets the minimum modified enchantment level for this vanilla enchantment.
         *
         * @return Minimum modified enchantment level for the enchantment
         */
        public int minM() {
            return minM;
        }

        /**
         * Gets the minimum base enchantment level for this vanilla enchantment.
         *
         * @return Minimum base enchantment level for the enchantment
         */
        public int minB() {
            return minB;
        }

        /**
         * Gets the maximum modified enchantment level for this vanilla enchantment.
         *
         * @return Maximum modified enchantment level for the enchantment
         */
        public int maxM() {
            return maxM;
        }

        /**
         * Gets the maximum base enchantment level for this vanilla enchantment.
         *
         * @return Maximum base enchantment level for the enchantment
         */
        public int maxB() {
            return maxB;
        }
    }

    /**
     * Enum representing groups of materials and their respective repair materials, if applicable.
     */
    public enum MaterialType {
        /**
         * Represents all wooden tools.
         */
        WOOD(new Material[]{WOODEN_SWORD, WOODEN_PICKAXE, WOODEN_AXE, WOODEN_SHOVEL, WOODEN_HOE}, OAK_PLANKS, SPRUCE_PLANKS, BIRCH_PLANKS, JUNGLE_PLANKS, ACACIA_PLANKS, DARK_OAK_PLANKS),
        /**
         * Represents all leather armor.
         */
        LEATHER(new Material[]{LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS}, Material.LEATHER),
        /**
         * Represents all stone tools.
         */
        STONE(new Material[]{STONE_SWORD, STONE_PICKAXE, STONE_AXE, STONE_SHOVEL, STONE_HOE}, COBBLESTONE),
        /**
         * Represents all iron tools.
         */
        IRON(new Material[]{IRON_SWORD, IRON_PICKAXE, IRON_AXE, IRON_SHOVEL, IRON_HOE}, IRON_INGOT),
        /**
         * Represents all iron armor.
         */
        IRON_ARMOR(new Material[]{IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS}, IRON_INGOT),
        /**
         * Represents all chainmail armor.
         */
        CHAIN(new Material[]{CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS}, IRON_INGOT),
        /**
         * Represents all golden tools.
         */
        GOLD(new Material[]{GOLDEN_SWORD, GOLDEN_PICKAXE, GOLDEN_AXE, GOLDEN_SHOVEL, GOLDEN_HOE}, GOLD_INGOT),
        /**
         * Represents all golden armor.
         */
        GOLD_ARMOR(new Material[]{GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS}, GOLD_INGOT),
        /**
         * Represents all diamond tools.
         */
        DIAMOND(new Material[]{DIAMOND_SWORD, DIAMOND_PICKAXE, DIAMOND_AXE, DIAMOND_SHOVEL, DIAMOND_HOE}, Material.DIAMOND),
        /**
         * Represents all diamond armor.
         */
        DIAMOND_ARMOR(new Material[]{DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS}, Material.DIAMOND),
        /**
         * Represents the turtle shell.
         */
        TURTLE_SHELL(new Material[]{TURTLE_HELMET}, SCUTE),
        /**
         * Represents the elytra.
         */
        ELYTRA(new Material[]{Material.ELYTRA}, PHANTOM_MEMBRANE),
        /**
         * Represents the enchanted book and book.
         */
        ENCHANTED_BOOK(new Material[]{Material.ENCHANTED_BOOK, BOOK}),
        /**
         * Represents all netherite tools.
         */
        NETHERITE(),
        /**
         * Represents all netherite armor.
         */
        NETHERITE_ARMOR(),
        /**
         * Represents all other materials.
         */
        OTHER();

        private final List<Material> materials;
        private final List<Material> repairs;

        MaterialType(Material[] materials, Material... repairs) {
            this.materials = new ArrayList<>(Arrays.asList(materials));
            this.repairs = new ArrayList<>(Arrays.asList(repairs));
            if (this.name().equals("WOOD") && VersionUtils.SERVER_VERSION >= 16) {
                this.repairs.add(WARPED_PLANKS);
                this.repairs.add(CRIMSON_PLANKS);
            } else if (this.toString().equals("STONE") && VersionUtils.SERVER_VERSION >= 16)
                this.repairs.add(BLACKSTONE);
            else if (this.toString().equals("STONE") && VersionUtils.SERVER_VERSION >= 17)
                this.repairs.add(COBBLED_DEEPSLATE);
        }

        MaterialType() {
            if (this.toString().equals("NETHERITE_ARMOR") && VersionUtils.SERVER_VERSION >= 16) {
                this.materials = Arrays.asList(NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS);
                this.repairs = Collections.singletonList(NETHERITE_INGOT);
            } else if (this.toString().equals("NETHERITE") && VersionUtils.SERVER_VERSION >= 16) {
                this.materials = Arrays.asList(NETHERITE_SWORD, NETHERITE_PICKAXE, NETHERITE_AXE, NETHERITE_SHOVEL, NETHERITE_HOE);
                this.repairs = Collections.singletonList(NETHERITE_INGOT);
            } else {
                this.materials = new ArrayList<>();
                this.repairs = new ArrayList<>();
            }
        }

        /**
         * Gets the <code>MaterialType</code> from the specified material.
         *
         * @param mat The material to get the type for
         * @return The <code>MaterialType</code> for the material; if no <code>MaterialType</code>
         * contains the material, it will return <code>OTHER</code>.
         */
        public static MaterialType fromMaterial(Material mat) {
            for (MaterialType matType : values()) {
                if (matType.contains(mat))
                    return matType;
            }
            return OTHER;
        }

        /**
         * Gets all materials in this <code>MaterialType</code>.
         *
         * @return List of all materials in this type
         */
        public List<Material> getMaterials() {
            return Collections.unmodifiableList(materials);
        }

        /**
         * Gets all repair materials for this <code>MaterialType</code>.
         *
         * @return List of all repair materials for this type
         */
        public List<Material> getRepairs() {
            return Collections.unmodifiableList(repairs);
        }

        /**
         * Checks if this <code>MaterialType</code> contains the specified material.
         *
         * @param mat The material to check for
         * @return <code>true</code> if this type contains the material; <code>false</code> otherwise.
         */
        public boolean contains(Material mat) {
            return materials.contains(mat);
        }

        /**
         * Checks if this <code>MaterialType</code> can be repaired by the specified material.
         *
         * @param mat The repair material to check
         * @return <code>true</code> if this type can be repaired by the material; <code>false</code> otherwise.
         */
        public boolean canRepairWith(Material mat) {
            return repairs.contains(mat);
        }
    }
}
