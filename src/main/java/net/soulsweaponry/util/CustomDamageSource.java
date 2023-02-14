package net.soulsweaponry.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;

public class CustomDamageSource extends DamageSource{

    public static final DamageSource BLEED = new CustomDamageSource("bleed").setBypassesArmor();
    public static final DamageSource OBLIVION = new CustomDamageSource("oblivion");
    public static final DamageSource TRUE_MAGIC = new CustomDamageSource("true_magic").setBypassesArmor().setUsesMagic();

    protected CustomDamageSource(String name) {
        super(name);
    }

    public static DamageSource summonDamageSource(String name, LivingEntity summon, Entity attacker) {
        return new ProjectileDamageSource(name, summon, attacker);
    }

    public static DamageSource obliterateDamageSource(LivingEntity attacker) {
        return new EntityDamageSource("obliterated", attacker);
    }

    public static DamageSource shadowOrb(Entity projectile, Entity attacker) {
        return new ProjectileDamageSource("shadow_orb", projectile, attacker).setProjectile();
    }

    public static DamageSource beam(LivingEntity attacker) {
        return new EntityDamageSource("beam", attacker);
    }

    public static DamageSource dragonMist(LivingEntity attacker) {
        return new EntityDamageSource("dragon_mist", attacker);
    }
}
