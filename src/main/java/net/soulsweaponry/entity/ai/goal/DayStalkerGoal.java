package net.soulsweaponry.entity.ai.goal;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.DayStalker;
import net.soulsweaponry.entity.mobs.WarmthEntity;
import net.soulsweaponry.entity.projectile.GrowingFireball;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.entity.projectile.UntargetableFireball;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import net.soulsweaponry.util.ParticleNetworking;
import org.jetbrains.annotations.Nullable;

public class DayStalkerGoal extends MeleeAttackGoal {

    private final DayStalker boss;
    private int attackCooldown;
    private int specialCooldown;
    private int attackStatus;
    private int attackLength;
    private final int[] flamethrowerMeleeFrames = {18, 27, 41, 50, 58, 63};
    private float attackRotation;
    private double targetMaxY;
    private double targetY;
    private boolean hasExploded;
    //private int flightTimer; // Testing only.
    private int changeFlightTargetTimer;
    private Vec3d flightPosAdder;

    public DayStalkerGoal(DayStalker boss, double speed, boolean pauseWhenMobIdle) {
        super(boss, speed, pauseWhenMobIdle);
        this.boss = boss;
    }

    /*
     * When issuing an attack, it periodically syncronises with the boss how much time that
     * is left before the animation is over. This way, if it is interrupted, the animation will go on,
     * and the next attack will have its cooldown set to how much time was left until the animation
     * is finished.
     *
     * Example:
     * Day Stalker uses an attack with 60 ticks as animation time, but the target gets in creative mode
     * or dies at 25 ticks. 35 ticks is saved in the boss class and reduced in the tickMovement or mobTick
     * method. When the AI kicks up again, it checks whether that saved cooldown is being reduced or not
     * before doing any further action.
     */

    @Override
    public boolean canStart() {
        if (this.boss.isFlying() && this.boss.getTarget() != null) {
            return true;
        }
        return super.canStart();
    }

    @Override
    public void stop() {
        super.stop();
        int remainingTicks = this.attackLength - this.attackStatus;
        if (remainingTicks > 0) this.boss.setWaitAnimation(true);
        this.boss.setRemainingAniTicks(remainingTicks);
        this.attackCooldown = 20;
        this.specialCooldown = 20;
        this.attackStatus = 0;
        this.attackLength = 0;
        this.boss.setParticleState(0);
        this.hasExploded = false;
        this.boss.setFlying(false);
        this.changeFlightTargetTimer = 0;
    }

