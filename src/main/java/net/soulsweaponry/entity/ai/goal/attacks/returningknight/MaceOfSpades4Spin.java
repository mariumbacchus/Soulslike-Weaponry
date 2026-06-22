package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.MaceOfSpadesHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class MaceOfSpades4Spin extends MaceAttack {

    private final float swipeDamage = EntityConfig.returning_knight_mace_of_spades_4_swipe_damage;
    private final float spinDamage = EntityConfig.returning_knight_mace_of_spades_4_spin_damage;
    private final float thrustDamage = EntityConfig.returning_knight_mace_of_spades_4_thrust_damage;

    public MaceOfSpades4Spin(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        MaceOfSpadesHitbox.updateMaceHitbox(this.hitbox, this.getBoss(), attackStatus);
        if (MaceOfSpadesHitbox.isDamageTickSwipe(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.swipeDamage, 0.5f);
        }
        if (MaceOfSpadesHitbox.isDamageSpin(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.spinDamage, 0.6f);
        }
        if (MaceOfSpadesHitbox.isDamageThrust(this.getBoss(), attackStatus)) {
            this.doMaceDamage(this.getWorld().getDamageSources().mobAttack(this.getBoss()), this.thrustDamage, living -> {
                target.takeKnockback(2f, -(target.getX() - this.getBoss().getX()), -(target.getZ() - this.getBoss().getZ()));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 1));
            });
        }
        if (this.isAnyTick(attackStatus, 21, 42)) {
            this.playSound(SoundRegistry.KNIGHT_SWIPE_EVENT, 3f, 0.75f, 1f);
        }
        if (this.isAnyTick(attackStatus, 43, 47, 50)) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 3f, 0.5f);
        }
        if (this.isTick(attackStatus, 71)) {
            this.playSound(SoundRegistry.KNIGHT_SWIPE_EVENT, 3f, 0.75f, 1f);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 3f, 0.5f);
        }
        if (this.isAnyTick(attackStatus, 32, 44, 54)) {
            this.hitEntities.clear();
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 80 && target.getBlockPos() != null && !this.cordsRegistered;
    }
}
