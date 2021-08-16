/*
 * Created according to https://minecraft.gamepedia.com/Enchanting_mechanics
 */

package com.hoodiecoder.enchantmentcore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import com.hoodiecoder.enchantmentcore.utils.EnchantmentInformation;
import com.hoodiecoder.enchantmentcore.utils.EnchantmentUtils;
import com.hoodiecoder.enchantmentcore.utils.EnchEnums.Rarity;

import static com.hoodiecoder.enchantmentcore.utils.EnchEnums.MaterialType.*;

/**
 * <p>Represents a fully functional Minecraft enchantment generator.</p>
 * <p>Contains methods that generate enchantments, including all custom enchantments and more customizable features.</p>
 */
public class EnchantmentGenerator {
	private final int version;
	private final Map<UUID, Integer> numberOfEnchants = new HashMap<UUID, Integer>();
	private final int randomModifier;
	private final Random r = new Random();
	/**
	 * Creates a new <code>EnchantmentGenerator</code> with a given Minecraft version.
	 * @param version Numeric Minecraft major version
	 * @see #getMinecraftVersion()
	 */
	public EnchantmentGenerator(int version) {
		this.version = version;
		randomModifier = (int)(Math.random()*Math.random()*1000);
	}
	
	/**
	 * Gets list of enchantment offers based on different generator parameters and settings.
	 * @param item				The item to get the offers for
	 * @param enchanter			The player enchanting the item
	 * @param bonus				The bonus enchantment number from bookshelves
	 * @param amountMultiplier 	The multiplier affecting probability of gaining multiple enchantments
	 * @param allowMulti		Indicates multiple enchantments are allowed in offer
	 * @param allowTreasure 	Indicates treasure enchantments are allowed in offer
	 * @return 					List of enchantment offers for given parameters
	 */
	public List<EnchantmentInformation> getOffers(ItemStack item, Player enchanter, int bonus, double amountMultiplier, boolean allowMulti, boolean allowTreasure, boolean axesAsWeapons) {
		if (bonus > 15) bonus = 15;
		UUID enchantID = enchanter.getUniqueId();
		if (!numberOfEnchants.containsKey(enchantID)) {
			numberOfEnchants.put(enchantID, 0);
		}
		int modifier = numberOfEnchants.get(enchantID) + randomModifier;
		List<EnchantmentInformation> offers = new ArrayList<EnchantmentInformation>();
		Material mat = item.getType();
		boolean allowMultiple = true;
		int base = ((r.nextInt(8)+1)+(int)Math.floor((double)bonus/2.0)+(r.nextInt(bonus+1)));
		int[] levels = {Math.max(base/3, 1), (base*2)/3+1, Math.max(base, bonus*2)};
		if (!allowMulti && ENCHANTED_BOOK.contains(mat)) {
			allowMultiple = false;
		}
		for (int i : levels) {
			offers.add(generateOffer(item, enchanter, i, modifier, 0, amountMultiplier, allowMultiple, allowTreasure, axesAsWeapons));
		}
		return offers;
	}
	/**
	 * Gets enchantment information for a given item in a loot table, given different generator parameters and settings.
	 * @param item			The item to get the offers for
	 * @param enchanter		The player opening the loot
	 * @param table			The loot table to generate for
	 * @param enchAmount	Amount of enchantments to get
	 * @param uniform		Whether the offers are uniformly generated
	 * @return				Loot offer for the given parameters
	 */
	public EnchantmentInformation getLootOffer(ItemStack item, Entity enchanter, LootTable table, int enchAmount, boolean uniform) {
		int modifier = r.nextInt();
		if (!uniform) {
			return generateOffer(item, enchanter, 30, modifier, enchAmount, 1.0, true, true, true);
		} else {
			List<Enchantment> offer = new ArrayList<>();
			List<Integer> levels = new ArrayList<>();
			Map<Enchantment, Integer> possibilities = generateUniformPossibilities(item, true);
			for (int i = 0; i < enchAmount; i++) {
				int randRange = r.nextInt(possibilities.size()*10);
				for (Entry<Enchantment, Integer> entry : possibilities.entrySet()) {
					Enchantment e = entry.getKey();
					int lvl = entry.getValue();
					if (version >= 16 && e.equals(Enchantment.SOUL_SPEED) && !table.equals(LootTables.BASTION_OTHER.getLootTable())) continue;
					randRange -= 10;
					if (randRange < 0) {
						offer.add(e);
						levels.add(lvl);
						Map<Enchantment, Integer> possibilitiesCopy = new HashMap<Enchantment, Integer>(possibilities);
						for (Enchantment ench : possibilities.keySet()) {
							if (ench.conflictsWith(e)) {
								possibilitiesCopy.remove(ench);
							}
						}
						possibilities = possibilitiesCopy;
						break;
					}
				}
			}
			return new EnchantmentInformation(offer, levels, 30);
		}
	}
	/**
	 * <p>Generates a single enchantment offer based on different generator parameters and settings.</p>
	 * <p><code>enchAmount</code> specifies a set number of enchantments to generate. Therefore,
	 * <code>amountMultiplier</code>, which affects the random chance of getting more enchantments,
	 *  only has an effect when <code>enchAmount &lt;= 0</code>.</p>
	 * @param item					The item to get the offer for
	 * @param enchanter				The player opening the loot
	 * @param baseLevel				The base enchantment level for the offer
	 * @param modifier				Arbitrary number that modifies the random seed
	 * @param enchAmount			The number of enchantments to generate
	 * @param amountMultiplier 		Multiplier that changes the random chance of generating more enchantments
	 * @param allowMultiple			Indicates multiple enchantments are allowed in offer
	 * @param allowTreasure			Indicates treasure enchantments are allowed in offer
	 * @param axesAsWeapons			Indicates axes can be treated as weapons
	 * @return						A single enchantment offer for the given parameters
	 */
	public EnchantmentInformation generateOffer(ItemStack item, Entity enchanter, int baseLevel, int modifier, int enchAmount, double amountMultiplier, boolean allowMultiple, boolean allowTreasure, boolean axesAsWeapons) {
		List<Enchantment> offer = new ArrayList<>();
		List<Integer> levels = new ArrayList<>();
		Material mat = item.getType();
		r.setSeed(enchanter.getUniqueId().hashCode() + mat.toString().hashCode() + modifier);
		int enchantability = generateEnchantability(mat);
		int modifiedLevel = baseLevel + r.nextInt(enchantability/4+1) + r.nextInt(enchantability/4+1) + 1;
		float bonus = 1 + (r.nextFloat() + r.nextFloat() - 1) * 0.15F;
		int finalLevel = Math.round(modifiedLevel*bonus);
		if (finalLevel < 1) finalLevel = 1;
		Map<Enchantment, Integer> possibilities = generatePossibilities(item, finalLevel, allowTreasure, axesAsWeapons);
		if (possibilities == null || possibilities.isEmpty()) return null;
		int totalWeight = 0;
		for (Enchantment e : possibilities.keySet()) {
			totalWeight += EnchantmentUtils.getRarity(e).weight();
		}
		int modifiedChance = enchAmount <= 0 ? (int) Math.round(finalLevel*2*amountMultiplier) : enchAmount;
		do {
			int randRange = r.nextInt(totalWeight);
			for (Entry<Enchantment, Integer> entry : possibilities.entrySet()) {
				Enchantment e = entry.getKey();
				int lvl = entry.getValue();
				randRange -= EnchantmentUtils.getRarity(e).weight();
				if (randRange < 0) {
					offer.add(e);
					levels.add(lvl);
					Map<Enchantment, Integer> possibilitiesCopy = new HashMap<Enchantment, Integer>(possibilities);
					for (Enchantment ench : possibilities.keySet()) {
						if (ench.conflictsWith(e)) {
							possibilitiesCopy.remove(ench);
						}
					}
					possibilities = possibilitiesCopy;
					break;
				}
			}
			if (enchAmount <= 0)
				modifiedChance = (int) Math.floor(((double)modifiedChance)/2.0);
			else
				modifiedChance--;
		} while (enchAmount <= 0 ? floatLessThan(r.nextFloat(), (modifiedChance+1)/50.0F) : modifiedChance >= 1);
		if (offer == null || offer.isEmpty()) return new EnchantmentInformation(offer, levels, baseLevel);
		if (enchAmount  <= 0) {
			int randomChoice = r.nextInt(offer.size());
			if (ENCHANTED_BOOK.contains(mat) && !allowMultiple) {
				List<Enchantment> offerCopy = new ArrayList<Enchantment>(offer);
				for (Enchantment ench : offer) {
					if (offer.indexOf(ench) != randomChoice) {
						offerCopy.remove(ench);
					}
				}
				offer = offerCopy;
			}
		}
		return new EnchantmentInformation(offer, levels, baseLevel);
	}
	private boolean floatLessThan(float f1, float f2) {
		if (f1 <= f2) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * <p>Gets enchantability numbers for a given material.</p>
	 * <p>Enchantability represents how "enchantable" a material is.
	 * Higher enchantability means it is more likely to have more enchantments
	 * and higher enchantment powers.</p>
	 * @param mat	The material to retrieve enchantability for
	 * @return		The enchantability of the specified material
	 */
	public int generateEnchantability(Material mat) {
		int enchantability = 1;
		if (WOOD.contains(mat) || LEATHER.contains(mat) || NETHERITE.contains(mat) || NETHERITE_ARMOR.contains(mat)) enchantability = 15;
		else if (DIAMOND.contains(mat) || DIAMOND_ARMOR.contains(mat)) enchantability = 10;
		else if (ENCHANTED_BOOK.contains(mat)) enchantability = 1;
		else if (TURTLE_SHELL.contains(mat) || IRON.contains(mat)) enchantability = 9;
		else if (STONE.contains(mat)) enchantability = 5;
		else if (CHAIN.contains(mat)) enchantability = 12;
		else if (IRON_ARMOR.contains(mat)) enchantability = 14;
		else if (GOLD.contains(mat)) enchantability = 25;
		else if (GOLD_ARMOR.contains(mat)) enchantability = 22;
		return enchantability;
	}
	/**
	 * Generates all possibilities for an item based on enchantment level.
	 * @param item			The item to generate possibilities for
	 * @param level			The enchantment level to generate for
	 * @param allowTreasure	Indicates treasure enchantments are allowed in possibilities
	 * @param axesAsWeapons	Indicates axes can be treated as weapons
	 * @return				Map containing possibilities for the given parameters
	 */
	public Map<Enchantment, Integer> generatePossibilities(ItemStack item, int level, boolean allowTreasure, boolean axesAsWeapons) {
		final Map<Enchantment, Integer> possibilities = new HashMap<>();
		Material mat = item.getType();
		for (Enchantment e : Enchantment.values()) {
			if (!(e instanceof CustomEnch) && ((!ENCHANTED_BOOK.contains(mat) && e.canEnchantItem(item)) || ENCHANTED_BOOK.contains(mat)) && (allowTreasure ? true : !e.isTreasure())) {
				if (item.getType().toString().endsWith("_AXE") && e.getItemTarget() == EnchantmentTarget.WEAPON && !EnchantmentUtils.generatorSettings().getBoolean("treat-axes-as-weapons"))
					continue;
				for (int i = e.getMaxLevel(); i >= 1; i--) {
					if (EnchantmentUtils.getMinModifiedLevel(e, i) <= level && EnchantmentUtils.getMaxModifiedLevel(e, i) >= level) {
						possibilities.put(e, i);
						break;
					}
				}
			}
		}
		for (CustomEnch e : CustomEnch.values()) {
			if (e.isDisabled()) continue;
			if (((!ENCHANTED_BOOK.contains(mat) && e.canEnchantItem(item)) || ENCHANTED_BOOK.contains(mat)) && (allowTreasure ? true : !e.isTreasure())) {
				if (item.getType().toString().endsWith("_AXE") && e.getItemTarget() == EnchantmentTarget.WEAPON && !EnchantmentUtils.generatorSettings().getBoolean("treat-axes-as-weapons"))
					continue;
				for (int i = e.getMaxLevel(); i >= 1; i--) {
					if (e.getModifiedMin(i) <= level && e.getModifiedMax(i) >= level) {
						possibilities.put(e, i);
					}
				}
			}
		}
		return possibilities;
	}
	/**
	 * Generates all possibilities for an item using uniform randomness.
	 * @param item			The item to generate possibilities for
	 * @param allowTreasure	Indicates treasure enchantments are allowed in possibilities
	 * @return				Map containing possibilities for the given parameters
	 */
	public Map<Enchantment, Integer> generateUniformPossibilities(ItemStack item, boolean allowTreasure) {
		final Map<Enchantment, Integer> possibilities = new HashMap<>();
		Material mat = item.getType();
		for (Enchantment e : Enchantment.values()) {
			if (((!ENCHANTED_BOOK.contains(mat) && e.canEnchantItem(item)) || ENCHANTED_BOOK.contains(mat)) && (allowTreasure ? true : !e.isTreasure())) {
				int lvl = r.nextInt(e.getMaxLevel())+1;
				possibilities.put(e, lvl);
			}
		}
		for (CustomEnch e : CustomEnch.values()) {
			if (e.isDisabled()) continue;
			if (e.getRarity() != Rarity.UNFINDABLE && ((!ENCHANTED_BOOK.contains(mat) && e.canEnchantItem(item)) || ENCHANTED_BOOK.contains(mat)) && (allowTreasure ? true : !e.isTreasure())) {
				int lvl = r.nextInt(e.getMaxLevel())+1;
				possibilities.put(e, lvl);
			}
		}
		return possibilities;
	}
	/**
	 * Gets the bonus number for enchanting based on surrounding bookshelves for the specified enchantment table block.
	 * @param table	Enchantment table block to test
	 * @return		Bonus number for the specified enchantment table block; returns <code>0</code> if the block is not an enchantment table.
	 */
	public int getBonusNumber(Block table) {
		World world = table.getWorld();
		int x = table.getX();
		int y = table.getY();
		int z = table.getZ();
		if (table.getType() != Material.ENCHANTING_TABLE) {
			return 0;
		}
		int bonus = 0;
		for (int j = -1; j <= 1; j++) { // From spigot net.minecraft.server.ContainerEnchantTable.java
			for (int k = -1; k <= 1; k++) {
				if ((j != 0 || k != 0) && world.getBlockAt(x+k, y, z+j).isEmpty() && world.getBlockAt(x+k, y+1, z+j).isEmpty()) {
					if (world.getBlockAt(x+k*2, y, z+j*2).getType() == Material.BOOKSHELF) {
						bonus++;
					}
					if (world.getBlockAt(x+k*2, y+1, z+j*2).getType() == Material.BOOKSHELF) {
						bonus++;
					}
					if (k != 0 && j != 0) {
						 if (world.getBlockAt(x+k*2, y, z+j).getType() == Material.BOOKSHELF) {
                             bonus++;
                         }

                         if (world.getBlockAt(x+k*2, y+1, z+j).getType() == Material.BOOKSHELF) {
                             bonus++;
                         }

                         if (world.getBlockAt(x+k, y, z+j*2).getType() == Material.BOOKSHELF) {
                             bonus++;
                         }

                         if (world.getBlockAt(x+k, y+1, z+j*2).getType() == Material.BOOKSHELF) {
                             bonus++;
                         }
					}
				}
			}
		}
		return bonus;
	}
	void updatePlayer(Player pl) {
		UUID enchantID = pl.getUniqueId();
		if (!numberOfEnchants.containsKey(enchantID)) {
			numberOfEnchants.put(enchantID, 0);
		} else {
			numberOfEnchants.put(enchantID, numberOfEnchants.get(enchantID)+1);
		}
	}
	/**
	 * <p>Gets the numeric Minecraft version of the generator.</p>
	 * <p>The Minecraft version number is based on the major release of Minecraft. For example:</p>
	 * <ul>
	 * <li>In version <code>"1.13.2"</code>, it will return <code>13</code></li>
	 * <li>In version <code>"1.16"</code>, will return <code>16</code></li>
	 * </ul>
	 * @return The current Minecraft version number of the generator
	 */
	public int getMinecraftVersion() {
		return version;
	}
}
