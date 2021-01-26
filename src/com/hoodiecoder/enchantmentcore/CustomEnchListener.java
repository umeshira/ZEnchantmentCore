package com.hoodiecoder.enchantmentcore;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import com.hoodiecoder.enchantmentcore.nms.CoreEnchParent;

public abstract class CustomEnchListener implements Listener {
	private final CoreEnchParent[] listEnchs;
	@SuppressWarnings("unused")
	private final Plugin implementer;
	@SuppressWarnings("unused")
	private final ConsoleCommandSender console;
	public CustomEnchListener(Plugin implementer, CoreEnchParent[] enchs) {
		this.implementer = implementer;
		listEnchs = enchs;
		console = implementer.getServer().getConsoleSender();
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityDeathEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityAirChangeEvent event) {
		//runApplicableTypes(event); Disabled until seen as necessary
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityDropItemEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityInteractEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityPotionEffectEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityShootBowEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityRegainHealthEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityTargetEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.ProjectileHitEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.block.BlockBreakEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.block.BlockPlaceEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.player.PlayerFishEvent event) {
		runApplicableTypes(event);
		
	}
	@EventHandler
	public void onEvent(org.bukkit.event.player.PlayerExpChangeEvent event) {
		runApplicableTypes(event);
	}
	private void runApplicableTypes(Event event) {
		List<ListenerType> ltypes = new LinkedList<>();
		for (ListenerType lt : ListenerType.values()) {
			if (lt.getEventClass().isInstance(event)) {
				ltypes.add(lt);
			}
		}
		for (ListenerType lt : ltypes) {
			runEnchEvent(event, lt);
		}
	}
	private void runEnchEvent(Event event, ListenerType lt) {
		ListenerType ltype = lt;
		if (ltype == null) return;
		Entity entityPlayer = getEntityFromType(ltype, event);
		if (!(entityPlayer instanceof Player)) {
			return;
		}
		Player player = (Player) entityPlayer;
		PlayerInventory playerInv = player.getInventory();
		callEvents(listEnchs, playerInv, event, ltype);
	}
	private static Entity getEntityFromType(ListenerType ltype, Event event) {
		Entity entityPlayer = null;
		switch (ltype) {
		case ENTITY_TAKE_DAMAGE:
		case ENTITY_AIR:
		case ENTITY_DEATH:
		case ENTITY_DROP:
		case ENTITY_INTERACT:
		case ENTITY_POTION:
		case ENTITY_REGAIN_HEALTH:
		case ENTITY_SHOOT_BOW:
			entityPlayer = ((EntityEvent) event).getEntity();
			break;
		case ENTITY_DEAL_DAMAGE:
			entityPlayer = ((EntityDamageByEntityEvent) event).getDamager();
			break;
		case ENTITY_BECOME_TARGETED:
			entityPlayer = ((org.bukkit.event.entity.EntityTargetEvent) event).getTarget();
			break;
		case ENTITY_HIT_PROJECTILE:
			entityPlayer = ((org.bukkit.event.entity.ProjectileHitEvent) event).getHitEntity();
			break;
		case ENTITY_BREAK_BLOCK:
			entityPlayer = ((org.bukkit.event.block.BlockBreakEvent) event).getPlayer();
			break;
		case ENTITY_PLACE_BLOCK:
			entityPlayer = ((org.bukkit.event.block.BlockPlaceEvent) event).getPlayer();
			break;
		case PLAYER_EXP_EVENT:
		case PLAYER_FISH_EVENT:
			entityPlayer = ((org.bukkit.event.player.PlayerEvent) event).getPlayer();
			break;
		default:
			break;
		
		}
		return entityPlayer;
	}
	private Object[] getApplicableItems(CoreEnchParent ce, PlayerInventory playerInv) {
		Object[] applicableItems;
		if (ce.isDisabled()) return null;
		Enchantment ench = ce.getCraftEnchant();
		switch (ench.getItemTarget()) {
		case ARMOR_FEET:
			ItemStack feet = playerInv.getBoots();
			if (ench.canEnchantItem(feet) && feet.containsEnchantment(ench)) {
			applicableItems = new Object[]{feet};
			} else {
				applicableItems = null;
			}
			break;
		case ARMOR_HEAD:
			ItemStack head = playerInv.getHelmet();
			if (ench.canEnchantItem(head) && head.containsEnchantment(ench)) {
				applicableItems = new Object[]{head};
				} else {
					applicableItems = null;
				}
			break;
		case ARMOR_LEGS:
			ItemStack legs = playerInv.getLeggings();
			if (ench.canEnchantItem(legs) && legs.containsEnchantment(ench)) {
				applicableItems = new Object[]{legs};
				} else {
					applicableItems = null;
				}
			break;
		case ARMOR_TORSO:
			ItemStack chest = playerInv.getChestplate();
			if (ench.canEnchantItem(chest) && chest.containsEnchantment(ench)) {
				applicableItems = new Object[]{chest};
				} else {
					applicableItems = null;
				}
			break;
		case ARMOR:
		case WEARABLE:
			ItemStack[] armorArr = playerInv.getArmorContents();
			List<ItemStack> armor = new LinkedList<ItemStack>();
			for (ItemStack a : armorArr) {
				if (a != null && a.containsEnchantment(ench) && ench.canEnchantItem(a)) {
					armor.add(a);
				}
			}
			if (armor.isEmpty()) {
				applicableItems = null;
			} else {
				applicableItems = armor.toArray();
			}
			break;
		case BOW:
		case CROSSBOW:
		case FISHING_ROD:
		case TOOL:
		case TRIDENT:
		case WEAPON:
		case BREAKABLE:
			List<ItemStack> itemS = new LinkedList<>();
			ItemStack itemMain = playerInv.getItemInMainHand();
			ItemStack itemOff = playerInv.getItemInOffHand();
			if (ench.canEnchantItem(itemMain) && itemMain.containsEnchantment(ench)) {
				itemS.add(itemMain);
			}
			if (ench.canEnchantItem(itemOff) && itemOff.containsEnchantment(ench)) {
				itemS.add(itemOff);
			}
			if (itemS.isEmpty()) {
				applicableItems = null;
			} else {
				applicableItems = itemS.toArray();
			}
			break;
		/*case VANISHABLE:
			break;*/
		default:
			applicableItems = null;
			break;	
		}
		return applicableItems;
	}
	private void callEvents(CoreEnchParent[] listEnchs, PlayerInventory playerInv, Event event, ListenerType ltype) {
		for (CoreEnchParent ce : listEnchs) {
			Enchantment ench = ce.getCraftEnchant();
			Object[] applicableItems = getApplicableItems(ce, playerInv);
			if (applicableItems == null) {
				continue;
			} else {
				List<Integer> levels = new LinkedList<>();
				List<ItemStack> items = new LinkedList<>();
				for (Object o : applicableItems) {
					items.add((ItemStack)o);
					levels.add(((ItemStack)o).getEnchantmentLevel(ench));
				}
				onCustomEvent(event, ce, levels, items, ltype);
			}
		}
		
	}
	public abstract void onCustomEvent(Event event, CoreEnchParent ench, List<Integer> levels, List<ItemStack> items, ListenerType ltype);
}