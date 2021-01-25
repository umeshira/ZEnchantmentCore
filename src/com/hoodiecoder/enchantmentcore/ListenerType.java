package com.hoodiecoder.enchantmentcore;

public enum ListenerType {
	ENTITY_DEATH(org.bukkit.event.entity.EntityDeathEvent.class),
	ENTITY_AIR(org.bukkit.event.entity.EntityAirChangeEvent.class),
	ENTITY_DROP(org.bukkit.event.entity.EntityDropItemEvent.class),
	ENTITY_INTERACT(org.bukkit.event.entity.EntityInteractEvent.class),
	ENTITY_POTION(org.bukkit.event.entity.EntityPotionEffectEvent.class),
	ENTITY_SHOOT_BOW(org.bukkit.event.entity.EntityShootBowEvent.class),
	ENTITY_REGAIN_HEALTH(org.bukkit.event.entity.EntityRegainHealthEvent.class),
	ENTITY_BECOME_TARGETED(org.bukkit.event.entity.EntityTargetEvent.class),
	ENTITY_HIT_PROJECTILE(org.bukkit.event.entity.ProjectileHitEvent.class),
	ENTITY_DEAL_DAMAGE(org.bukkit.event.entity.EntityDamageByEntityEvent.class),
	ENTITY_BREAK_BLOCK(org.bukkit.event.block.BlockBreakEvent.class),
	ENTITY_PLACE_BLOCK(org.bukkit.event.block.BlockPlaceEvent.class),
	ENTITY_TAKE_DAMAGE(org.bukkit.event.entity.EntityDamageByEntityEvent.class),
	PLAYER_FISH_EVENT(org.bukkit.event.player.PlayerFishEvent.class),
	PLAYER_EXP_EVENT(org.bukkit.event.player.PlayerExpChangeEvent.class);
	private final Class<?> eventType;
	private ListenerType(Class<?> cl) {
		eventType = cl;
	}
	public Class<?> getEventClass() {
		return eventType;
	}
}
