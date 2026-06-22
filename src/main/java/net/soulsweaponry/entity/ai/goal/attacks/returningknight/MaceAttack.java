package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class MaceAttack extends ReturningKnightAttack {

    public boolean cordsRegistered = false;
    public final Set<UUID> hitEntities = new HashSet<>();
    public BlockPos targetPos;
    public final RotatableHitbox hitbox = BossHitboxHelper.createPlaceholder(new Vec3d(6, 5, 6));

    public MaceAttack(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        this.getBoss().getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
        this.getBoss().getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
    }

    /**
     * Does damage to all mobs intersecting with the mace hitbox.
     * @param damageSource damage source
     * @param damage damage
     * @param targetConsumer consumer to trigger on each target hit, such as applying knockback
     */
    public void doMaceDamage(DamageSource damageSource, float damage, Consumer<LivingEntity> targetConsumer) {
        List<LivingEntity> entities = this.hitbox.getIntersectingTargets(this.getWorld(), this.getBoss());
        for (LivingEntity living : entities) {
            if (!this.hitEntities.add(living.getUuid())) {
                continue;
            }
            living.damage(damageSource, this.getGoal().getModifiedDamage(damage));
            targetConsumer.accept(living);
        }
    }

    /**
     * Does damage to all mobs intersecting with the mace hitbox, knocking targets hit up in the air
     * and summoning Remnants if the target dies. Damage source will be "OBLITERATED".
     */
    public void doObliterateDamage(float damage) {
        this.doMaceDamage(DamageSourceRegistry.create(this.getWorld(), DamageSourceRegistry.OBLITERATED, this.getBoss()), damage, target -> {
            target.addVelocity(0, 1, 0);
            if (target.isDead() && this.getGoal().isValidSpawn(target.getBlockPos())) {
                this.hitEntities.add(this.getGoal().summonAllies(target.getPos(), false));
                if (this.getBoss().getHealth() <= this.getBoss().getMaxHealth() / 2.0F) {
                    this.hitEntities.add(this.getGoal().summonAllies(target.getPos(), false));
                }
            }
        });
    }

    /**
     * Does damage to all mobs intersecting with the mace hitbox, knocking targets
     * back based on the strength parameter.
     */
    public void doSwingDamage(float damage, float knockbackStrength) {
        this.doMaceDamage(this.getWorld().getDamageSources().mobAttack(this.getBoss()), damage, target -> {
            target.takeKnockback(knockbackStrength, -(target.getX() - this.getBoss().getX()), -(target.getZ() - this.getBoss().getZ()));
        });
    }

    public void doObliterateParticles() {
        Vec3d effectPos = BossHitboxHelper.findGroundImpactPos(this.getWorld(), this.hitbox.getCenter(), 2);
        this.playSound(BlockPos.ofFloored(effectPos), SoundRegistry.NIGHTFALL_BONK_EVENT, 3f);
        ParticleHandler.particleOutburstMap(this.getWorld(), 300, effectPos.x, effectPos.y, effectPos.z, ParticleEvents.OBLITERATE_MAP, 1f);
    }

    public void doGroundScrapeParticles() {
        Vec3d center = this.hitbox.getCenter();
        Vec3d effectPos = BossHitboxHelper.findGroundImpactPos(this.getWorld(), center, 2);
        Vec3d forward = BossHitboxHelper.getForwardFromYaw(this.getBoss().bodyYaw);
        Vec3d right = new Vec3d(0.0D, 1.0D, 0.0D).crossProduct(forward).normalize();
        for (int i = -2; i <= 2; i++) {
            Vec3d pos = effectPos.add(right.multiply(i * 0.8D));
            ParticleHandler.particleOutburstMap(this.getWorld(), 20, pos.x, pos.y + 0.1D, pos.z, ParticleEvents.MACE_SCRAPE_MAP, 1.5f);
        }
    }

    /**
     * Clears the set of hit targets so that targets can get hit again by the hitbox, and saves the targets block
     * position to {@link #targetPos} and {@link ReturningKnight#getObliterateTarget()} for a future attack.
     */
    public void prepareNextMaceHit(LivingEntity target) {
        if (target.getBlockPos() != null) {
            this.targetPos = target.getBlockPos();
            this.getBoss().setObliterateTarget(this.targetPos);
            this.hitEntities.clear();
        }
    }

    @Override
    public void initiateAttackVariables(LivingEntity target) {
        this.targetPos = target.getBlockPos();
        this.cordsRegistered = true;
        this.hitEntities.clear();
        this.getBoss().setAttackStartWorldTime(this.getWorld().getTime());
        this.getBoss().setObliterateTarget(this.targetPos);
    }

    @Override
    public void resetAttackVariables() {
        this.cordsRegistered = false;
        this.getBoss().setAttackStartWorldTime(-1L);
        this.hitEntities.clear();
    }
}
