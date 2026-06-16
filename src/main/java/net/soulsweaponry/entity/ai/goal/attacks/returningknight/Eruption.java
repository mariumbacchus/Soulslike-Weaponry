package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.List;

public class Eruption extends ReturningKnightAttack {

    private final float attackRangeBlocks = EntityConfig.returning_knight_eruption_range_blocks;
    private final float damage = EntityConfig.returning_knight_eruption_damage;

    public Eruption(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        Box aoe = new Box(this.getBoss().getX() - this.attackRangeBlocks, this.getBoss().getY() - this.attackRangeBlocks/2, this.getBoss().getZ() - this.attackRangeBlocks, this.getBoss().getX() + this.attackRangeBlocks, this.getBoss().getY() + this.attackRangeBlocks/2, this.getBoss().getZ() + this.attackRangeBlocks);
        List<Entity> entities = this.getBoss().getWorld().getOtherEntities(this.getBoss(), aoe);
        if (this.isTick(attackStatus, 42) || this.isTick(attackStatus, 66)) {
            for (Entity entity : entities) {
                this.playSound(entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f);
            }
        }
        if (this.isTick(attackStatus, 104)) {
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    this.damageTarget(living, this.damage);
                    entity.setVelocity(entity.getVelocity().x, 1.5f, entity.getVelocity().z);
                    this.playSound(entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1f);
                    if (!this.getWorld().isClient) {
                        ParticleHandler.particleOutburstMap(this.getWorld(), 300, entity.getX(), entity.getY(), entity.getZ(), ParticleEvents.GROUND_RUPTURE_MAP, 1f);
                    }
                }
            }
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return true;
    }
}
