package com.hoodiecoder.enchantmentcore;

import com.hoodiecoder.enchantmentcore.enchant.*;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
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

    public static <T> void executeEnchantEvent(LivingEntity livingEntity, Class<T> handlerClass, TriConsumer<T, List<Integer>, List<ItemStack>> handlerConsumer) {
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        Arrays.stream(CustomEnch.values()).forEach(customEnch -> {
            if (!handlerClass.isInstance(customEnch)) {
                return;
            }
            T handler = handlerClass.cast(customEnch);
            ItemStack[] applicableItems = getApplicableItems(customEnch, entityEquipment);
            if (applicableItems.length <= 0) {
                return;
            }
            List<Integer> levels = new ArrayList<>();
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack o : applicableItems) {
                items.add(o);
                levels.add(o.getItemMeta().getEnchantLevel(customEnch));
            }
            handlerConsumer.accept(handler, levels, items);
        });
    }

    private static ItemStack[] getApplicableItems(CustomEnch ench, EntityEquipment entityEquipment) {
        ItemStack[] applicableItems = new ItemStack[0];
        if (ench.isDisabled()) return applicableItems;
        switch (ench.getItemTarget()) {
            case ARMOR_FEET:
                ItemStack feet = entityEquipment.getBoots();
                if (feet != null && ench.canEnchantItem(feet) && feet.getItemMeta().hasEnchant(ench)) {
                    applicableItems = new ItemStack[]{feet};
                }
                break;
            case ARMOR_HEAD:
                ItemStack head = entityEquipment.getHelmet();
                if (head != null && ench.canEnchantItem(head) && head.getItemMeta().hasEnchant(ench)) {
                    applicableItems = new ItemStack[]{head};
                }
                break;
            case ARMOR_LEGS:
                ItemStack legs = entityEquipment.getLeggings();
                if (legs != null && ench.canEnchantItem(legs) && legs.getItemMeta().hasEnchant(ench)) {
                    applicableItems = new ItemStack[]{legs};
                }
                break;
            case ARMOR_TORSO:
                ItemStack chest = entityEquipment.getChestplate();
                if (chest != null && ench.canEnchantItem(chest) && chest.getItemMeta().hasEnchant(ench)) {
                    applicableItems = new ItemStack[]{chest};
                }
                break;
            case ARMOR:
            case WEARABLE:
                ItemStack[] armorArr = entityEquipment.getArmorContents();
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
                ItemStack itemMain = entityEquipment.getItemInMainHand();
                ItemStack itemOff = entityEquipment.getItemInOffHand();
                if (itemMain.getItemMeta().hasEnchant(ench) && ench.canEnchantItem(itemMain)) {
                    itemS.add(itemMain);
                }
                if (itemOff.getItemMeta().hasEnchant(ench) && ench.canEnchantItem(itemOff)) {
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

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityDeathEvent event) {
        executeEnchantEvent(event.getEntity(), DeathHandler.class, (handler, levels, itemStacks) -> handler.onDeath(event.getEntity(), levels, itemStacks, event));
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityAirChangeEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            executeEnchantEvent(entity, AirChangeHandler.class, (handler, levels, itemStacks) -> handler.onAir(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityDropItemEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            executeEnchantEvent(entity, DropItemHandler.class, (handler, levels, itemStacks) -> handler.onDropItem(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityInteractEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            executeEnchantEvent(entity, InteractHandler.class, (handler, levels, itemStacks) -> handler.onInteract(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            executeEnchantEvent(entity, PotionEffectHandler.class, (handler, levels, itemStacks) -> handler.onPotionReceived(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityShootBowEvent event) {
        LivingEntity entity = event.getEntity();
        executeEnchantEvent(entity, ShootBowHandler.class, (handler, levels, itemStacks) -> handler.onShootBow(entity, levels, itemStacks, event));
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            executeEnchantEvent(entity, RegainHealthHandler.class, (handler, levels, itemStacks) -> handler.onRegainHealth(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityTargetEvent event) {
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            executeEnchantEvent(entity, TargetHandler.class, (handler, levels, itemStacks) -> handler.onTargeted(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            executeEnchantEvent(entity, ProjectileHandler.class, (handler, levels, itemStacks) -> handler.onHit(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.block.BlockBreakEvent event) {
        executeEnchantEvent(event.getPlayer(), BlockHandler.class, (handler, levels, itemStacks) -> handler.onBreakBlock(event.getPlayer(), levels, itemStacks, event));
    }

    @EventHandler
    public void onEvent(org.bukkit.event.block.BlockPlaceEvent event) {
        executeEnchantEvent(event.getPlayer(), BlockHandler.class, (handler, levels, itemStacks) -> handler.onPlaceBlock(event.getPlayer(), levels, itemStacks, event));
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getDamager();
            executeEnchantEvent(entity, DamageHandler.class, (handler, levels, itemStacks) -> handler.onDealDamage(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            executeEnchantEvent(entity, DamageHandler.class, (handler, levels, itemStacks) -> handler.onTakeDamage(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.player.PlayerFishEvent event) {
        executeEnchantEvent(event.getPlayer(), FishHandler.class, (handler, levels, itemStacks) -> handler.onFish(event.getPlayer(), levels, itemStacks, event));
    }

    @EventHandler
    public void onEvent(org.bukkit.event.player.PlayerExpChangeEvent event) {
        executeEnchantEvent(event.getPlayer(), ExpChangeHandler.class, (handler, levels, itemStacks) -> handler.onGainExp(event.getPlayer(), levels, itemStacks, event));
    }
}