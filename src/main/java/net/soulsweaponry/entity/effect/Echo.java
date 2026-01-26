package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.entitydata.EchoDamageData;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.ParticleRegistry;

public class Echo extends StatusEffect {

    public Echo() {
        super(StatusEffectCategory.HARMFUL, 0x004234);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        float damage = EchoDamageData.getEchoDamage(entity) * ((amplifier * 0.5f) + 1);
        if (damage <= 0) {
            return;
        }
        entity.damage(DamageSourceRegistry.create(entity.getWorld(), DamageSourceRegistry.MAGIC_DAMAGE_BYPASS_COOLDOWN), damage);
        EchoDamageData.setEchoDamage(entity, 0);

        entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_SCULK_SHRIEKER_BREAK, SoundCategory.HOSTILE, 5f, 2f);
        entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_SCULK_SHRIEKER_BREAK, SoundCategory.HOSTILE, 5f, 1f);
        entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.HOSTILE, 1f, 2f);
        if (!entity.getWorld().isClient) {
            ParticleHandler.particleOutburst(entity.getWorld(), 20, entity.getParticleX(0.5f), entity.getBodyY(0.5f), entity.getParticleZ(0.5f), ParticleRegistry.ECHO_SMOKE, new Vec3d(1, 1, 1), 0.5f);
            ParticleHandler.singleParticle(entity.getWorld(), ParticleRegistry.ECHO_SWEEP_ATTACK, entity.getParticleX(0.5f), entity.getBodyY(0.5f), entity.getParticleZ(0.5f), 0, 0, 0);
            ParticleHandler.singleParticle(entity.getWorld(), ParticleRegistry.ECHO_SWEEP_ATTACK, entity.getParticleX(0.5f), entity.getBodyY(0.5f), entity.getParticleZ(0.5f), 0, 0, 0);
        }
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        EchoDamageData.setEchoDamage(entity, 0);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration == 1;
    }
}