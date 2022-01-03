package io.zivoric.enchantmentcore;

import io.zivoric.enchantmentcore.enchant.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.function.Consumer;

import static io.zivoric.enchantmentcore.utils.EnchantmentUtils.executeEnchantEvent;

/**
 * Event listener responsible for sending events to custom enchantments for handling.
 */
public class CustomEnchListener implements Listener {
    private final Plugin implementer;

    public CustomEnchListener(Plugin implementer) {
        this.implementer = implementer;
    }

    public void register() {
        registerEvent(EntityDeathEvent.class, event ->
                executeEnchantEvent(event.getEntity(), DeathHandler.class, (handler, levels, itemStacks) ->
                        handler.onDeath(event.getEntity(), levels, itemStacks, event)
                )
        );
        registerEvent(EntityAirChangeEvent.class, event -> {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                executeEnchantEvent(entity, AirChangeHandler.class, (handler, levels, itemStacks) ->
                        handler.onAir(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(EntityDropItemEvent.class, event -> {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                executeEnchantEvent(entity, DropItemHandler.class, (handler, levels, itemStacks) ->
                        handler.onDropItem(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(EntityInteractEvent.class, event -> {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                executeEnchantEvent(entity, InteractHandler.class, (handler, levels, itemStacks) ->
                        handler.onInteract(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(EntityPotionEffectEvent.class, event -> {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                executeEnchantEvent(entity, PotionEffectHandler.class, (handler, levels, itemStacks) ->
                        handler.onPotionReceived(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(EntityShootBowEvent.class, event -> {
            LivingEntity entity = event.getEntity();
            executeEnchantEvent(entity, ShootBowHandler.class, (handler, levels, itemStacks) ->
                    handler.onShootBow(entity, levels, itemStacks, event)
            );
        });
        registerEvent(EntityRegainHealthEvent.class, event -> {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                executeEnchantEvent(entity, RegainHealthHandler.class, (handler, levels, itemStacks) ->
                        handler.onRegainHealth(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(EntityTargetEvent.class, event -> {
            if (event.getTarget() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getTarget();
                executeEnchantEvent(entity, TargetHandler.class, (handler, levels, itemStacks) ->
                        handler.onTargeted(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(ProjectileHitEvent.class, event -> {
            if (event.getHitEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getHitEntity();
                executeEnchantEvent(entity, ProjectileHandler.class, (handler, levels, itemStacks) ->
                        handler.onHit(entity, levels, itemStacks, event)
                );
            }
            if (event.getEntity().getShooter() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity().getShooter();
                executeEnchantEvent(entity, ProjectileHandler.class, (handler, levels, itemStacks) ->
                        handler.onTargetHit(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(BlockBreakEvent.class, event ->
                executeEnchantEvent(event.getPlayer(), BlockHandler.class, (handler, levels, itemStacks) ->
                        handler.onBreakBlock(event.getPlayer(), levels, itemStacks, event)
                )
        );
        registerEvent(BlockPlaceEvent.class, event ->
                executeEnchantEvent(event.getPlayer(), BlockHandler.class, (handler, levels, itemStacks) ->
                        handler.onPlaceBlock(event.getPlayer(), levels, itemStacks, event)
                )
        );
        registerEvent(EntityDamageByEntityEvent.class, event -> {
            if (event.getDamager() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getDamager();
                executeEnchantEvent(entity, DamageHandler.class, (handler, levels, itemStacks) ->
                        handler.onDealDamage(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(EntityDamageEvent.class, event -> {
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                executeEnchantEvent(entity, DamageHandler.class, (handler, levels, itemStacks) ->
                        handler.onTakeDamage(entity, levels, itemStacks, event)
                );
            }
        });
        registerEvent(PlayerFishEvent.class, event ->
                executeEnchantEvent(event.getPlayer(), FishHandler.class, (handler, levels, itemStacks) ->
                        handler.onFish(event.getPlayer(), levels, itemStacks, event)
                )
        );
        registerEvent(PlayerExpChangeEvent.class, event ->
                executeEnchantEvent(event.getPlayer(), ExpChangeHandler.class, (handler, levels, itemStacks) ->
                        handler.onGainExp(event.getPlayer(), levels, itemStacks, event)
                )
        );
    }

    public <T extends Event> void registerEvent(Class<T> eventClass, Consumer<T> consumer) {
        registerEvent(eventClass, consumer, EventPriority.NORMAL, false);
    }

    public <T extends Event> void registerEvent(Class<T> eventClass, Consumer<T> consumer, EventPriority priority, boolean ignoreCancelled) {
        List<String> disabledEvents = implementer.getConfig().getStringList("disabled-events");
        if (disabledEvents.contains(eventClass.getSimpleName())) {
            return;
        }
        Bukkit.getPluginManager().registerEvent(eventClass, this, priority, (listener, event) -> {
            if (eventClass.isInstance(event)) consumer.accept(eventClass.cast(event));
        }, implementer, ignoreCancelled);
    }
}