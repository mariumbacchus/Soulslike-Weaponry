package net.soulsweaponry.entity.ai.goal.attacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;

//TODO needs to be updated to use rotatable hitboxes, can maybe make obliterate super parent class with common vars
public class MaceOfSpades extends ReturningKnightAttack {

    private boolean cordsRegistered;
    private BlockPos targetPos;

    private final float damageSwipe = EntityConfig.returning_knight_mace_of_spades_swipe_damage;
    private final float damageSmash = EntityConfig.returning_knight_mace_of_spades_smash_damage;

    public MaceOfSpades(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        this.getBoss().getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
        this.getBoss().getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);

        Box aoe = new Box(targetPos.getX() - 5, targetPos.getY() - 2, targetPos.getZ() - 5, targetPos.getX() + 5, targetPos.getY() + 2, targetPos.getZ() + 5);
        List<Entity> entities = this.getWorld().getOtherEntities(this.getBoss(), aoe);
        if (attackStatus == 14) {
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.takeKnockback(2f, -(livingEntity.getX() - this.getBoss().getX()), -(livingEntity.getZ() - this.getBoss().getZ()));
                    this.damageTarget(livingEntity, this.damageSwipe);
                }
            }
            this.getWorld().playSound(null, this.targetPos, SoundRegistry.KNIGHT_SWIPE_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        }
        if (attackStatus == 26 && target.getBlockPos() != null) {
            this.targetPos = target.getBlockPos();
            this.getBoss().setObliterateTarget(this.targetPos);
        }
        if (attackStatus == 42 && this.targetPos != null) {
            entities = this.getWorld().getOtherEntities(this.getBoss(), new Box(this.targetPos).expand(3D));
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.addVelocity(0, 1, 0);
                    this.damageTarget(livingEntity, this.damageSmash);
                }
            }
            this.playSound(this.targetPos, SoundRegistry.NIGHTFALL_BONK_EVENT, 1f);
            if (!this.getWorld().isClient) {
                ParticleHandler.particleOutburstMap(this.getWorld(), 300, this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
            }
        }
    }

    @Override
    public void initiateAttackVariables(LivingEntity target) {
        this.targetPos = target.getBlockPos();
        this.getBoss().setObliterateTarget(this.targetPos);
        this.cordsRegistered = true;
    }

    @Override
    public void resetAttackVariables() {
        this.cordsRegistered = false;
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 75D && target.getBlockPos() != null && !this.cordsRegistered;
    }
}
