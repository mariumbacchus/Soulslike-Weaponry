package net.soulsweaponry.entity.ai.goal;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.AccursedLordBoss;
import net.soulsweaponry.entity.mobs.AccursedLordBoss.AccursedLordAnimations;
import net.soulsweaponry.entity.projectile.ShadowOrb;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class AccursedLordGoal extends Goal {
    private final AccursedLordBoss boss;
    private int attackCooldown;
    private int attackStatus;
    private BlockPos attackPos;
    private boolean cordsRegistered;
    private int targetNotVisibleTicks;
    private int specialCooldown;
    private int lavaRadius = 4;
    private int lavaTimer;

    public AccursedLordGoal(AccursedLordBoss boss) {
        this.boss = boss;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.boss.getTarget();
        return target != null && target.isAlive() && this.boss.canTarget(target);
    }

    public void resetAttackCooldown(float cooldownModifier) {
        this.attackStatus = 0;
        this.cordsRegistered = false;
        this.attackCooldown = (int) Math.floor(ConfigConstructor.decaying_king_attack_cooldown_ticks * cooldownModifier) - this.boss.getReducedCooldownAttackers();
    }

    public void resetSpecialCooldown(float cooldownModifier) {
        this.specialCooldown = (int) (Math.floor(ConfigConstructor.decaying_king_special_cooldown_ticks * cooldownModifier) - this.boss.getReducedCooldownAttackers());
    }

    public float getModifiedDamage(float damage) {
        return damage * ConfigConstructor.decaying_king_damage_modifier;
    }

    private void damageTarget(LivingEntity target, DamageSource source, float amount) {
        if (target.damage(source, this.getModifiedDamage(amount))) {
            this.boss.heal(this.getModifiedDamage(amount)/5 + this.boss.getAttackers().size()*2);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.boss.setAttackAnimation(AccursedLordAnimations.IDLE);
        this.attackCooldown = 10;
        this.attackStatus = 0;
        this.cordsRegistered = false;
        this.boss.removePlacedLava();
        this.lavaRadius = 4;
        this.lavaTimer = 0;
    }

    /**
     * Checks if the specified attack animation is idle or the target is null, it will then launch IDLE animation.
     * <p>
     * Gets a random attack and stores it in a variable, if {@code specificAttack} is not null, the random attack
     * will be replaced with it. If the attack equals {@code DEATH, SPAWN or IDLE} it will also replace it with
     * a {@code SWORD_SLAM} attack.
     * <p>
     * After that, a switch statement checks other details for the attack to pass through; Is the target close enough?
     * Is the blockPos null? Has it taken too much time for anything to happen?
     * Each check varies based on chosen attack. If it fails the check, it will pass the {@code IDLE}
     * animation until a proper attack is chosen. Some checks have a time limit, and will set a new random attack
     * if the timer runs out, namely {@code attackCooldown} being under a threshold.
     */
    public void checkAndSetAttack(@Nullable AccursedLordAnimations specificAttack, LivingEntity target) {
        if (target == null || (specificAttack != null && specificAttack.equals(AccursedLordAnimations.IDLE))) {
            this.boss.setAttackAnimation(specificAttack);
            return;
        }
        int rand = this.boss.getRandom().nextInt(AccursedLordAnimations.values().length);
        AccursedLordAnimations attack = AccursedLordAnimations.values()[rand];
        if (specificAttack != null) {
            attack = specificAttack;
        } else if (attack.equals(AccursedLordAnimations.DEATH) || attack.equals(AccursedLordAnimations.SPAWN) || attack.equals(AccursedLordAnimations.IDLE)) {
            attack = AccursedLordAnimations.SWORDSLAM;
        }
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        switch (attack) {
            case FIREBALLS, WITHERBALLS -> {
                if (distanceToEntity < this.getFollowRange() * this.getFollowRange()) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case HAND_SLAM, HEATWAVE, SPIN -> {
                if (distanceToEntity < 30f && this.specialCooldown < 0) {
                    this.boss.setAttackAnimation(attack);
                } else if (this.attackCooldown < -30 || this.specialCooldown > 10) {
                    this.boss.setAttackAnimation(AccursedLordAnimations.IDLE);
                }
            }
            case PULL -> {
                if (distanceToEntity < this.getFollowRange() * this.getFollowRange() * 2 && distanceToEntity > 60f) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case SWORDSLAM -> {
                if (distanceToEntity < 40f && !this.cordsRegistered && target.getBlockPos() != null) {
                    this.attackPos = target.getBlockPos();
                    this.cordsRegistered = true;
                    this.boss.setAttackAnimation(attack);
                } else if (this.attackCooldown < -30) {
                    this.boss.setAttackAnimation(AccursedLordAnimations.IDLE);
                }
            }
            default -> this.boss.setAttackAnimation(AccursedLordAnimations.IDLE);
        }
    }

    private double getFollowRange() {
        return this.boss.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
    }

    public void tick() {
        this.attackCooldown--;
        this.specialCooldown--;

        if (this.lavaTimer > 0) {
            this.lavaTimer--;
            if (this.lavaTimer <= 0) {
                this.boss.removePlacedLava();
            }
        }

        LivingEntity target = this.boss.getTarget();
        if (target != null && !this.boss.getAttackAnimation().equals(AccursedLordAnimations.SPAWN) && !this.boss.getAttackAnimation().equals(AccursedLordAnimations.DEATH)) {
            this.boss.setAttacking(true);
            this.boss.getLookControl().lookAt(target.getX(), target.getEyeY(), target.getZ());
            boolean entityInSight = this.boss.getVisibilityCache().canSee(target);
            if (entityInSight) {
                this.targetNotVisibleTicks = 0;
            } else {
                ++this.targetNotVisibleTicks;
            }
            double distanceToEntity = this.boss.squaredDistanceTo(target);

            if (this.attackCooldown > 0) this.boss.setAttackAnimation(AccursedLordAnimations.IDLE);

            if (this.attackCooldown < 0 && this.boss.getAttackAnimation().equals(AccursedLordAnimations.IDLE)) {
                this.checkAndSetAttack(null, target);
            }
            switch (this.boss.getAttackAnimation()) {
                case FIREBALLS -> this.projectileBarrage(target, distanceToEntity, BarrageProjectiles.FIREBALLS);
                case WITHERBALLS -> this.projectileBarrage(target, distanceToEntity, BarrageProjectiles.WITHERBALLS);
                case HEATWAVE -> this.heatWaveAttack();
                case PULL -> this.pullAttack(target);
                case SPIN -> this.spinAttack();
                case SWORDSLAM -> this.swordSlam();
                case HAND_SLAM -> this.handSlamLava();
                default -> this.boss.setAttackAnimation(AccursedLordAnimations.IDLE);
            }

            if (this.targetNotVisibleTicks < 5) {
                this.boss.getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 1.0D);
            }
            super.tick();
        }
    }

    private void handSlamLava() {
        this.attackStatus++;
        if (this.attackStatus == 13 || this.attackStatus == 18 || this.attackStatus == 24) {
            this.summonLava(lavaRadius);
            if (!this.boss.getWorld().isClient) {
                ParticleHandler.particleOutburstMap(this.boss.getWorld(), 200, this.boss.getX(), this.boss.getY(), this.boss.getZ(), ParticleEvents.DARKIN_BLADE_SLAM_MAP, 1f);
            }
            this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
            this.lavaRadius++;
        }
        if (this.attackStatus >= 25) {
            this.resetAttackCooldown(.5f);
            this.resetSpecialCooldown(1.2f);
            this.checkAndSetAttack(null, this.boss.getTarget());
            this.lavaRadius = 4;
        }
    }

    private void summonLava(int r) {
        for (int theta = 0; theta < 360; theta += 15) {
            double x0 = this.boss.getX();
            double z0 = this.boss.getZ();
            double x = x0 + r * Math.cos(theta * Math.PI / 180);
            double z = z0 + r * Math.sin(theta * Math.PI / 180);
            BlockPos pos = BlockPos.ofFloored(x, this.boss.getY(), z);
            if (this.boss.getWorld().getBlockState(pos).isAir()) {
                this.boss.getWorld().setBlockState(pos, Blocks.LAVA.getDefaultState());
                this.boss.lavaPos.add(pos);
            }
        }
        this.lavaTimer = 30;
    }

    private void spinAttack() {
        this.attackStatus++;
        if (this.attackStatus >= 7 && this.attackStatus <= 40) {
            Box chunkBox = new Box(this.boss.getBlockPos()).expand(6);
            List<Entity> nearbyEntities = this.boss.getWorld().getOtherEntities(this.boss, chunkBox);
            for (Entity nearbyEntity : nearbyEntities) {
                if (nearbyEntity instanceof LivingEntity closestTarget) {
                    double x = closestTarget.getX() - (this.boss.getX());
                    double z = closestTarget.getZ() - this.boss.getZ();
                    this.damageTarget(closestTarget, this.boss.getWorld().getDamageSources().mobAttack(this.boss), 10f);
                    closestTarget.takeKnockback(4F, -x, -z);
                }
            }
        }
        if (this.attackStatus >= 45) {
            this.resetAttackCooldown(.5f);
            this.resetSpecialCooldown(1.2f);
            this.checkAndSetAttack(null, this.boss.getTarget());
        }
    }

    private void heatWaveAttack() {
        this.attackStatus++;
        if (this.attackStatus >= 16 && this.attackStatus <= 18) {
            if (this.attackStatus == 17) {
                this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 5f, 1f);
            }
            Box chunkBox = new Box(this.boss.getBlockPos()).expand(5);
            List<Entity> nearbyEntities = this.boss.getWorld().getOtherEntities(this.boss, chunkBox);
            for (Entity nearbyEntity : nearbyEntities) {
                if (nearbyEntity instanceof LivingEntity closestTarget) {
                    double x = closestTarget.getX() - (this.boss.getX());
                    double z = closestTarget.getZ() - this.boss.getZ();
                    closestTarget.takeKnockback(10F, -x, -z);
                    this.damageTarget(closestTarget, this.boss.getWorld().getDamageSources().mobAttack(this.boss), 50f);
                }
            }
            if (!this.boss.getWorld().isClient) {
                ParticleHandler.particleSphere(this.boss.getWorld(), 1000, this.boss.getX(), this.boss.getY() + .4f, this.boss.getZ(), ParticleTypes.FLAME, 1f);
                ParticleHandler.particleOutburstMap(this.boss.getWorld(), 200, this.boss.getX(), this.boss.getY() + .1f, this.boss.getZ(), ParticleEvents.DAWNBREAKER_MAP, 1f);
            }
        }
        if (this.attackStatus >= 30) {
            this.resetAttackCooldown(1.5f);
            this.checkAndSetAttack(null, this.boss.getTarget());
            this.resetSpecialCooldown(1f);
        }
    }

    private void pullAttack(LivingEntity target) {
        this.attackStatus++;
        if (this.attackStatus == 20) {
            double x = target.getX() - (this.boss.getX());
            double z = target.getZ() - this.boss.getZ();
            this.damageTarget(target, this.boss.getWorld().getDamageSources().mobAttack(this.boss), 5f);
            target.takeKnockback(5F, x, z);
        }
        if (this.attackStatus >= 25) {
            this.attackStatus = 0;
            this.resetAttackCooldown(0.25f);
            this.checkAndSetAttack(null, this.boss.getTarget());
        }
    }

    private void swordSlam() {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(this.attackPos.getX(), this.attackPos.getY(), this.attackPos.getZ());
        this.boss.getNavigation().startMovingTo(this.attackPos.getX(), this.attackPos.getY(), this.attackPos.getZ(), 0.0D);
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));

        Box aoe = new Box(this.attackPos).expand(3);
        List<Entity> entities = this.boss.getWorld().getOtherEntities(this.boss, aoe);
        if (this.attackStatus == 17) {
            if (!this.boss.getWorld().isClient) {
                ParticleHandler.particleOutburstMap(this.boss.getWorld(), 200, this.attackPos.getX(), this.attackPos.getY(), this.attackPos.getZ(), ParticleEvents.DARKIN_BLADE_SLAM_MAP, 1f);
            }
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    this.damageTarget((LivingEntity) entity, CustomDamageSource.create(this.boss.getWorld(), CustomDamageSource.OBLITERATED, this.boss), 30f);
                    entity.setVelocity(entity.getVelocity().x, .3f, entity.getVelocity().z);
                }
            }
            this.boss.getWorld().playSound(null, this.attackPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
        }
        if (this.attackStatus >= 20) {
            this.resetAttackCooldown(.5f);
            this.cordsRegistered = false;
            this.boss.getNavigation().stop();
            this.checkAndSetAttack(null, this.boss.getTarget());
        }
    }

    private void projectileBarrage(LivingEntity target, double distanceToEntity, BarrageProjectiles entity) {
        int fireSprayCount = 8;
        this.attackStatus++;
        double e = target.getX() - (this.boss.getX());
        double f = target.getBodyY(0.5D) - this.boss.getBodyY(1.0D);
        double g = target.getZ() - this.boss.getZ();
        if (this.attackStatus % 2 == 0 && this.attackStatus > 8) {
            double h = Math.sqrt(Math.sqrt(distanceToEntity)) * 0.5D;
            this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 2f, 1f);
            for(int i = 0; i < fireSprayCount; ++i) {
                new SmallFireballEntity(this.boss.getWorld(), this.boss, e + this.boss.getRandom().nextGaussian() * h, f, g + this.boss.getRandom().nextGaussian() * h);
                ProjectileEntity projectile = switch (entity) {
                    case FIREBALLS ->
                            new SmallFireballEntity(this.boss.getWorld(), this.boss, e + this.boss.getRandom().nextGaussian() * h, f, g + this.boss.getRandom().nextGaussian() * h);
                    case WITHERBALLS ->
                            new ShadowOrb(this.boss.getWorld(), this.boss, e + this.boss.getRandom().nextGaussian() * h, f, g + this.boss.getRandom().nextGaussian() * h,
                                    new StatusEffect[] {StatusEffects.WITHER, EffectRegistry.DECAY.get()});
                };
                projectile.setPosition(projectile.getX(), this.boss.getBodyY(1.0D) - 1.5D, projectile.getZ());
                this.boss.getWorld().spawnEntity(projectile);
            }
        }
        if (this.attackStatus >= 30) {
            this.resetAttackCooldown(1);
            this.checkAndSetAttack(null, this.boss.getTarget());
        }
    }

    enum BarrageProjectiles {
        FIREBALLS,
        WITHERBALLS
    }
}