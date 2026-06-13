package net.soulsweaponry.entity.ai.goal.attacks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

public class BlindingLight extends ReturningKnightAttack {

    private final float damage = EntityConfig.returning_knight_blinding_light_damage;

    public BlindingLight(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (attackStatus == 24 && distanceToTarget < 30) {
            double x = target.getX() - this.getBoss().getX();
            double z = target.getZ() - this.getBoss().getZ();
            this.damageTarget(target, this.damage);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0));
            target.takeKnockback(2f, -x, -z);
            this.playSound(target.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_FALL, 1f);
            if (!this.getWorld().isClient) {
                ParticleHandler.particleOutburstMap(this.getWorld(), 150, target.getX(), target.getEyeY(), target.getZ(), ParticleEvents.BLINDING_LIGHT_MAP, 1f);
            }
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 40D;
    }
}
