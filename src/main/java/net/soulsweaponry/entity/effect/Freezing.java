package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.EntityTypeTags;
import net.soulsweaponry.config.EntityStatsConfig;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.IAnimatedDeath;

public class Freezing extends StatusEffect {

    public Freezing() {
        super(StatusEffectCategory.HARMFUL, 9238001);
    }
    
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        int ticks = entity.getFrozenTicks();
        if (entity.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || entity.hasStatusEffect(EffectRegistry.FROST_MOON)) {
            return false;
        }
        FrostData.addFrost(entity, (int) (EntityStatsConfig.permafrost_effect_base_frost_buildup + EntityStatsConfig.permafrost_effect_buildup_per_amp * (amplifier + 1)));
        entity.setInPowderSnow(true);
        entity.setFrozenTicks(Math.min(entity.getMinFreezeDamageTicks(), ticks + amplifier));
        if (!entity.getWorld().isClient) {
            ParticleHandler.singleParticle(entity.getWorld(), ParticleTypes.SNOWFLAKE, entity.getParticleX(0.5D), entity.getRandomBodyY(), entity.getParticleZ(0.5D), 0, 0, 0);
        }
        if (entity.isDead()) {
            if (entity instanceof IAnimatedDeath animated) {
                if (animated.getDeathTicks() < 2) {
                    Permafrost.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity, (amplifier + 1) * 1.5f, amplifier);
                }
            }
            else if (entity.deathTime < 2) {
                Permafrost.iceExplosion(entity.getWorld(), entity.getBlockPos(), entity, (amplifier + 1) * 1.5f, amplifier);
            }
        }
        return true;
    }
}
