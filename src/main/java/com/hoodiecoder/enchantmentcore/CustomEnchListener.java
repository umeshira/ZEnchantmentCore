package com.hoodiecoder.enchantmentcore;

import com.hoodiecoder.enchantmentcore.utils.EnchEnums.ListenerType;
import org.bukkit.command.ConsoleCommandSender;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Event listener responsible for sending events to custom enchantments for handling.
 */
public class CustomEnchListener implements Listener {
    @SuppressWarnings("unused")
    private final Plugin implementer;
    @SuppressWarnings("unused")
    private final ConsoleCommandSender console;

    public CustomEnchListener(Plugin implementer) {
        this.implementer = implementer;
        console = implementer.getServer().getConsoleSender();
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

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityDeathEvent event) {
        runApplicableTypes(event);

    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityAirChangeEvent event) {
        runApplicableTypes(event);

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
        List<ListenerType> ltypes = new ArrayList<>();
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
        callEvents(Arrays.asList(CustomEnch.values()), playerInv, event, ltype);
    }

    private ItemStack[] getApplicableItems(CustomEnch ench, PlayerInventory playerInv) {
        ItemStack[] applicableItems = new ItemStack[0];
        if (ench.isDisabled()) return applicableItems;
        switch (ench.getItemTarget()) {
            case ARMOR_FEET:
                ItemStack feet = playerInv.getBoots();
                if (ench.canEnchantItem(feet) && feet.getItemMeta().hasEnchant(ench)) {
                    applicableItems = new ItemStack[]{feet};
                }
                break;
            case ARMOR_HEAD:
                ItemStack head = playerInv.getHelmet();
                if (ench.canEnchantItem(head) && head.getItemMeta().hasEnchant(ench)) {
                    applicableItems = new ItemStack[]{head};
                }
                break;
            case ARMOR_LEGS:
                ItemStack legs = playerInv.getLeggings();
                if (ench.canEnchantItem(legs) && legs.getItemMeta().hasEnchant(ench)) {
                    applicableItems = new ItemStack[]{legs};
                }
                break;
            case ARMOR_TORSO:
                ItemStack chest = playerInv.getChestplate();
                if (ench.canEnchantItem(chest) && chest.getItemMeta().hasEnchant(ench)) {
                    applicableItems = new ItemStack[]{chest};
                }
                break;
            case ARMOR:
            case WEARABLE:
                ItemStack[] armorArr = playerInv.getArmorContents();
                List<ItemStack> armor = new ArrayList<>();
                for (ItemStack a : armorArr) {
                    if (a != null && a.getItemMeta().hasEnchant(ench) && ench.canEnchantItem(a)) {
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
                ItemStack itemMain = playerInv.getItemInMainHand();
                ItemStack itemOff = playerInv.getItemInOffHand();
                if (ench.canEnchantItem(itemMain) && itemMain.getItemMeta().hasEnchant(ench)) {
                    itemS.add(itemMain);
                }
                if (ench.canEnchantItem(itemOff) && itemOff.getItemMeta().hasEnchant(ench)) {
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
    }

    private void callEvents(List<CustomEnch> listEnchs, PlayerInventory playerInv, Event event, ListenerType ltype) {
        for (CustomEnch ce : listEnchs) {
            ItemStack[] applicableItems = getApplicableItems(ce, playerInv);
            if (applicableItems.length > 0) {
                List<Integer> levels = new ArrayList<>();
                List<ItemStack> items = new ArrayList<>();
                for (ItemStack o : applicableItems) {
                    items.add(o);
                    levels.add(o.getItemMeta().getEnchantLevel(ce));
                }
                switch (ltype) {
                    case ENTITY_AIR:
                        ce.onAir((org.bukkit.event.entity.EntityAirChangeEvent) event, levels, items);
                        break;
                    case ENTITY_BECOME_TARGETED:
                        ce.onTargeted((org.bukkit.event.entity.EntityTargetEvent) event, levels, items);
                        break;
                    case ENTITY_BREAK_BLOCK:
                        ce.onBreakBlock((org.bukkit.event.block.BlockBreakEvent) event, levels, items);
                        break;
                    case ENTITY_DEAL_DAMAGE:
                        ce.onDealDamage((org.bukkit.event.entity.EntityDamageByEntityEvent) event, levels, items);
                        break;
                    case ENTITY_DEATH:
                        ce.onDeath((org.bukkit.event.entity.EntityDeathEvent) event, levels, items);
                        break;
                    case ENTITY_DROP:
                        ce.onDropItem((org.bukkit.event.entity.EntityDropItemEvent) event, levels, items);
                        break;
                    case ENTITY_HIT_PROJECTILE:
                        ce.onHit((org.bukkit.event.entity.ProjectileHitEvent) event, levels, items);
                        break;
                    case ENTITY_INTERACT:
                        ce.onInteract((org.bukkit.event.entity.EntityInteractEvent) event, levels, items);
                        break;
                    case ENTITY_PLACE_BLOCK:
                        ce.onPlaceBlock((org.bukkit.event.block.BlockPlaceEvent) event, levels, items);
                        break;
                    case ENTITY_POTION:
                        ce.onPotionReceived((org.bukkit.event.entity.EntityPotionEffectEvent) event, levels, items);
                        break;
                    case ENTITY_REGAIN_HEALTH:
                        ce.onRegainHealth((org.bukkit.event.entity.EntityRegainHealthEvent) event, levels, items);
                        break;
                    case ENTITY_SHOOT_BOW:
                        ce.onShootBow((org.bukkit.event.entity.EntityShootBowEvent) event, levels, items);
                        break;
                    case ENTITY_TAKE_DAMAGE:
                        ce.onTakeDamage((org.bukkit.event.entity.EntityDamageByEntityEvent) event, levels, items);
                        break;
                    case PLAYER_EXP_EVENT:
                        ce.onGainExp((org.bukkit.event.player.PlayerExpChangeEvent) event, levels, items);
                        break;
                    case PLAYER_FISH_EVENT:
                        ce.onFish((org.bukkit.event.player.PlayerFishEvent) event, levels, items);
                        break;
                    default:
                        break;
                }
            }
        }

    }
}