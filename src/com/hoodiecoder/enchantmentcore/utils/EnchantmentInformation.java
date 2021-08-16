package com.hoodiecoder.enchantmentcore.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

/**
 * Represents a unit of enchantment information that stores multiple enchantments and their associated levels and cost.
 */
public class EnchantmentInformation {
	private final List<Enchantment> enchs;
	private final List<Integer> levels;
	private final int cost;
	/**
	 * Creates a new <code>EnchantmentInformation</code> object with a list of enchantments and their associated levels.
	 * If <code>enchs</code> and <code>levels</code> are not the same size, the information will only be stored up to the
	 * size of the smaller list.
	 * @param enchs		The list of enchantments to store
	 * @param levels	The levels of the enchantments
	 * @param cost		The enchantment cost
	 */
	public EnchantmentInformation(List<Enchantment> enchs, List<Integer> levels, int cost) {
		if (enchs.size() > levels.size()) {
			for (int i = enchs.size()-1; i>=levels.size(); i--) {
				enchs.remove(i);
			}
		} else if (enchs.size() < levels.size()) {
			for (int i = levels.size()-1; i>=enchs.size(); i--) {
				levels.remove(i);
			}
		}
		this.enchs = enchs;
		this.levels = levels;
		this.cost = cost;
	}
	/**
	 * Gets all enchantments in this object.
	 * @return	List of all enchantments stored
	 */
	public List<Enchantment> getEnchs() {
		return enchs;
	}
	/**
	 * Gets the levels of each of the enchantments in this object.
	 * @return	List of levels for all enchantments stored
	 * @see #getEnchs()
	 */
	public List<Integer> getLevels() {
		return levels;
	}
	/**
	 * Gets the enchantment cost for this object.
	 * @return The cost of the enchantments
	 */
	public int getCost() {
		return cost;
	}
	/**
	 * Returns a map containing all enchantments in this object and their associated levels.
	 * @return Map containing all enchantments stored and their levels
	 */
	public Map<Enchantment, Integer> toMap() {
		Map<Enchantment, Integer> map = new HashMap<>();
		for (Enchantment e : enchs) {
			map.put(e, levels.get(enchs.indexOf(e)));
		}
		return map;
	}
	@Override
	public String toString() {
		List<String> strlist = new ArrayList<>();
		for (Enchantment ench : enchs) {
			strlist.add(ench.getKey().getKey());
		}
		return "EnchantmentInformation[" + strlist + ", " + levels + ", " + cost + "]";
	}
}
