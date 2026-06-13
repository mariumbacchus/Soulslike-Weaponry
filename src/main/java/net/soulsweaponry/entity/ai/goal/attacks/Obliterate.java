package net.soulsweaponry.entity.ai.goal.attacks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.ReturningKnightHitboxes;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Obliterate extends ReturningKnightAttack {

    private boolean cordsRegistered = false;
    private final Set<UUID> obliterateHitEntities = new HashSet<>();
    private boolean obliterateImpactDone;
    private BlockPos targetPos;
    private final RotatableHitbox obliterateMaceHitbox = ReturningKnightHitboxes.createObliterateMaceHitboxPlaceholder();
    private final float damage = EntityConfig.returning_knight_obliterate_damage;

    public Obliterate(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        ReturningKnightHitboxes.updateObliterateMaceHitbox(this.obliterateMaceHitbox, this.getBoss(), this.targetPos, attackStatus);
        this.getBoss().getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
        this.getBoss().getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (ReturningKnightHitboxes.isObliterateDamageTick(attackStatus)) {
            List<LivingEntity> entities = this.obliterateMaceHitbox.getIntersectingTargets(this.getWorld(), this.getBoss());
            for (LivingEntity living : entities) {
                if (!this.obliterateHitEntities.add(living.getUuid())) {
                    continue;
                }
                this.damageTarget(DamageSourceRegistry.OBLITERATED, living, this.damage);
                living.setVelocity(living.getVelocity().x, 1.0, living.getVelocity().z);
                if (living.isDead() && this.getGoal().isValidSpawn(living.getBlockPos())) {
                    this.obliterateHitEntities.add(this.getGoal().summonAllies(living.getPos(), false));
                    if (this.getBoss().getHealth() <= this.getBoss().getMaxHealth() / 2.0F) {
                        this.obliterateHitEntities.add(this.getGoal().summonAllies(living.getPos(), false));
                    }
                }
            }
            if (!this.obliterateImpactDone && attackStatus >= 37) {
                this.obliterateImpactDone = true;
                Vec3d effectPos = BossHitboxHelper.findGroundImpactPos(this.getWorld(), this.obliterateMaceHitbox.getCenter(), 2);
                this.getWorld().playSound(null, BlockPos.ofFloored(effectPos), SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.HOSTILE, 3f, 1f);
                ParticleHandler.particleOutburstMap(this.getWorld(), 300, effectPos.x, effectPos.y, effectPos.z, ParticleEvents.OBLITERATE_MAP, 1f);
            }
        }
        if (ReturningKnightHitboxes.isObliterateDamageTick(attackStatus)) {
            BossHitboxHelper.breakBlocksInsideHitbox(this.obliterateMaceHitbox.copy().offsetWorld(0, ReturningKnightHitboxes.OBLITERATE_MACE_SIZE.y / 2.0D, 0), this.getWorld(), this.getBoss(), this.getBoss()::canDestroy, false);
        }
    }

    @Override
    public void initiateAttackVariables(LivingEntity target) {
        this.targetPos = target.getBlockPos();
        this.cordsRegistered = true;
        this.obliterateImpactDone = false;
        this.obliterateHitEntities.clear();
        this.getBoss().setAttackStartWorldTime(this.getWorld().getTime());
        this.getBoss().setObliterateTarget(this.targetPos);
    }

    @Override
    public void resetAttackVariables() {
        this.cordsRegistered = false;
        this.getBoss().setAttackStartWorldTime(-1L);
        this.obliterateHitEntities.clear();
        this.obliterateImpactDone = false;
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 100D && target.getBlockPos() != null && !this.cordsRegistered;
    }
}
