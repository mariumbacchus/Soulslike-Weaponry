package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1.MaceOfSpades3;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.shadow.MaceOfSpadesShadowHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.shadow.MaceOfSpadesShadowTimings;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class MaceOfSpades3Shadow extends MaceOfSpades3 {

    public final RotatableHitbox shadowHitbox = BossHitboxHelper.createPlaceholder(MaceOfSpadesShadowTimings.MACE_SIZE_3);
    private final float swipeDamage = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_swipe_damage;
    private final float spinDamage = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_spin_damage;
    private final float thrustDamage = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_thrust_damage;
    private final int darknessOrbAmountPerTick = (int) EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_spin_darkness_orb_amount_per_tick;
    private final float darknessOrbDamage = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_spin_darkness_orb_damage;
    private final float darknessOrbSpeed = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_spin_darkness_orb_speed;

    public MaceOfSpades3Shadow(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        MaceOfSpadesShadowHitbox.updateMaceHitbox(this.shadowHitbox, this.getBoss(), attackStatus);
        if (MaceOfSpadesShadowHitbox.isDamageTickSwipe(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.shadowHitbox, this.swipeDamage, 0.5f);
        }
        if (MaceOfSpadesShadowHitbox.isDamageSpin(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.shadowHitbox, this.spinDamage, 0.6f);
            // Shadow orbs out of the mace during spin based on mace facing direction
            Vec3d spawnPos = this.shadowHitbox.getCenter();
            this.spawnDarknessOrb(spawnPos, this.darknessOrbAmountPerTick, this.darknessOrbDamage, this.darknessOrbSpeed);
        }
        if (MaceOfSpadesShadowHitbox.isDamageThrust(this.getBoss(), attackStatus)) {
            this.doMaceDamage(this.shadowHitbox, this.getWorld().getDamageSources().mobAttack(this.getBoss()), this.thrustDamage, living -> {
                target.takeKnockback(2f, -(target.getX() - this.getBoss().getX()), -(target.getZ() - this.getBoss().getZ()));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 1));
            }, false);
        }
        if (this.isAnyTick(attackStatus, 21, 52)) {
            this.playSound(SoundRegistry.KNIGHT_SWIPE_EVENT, 3f, 0.75f, 1f);
        }
        if (this.isAnyTick(attackStatus, 53, 57, 60)) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 3f, 0.5f);
        }
        if (this.isTick(attackStatus, 81)) {
            this.playSound(SoundRegistry.KNIGHT_SWIPE_EVENT, 3f, 0.75f, 1f);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 3f, 0.5f);
        }
    }

    @Override
    public boolean isAvailable() {
        return this.getBoss().isPhaseTwo();
    }
}