    private void checkAndSetAttack(LivingEntity target) {
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        int rand = this.boss.getRandom().nextInt(DayStalker.ATTACKS_LENGTH);
        DayStalker.Attacks attack = DayStalker.Attacks.values()[rand];
        switch (attack) {
            case AIR_COMBUSTION -> this.boss.setAttackAnimation(attack);
            case BLAZE_BARRAGE, SUNFIRE_RUSH -> {
                if ((this.boss.isFlying() || this.boss.isPhaseTwo()) && !this.isInMeleeRange(target)) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case CONFLAGRATION -> {
                if (this.boss.isFlying() || (this.boss.isPhaseTwo() && this.specialCooldown <= 0)) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case DECIMATE, DAWNBREAKER -> {
                if ((this.isInMeleeRange(target) && !this.boss.isFlying()) || this.boss.isPhaseTwo()) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case FLAMES_EDGE -> {
                if (distanceToEntity < (this.boss.isPhaseTwo() ? 80D : 55D) && !this.boss.isFlying()) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case CHAOS_STORM -> {
                if (!this.boss.isFlying() && distanceToEntity < 80D && this.specialCooldown <= 0) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case FLAMETHROWER -> {
                if ((this.boss.isFlying() || this.boss.isPhaseTwo()) && distanceToEntity < 260D) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case RADIANCE -> {
                if (this.boss.isPhaseTwo()) {
                    if (this.isInMeleeRange(target) && this.specialCooldown <= 0) {
                        this.boss.setAttackAnimation(attack);
                    }
                }
            }
            case WARMTH -> {
                if (this.boss.isPhaseTwo() && this.specialCooldown <= 0) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case OVERHEAT, INFERNO -> {
                if (this.boss.isPhaseTwo() && distanceToEntity < 200D) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            case FLAMES_REACH -> {
                if (!this.boss.isFlying() && distanceToEntity < (this.boss.isPhaseTwo() ? 140D : 120)) {
                    this.boss.setAttackAnimation(attack);
                }
            }
            default -> this.boss.setAttackAnimation(DayStalker.Attacks.IDLE);
        }
    }

    @Override
    public void tick() {
        if (this.boss.getRemainingAniTicks() > 0) {
            return;
        }
        if (this.boss.isInitiatingPhaseTwo()) {
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 255));
            return;
        }
        this.attackCooldown = Math.max(this.attackCooldown - 1, 0);
        this.specialCooldown = Math.max(this.specialCooldown - 1, 0);
        if (!this.boss.isFlying() && this.boss.shouldChaseTarget()) {
            super.tick();
        }
        //NOTE: For testing purposes only.
        /*
        *
        if (!this.boss.isPhaseTwo()) {
            if (this.flightTimer == 0 && this.boss.getRandom().nextInt(4 ) == 1) {
                this.flightTimer = 1;
                this.boss.setFlying(true);
            }
            if (this.flightTimer >= 1) {
                this.flightTimer++;
                if (flightTimer >= 300) {
                    this.boss.setFlying(false);
                    this.flightTimer = 0;
                }
            }
        }
        * */
        LivingEntity target = this.boss.getTarget();
        if (target != null) {
            if (this.boss.isFlying()) {
                this.moveAboveTarget(target);
            }
            double distanceToEntity = this.boss.squaredDistanceTo(target);
            if (this.attackCooldown <= 0 && this.boss.getAttackAnimation().equals(DayStalker.Attacks.IDLE)) {
                this.checkAndSetAttack(target);
            }
            switch (this.boss.getAttackAnimation()) {
                case AIR_COMBUSTION -> {
                    this.attackLength = 55;
                    this.airCombustion(target);
                }
                case DECIMATE -> {
                    this.attackLength = 60;
                    this.decimate(target);
                }
                case DAWNBREAKER -> {
                    this.attackLength = 40;
                    this.dawnbreaker(target);
                }
                case CHAOS_STORM -> {
                    this.attackLength = 90;
                    this.chaosStorm();
                }
                case FLAMETHROWER -> {
                    this.attackLength = 90;
                    this.flamethrower(target, distanceToEntity);
                }
                case SUNFIRE_RUSH -> {
                    this.attackLength = 117;
                    this.sunfireRush(target);
                }
                case CONFLAGRATION -> {
                    this.attackLength = this.boss.isPhaseTwo() ? 140 : 60;
                    this.conflagration(target);
                }
                case BLAZE_BARRAGE -> {
                    this.attackLength = 100;
                    this.blazeBarrage(target);
                }
                case FLAMES_EDGE -> {
                    this.attackLength = this.boss.isPhaseTwo() ? 90 : 60;
                    this.flamesEdge();
                }
                case RADIANCE -> {
                    this.attackLength = 120;
                    this.radiance();
                }
                case WARMTH -> {
                    this.attackLength = 100;
                    this.warmth();
                }
                case OVERHEAT -> {
                    this.attackLength = 100;
                    this.overheat(target);
                }
                case INFERNO -> {
                    this.attackLength = 160;
                    this.inferno();
                }
                case FLAMES_REACH -> {
                    this.attackLength = 60;
                    this.flamesReach(target, distanceToEntity);
                }
            }
        }
    }

    private Vec3d randomizeVecAdder() {
        return new Vec3d(this.boss.getRandom().nextBetween(3, 10) * (this.boss.getRandom().nextBoolean() ? -1 : 1), 0, this.boss.getRandom().nextBetween(3, 10) * (this.boss.getRandom().nextBoolean() ? -1 : 1));
    }

    private void moveAboveTarget(LivingEntity target) {
        this.boss.getLookControl().lookAt(target);
        this.boss.lookAtEntity(target, this.boss.getMaxLookYawChange(), this.boss.getMaxLookPitchChange());
        Vec3d vec3d = this.boss.getVelocity().multiply(0.8f, 0.6f, 0.8f);
        double d = vec3d.y;
        if (this.boss.getY() < target.getY() || this.boss.getY() < target.getY() + 6.0) {
            d = Math.max(0.0, d);
            d += 0.3 - d * (double)0.6f;
        }
        vec3d = new Vec3d(vec3d.x, d, vec3d.z);
        if (this.flightPosAdder != null) {
            this.changeFlightTargetTimer++;
            if (this.changeFlightTargetTimer >= 40) {
                this.flightPosAdder = this.randomizeVecAdder();
                this.changeFlightTargetTimer = 0;
            }
        } else {
            this.flightPosAdder = this.randomizeVecAdder();
        }
        Vec3d vec3d2 = new Vec3d(target.getX() - this.boss.getX() + this.flightPosAdder.x, 0.0, target.getZ() - this.boss.getZ() + this.flightPosAdder.z);
        if (vec3d2.horizontalLengthSquared() > 9.0) {
            Vec3d vec3d3 = vec3d2.normalize();
            vec3d = vec3d.add(vec3d3.x * 0.3 - vec3d.x * 0.6, 0.0, vec3d3.z * 0.3 - vec3d.z * 0.6);
        }
        this.boss.setVelocity(vec3d);
    }

    private void checkAndReset(int attackCooldown, int specialCooldown) {
        if (this.attackStatus > this.attackLength) {
            this.attackStatus = 0;
            this.attackCooldown = attackCooldown;
            if (specialCooldown != 0) this.specialCooldown = specialCooldown;
            this.attackLength = 0;
            this.boss.setAttackAnimation(DayStalker.Attacks.IDLE);
            this.boss.setChaseTarget(true);
            this.hasExploded = false;
        }
    }

    private float getModifiedDamage(float damage) {
        return damage * ConfigConstructor.day_stalker_damage_modifier;
    }

    private boolean damageTarget(LivingEntity target, float damage) {
        if (this.boss.isPartner(target) || target instanceof WarmthEntity) {
            return false;
        }
        if (target.damage(DamageSource.mob(this.boss), this.getModifiedDamage(damage))) {
            if (this.boss.isEmpowered()) {
                target.setOnFireFor(2);
            }
            return true;
        }
        return false;
    }

    private void airCombustion(LivingEntity target) {
        this.attackStatus++;
        if (this.attackStatus < 24 && target.getBlockPos() != null) {
            this.boss.setTargetPos(new BlockPos(target.getBlockX(), (int) target.getEyeY(), target.getBlockZ()));
        }
        this.boss.setChaseTarget(false);
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 255));
        this.boss.getLookControl().lookAt(this.boss.getTargetPos().toCenterPos());
        if (this.attackStatus >= 16 && this.attackStatus <= 25 && this.attackStatus % 4 == 0) {
            this.boss.world.playSound(null, this.boss.getTargetPos(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.HOSTILE, 1f, 1f);
            this.boss.world.playSound(null, this.boss.getTargetPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1f, 1f);
        }
        if (this.attackStatus >= 20 && this.attackStatus <= 36) {
            if (!this.boss.world.isClient) {
                ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.PRE_EXPLOSION_ID, this.boss.getTargetPos());
            }
        }
        if (this.attackStatus == 37) {
            if (!this.boss.world.isClient) {
                this.boss.world.playSound(null, this.boss.getTargetPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
                ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.AIR_COMBUSTION_ID, this.boss.getTargetPos(), 100);
                for (Entity entity : this.boss.world.getOtherEntities(this.boss, new Box(this.boss.getTargetPos()).expand(this.boss.isPhaseTwo() ? 2D : 1D))) {
                    if (entity instanceof LivingEntity living) {
                        this.damageTarget(living, 35f);
                    }
                }
            }
        }
        this.checkAndReset(5, 0);
    }

    private void decimate(LivingEntity target) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 255));
        this.boss.getLookControl().lookAt(target);
        if (this.attackStatus == 17) {
            this.playSound(this.boss.getBlockPos(), SoundRegistry.DAY_STALKER_DECIMATE, 1f, 1f);
        }
        if (this.attackStatus == 29) {
            if (this.isInMeleeRange(target)) {
                if (this.damageTarget(target, 30f)) {
                    target.addVelocity(0, 1D, 0);
                }
            }
            if (this.boss.isPhaseTwo()) {
                double d = Math.min(target.getY(), this.boss.getY());
                double e = Math.max(target.getY(), this.boss.getY()) + 1.0;
                float f = (float)MathHelper.atan2(target.getZ() - this.boss.getZ(), target.getX() - this.boss.getX());
                for (int i = 0; i < 16; ++i) {
                    double h = 1.25 * (double)(i + 1);
                    BlockPos p = this.conjureFlames(this.boss.getX() + (double)MathHelper.cos(f) * h, this.boss.getZ() + (double)MathHelper.sin(f) * h, d, e);
                    if (p != null) {
                        this.boss.world.setBlockState(p, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }
        if (this.attackStatus == 42) {
            boolean phase2 = this.boss.isPhaseTwo();
            this.shootSunlight(target, phase2 ? 1 : 0, phase2 ? 25f : 15f, MoonlightProjectile.RotationState.NORMAL);
        }
        this.checkAndReset(10, 0);
    }

    private void shootProjectile(LivingEntity target, ProjectileEntity projectile, SoundEvent sound) {
        double x = target.getX() - (this.boss.getX());
        double y = target.getEyeY() - this.boss.getBodyY(1f);
        double z = target.getZ() - this.boss.getZ();
        this.boss.world.playSound(null, this.boss.getBlockPos(), sound, SoundCategory.HOSTILE, 1f, 1f);
        if (target.getBlockPos() != null) this.boss.world.playSound(null, target.getBlockPos(), sound, SoundCategory.HOSTILE, 1f, 1f);
        projectile.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
        projectile.setVelocity(x, y, z, 1.5f, 1f);
        this.boss.world.spawnEntity(projectile);
    }

    /**
     * When used in a for loop, forms a line directly through the target. (See decimate() function when in phase 2)
     * @param x start X * rotation
     * @param z start Z * rotation
     * @param maxY max Y coordinate
     * @param y the greater Y coordinate between owner and target
     * @return Returns a BlockPos to be used in logic, for example to place fire or cause an eruption
     */
    private BlockPos conjureFlames(double x, double z, double maxY, double y) {
        BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
        boolean bl = false;
        double d = 0.0;
        do {
            VoxelShape voxelShape;
            BlockPos blockPos2;
            if (!this.boss.world.getBlockState(blockPos2 = blockPos.down()).isSideSolidFullSquare(this.boss.world, blockPos2, Direction.UP)) continue;
            if (!this.boss.world.isAir(blockPos) && !(voxelShape = this.boss.world.getBlockState(blockPos).getCollisionShape(this.boss.world, blockPos)).isEmpty()) {
                d = voxelShape.getMax(Direction.Axis.Y);
            }
            bl = true;
            break;
        } while ((blockPos = blockPos.down()).getY() >= MathHelper.floor(maxY) - 1);
        if (bl) {
            return new BlockPos(blockPos.getX(), (int) (blockPos.getY() + d), blockPos.getZ());
        } else {
            return null;
        }
    }

    private void chaosStorm() {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 255));
        this.mob.getNavigation().stop();
        boolean phase2 = this.boss.isPhaseTwo();
        if (this.attackStatus == 1) this.playSound(this.boss.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, 1f, 1f);
        if (this.attackStatus >= 25 && this.attackStatus < 80 && this.attackStatus % (phase2 ? 2 : 4) == 0) {
            int x = this.boss.getBlockX() + this.boss.getRandom().nextInt(16) - 8;
            int y = this.boss.getBlockY();
            int z = this.boss.getBlockZ() + this.boss.getRandom().nextInt(16) - 8;
            BlockPos randomPos = new BlockPos(x, y, z);
            if (!this.boss.world.getBlockState(randomPos).isAir()) {
                randomPos = randomPos.up();
            } else if (this.boss.world.getBlockState(randomPos.down()).isAir()) {
                randomPos = randomPos.down();
            }
            for (Entity entity : this.boss.world.getOtherEntities(this.boss, new Box(randomPos).expand(1D))) {
                if (entity instanceof LivingEntity living) {
                    this.damageTarget(living, 48f);
                }
            }
            if (!this.boss.world.isClient) {
                ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.FLAME_RUPTURE_ID, randomPos);
            }
            this.playSound(randomPos, SoundRegistry.DAY_STALKER_CHAOS_STORM, 1f, 1f);
            if (this.boss.world.getBlockState(randomPos).isAir()) {
                this.boss.world.setBlockState(randomPos, Blocks.FIRE.getDefaultState());
            }
        }
        this.checkAndReset(10, this.boss.isPhaseTwo() ? 0 : 100);
    }

    private void dawnbreaker(LivingEntity target) {
        this.attackStatus++;
        this.mob.getNavigation().stop();
        if (this.attackStatus == 11 || this.attackStatus == 21) {
            this.playSound(null, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            if (this.isInMeleeRange(target)) {
                this.damageTarget(target, 10f + (float)this.attackStatus/2f);
                if (!this.boss.world.isClient) {
                    ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.SWORD_SWIPE_ID, target.getBlockPos(), target.getEyeY());
                }
            }
            if (this.boss.isPhaseTwo()) {
                this.shootSunlight(target, 0, 20f, MoonlightProjectile.RotationState.NORMAL);
            }
        }
        if (this.attackStatus == 32) {
            this.playSound(null, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.75f);
            if (this.isInMeleeRange(target)) {
                this.damageTarget(target, 25f);
                if (!this.boss.world.isClient) {
                    ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.SWORD_SWIPE_ID, target.getBlockPos(), target.getEyeY());
                }
            }
            if (this.boss.isPhaseTwo()) {
                this.shootSunlight(target, 2, 30f, MoonlightProjectile.RotationState.NORMAL);
            }
        }
        this.checkAndReset(10, 0);
    }

    private void shootSunlight(LivingEntity target, int typeIndex, float damage, MoonlightProjectile.RotationState rotationState) {
        if (typeIndex == 0) {
            MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.SUNLIGHT_PROJECTILE_SMALL, this.boss.world, this.boss);
            if (this.boss.isEmpowered()) projectile.applyFireTicks(20);
            projectile.setAgeAndPoints(15, 30, 2);
            projectile.setDamage(this.getModifiedDamage(damage));
            projectile.setRotateState(rotationState);
            projectile.setExplosionParticleType(ParticleTypes.FLAME);
            projectile.setTrailParticleType(ParticleTypes.WAX_ON);
            this.shootProjectile(target, projectile, SoundEvents.ENTITY_BLAZE_SHOOT);
        } else if (typeIndex == 1) {
            MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.SUNLIGHT_PROJECTILE_BIG, this.boss.world, this.boss);
            if (this.boss.isEmpowered()) projectile.applyFireTicks(40);
            projectile.setAgeAndPoints(30, 150, 2);
            projectile.setDamage(this.getModifiedDamage(damage));
            projectile.setRotateState(rotationState);
            projectile.setExplosionParticleType(ParticleTypes.FLAME);
            projectile.setTrailParticleType(ParticleTypes.WAX_ON);
            this.shootProjectile(target, projectile, SoundEvents.ENTITY_BLAZE_SHOOT);
        } else {
            MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.VERTICAL_SUNLIGHT_PROJECTILE, this.boss.world, this.boss);
            if (this.boss.isEmpowered()) projectile.applyFireTicks(60);
            projectile.setAgeAndPoints(30, 75, 5);
            projectile.setDamage(this.getModifiedDamage(damage));
            projectile.setHugeExplosion(true);
            projectile.setExplosionParticleType(ParticleTypes.FLAME);
            projectile.setTrailParticleType(ParticleTypes.WAX_ON);
            this.shootProjectile(target, projectile, SoundEvents.ENTITY_BLAZE_SHOOT);
        }
    }

    private void flamethrower(LivingEntity target, double distance) {
        this.attackStatus++;
        if (this.attackStatus >= 20 && this.attackStatus < 65 && target.getBlockPos() != null) {
            BlockPos targetPos;
            double length = 260D;
            if (distance < length) {
                this.boss.setFlamethrowerTarget(target.getBlockPos());
                targetPos = new BlockPos(target.getBlockX(), (int) target.getBodyY(0.5D), target.getBlockZ());
            } else {
                double x = target.getBlockX() - this.boss.getBlockX();
                double y = target.getBodyY(0.5D) - this.boss.getBlockY();
                double z = target.getBlockZ() - this.boss.getBlockZ();
                int newX = MathHelper.floor(this.getPointBetweenTwoPos(length, distance, x, this.boss.getBlockX()));
                int newY = MathHelper.ceil(this.getPointBetweenTwoPos(length, distance, y, this.boss.getBlockY()));
                int newZ = MathHelper.floor(this.getPointBetweenTwoPos(length, distance, z, this.boss.getBlockZ()));
                targetPos = new BlockPos(newX, newY, newZ);
                this.boss.setFlamethrowerTarget(targetPos);
            }
            this.boss.setParticleState(1);
            if (this.attackStatus % 4 == 0) {
                Box box = new Box(targetPos.toCenterPos(), this.boss.getPos()).expand(1D);
                for (Entity entity : this.boss.world.getOtherEntities(this.boss, box)) {
                    if (entity instanceof LivingEntity living && !this.boss.isPartner(living)) {
                        living.damage(DamageSource.MAGIC, this.getModifiedDamage(this.boss.isPhaseTwo() ? 2f : 1f));
                        living.setOnFireFor(3);
                    }
                }
                this.playSound(targetPos, SoundEvents.ENTITY_BLAZE_SHOOT, 0.9f, 1f);
            }
            if (this.boss.isPhaseTwo()) {
                for (int frame : this.flamethrowerMeleeFrames) {
                    if (this.attackStatus == frame) {
                        this.aoeMelee(target, distance, 2D, 15f);
                        this.playSound(null, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
                    }
                }
            }
        }
        if (this.attackStatus >= 65) {
            this.boss.setParticleState(0);
        }
        this.checkAndReset(this.boss.isPhaseTwo() ? 10 : 40, 0);
    }

    /**
     * Returns one of the necessary values to calculate a new point between two points.
     * Used when calculating a new target position between the boss and the other target
     * if the target is too far away.
     *
     * @param maxDistance Max distance outwards the new point will be calculated at
     * @param currentDistance Current distance between the target and the boss
     * @param vecValToTarget Vector value calculated beforehand between the target and the boss.
     * Example: double x = target.getBlockX() - this.boss.getBlockX();
     * @return One of the new position values, example x.
     */
    private double getPointBetweenTwoPos(double maxDistance, double currentDistance, double vecValToTarget, int origin) {
        return origin + (maxDistance/currentDistance) * vecValToTarget;
    }

    /**
     * Returns a new rounded block position between the target and the boss based on the max distance input.
     *
     * @param maxDistance Max distance outwards the new point will be calculated at
     * @param currentDistance Current distance between the target and the boss
     * @param vecToTarget Vector between the boss and target
     * @return A rounded Block Position
     */
    private BlockPos getPosBetweenTwoPos(double maxDistance, double currentDistance, Vec3d vecToTarget) {
        int newX = MathHelper.floor(this.getPointBetweenTwoPos(maxDistance, currentDistance, vecToTarget.getX(), this.boss.getBlockX()));
        int newY = MathHelper.floor(this.getPointBetweenTwoPos(maxDistance, currentDistance, vecToTarget.getY(), this.boss.getBlockY()));
        int newZ = MathHelper.floor(this.getPointBetweenTwoPos(maxDistance, currentDistance, vecToTarget.getZ(), this.boss.getBlockZ()));
        return new BlockPos(newX, newY, newZ);
    }

    /**
     * Damages all living entities within a position determined by whether the original target was within melee attack
     * distance or not. If it was within attack range, the box would expand at the target's position. If not, a position
     * would be calculated between the original target and the boss.
     * @param target Original target
     * @param distanceToTarget Distance to original target
     * @param expansion Box that looks for entities expansion range
     * @param damage Damage to be done to all entities in the box
     */
    private void aoeMelee(LivingEntity target, double distanceToTarget, double expansion, float damage) {
        double maxDistance = this.getSquaredMaxAttackDistance(target);
        BlockPos pos;
        if (this.isInMeleeRange(target)) {
            pos = target.getBlockPos();
        } else {
            double x = target.getBlockX() - this.boss.getBlockX();
            double y = target.getEyeY() - this.boss.getBlockY();
            double z = target.getBlockZ() - this.boss.getBlockZ();
            pos = this.getPosBetweenTwoPos(maxDistance, distanceToTarget, new Vec3d(x, y, z));
        }
        for (Entity entity : this.boss.world.getOtherEntities(this.boss, new Box(pos).expand(expansion))) {
            if (entity instanceof LivingEntity living) {
                this.damageTarget(living, damage);
            }
        }
    }

    private void sunfireRush(LivingEntity target) {
        this.attackStatus++;
        if (this.attackStatus == 45 || this.attackStatus == 60) {
            this.shootSunlight(target, 0, 15f, MoonlightProjectile.RotationState.NORMAL);
        } else if (this.attackStatus == 71) {
            this.shootSunlight(target, 1, 20f, MoonlightProjectile.RotationState.SWIPE_FROM_RIGHT);
        } else if (this.attackStatus == 83) {
            this.shootSunlight(target, 1, 20f, MoonlightProjectile.RotationState.SWIPE_FROM_LEFT);
        } else if (this.attackStatus == 96) {
            this.shootSunlight(target, 2, 25f, MoonlightProjectile.RotationState.NORMAL);
        }
        this.checkAndReset(this.boss.isPhaseTwo() ? 10 : 40, 0);
    }

    private void conflagration(LivingEntity target) {
        this.attackStatus++;
        boolean phase2 = this.boss.isPhaseTwo();
        if (!phase2) {
            if (this.attackStatus >= 20 && this.attackStatus <= 40 && this.attackStatus % 4 == 0) {
                double f = target.getX() - this.boss.getX();
                double g = target.getBodyY(0.5) - (this.boss.getEyeY() + 1.5D);
                double h = target.getZ() - this.boss.getZ();
                UntargetableFireball fireballEntity = new UntargetableFireball(this.boss.world, this.boss, f, g, h, 2);
                fireballEntity.setPosition(this.boss.getX(), this.boss.getEyeY() + 1D, fireballEntity.getZ());
                this.boss.world.spawnEntity(fireballEntity);
            }
        } else {
            this.boss.getNavigation().stop();
            if (this.attackStatus == 15) {
                GrowingFireball fireballEntity = new GrowingFireball(this.boss.world, this.boss);
                fireballEntity.setPosition(this.boss.getX(), this.boss.getEyeY() + 4D, this.boss.getZ());
                fireballEntity.setTargetUuid(target.getUuid());
                fireballEntity.setRadiusGrowth(5f / (float) fireballEntity.getMaxAge());
                this.boss.world.spawnEntity(fireballEntity);
            }
        }
        this.checkAndReset(phase2 ? 20 : 40, phase2 ? 160 : 0);
    }

    private void blazeBarrage(LivingEntity target) {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        for (int i = 0; i < (this.boss.isPhaseTwo() ? 2 : 1); i++) {
            double x = target.getX() - (this.boss.getX());
            double y = target.getEyeY() - this.boss.getBodyY(1f);
            double z = target.getZ() - this.boss.getZ();
            Vec3d vec3d = this.boss.getRotationVec(1.0f);
            SmallFireballEntity fireball = new SmallFireballEntity(this.boss.world, this.boss.getX(), this.boss.getY(), this.boss.getZ(), x, y, z);
            fireball.setPosition(this.boss.getX() + vec3d.x * this.boss.getRandom().nextInt(6) * (this.boss.getRandom().nextBoolean() ? -1 : 1),
                    this.boss.getBodyY(0.5) + this.boss.getRandom().nextInt(4) - 1,
                    this.boss.getZ() + vec3d.z * this.boss.getRandom().nextInt(6) * (this.boss.getRandom().nextBoolean() ? -1 : 1));
            this.boss.world.spawnEntity(fireball);
            if (this.attackStatus % 4 == 0) this.boss.world.playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1f, 1f);
        }
        this.checkAndReset(this.boss.isPhaseTwo() ? 40 : 1, 0);
    }

    private void flamesEdge() {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        if (!this.boss.isPhaseTwo()) {
            this.boss.setFlamesEdgeRadius(5f);
            if (this.attackStatus >= 5 && this.attackStatus < 22) {
                this.boss.setParticleState(2);
            } else if (this.attackStatus >= 22 && this.attackStatus <= 24) {
                this.boss.setParticleState(3);
            } else if (this.attackStatus >= 25) {
                this.boss.setParticleState(0);
            }
            if (this.attackStatus == 22) {
                this.playSound(null, SoundRegistry.DAY_STALKER_FLAMES_EDGE_NORMAL, 1f, 1f);
                this.aoe(3.3D, 35f, 1f);
            }
        } else {
            this.boss.setFlamesEdgeRadius(6.5f);
            if (this.attackStatus >= 20 && this.attackStatus < 41) {
                this.boss.setParticleState(2);
            } else if (this.attackStatus >= 41 && this.attackStatus <= 43) {
                this.boss.setParticleState(3);
            } else if (this.attackStatus >= 44) {
                this.boss.setParticleState(0);
            }
            if (this.attackStatus == 41) {
                this.playSound(null, SoundRegistry.DAY_STALKER_FLAMES_EDGE_EMPOWERED, 1f, 1f);
                this.aoe(5.2D, 40f, 1.5f);
            }
        }
        this.checkAndReset(20, 0);
    }

    public void aoe(double expansion, float damage, float knockback) {
        for (Entity entity : this.boss.world.getOtherEntities(this.boss, this.boss.getBoundingBox().expand(expansion))) {
            if (entity instanceof LivingEntity target) {
                if (this.damageTarget(target, damage) && knockback > 0) {
                    double x = target.getX() - this.boss.getX();
                    double z = target.getZ() - this.boss.getZ();
                    target.takeKnockback(knockback, -x, -z);
                }
            }
        }
    }

    private void radiance() {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        if (this.attackStatus == 75) {
            this.playSound(null, SoundRegistry.DAY_STALKER_RADIANCE, 1f, 1f);
        }
        if (this.attackStatus == 80) {
            CustomDeathHandler.deathExplosionEvent(this.boss.world, this.boss.getBlockPos(), false, SoundRegistry.DAWNBREAKER_EVENT);
            this.aoe(4D, 60f, 4f);
        }
        this.checkAndReset(40, 200);
    }

    private void warmth() {
        this.attackStatus++;
        if (this.attackStatus == 84) {
            this.boss.world.playSound(null, this.boss.getBlockPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.HOSTILE, 1f, 1f);
            for (int i = 0; i < 4; i++) {
                WarmthEntity entity = new WarmthEntity(EntityRegistry.WARMTH_ENTITY, this.boss.world);
                Vec3d pos = new Vec3d(this.boss.getX() + this.boss.getRandom().nextBetween(2, 5) * (this.boss.getRandom().nextBoolean() ? -1 : 1),
                        this.boss.getY() + this.boss.getRandom().nextInt(4) + 2,
                        this.boss.getZ() + this.boss.getRandom().nextBetween(2, 5) * (this.boss.getRandom().nextBoolean() ? -1 : 1));
                entity.setPosition(this.getAirBlockPos(pos, 0));
                this.boss.world.spawnEntity(entity);
            }
        }
        this.checkAndReset(20, 160);
    }

    private void overheat(LivingEntity target) {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        if (this.attackStatus == 1) {
            this.boss.world.playSound(null, this.boss.getBlockPos(), SoundRegistry.OVERHEAT_CHARGE_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            this.boss.world.playSound(null, target.getBlockPos(), SoundRegistry.OVERHEAT_CHARGE_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        }
        if (this.attackStatus <= 57) {
            this.targetMaxY = Math.min(target.getY(), this.boss.getY());
            this.targetY = Math.max(target.getY(), this.boss.getY()) + 1.0;
            this.attackRotation = (float)MathHelper.atan2(target.getZ() - this.boss.getZ(), target.getX() - this.boss.getX());
        }
        if (this.attackStatus == 67 && this.attackRotation != 0 && this.targetY != 0 && this.targetMaxY != 0) {
            double d = this.targetMaxY;
            double e = this.targetY;
            float f = this.attackRotation;
            int length = 15;
            for (int i = 0; i <= length; ++i) {
                double h = 1.25 * (double)(i + 1);
                BlockPos pos = this.conjureFlames(this.boss.getX() + (double)MathHelper.cos(f) * h, this.boss.getZ() + (double)MathHelper.sin(f) * h, d, e);
                if (pos != null) {
                    if (!this.boss.world.isClient) {
                        ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.FLAME_RUPTURE_ID, pos);
                    }
                    this.boss.world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
                }
            }
            BlockPos lastPos = this.conjureFlames(this.boss.getX() + (double)MathHelper.cos(f) * 1.25 * (double)(length + 1), this.boss.getZ() + (double)MathHelper.sin(f) * 1.25 * (double)(length + 1), d, e);
            if (lastPos != null) {
                for (Entity entity : this.boss.world.getOtherEntities(this.boss, new Box(this.boss.getPos().add(0, 2, 0), lastPos.toCenterPos()).expand(1D))) {
                    if (entity instanceof LivingEntity living) {
                        this.damageTarget(living, 40f);
                    }
                }
            }
        }
        this.checkAndReset(40, 0);
    }

    private void inferno() {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        if (this.attackStatus == 23) {
            this.playSound(null, SoundEvents.ENTITY_BLAZE_SHOOT, 1f, 1f);
            this.boss.setFlying(true);
            this.boss.addVelocity(0, 0.75f, 0);
        }
        if (this.attackStatus == 69) {
            this.boss.setFlying(false);
            this.boss.setVelocity(0, -2f, 0);
        }
        if (this.attackStatus >= 70 && this.attackStatus <= 100) {
            if (this.boss.isOnGround() && !this.hasExploded) {
                this.boss.world.createExplosion(this.boss, this.boss.getX(), this.boss.getY(), this.boss.getZ(), 6f, World.ExplosionSourceType.NONE);
                this.hasExploded = true;
            }
        }
        if (this.attackStatus == 125) {
            this.boss.world.createExplosion(this.boss, this.boss.getX(), this.boss.getY(), this.boss.getZ(), 8f, World.ExplosionSourceType.NONE);
        }
        this.checkAndReset(30, 0);
    }

    private void flamesReach(LivingEntity target, double distance) {
        this.attackStatus++;
        this.boss.getNavigation().stop();
        double maxDistance = this.boss.isPhaseTwo() ? 140D : 120D;
        if (this.attackStatus == 5) {
            this.playSound(null, SoundRegistry.DAY_STALKER_WINDUP, 1f, 1f);
        }
        if (this.attackStatus == 29) {
            BlockPos targetPos;
            if (distance < maxDistance) {
                targetPos = target.getBlockPos();
            } else {
                double x = target.getBlockX() - this.boss.getBlockX();
                double y = target.getEyeY() - this.boss.getBlockY();
                double z = target.getBlockZ() - this.boss.getBlockZ();
                int newX = MathHelper.floor(this.getPointBetweenTwoPos(maxDistance, distance, x, this.boss.getBlockX()));
                int newY = MathHelper.floor(this.getPointBetweenTwoPos(maxDistance, distance, y, this.boss.getBlockY()));
                int newZ = MathHelper.floor(this.getPointBetweenTwoPos(maxDistance, distance, z, this.boss.getBlockZ()));
                targetPos = new BlockPos(newX, newY, newZ);
            }
            Box box = new Box(targetPos.toCenterPos(), this.boss.getPos()).expand(1D);
            for (Entity entity : this.boss.world.getOtherEntities(this.boss, box)) {
                if (entity instanceof LivingEntity living) {
                    this.damageTarget(living, 20f);
                    double x = target.getX() - (this.boss.getX());
                    double z = target.getZ() - this.boss.getZ();
                    target.takeKnockback(3F, x, z);
                }
            }
            this.playSound(targetPos, SoundRegistry.DAY_STALKER_PULL, 1f, 1f);
        }
        if (this.attackStatus == 43) {
            Vec3d vec3d = this.boss.getRotationVec(1.0f);
            BlockPos pos = new BlockPos(new Vec3d(this.boss.getBlockX() + vec3d.x * 3, this.boss.getBlockY(), this.boss.getBlockZ() + vec3d.z * 3));
            if (!this.boss.world.isClient) {
                ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.DARKIN_BLADE_SLAM_PACKET_ID, pos, 150);
            }
            this.playSound(pos, SoundEvents.ENTITY_GENERIC_EXPLODE, 1f, 1f);
            for (Entity entity : this.boss.world.getOtherEntities(this.boss, new Box(pos).expand(3D))) {
                if (entity instanceof LivingEntity living) {
                    this.damageTarget(living, 25f);
                }
            }
        }
        this.checkAndReset(20, 0);
    }

    /**
     * Easily accessible playSound function.
     * @param pos Position to play sound on, if set to null, will play on the boss' position
     * @param sound Sound to be played
     * @param volume Volume
     * @param pitch Pitch
     */
    private void playSound(@Nullable  BlockPos pos, SoundEvent sound, float volume, float pitch) {
        if (pos == null) pos = this.boss.getBlockPos();
        this.boss.world.playSound(null, pos, sound, SoundCategory.HOSTILE, volume, pitch);
    }

    private Vec3d getAirBlockPos(Vec3d start, int counter) {
        if (this.boss.world.getBlockState(new BlockPos(start)).isAir() || counter >= 5) {
            return start;
        } else {
            return getAirBlockPos(start.add(0, 1, 0), counter + 1);
        }
    }

    @Override
    protected void attack(LivingEntity target, double squaredDistance) {}

    protected boolean isInMeleeRange(LivingEntity target) {
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        return distanceToEntity <= this.getSquaredMaxAttackDistance(target);
    }
}