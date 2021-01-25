package com.hoodiecoder.enchantmentcore.nms;

import org.bukkit.craftbukkit.v1_16_R2.enchantments.CraftEnchantment;

import net.minecraft.server.v1_16_R2.Enchantment;
import net.minecraft.server.v1_16_R2.IRegistry;

public class NameableCraftEnchantment_v1_16_R2 extends CraftEnchantment implements NameableCraftEnchantment {
	private final Enchantment target;
	private final String id;
	
	public NameableCraftEnchantment_v1_16_R2(Enchantment target, String name) {
		super(target);
		this.target = target;
		this.id = name.toUpperCase();
		// TODO Auto-generated constructor stub
	}
	@Override
    public String getName() {
        // PAIL: migration paths
        switch (IRegistry.ENCHANTMENT.a(target)) {
        case 0:
            return "PROTECTION_ENVIRONMENTAL";
        case 1:
            return "PROTECTION_FIRE";
        case 2:
            return "PROTECTION_FALL";
        case 3:
            return "PROTECTION_EXPLOSIONS";
        case 4:
            return "PROTECTION_PROJECTILE";
        case 5:
            return "OXYGEN";
        case 6:
            return "WATER_WORKER";
        case 7:
            return "THORNS";
        case 8:
            return "DEPTH_STRIDER";
        case 9:
            return "FROST_WALKER";
        case 10:
            return "BINDING_CURSE";
        case 11:
            return "SOUL_SPEED";
        case 12:
            return "DAMAGE_ALL";
        case 13:
            return "DAMAGE_UNDEAD";
        case 14:
            return "DAMAGE_ARTHROPODS";
        case 15:
            return "KNOCKBACK";
        case 16:
            return "FIRE_ASPECT";
        case 17:
            return "LOOT_BONUS_MOBS";
        case 18:
            return "SWEEPING_EDGE";
        case 19:
            return "DIG_SPEED";
        case 20:
            return "SILK_TOUCH";
        case 21:
            return "DURABILITY";
        case 22:
            return "LOOT_BONUS_BLOCKS";
        case 23:
            return "ARROW_DAMAGE";
        case 24:
            return "ARROW_KNOCKBACK";
        case 25:
            return "ARROW_FIRE";
        case 26:
            return "ARROW_INFINITE";
        case 27:
            return "LUCK";
        case 28:
            return "LURE";
        case 29:
            return "LOYALTY";
        case 30:
            return "IMPALING";
        case 31:
            return "RIPTIDE";
        case 32:
            return "CHANNELING";
        case 33:
            return "MULTISHOT";
        case 34:
            return "QUICK_CHARGE";
        case 35:
            return "PIERCING";
        case 36:
            return "MENDING";
        case 37:
            return "VANISHING_CURSE";
        default:
            return id;
        }
    }
}
