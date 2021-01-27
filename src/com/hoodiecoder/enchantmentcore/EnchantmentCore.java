package com.hoodiecoder.enchantmentcore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class EnchantmentCore extends JavaPlugin {
private static EnchantmentCore instance;
private int enchLimit = 37;
private boolean firstEnch = true;
private final List<CoreEnchWrapper> enchList = new ArrayList<>();
public static final String[] numerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

@Override
public void onEnable() {
	instance = getPlugin(this.getClass());
	FileConfiguration config = getConfig();
	config.options().copyDefaults(true);
	saveConfig();
	PluginManager m = getServer().getPluginManager();
	ItemEnchantListener iel = new ItemEnchantListener(this);
	AutoEnchListener ael = new AutoEnchListener(this);
	m.registerEvents(iel, this);
	m.registerEvents(ael, this);
	enchLimit = CoreEnchWrapper.getEnchLimit();
	new UpdateChecker(this, 88310).getVersion(version -> {
		if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
			String str = ChatColor.DARK_AQUA + "ZEnchantmentCore » " + ChatColor.GRAY + "There is an update to version " + ChatColor.DARK_AQUA + version + ChatColor.GRAY + " available for ZEnchantmentCore! (Current version: " + ChatColor.DARK_AQUA + this.getDescription().getVersion() + ChatColor.GRAY + ")";
			Bukkit.getConsoleSender().sendMessage(str);
			Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "You can download it at " + ChatColor.DARK_AQUA + "https://www.spigotmc.org/resources/zenchantmentcore.88310/");
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("zenchantmentcore.util")) {
					p.sendMessage(str);
					TextComponent tc = new TextComponent("You can download it ");
					tc.setColor(net.md_5.bungee.api.ChatColor.GRAY);
					TextComponent clickable = new TextComponent("here");
					clickable.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
					clickable.setUnderlined(true);
					clickable.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/zenchantmentcore.88310/"));
					tc.addExtra(clickable);
					TextComponent period = new TextComponent(".");
					period.setColor(net.md_5.bungee.api.ChatColor.GRAY);
					tc.addExtra(period);
					p.spigot().sendMessage(tc);
				}
			}
		}
	});
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
			if (sender.hasPermission("zenchantmentcore.help")) {
			sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantmentCore" + ChatColor.GRAY + " version " + ChatColor.DARK_AQUA + getDescription().getVersion() + ChatColor.GRAY + ". Subcommands:");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " list" + ChatColor.GRAY + " - lists all enchantments");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " help" + ChatColor.GRAY + " - displays this page");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " check" + ChatColor.GRAY + " - checks enchantments on main hand");
			sender.sendMessage(ChatColor.GRAY + "More commands will be added soon\u2122!");
			} else {
				sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Invalid permission.");
			}
			return true;
		} else {
			switch (args[0]) {
			case "list":
				if (sender.hasPermission("zenchantmentcore.util")) {
				sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "List of added enchantments (gray is disabled):");
				for (CoreEnchWrapper cew : enchList) {
					CoreEnchParent ce = cew.getCoreEnch();
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
				} else {
					sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Invalid permission.");
				}
				break;
			case "check":
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Cannot be run from console.");
					break;
				} else {
					if (sender.hasPermission("zenchantmentcore.util")) {
					if (player.getInventory().getItemInMainHand().getType() == null || player.getInventory().getItemInMainHand().getType() == org.bukkit.Material.AIR) {
					sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Cannot check for empty hand.");
					break;
					} else {
						org.bukkit.inventory.ItemStack main = player.getInventory().getItemInMainHand();
						List<String> messages = new LinkedList<>();
						for (CoreEnchWrapper cew : enchList) {
							CoreEnchParent ce = cew.getCoreEnch();
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
					} else {
						sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY + "Invalid permission.");
						break;
					}
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
			for (CoreEnchWrapper cew : getInstance().enchList) {
				CoreEnchParent ne = cew.getCoreEnch();
				if (getInstance().enchList.get(ne.getCoreID()) != null && s.startsWith(enchCode + ne.getDisplayName()) &&  ne.equals(getInstance().enchList.get(ne.getCoreID()).getCoreEnch())) {
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
		for (CoreEnchWrapper cew : getInstance().enchList) {
			CoreEnchParent ne = cew.getCoreEnch();
			if (!ne.isDisabled() && ne.getCraftEnchant() != null && e.getKey().equals(ne.getCraftEnchant())) {
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
	for (CoreEnchWrapper cew : getInstance().enchList) {
		CoreEnchParent ce = cew.getCoreEnch();
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
public List<CoreEnchWrapper> getEnchList() {
	return Collections.unmodifiableList(enchList);
}
void addEnchant(CoreEnchWrapper cew) {
	enchList.add(cew);
	CoreEnchParent ne = cew.getCoreEnch();
	if (!getDisabledEnchants().isEmpty() && getDisabledEnchants().contains(ne.getInternalName())) {
		cew.setDisabled(true);
	}
	if (!ne.isDisabled()) {
		System.out.println("registering ench " + enchLimit+ne.getCoreID()+1);
		cew.checkRegisterEnch(firstEnch, enchLimit+ne.getCoreID()+1);
		if (firstEnch == true) {
			firstEnch = false;
		}
	}
}
}
