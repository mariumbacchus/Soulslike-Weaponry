package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.MaceOfSpadesHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MaceOfSpades extends ReturningKnightAttack {

    private boolean cordsRegistered;
    private BlockPos targetPos;
    private final RotatableHitbox hitbox = MaceOfSpadesHitbox.createMaceHitboxPlaceholder();
    private final Set<UUID> hitEntities = new HashSet<>();

    private final float damageSwipe = EntityConfig.returning_knight_mace_of_spades_swipe_damage;
    private final float damageSmash = EntityConfig.returning_knight_mace_of_spades_smash_damage;

    public MaceOfSpades(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        MaceOfSpadesHitbox.updateMaceHitbox(this.hitbox, this.getBoss(), attackStatus);
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        this.getBoss().getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
        this.getBoss().getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);

        if (MaceOfSpadesHitbox.isDamageTickSwipe(this.getBoss(), attackStatus)) {
            List<LivingEntity> entities = this.hitbox.getIntersectingTargets(this.getWorld(), this.getBoss());
            for (LivingEntity living : entities) {
                if (!this.hitEntities.add(living.getUuid())) {
                    continue;
                }
                living.takeKnockback(2f, -(living.getX() - this.getBoss().getX()), -(living.getZ() - this.getBoss().getZ()));
                this.damageTarget(living, this.damageSwipe);
            }
            if (this.isTick(attackStatus, 14)) {
                this.playSound(this.targetPos, SoundRegistry.KNIGHT_SWIPE_EVENT, 1f);
            }
        }
        if (this.isTick(attackStatus, 26) && target.getBlockPos() != null) {
            this.targetPos = target.getBlockPos();
            this.getBoss().setObliterateTarget(this.targetPos);
            this.hitEntities.clear();
        }
        if (MaceOfSpadesHitbox.isDamageTickSmash(this.getBoss(), attackStatus) && this.targetPos != null) {
            List<LivingEntity> entities = this.hitbox.getIntersectingTargets(this.getWorld(), this.getBoss());
            for (LivingEntity living : entities) {
                if (!this.hitEntities.add(living.getUuid())) {
                    continue;
                }
                living.addVelocity(0, 1, 0);
                this.damageTarget(living, this.damageSmash);
            }
        }
        if (this.isTick(attackStatus, 43)) {
            Vec3d effectPos = BossHitboxHelper.findGroundImpactPos(this.getWorld(), this.hitbox.getCenter(), 2);
            this.playSound(BlockPos.ofFloored(effectPos), SoundRegistry.NIGHTFALL_BONK_EVENT, 2f);
            ParticleHandler.particleOutburstMap(this.getWorld(), 300, effectPos.x, effectPos.y, effectPos.z, ParticleEvents.OBLITERATE_MAP, 1f);
        }
    }

    @Override
    public void initiateAttackVariables(LivingEntity target) {
        this.targetPos = target.getBlockPos();
        this.cordsRegistered = true;
        this.getBoss().setObliterateTarget(this.targetPos);
        this.hitEntities.clear();
        this.getBoss().setAttackStartWorldTime(this.getWorld().getTime());
    }

    @Override
    public void resetAttackVariables() {
        this.cordsRegistered = false;
        this.hitEntities.clear();
        this.getBoss().setAttackStartWorldTime(-1L);
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 75D && target.getBlockPos() != null && !this.cordsRegistered;
    }
}
