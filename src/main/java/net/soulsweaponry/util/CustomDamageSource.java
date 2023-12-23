package net.soulsweaponry.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CustomDamageSource extends DamageSource {

    public static final DamageSource BLEED = new CustomDamageSource("bleed").bypassArmor();
    public static final DamageSource OBLIVION = new CustomDamageSource("oblivion");
    public static final DamageSource TRUE_MAGIC = new CustomDamageSource("true_magic").bypassArmor().setMagic();

    protected CustomDamageSource(String name) {
        super(name);
    }

    // ProjectileDamageSource = IndirectEntityDamageSource
    public static DamageSource summonDamageSource(String name, LivingEntity summon, Entity attacker) {
        return new IndirectEntityDamageSource(name, summon, attacker);
    }

    public static DamageSource obliterateDamageSource(LivingEntity attacker) {
        return new EntityDamageSource("obliterated", attacker);
    }

    public static DamageSource shadowOrb(Entity projectile, Entity attacker) {
        return new IndirectEntityDamageSource("shadow_orb", projectile, attacker).setProjectile();
    }

    public static DamageSource beam(LivingEntity attacker) {
        return new EntityDamageSource("beam", attacker);
    }

    public static DamageSource dragonMist(LivingEntity attacker) {
        return new EntityDamageSource("dragon_mist", attacker);
    }
}
