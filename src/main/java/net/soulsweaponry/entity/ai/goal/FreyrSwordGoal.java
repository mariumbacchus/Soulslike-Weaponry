package net.soulsweaponry.entity.ai.goal;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.EnumSet;

public class FreyrSwordGoal extends Goal {

    private final FreyrSwordEntity entity;
    private int attackTicks;
    private final double[][] hitFrames = {{6, 1.0}, {13, 1.0}, {20, 1.1}, {29, 1.25}}; //Frames and damage modifier
    /* private double[][] hitFrames = {
        {0.5417, 0.6667},
        {1.2083, 1.3333},
        {1.875, 2},
        {2.7917, 2.9167}
    }; */
    private final double animationFrameCap = 3.5D;
    
    public FreyrSwordGoal(FreyrSwordEntity entity) {
        this.entity = entity;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.entity.getTarget();
        return this.entity.getOwner() != null && target != null && target.isAlive() && this.entity.canTarget(target);
    }

    @Override
    public boolean shouldContinue() {
        if (this.entity.getOwner() != null && this.entity.getTarget() != null) {
            if (!this.entity.isBlockPosNullish(this.entity.getStationaryPos()) /* != null */ && this.entity.squaredDistanceTo(this.entity.getTarget()) > this.entity.getFollowRange()) {
                this.stop();
                return false;
            } else if (this.entity.getOwner().squaredDistanceTo(this.entity.getTarget()) > this.entity.getFollowRange()) {
                this.stop();
                return false;
            }
        }
        return super.shouldContinue();
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimationAttacking(false);
        this.attackTicks = 0;
        this.entity.setTarget(null);
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            this.attackTarget(target, this.entity.getWorld());
        }
    }

    private void attackTarget(LivingEntity target, World world) {
        this.attackTicks++;
        if (this.attackTicks >= this.animationFrameCap*10) this.attackTicks = 0;
        this.entity.getLookControl().lookAt(target);
        Vec3d vecTarget = this.entity.getRotationVector().add(target.getPos());
        this.entity.updatePosition(vecTarget.getX(), vecTarget.getY(), vecTarget.getZ());
        this.entity.setAnimationAttacking(true);
        for (double[] hitFrame : this.hitFrames) {
            if (this.attackTicks == hitFrame[0]) {
                //target.damage(DamageSource.mobProjectile(this.entity, this.entity.getOwner()), this.entity.getAttackDamage(this.entity.getOwner()))
                if (target.damage(CustomDamageSource.create(this.entity.getWorld(), CustomDamageSource.FREYR_SWORD, this.entity, this.entity.getOwner()), (float) (this.getAttackDamage(target) * hitFrame[1]))) {
                    int fire;
                    if ((fire = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, this.entity.asItemStack())) > 0) {
                        target.setOnFireFor(fire * 4);
                    }
                    if (!world.isClient) {
                        ParticleHandler.singleParticle(this.entity.getWorld(), ParticleTypes.SWEEP_ATTACK, target.getX(), target.getEyeY(), target.getZ(), 0, 0, 0);
                    } else {
                        world.addParticle(ParticleTypes.SWEEP_ATTACK, true, target.getX(), target.getEyeY(), target.getZ(), 0, 0, 0);
                    }
                    world.playSound(null, this.entity.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, .8f, 1f);
                }
            }
        }
    }

    public float getAttackDamage(LivingEntity target) {
        return target != null ? (ConfigConstructor.sword_of_freyr_damage + EnchantmentHelper.getAttackDamage(this.entity.asItemStack(), target.getGroup())) : ConfigConstructor.sword_of_freyr_damage;
    }
}
