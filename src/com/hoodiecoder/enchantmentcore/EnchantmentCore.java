package com.hoodiecoder.enchantmentcore;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.hoodiecoder.enchantmentcore.nms.CoreEnchParent;
import com.hoodiecoder.enchantmentcore.nms.CoreEnchWrapper;

import net.md_5.bungee.api.ChatColor;

public class EnchantmentCore extends JavaPlugin implements Listener {
private static EnchantmentCore instance;
private static int enchLimit = 37;
public static List<CoreEnchParent> enchList = new LinkedList<>();
static final String[] numerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

@Override
public void onEnable() {
	instance = this;
	FileConfiguration config = getConfig();
	config.options().copyDefaults(true);
	saveConfig();
	PluginManager m = getServer().getPluginManager();
	ItemEnchantListener iel = new ItemEnchantListener(this);
	AutoEnchListener ael = new AutoEnchListener(this);
	m.registerEvents(iel, this);
	m.registerEvents(ael, this);
	boolean first = true;
	int i = 1;
	enchLimit = CoreEnchWrapper.getEnchLimit();
	for (CoreEnchParent ne : enchList) {
		if (!getDisabledEnchants().isEmpty() && getDisabledEnchants().contains(ne.getInternalName())) {
			ne.setDisabled(true);
		}
		if (!ne.isDisabled()) {
			ne.checkRegisterEnch(first, enchLimit+i);
			if (first == true) {
				first = false;
			}
		}
	i++;
	}
}
@Override
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	Player player = null;
	//ConsoleCommandSender console = null;
	if (sender instanceof Player) player = (Player) sender;
	String lowerCmd = cmd.getName().toLowerCase();
	//if (sender instanceof ConsoleCommandSender) console = getServer().getConsoleSender();
	switch (lowerCmd) {
	case "ze":
	case "zenchantment":
		if (args.length == 0 || args[0].equals("help")) {
			sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantmentCore" + ChatColor.GRAY + " version " + ChatColor.DARK_AQUA + getDescription().getVersion() + ChatColor.GRAY + ". Subcommands:");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " list" + ChatColor.GRAY + " - lists all enchantments");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " help" + ChatColor.GRAY + " - displays this page");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " check" + ChatColor.GRAY + " - checks enchantments on main hand");
			sender.sendMessage(ChatColor.GRAY + "More commands will be added soon\u2122!");
			return true;
		} else {
			switch (args[0]) {
			case "list":
				sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "List of added enchantments (gray is disabled):");
				for (CoreEnchParent ce : enchList) {
					ChatColor chatColor;
					String levelRange;
					if (ce.isDisabled()) {
						chatColor = ChatColor.GRAY;
					} else {
						chatColor = ChatColor.DARK_AQUA;
					}
					if (ce.getMaxLevel()==1) {
						levelRange = "";
					} else {
						levelRange = numerals[0] + "-" + numerals[ce.getMaxLevel()-1];
					}
					sender.sendMessage(ChatColor.GRAY + " - " + chatColor + ce + ", " + ce.getDisplayName() + " " + levelRange);
				}
				break;
			case "check":
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Cannot be run from console.");
					break;
				} else if (player.getInventory().getItemInMainHand().getType() == null || player.getInventory().getItemInMainHand().getType() == org.bukkit.Material.AIR) {
					sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Cannot check for empty hand.");
					break;
				} else {
					org.bukkit.inventory.ItemStack main = player.getInventory().getItemInMainHand();
					List<String> messages = new LinkedList<>();
					for (CoreEnchParent ce : enchList) {
						if (!ce.isDisabled() && main.getEnchantments().containsKey(ce.getCraftEnchant())) {
							messages.add(ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + ce + ", " + ce.getDisplayName() + " " + numerals[main.getEnchantmentLevel(ce.getCraftEnchant())-1]);
						}
					}
					if (messages.isEmpty()) {
						sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Enchantments on main hand: None.");
					} else {
						sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Enchantments on main hand:");
						for (String msg : messages) {
							sender.sendMessage(msg);
						}
					}
					break;
				}
			default:
				sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Argument not recognized.");
				break;
			}
			return true;
		}
	default:
		sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Command not recognized.");
		return true;
	}
}
private List<String> getDisabledEnchants() {
	return getConfig().getStringList("disabled-enchantments");
}
public static Map<Enchantment, Integer> parseLore(List<String> lore) {
	String enchCode = ChatColor.GRAY.toString();
	String endCode = ChatColor.GRAY + "" + ChatColor.MAGIC + " ";
	Map<Enchantment, Integer> enchMap = new HashMap<>();
	if (lore == null || lore.isEmpty()) return null;
	for (String s : lore) {
		String display = s.substring(enchCode.length());
		int power = 1;
		for (String n : numerals) {
			if (display.indexOf(" " + n + endCode) != -1) {
				display = display.substring(0, display.length()-n.length()-endCode.length()-1);
				power = ArrayUtils.indexOf(numerals, n)+1;
			}
		}
		if (s.startsWith(enchCode)) {
			for (CoreEnchParent ne : enchList) {
				if (enchList.get(ne.getCoreID()) != null && s.startsWith(enchCode + ne.getDisplayName()) &&  ne.equals(enchList.get(ne.getCoreID()))) {
					enchMap.put(ne.getCraftEnchant(), power);
				}
			}
		}
	}
	return enchMap;
}
public static List<String> createLore(Map<Enchantment, Integer> enchs, List<String> currentLore) {
	String enchCode = ChatColor.GRAY.toString();
	String endCode = ChatColor.GRAY + "" + ChatColor.MAGIC + " ";
	List<String> lore = new LinkedList<String>();
	for (Entry<Enchantment, Integer> e : enchs.entrySet()) {
		for (CoreEnchParent ne : enchList) {
			if (!ne.isDisabled() && e.getKey().equals(ne.getCraftEnchant())) {
				if (currentLore == null || (ne.getMaxLevel() > 1 && !currentLore.contains(enchCode + ne.getDisplayName() + " " + numerals[e.getValue()-1] + endCode)) || (ne.getMaxLevel() <= 1 && !currentLore.contains(enchCode + ne.getDisplayName() + endCode))) {
					if (ne.getMaxLevel() > 1) {
						lore.add(enchCode + ne.getDisplayName() + " " + numerals[e.getValue()-1] + endCode);
					} else {
						lore.add(enchCode + ne.getDisplayName() + endCode);
					}
				}
			}
		}
	}
	return lore;
}

public static EnchantmentCore getInstance() {
	return instance;
}
@Override
public void onDisable() {
	for (CoreEnchParent ce : enchList) {
		if (!ce.isDisabled()) {
			unregisterEnch(ce.getCraftEnchant());
		}
	}
}
@SuppressWarnings("deprecation")
private void unregisterEnch(Enchantment ench) {
	try {
		Field kf = Enchantment.class.getDeclaredField("byKey");
		kf.setAccessible(true);
		@SuppressWarnings("unchecked")
		HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) kf.get(null);
		if(byKey.containsKey(ench.getKey())) {
			byKey.remove(ench.getKey());
		}
		Field nf = Enchantment.class.getDeclaredField("byName");
		nf.setAccessible(true);
		@SuppressWarnings("unchecked")
		HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nf.get(null);
		if(byName.containsKey(ench.getName())) {
			byName.remove(ench.getName());
		}
		} catch (Exception e) {
			e.printStackTrace();
	}
}
}
