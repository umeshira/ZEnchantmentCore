package com.hoodiecoder.enchantmentcore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import com.hoodiecoder.enchantmentcore.utils.EnchantmentUtils;
import com.hoodiecoder.enchantmentcore.utils.UpdateChecker;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Main plugin class for ZEnchantmentCore.
 */
public class EnchantmentCore extends JavaPlugin {
private FileConfiguration config;
private static EnchantmentCore instance;
private CustomEnchListener customListener;
private EnchantmentGenerator coreGenerator;
private int version, enchLimit;
private static String messagePrefix = ChatColor.DARK_AQUA + "ZEnchantment » " + ChatColor.GRAY;

@Override
public void onEnable() {
	String bukkitVers = Bukkit.getVersion();
	PluginManager m = getServer().getPluginManager();
	if (bukkitVers.contains("1.17")) {
		enchLimit = 37;
		version = 17;
	} else if (bukkitVers.contains("1.16")) {
		enchLimit = 37;
		version = 16;
	} else if (bukkitVers.contains("1.15")) {
		enchLimit = 36;
		version = 15;
	} else if (bukkitVers.contains("1.14")) {
		enchLimit = 36;
		version = 14;
	} else if (bukkitVers.contains("1.13")) {
		enchLimit = 33;
		version = 13;
	} else {
		getLogger().log(Level.WARNING, "Version incompatible. Exiting plugin.");
		m.disablePlugin(this);
		return;
	}
	coreGenerator = new EnchantmentGenerator(version);
	instance = getPlugin(this.getClass());
	reloadableEnable(false);
	ItemEnchantListener iel = new ItemEnchantListener(this, coreGenerator);
	AutoEnchListener ael = new AutoEnchListener(this, coreGenerator);
	customListener = new CustomEnchListener(this);
	m.registerEvents(iel, this);
	m.registerEvents(ael, this);
	m.registerEvents(customListener, this);
	new UpdateChecker(this, 88310).getVersion(version -> {
		if (!this.getDescription().getVersion().equalsIgnoreCase(version.substring(1))) {
			String str = ChatColor.DARK_AQUA + "ZEnchantmentCore » " + ChatColor.GRAY + "There is an update to version " + ChatColor.DARK_AQUA + version + ChatColor.GRAY + " available for ZEnchantmentCore! (Current version: " + ChatColor.DARK_AQUA + "v" + this.getDescription().getVersion() + ChatColor.GRAY + ")";
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
	if (sender instanceof Player) player = (Player) sender;
	String lowerCmd = cmd.getName().toLowerCase();
	switch (lowerCmd) {
	case "ze":
	case "zenchantment":
		if (args.length == 0 || args[0].equals("help")) {
			if (sender.hasPermission("zenchantmentcore.help")) {
			sender.sendMessage(ChatColor.DARK_AQUA + "ZEnchantmentCore" + ChatColor.GRAY + " version " + ChatColor.DARK_AQUA + "v" + getDescription().getVersion() + ChatColor.GRAY + ". Subcommands:");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " list" + ChatColor.GRAY + " - lists all enchantments");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " help" + ChatColor.GRAY + " - displays this page");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " check" + ChatColor.GRAY + " - checks enchantments on main hand");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " enchant <enchantment> [level]" + ChatColor.GRAY + " - adds enchantment to main hand");
			sender.sendMessage(ChatColor.DARK_AQUA + "/" + lowerCmd + " reload" + ChatColor.GRAY + " - reloads config and enchantments");
			sender.sendMessage(ChatColor.GRAY + "More commands will be added soon\u2122!");
			} else {
				sender.sendMessage(messagePrefix + "Invalid permission.");
			}
			return true;
		} else {
			switch (args[0].toLowerCase()) {
			case "list":
				if (sender.hasPermission("zenchantmentcore.util")) {
					sender.sendMessage(messagePrefix + "List of enchantments: " + (CustomEnch.values().length == 0 ? "None." : ""));
					for (CustomEnch ce : Stream.of(CustomEnch.allValues()).sorted(Comparator.comparing(ce->((CustomEnch)ce).getKey().getKey())).collect(Collectors.toList())) {
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
							levelRange = EnchantmentUtils.getRomanNumeral(1) + "-" + EnchantmentUtils.getRomanNumeral(ce.getMaxLevel());
						}
						sender.sendMessage(ChatColor.GRAY + " - " + chatColor + ce.getName().toLowerCase() + ", " + ce.getDisplayName() + " " + levelRange);
					}
					break;
				} else {
					sender.sendMessage(messagePrefix + "Invalid permission.");
				}
				break;
			case "check":
				if (!(sender instanceof Player)) {
					sender.sendMessage(messagePrefix + "Cannot be run from console.");
					break;
				} else {
					if (sender.hasPermission("zenchantmentcore.util")) {
					if (player.getInventory().getItemInMainHand().getType() == null || player.getInventory().getItemInMainHand().getType() == org.bukkit.Material.AIR) {
					sender.sendMessage(messagePrefix + "Cannot check for empty hand.");
					break;
					} else {
						org.bukkit.inventory.ItemStack main = player.getInventory().getItemInMainHand();
						List<String> messages = new LinkedList<>();
						Map<Enchantment,Integer> enchs = new HashMap<>();
						if (main.getItemMeta() != null) {
							if (main.getItemMeta() instanceof EnchantmentStorageMeta) {
								enchs = ((EnchantmentStorageMeta)main.getItemMeta()).getStoredEnchants();
							} else
								enchs = main.getItemMeta().getEnchants();
						}
						for (Entry<Enchantment, Integer> entry : enchs.entrySet()) {
								Enchantment ench = entry.getKey();
								messages.add(ChatColor.GRAY + " - " + ChatColor.DARK_AQUA + ench.getKey().getKey().toLowerCase() + " " + entry.getValue());
						}
						if (messages.isEmpty()) {
							sender.sendMessage(messagePrefix + "Enchantments on main hand: None.");
						} else {
							sender.sendMessage(messagePrefix + "Enchantments on main hand:");
							for (String msg : messages) {
								sender.sendMessage(msg);
							}
						}
						break;
					}
					} else {
						sender.sendMessage(messagePrefix + "Invalid permission.");
						break;
					}
				}
			case "reload":
				if (!(sender instanceof Player) || sender.hasPermission("zenchantmentcore.reload")) {
					sender.sendMessage(messagePrefix + "Reloading...");
					reloadableDisable(true);
					reloadableEnable(true);
					sender.sendMessage(messagePrefix + "Reloaded!");
					break;
				} else {
					sender.sendMessage(messagePrefix + "Invalid permission.");
					break;
				}
			case "enchant":
				return enchantCommand(sender, Arrays.copyOfRange(args, 1, args.length), false);
			case "info":
				if (args.length <= 1) {
					sender.sendMessage(messagePrefix + "Must specify an enchantment.");
					break;
				} else if (!sender.hasPermission("zenchantmentcore.util")) {
					sender.sendMessage(messagePrefix + "Invalid permission.");
					break;
				}
				CustomEnch ench = CustomEnch.getByKey(NamespacedKey.minecraft(args[1]));
				if (ench == null) {
					sender.sendMessage(messagePrefix + "Invalid custom enchantment.");
					break;
				}
				sender.sendMessage(messagePrefix + "Enchantment information for " + ChatColor.DARK_AQUA + ench.getName() + ChatColor.GRAY + ":");
				sender.sendMessage(ChatColor.DARK_AQUA + "Status: " + (ench.isDisabled() ? (ChatColor.RED + "Disabled") : (ChatColor.GREEN + "Enabled")));
				sender.sendMessage(ChatColor.DARK_AQUA + "Internal ID: " + ChatColor.AQUA + ench.getKey());
				sender.sendMessage(ChatColor.DARK_AQUA + "Display name: " + ChatColor.AQUA + ench.getDisplayName());
				sender.sendMessage(ChatColor.DARK_AQUA + "Owner plugin: " + ChatColor.AQUA + ench.getOwnerPlugin().getName());
				sender.sendMessage(ChatColor.DARK_AQUA + "Maximum level: " + ChatColor.AQUA + ench.getMaxLevel());
				sender.sendMessage(ChatColor.DARK_AQUA + "Rarity: " + ChatColor.AQUA + ench.getRarity().displayName());
				String lowerTarget = ench.getItemTarget().toString().replace('_', ' ').toLowerCase();
				sender.sendMessage(ChatColor.DARK_AQUA + "Enchantment target: " + ChatColor.AQUA + lowerTarget.substring(0,1).toUpperCase() + lowerTarget.substring(1));
				sender.sendMessage(ChatColor.DARK_AQUA + "Enchantment type: " + ChatColor.AQUA + (ench.isCursed() ? "Curse" : "Normal"));
				sender.sendMessage(ChatColor.DARK_AQUA + "Occurrence: " + ChatColor.AQUA + (ench.isTreasure() ? "Treasure (loot only)" : "Normal"));
				break;
			default:
				sender.sendMessage(messagePrefix + "Argument not recognized.");
				break;
			}
			return true;
		}
	case "enchant":
		return enchantCommand(sender, args, true);
	default:
		sender.sendMessage(messagePrefix + "Command not recognized.");
		return true;
	}
}
private boolean enchantCommand(CommandSender sender, String[] args, boolean includeVanilla) {
	if (args.length <= 0) {
		sender.sendMessage(messagePrefix + "Must specify an enchantment.");
	} else if (args.length <= 1) {
		sender.sendMessage(messagePrefix + "Must specify an enchantment.");
	} else if (!sender.hasPermission("zenchantmentcore.enchant")) {
		sender.sendMessage(messagePrefix + "Invalid permission.");
	} else {
		Player target = getServer().getPlayer(args[0]);
		if (target == null || !target.isOnline()) {
			sender.sendMessage(messagePrefix + "That player is not online.");
			return true;
		}
		Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(args[1]));
		if (ench == null || (includeVanilla ? false : !(ench instanceof CustomEnch)) || (ench instanceof CustomEnch && ((CustomEnch) ench).isDisabled())) {
			sender.sendMessage(messagePrefix + "Invalid" + (includeVanilla ? "" : " or vanilla") + " enchantment.");
			return true;
		}
		int lvl;
		if (args.length >= 3) {
			try {
				lvl = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				lvl = 1;
			}
		} else {
			lvl = 1;
		}
		if (lvl > ench.getMaxLevel()) {
			sender.sendMessage(messagePrefix + args[2] + " is higher than the max level of " + ench.getMaxLevel());
			return true;
		}
		ItemStack item = target.getInventory().getItemInMainHand();
		ItemMeta oldMeta = item.getItemMeta();
		ItemMeta meta = item.getItemMeta();
		if ((item.getType() != Material.ENCHANTED_BOOK && item.getType() != Material.BOOK && !ench.canEnchantItem(item)) || (!(meta instanceof EnchantmentStorageMeta) && meta.hasEnchant(ench)) || (meta instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta) meta).hasStoredEnchant(ench))) {
			sender.sendMessage(messagePrefix + "Cannot apply enchantment to " + item.getType().name().toLowerCase());
			return true;
		}
		sender.sendMessage(messagePrefix + "Applying enchantment " + args[1] + " level " + lvl + " to " + item.getType().name().toLowerCase());
		boolean revertBook = false;
		if (item.getType() == Material.BOOK) {
			item.setType(Material.ENCHANTED_BOOK);
			meta = item.getItemMeta();
			revertBook = true;
		}
		if (!(meta instanceof EnchantmentStorageMeta) && !meta.addEnchant(ench, lvl, false)) {
			sender.sendMessage(messagePrefix + "Enchantment unsuccessful.");
			return true;
		}
		else if (meta instanceof EnchantmentStorageMeta && !((EnchantmentStorageMeta) meta).addStoredEnchant(ench, lvl, false)) {
			sender.sendMessage(messagePrefix + "Enchantment unsuccessful.");
			if (revertBook) {
				item.setType(Material.BOOK);
				item.setItemMeta(oldMeta);
			}
			return true;
		}
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new LinkedList<String>();
		}
		Map<Enchantment, Integer> enchMap = new HashMap<>();
		enchMap.put(ench, lvl);
		List<String> createdLore = EnchantmentUtils.createLore(enchMap, lore);
		lore.addAll(0, createdLore);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	return true;
}
private List<String> enchantComplete(String[] args, boolean includeVanilla) {
	final List<String> COMMANDS = new ArrayList<>();
	final List<String> completions = new ArrayList<>();
	if (args.length == 1) {
		for (Player player : getServer().getOnlinePlayers()) {
			COMMANDS.add(player.getName());
		}
		StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
	} else if (args.length == 2) {
		
		for (Enchantment ench : includeVanilla ? Enchantment.values() : CustomEnch.values()) {
			if (!(ench instanceof CustomEnch) || !((CustomEnch)ench).isDisabled()) 
				COMMANDS.add(ench.getKey().getKey().toLowerCase());
		}
		StringUtil.copyPartialMatches(args[1], COMMANDS, completions);
	} else if (args.length == 3) {
		Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(args[1]));
		if (ench == null) return completions;
		for (int i = 1; i <= ench.getMaxLevel(); i++) {
			COMMANDS.add(Integer.toString(i));
		}
		StringUtil.copyPartialMatches(args[2], COMMANDS, completions);
	}
	return completions;
}
public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
	final List<String> COMMANDS;
	final List<String> completions;
	switch (cmd.getName().toLowerCase()) {
	case "ze":
	case "zenchantment":
		completions = new ArrayList<>();
		if (args.length == 1) {
			COMMANDS = Arrays.asList("list", "help", "check", "enchant", "reload", "info");
			StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
		} else if (args.length >= 2) {
			switch (args[0]) {
			case "enchant":
				return enchantComplete(Arrays.copyOfRange(args, 1, args.length), false);
			case "info":
				COMMANDS = new ArrayList<>();
				for (CustomEnch ench : CustomEnch.values())
					COMMANDS.add(ench.getKey().getKey().toLowerCase());
				StringUtil.copyPartialMatches(args[1], COMMANDS, completions);
			case "reload":
			case "check":
			case "list":
			case "help":
			default:
				break;
			}
		}
		return completions;
	case "enchant":
		return enchantComplete(args, true);
	default:
		return new ArrayList<>();
	}
}

/**
 * Gets the current instance of this plugin.
 * @return The current plugin instance
 */
public static EnchantmentCore getInstance() {
	return instance;
}
@Override
public void onDisable() {
	reloadableDisable(false);
}
private void reloadableEnable(boolean reloading) {
	saveResourceConfig();
	config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
	getLogger().info("Custom enchantment generator enabled? " + getConfig().getBoolean("enable-custom-generator"));
	if (reloading) {
		CustomEnch.batchRegister();
	} else {
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				CustomEnch.batchRegister();
			}
			
		};
		runnable.runTaskLater(this, 1L);
	}
}
private void reloadableDisable(boolean reloading) {
	for (CustomEnch ce : CustomEnch.values())
		ce.unregisterEnchantment();
}

public int getEnchLimit() {
	return enchLimit;
}
/**
 * Gets the current {@link EnchantmentGenerator} object.
 * @return The current enchantment generator
 */
public EnchantmentGenerator getGenerator() {
	return coreGenerator;
}
void saveResourceConfig() {
	if (!new File(getDataFolder(), "config.yml").exists()) {
		saveResource("config.yml", false);
	}
}
@Override
public FileConfiguration getConfig() {
	return config;
}
}
