package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1.MaceOfSpades3;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.shadow.MaceOfSpadesShadowHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.shadow.MaceOfSpadesShadowTimings;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.entity.projectile.ShadowOrb;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;

public class MaceOfSpades3Shadow extends MaceOfSpades3 {

    public final RotatableHitbox shadowHitbox = BossHitboxHelper.createPlaceholder(MaceOfSpadesShadowTimings.MACE_SIZE_3);
    private final float swipeDamage = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_swipe_damage;
    private final float spinDamage = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_spin_damage;
    private final float thrustDamage = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_thrust_damage;
    private final int shadowOrbAmountPerTick = (int) EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_spin_shadow_orb_amount_per_tick;
    private final float shadowOrbDamage = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_spin_shadow_orb_damage;
    private final float shadowOrbSpeed = EntityConfig.returning_knight_mace_of_spades_3_phase_2_shadow_spin_shadow_orb_speed;

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
            Vec3d outward = new Vec3d(spawnPos.x - this.getBoss().getX(), 0.0D, spawnPos.z - this.getBoss().getZ()).normalize();
            float spacingDegrees = 6.0F;
            for (int i = 0; i < this.shadowOrbAmountPerTick; i++) {
                // Add deviation along x/z but ignore y-axis to avoid orbs crashing into ground
                float centeredIndex = i - (this.shadowOrbAmountPerTick - 1) / 2f;
                float angle = centeredIndex * spacingDegrees * MathHelper.RADIANS_PER_DEGREE;
                Vec3d direction = outward.rotateY(angle);
                // TODO use bigger shadow orbs that go through walls, same ones used for bullet hell phase
                ShadowOrb orb = new ShadowOrb(this.getWorld(), this.getBoss(), direction, List.of(StatusEffects.SLOWNESS), this.shadowOrbDamage);
                orb.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
                orb.setVelocity(direction.x, 0.0D, direction.z, this.shadowOrbSpeed, 0.0F);
                this.getWorld().spawnEntity(orb);
            }
            this.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 3f, 0.5f);
        }
        if (MaceOfSpadesShadowHitbox.isDamageThrust(this.getBoss(), attackStatus)) {
            this.doMaceDamage(this.shadowHitbox, this.getWorld().getDamageSources().mobAttack(this.getBoss()), this.thrustDamage, living -> {
                target.takeKnockback(2f, -(target.getX() - this.getBoss().getX()), -(target.getZ() - this.getBoss().getZ()));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 100, 1));
            });
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
        if (this.isAnyTick(attackStatus, 28, 35, 42, 55, 56, 58, 59, 64, 76)) {
            this.hitEntities.clear();
        }
    }

    @Override
    public boolean isAvailable() {
        return this.getBoss().isPhaseTwo();
    }
}
