package io.zivoric.enchantmentcore;

import io.zivoric.enchantmentcore.enchant.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import static io.zivoric.enchantmentcore.utils.EnchantmentUtils.executeEnchantEvent;

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
            LivingEntity entity = (LivingEntity) event.getTarget();
            executeEnchantEvent(entity, TargetHandler.class, (handler, levels, itemStacks) -> handler.onTargeted(entity, levels, itemStacks, event));
        }
    }

    @EventHandler
    public void onEvent(org.bukkit.event.entity.ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getHitEntity();
            executeEnchantEvent(entity, ProjectileHandler.class, (handler, levels, itemStacks) -> handler.onHit(entity, levels, itemStacks, event));
        }
        if (event.getEntity().getShooter() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity().getShooter();
            executeEnchantEvent(entity, ProjectileHandler.class, (handler, levels, itemStacks) -> handler.onTargetHit(entity, levels, itemStacks, event));
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