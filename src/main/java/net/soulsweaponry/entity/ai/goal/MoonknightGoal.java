package net.soulsweaponry.entity.ai.goal;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.EvilRemnant;
import net.soulsweaponry.entity.mobs.Moonknight;
import net.soulsweaponry.entity.mobs.Moonknight.MoonknightPhaseOne;
import net.soulsweaponry.entity.mobs.Moonknight.MoonknightPhaseTwo;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.entity.projectile.MoonlightProjectile.RotationState;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.ParticleNetworking;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Objects;

public class MoonknightGoal extends Goal {
    
    private final Moonknight boss;
    private int attackStatus;
    private int attackCooldown;
    private int specialCooldown;
    private int targetNotVisibleTicks;
    private BlockPos targetPos;
    private double moonfallRuptureMod = 0.5D;
    private RotationState projectileRotation = RotationState.SWIPE_FROM_RIGHT;
    private float bonusBeamHeight = 0f;
    private double height = 0D;
    private BlockPos pos;

    public MoonknightGoal(Moonknight boss) {
        this.boss = boss;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.boss.getTarget();
        return target != null && target.isAlive() && this.boss.canTarget(target);
    }

    public float getModifiedDamage(float damage) {
        return damage * ConfigConstructor.fallen_icon_damage_modifier;
    }

    private void resetAttack(float attackCDModifier, boolean wasSpecial, float specialCDModifier) {
        if (!this.boss.isPhaseTwo()) {
            this.checkAttackPhaseOne(MoonknightPhaseOne.IDLE, this.boss.getTarget());
        } else {
            this.checkAttackPhaseTwo(MoonknightPhaseTwo.IDLE, this.boss.getTarget());
        }
        this.attackStatus = 0;
        this.attackCooldown = ((int) Math.floor((this.boss.isPhaseTwo() ? ConfigConstructor.fallen_icon_attack_cooldown_ticks_phase_2 : ConfigConstructor.fallen_icon_attack_cooldown_ticks_phase_1) * attackCDModifier) - this.boss.getReducedCooldownAttackers()*2);
        if (wasSpecial) this.specialCooldown = (int) Math.floor(ConfigConstructor.fallen_icon_special_cooldown_ticks * specialCDModifier) - this.boss.getReducedCooldownAttackers()*2;
    }

    private void reset() {
        this.attackCooldown = 0;
        this.attackStatus = 0;
        this.specialCooldown = 0;
        this.bonusBeamHeight = 0f;
        this.projectileRotation = RotationState.SWIPE_FROM_RIGHT;
        this.moonfallRuptureMod = 0.5D;
    }

    @Override
    public void stop() {
        super.stop();
        this.reset();
        this.boss.setAttacking(false);
        if (this.boss.isPhaseTwo()) {
            this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
        } else {
            this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
        }
    }

    private MoonknightPhaseOne randomAttackPhaseOne() {
        int rand = this.boss.getRandom().nextInt(MoonknightPhaseOne.values().length);
        MoonknightPhaseOne attack = MoonknightPhaseOne.values()[rand];
        if (attack.equals(MoonknightPhaseOne.IDLE)) {
            return this.randomAttackPhaseOne();
        } else {
            return attack;
        }
    }

    private MoonknightPhaseTwo randomAttackPhaseTwo() {
        int rand = this.boss.getRandom().nextInt(MoonknightPhaseTwo.values().length);
        MoonknightPhaseTwo attack = MoonknightPhaseTwo.values()[rand];
        if (attack.equals(MoonknightPhaseTwo.IDLE)) {
            return this.randomAttackPhaseTwo();
        } else {
            return attack;
        }
    }

    private void checkAttackPhaseOne(@Nullable MoonknightPhaseOne specificPhaseOne, LivingEntity target) {
        if (target == null || (specificPhaseOne != null && specificPhaseOne.equals(MoonknightPhaseOne.IDLE))) {
            this.boss.setPhaseOneAttack(specificPhaseOne);
            return;
        }
        MoonknightPhaseOne attack;
        attack = Objects.requireNonNullElseGet(specificPhaseOne, this::randomAttackPhaseOne);
        double distance = this.boss.squaredDistanceTo(target);
        switch (attack) {
            case BLINDING_LIGHT -> {
                if (distance < 30D) {
                    this.boss.setPhaseOneAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
                }
            }
            case MACE_OF_SPADES -> {
                if (distance < 50D && target.getBlockPos() != null) {
                    this.boss.setPhaseOneAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
                }
            }
            case OBLITERATE -> {
                if (distance < 75D && target.getBlockPos() != null) {
                    this.boss.setPhaseOneAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
                }
            }
            case RUPTURE, SUMMON -> {
                if (this.specialCooldown < 0) {
                    this.boss.setPhaseOneAttack(attack);
                } else if (this.specialCooldown > 20) {
                    this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
                }
            }
            default -> this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
        }
    }

    private void checkAttackPhaseTwo(@Nullable MoonknightPhaseTwo specificPhaseTwo, LivingEntity target) {
        if (target == null || (specificPhaseTwo != null && specificPhaseTwo.equals(MoonknightPhaseTwo.IDLE))) {
            this.boss.setPhaseTwoAttack(specificPhaseTwo);
            return;
        }
        MoonknightPhaseTwo attack;
        attack = Objects.requireNonNullElseGet(specificPhaseTwo, this::randomAttackPhaseTwo);
        double distance = this.boss.squaredDistanceTo(target);
        switch (attack) {
            case BLINDING_LIGHT -> {
                if (distance < 32D) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }
            case CORE_BEAM -> {
                if (this.specialCooldown < 0 && distance < 128D) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.specialCooldown > 10 || this.attackCooldown < -30) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }
            case MOONFALL -> {
                if (distance < 80D && target.getBlockPos() != null) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }
            case MOONVEIL -> {
                if (distance < 36D && this.specialCooldown < 0) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.specialCooldown > 10 || this.attackCooldown < -10) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }
            case SWORD_OF_LIGHT -> {
                if (distance < 300D) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.SWORD_OF_LIGHT);
                }
            }
            case THRUST -> {
                if (distance < 128D) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }
            default -> this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
        }
    }

    @Override
    public void tick() {
        this.attackCooldown--;
        this.specialCooldown--;
        LivingEntity target = this.boss.getTarget();
        if (this.boss.isInitiatingPhaseTwo()) this.reset();
        if (target != null && !this.boss.isDead() && !this.boss.getSpawning() && !this.boss.isInitiatingPhaseTwo() && !this.boss.getUnbreakable()) {
            this.boss.setAttacking(true);
            this.boss.getLookControl().lookAt(target.getX(), target.getEyeY(), target.getZ());
            boolean entityInSight = this.boss.getVisibilityCache().canSee(target);
            if (entityInSight) {
                this.targetNotVisibleTicks = 0;
            } else {
                ++this.targetNotVisibleTicks;
            }

            if (this.attackCooldown > 0) {
                if (!this.boss.isPhaseTwo()) {
                    this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
                } else {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            } else if (this.attackCooldown < 0 && this.attackCooldown % 5 == 0) {
                if (!this.boss.isPhaseTwo() && this.boss.getPhaseOneAttack().equals(MoonknightPhaseOne.IDLE)) {
                    this.checkAttackPhaseOne(null, target);
                } else if (this.boss.getPhaseTwoAttack().equals(MoonknightPhaseTwo.IDLE)) {
                    this.checkAttackPhaseTwo(null, target);
                }
            }

            if (!this.boss.isPhaseTwo()) {
                switch (this.boss.getPhaseOneAttack()) {
                    case BLINDING_LIGHT -> this.blindingLightLogic();
                    case MACE_OF_SPADES -> this.maceOfSpadesLogic(target);
                    case OBLITERATE ->
                            this.obliterateLogic(target, 13, 3, 28, 50f, SoundRegistry.NIGHTFALL_BONK_EVENT, false);
                    case RUPTURE -> this.ruptureLogic();
                    case SUMMON -> this.summonLogic(target);
                    default -> this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
                }
            } else {
                switch (this.boss.getPhaseTwoAttack()) {
                    case BLINDING_LIGHT -> this.blindingLightLogic();
                    case CORE_BEAM -> this.coreBeam(target);
                    case MOONFALL -> this.moonfallLogic(target);
                    case MOONVEIL -> this.moonveilLogic();
                    case SWORD_OF_LIGHT -> this.swordOfLight(target);
                    case THRUST -> this.thrustLogic(target);
                    default -> this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }

            if (this.targetNotVisibleTicks < 5) {
                this.boss.getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 1.0D);
            }
        }
        super.tick();
    }

    private void coreBeam(LivingEntity target) {
        this.attackStatus++;
        if (attackStatus == 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus < 20 && target.getBlockPos() != null) {
            this.targetPos = target.getBlockPos();
        }
        if (this.targetPos != null) {
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY() + this.bonusBeamHeight, this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY() + this.bonusBeamHeight, this.targetPos.getZ(), 0.0D);
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            if (this.attackStatus >= 21 && this.attackStatus <= 47) {
                if (attackStatus == 21) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.KNIGHT_CORE_BEAM_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                float range = (float) this.boss.squaredDistanceTo(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                this.boss.setCanBeam(true);
                this.boss.setBeamLocation(targetPos);
                this.boss.setBeamLength(range);
                if (this.attackStatus % 2 == 0) {
                    this.boss.getWorld().createExplosion(boss, CustomDamageSource.create(this.boss.getWorld(), CustomDamageSource.BEAM, this.boss), null, targetPos.getX(), targetPos.getY() + this.bonusBeamHeight, targetPos.getZ(), 2f, true, this.boss.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? World.ExplosionSourceType.TNT : World.ExplosionSourceType.NONE);
                    for (Entity entity : this.boss.getWorld().getOtherEntities(boss, new Box(targetPos, this.boss.getBlockPos().add(0, 4, 0)))) {
                        if (entity instanceof LivingEntity) {
                            entity.damage(CustomDamageSource.create(this.boss.getWorld(), CustomDamageSource.BEAM, this.boss), this.getModifiedDamage(20f));
                            entity.setOnFireFor(4);
                        }
                    }
                    this.bonusBeamHeight += 0.1f + this.bonusBeamHeight/2;
                    this.boss.setBeamHeight(this.bonusBeamHeight);
                }
            }
            if (this.attackStatus > 40) {
                this.targetPos = this.targetPos.add(0, MathHelper.floor(-this.bonusBeamHeight), 0);
                this.boss.setCanBeam(false);
            }
        }
        if (this.attackStatus >= 77) {
            this.boss.getNavigation().stop();
            this.bonusBeamHeight = 0f;
            this.resetAttack(1f, true, 1f);
        }
    }

    private void swordOfLight(LivingEntity target) {
        this.attackStatus++;
        double distance = this.boss.squaredDistanceTo(target);
        if (target.getBlockPos() != null) {
            if (distance < 65D) {
                this.targetPos = target.getBlockPos();
            } else {
                Vec3d direction = new Vec3d(target.getX() - this.boss.getBlockX(), 0, target.getZ() - this.boss.getBlockZ()).multiply(.5f);
                Vec3d spot = new Vec3d(this.boss.getX(), this.boss.getY(), this.boss.getZ()).add(direction);
                this.targetPos = BlockPos.ofFloored(spot);
            }
        }
        if (this.targetPos != null && target.getPos() != null) {
            double x = target.getX() - (this.boss.getX());
            double y = target.getEyeY() - this.boss.getBodyY(1f);
            double z = target.getZ() - this.boss.getZ();
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            if (attackStatus == 8 || attackStatus == 15 || attackStatus == 20 || attackStatus == 25) {
                this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.HOSTILE, 1f, 0.75f);
                for (Entity entity : this.boss.getWorld().getOtherEntities(boss, new Box(this.targetPos).expand(3))) {
                    if (entity instanceof LivingEntity) {
                        entity.damage(this.boss.getWorld().getDamageSources().mobAttack(boss), this.getModifiedDamage(25f));
                        ((LivingEntity) entity).takeKnockback(2f, -(entity.getX() - this.boss.getX()), -(entity.getZ() - this.boss.getZ()));
                    }
                }

                this.boss.getWorld().playSound(null, this.targetPos, SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, this.boss.getWorld(), this.boss);
                projectile.setAgeAndPoints(30, 150, 4);
                projectile.setDamage(this.getModifiedDamage(25f));
                projectile.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
                projectile.setVelocity(x, y, z, 1.5f, 1f);
                projectile.setRotateState(projectileRotation);
                if (Objects.requireNonNull(projectileRotation) == RotationState.SWIPE_FROM_RIGHT) {
                    projectileRotation = RotationState.SWIPE_FROM_LEFT;
                } else {
                    projectileRotation = RotationState.NORMAL;
                }
                this.boss.getWorld().spawnEntity(projectile);
            }
            if (attackStatus == 27) this.boss.getWorld().playSound(null, this.targetPos, SoundRegistry.KNIGHT_SWORD_SMASH_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            if (attackStatus == 31) {
                this.smashGround(30f, SoundRegistry.KNIGHT_SWORD_SMASH_EVENT, true);
                this.boss.getWorld().playSound(null, this.targetPos, SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.HOSTILE, 1f, 0.75f);
                MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.VERTICAL_MOONLIGHT_ENTITY_TYPE, this.boss.getWorld(), this.boss);
                projectile.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
                projectile.setVelocity(x, y, z, 1.5f, 1f);
                projectile.setDamage(this.getModifiedDamage(40f));
                projectile.setHugeExplosion(true);
                projectile.setAgeAndPoints(30, 75, 10);
                this.boss.getWorld().spawnEntity(projectile);
            }
        }
        if (attackStatus >= 37) {
            this.boss.getNavigation().stop();
            this.projectileRotation = RotationState.SWIPE_FROM_RIGHT;
            this.resetAttack(1f, false, 1f);
        }
    }

    private void thrustLogic(LivingEntity target) {
        attackStatus++;
        if (attackStatus == 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.KNIGHT_CHARGE_SWORD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus < 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus < 13 && target.getBlockPos() != null) {
            this.targetPos = target.getBlockPos();
            this.height = target.getEyeY();
        } else if (this.targetPos != null) {
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            if (attackStatus == 15) this.boss.getWorld().playSound(null, targetPos, SoundRegistry.KNIGHT_THRUST_SWORD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            if (this.attackStatus == 17 && this.boss.squaredDistanceTo(target) < 128D) {
                for (Entity entity : boss.getWorld().getOtherEntities(this.boss, new Box(targetPos).expand(1))) {
                    if (entity instanceof LivingEntity) {
                        entity.damage(this.boss.getWorld().getDamageSources().mobAttack(boss), this.getModifiedDamage(50f));
                        ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200, 1));
                        ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1));
                        ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(EffectRegistry.DISABLE_HEAL, 100, 0));
                    }
                }
                if (!boss.getWorld().isClient) ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.SOUL_FLAME_SMALL_OUTBURST_ID, this.targetPos, this.height == 0 ? targetPos.getY() : this.height);
            }
        }
        if (this.attackStatus >= 23) {
            this.boss.getNavigation().stop();
            this.resetAttack(0.75f, false, 1f);
        }
    }

    private void moonfallLogic(LivingEntity target) {
        this.obliterateLogic(target, 25, 15, 43, 65f, SoundRegistry.KNIGHT_SWORD_SMASH_EVENT, true);
        if (attackStatus == 21) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.KNIGHT_SWORD_SMASH_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        if (attackStatus == 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.KNIGHT_CHARGE_SWORD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus > 26 && this.attackStatus < 40) {
            Vec3d direction = new Vec3d(this.targetPos.getX() - this.boss.getBlockX(), 0, this.targetPos.getZ() - this.boss.getBlockZ()).multiply(this.moonfallRuptureMod);
            Vec3d spot = new Vec3d(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ()).add(direction);
            for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, new Box(spot.getX() - 1, spot.getY() - 1, spot.getZ() - 1, spot.getX() + 1, spot.getY() + 1, spot.getZ() + 1))) {
                if (entity instanceof LivingEntity) {
                    entity.damage(this.boss.getWorld().getDamageSources().mobAttack(boss), this.getModifiedDamage(30f));
                    entity.addVelocity(0, 1.0, 0);
                }
            }
            this.boss.getWorld().playSound(null, BlockPos.ofFloored(spot), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
            if (!this.boss.getWorld().isClient) {
                ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.SOUL_FLAME_RUPTURE_ID, BlockPos.ofFloored(spot), spot.getX(), (float)spot.getZ());
            }
            this.moonfallRuptureMod += 0.25D;
        } else if (this.attackStatus >= 41) {
            this.moonfallRuptureMod = 0.5D;
        }
    }

    private void moonveilLogic() {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 5));
        if (this.attackStatus == 29 || this.attackStatus == 42) {
            this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
            for (Entity entity : this.boss.getWorld().getOtherEntities(boss, this.boss.getBoundingBox().expand(3))) {
                if (entity instanceof LivingEntity livingEntity) {
                    double x = livingEntity.getX() - (this.boss.getX());
                    double z = livingEntity.getZ() - this.boss.getZ();
                    livingEntity.takeKnockback(10F, -x, -z);
                    livingEntity.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(50f));
                }
            }
            if (!boss.getWorld().isClient) ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.DEATH_EXPLOSION_PACKET_ID, this.boss.getBlockPos(), true);
        }
        if (this.attackStatus >= 55) {
            this.resetAttack(1.2f, true, 1f);
        }
    }

    private void smashGround(float damage, SoundEvent sound, boolean isSoundDelayed) {
        for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, new Box(this.targetPos).expand(3))) {
            if (entity instanceof LivingEntity) {
                entity.damage(CustomDamageSource.create(this.boss.getWorld(), CustomDamageSource.OBLITERATED, this.boss), this.getModifiedDamage(damage));
                entity.addVelocity(0, 1, 0);
            }
        }
        if (!isSoundDelayed) this.boss.getWorld().playSound(null, this.targetPos, sound, SoundCategory.HOSTILE, 1f, 1f);
        if (!this.boss.getWorld().isClient) {
            ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.OBLITERATE_ID, this.targetPos, 200);
        }
    }

    private void obliterateLogic(LivingEntity target, int hitFrame, int followTargetTicks, int attackFinishedTicks, float damage, SoundEvent sound, boolean isSoundDelayed) {
        this.attackStatus++;
        if (this.attackStatus < followTargetTicks && target.getBlockPos() != null) {
            this.targetPos = target.getBlockPos();
        } else if (this.targetPos != null) {
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            if (this.attackStatus == hitFrame) {
                this.smashGround(damage, sound, isSoundDelayed);
            }
            if (this.attackStatus >= attackFinishedTicks) {
                this.boss.getNavigation().stop();
                this.resetAttack(1f, false, 1f);
            }
        }
    }

    private void maceOfSpadesLogic(LivingEntity target) {
        this.attackStatus++;
        if (this.attackStatus < 3 && target.getBlockPos() != null) {
            this.targetPos = target.getBlockPos();
        } else if (this.targetPos != null) {
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
            if (this.attackStatus == 7) {
                for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, new Box(targetPos.getX() - 5, targetPos.getY() - 2, targetPos.getZ() - 5, targetPos.getX() + 5, targetPos.getY() + 2, targetPos.getZ() + 5))) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.takeKnockback(2f, -(livingEntity.getX() - this.boss.getX()), -(livingEntity.getZ() - this.boss.getZ()));
                        livingEntity.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(20f));
                    }
                }
                this.boss.getWorld().playSound(null, this.targetPos, SoundRegistry.KNIGHT_SWIPE_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.attackStatus == 13 && target.getBlockPos() != null) {
                this.targetPos = target.getBlockPos();
            }
            if (this.attackStatus == 21 && this.targetPos != null) {
                for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, new Box(this.targetPos).expand(3D))) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.addVelocity(0, 1, 0);
                        livingEntity.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(25f));
                    }
                }
                this.boss.getWorld().playSound(null, this.targetPos, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                if (!this.boss.getWorld().isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.OBLITERATE_ID, this.targetPos, 200);
                }
            }
            if (this.attackStatus >= 36) {
                this.boss.getNavigation().stop();
                this.resetAttack(1f, false, 1f);
            }
        }
    }

    private void ruptureLogic() {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (attackStatus == 21 || attackStatus == 33) {
            for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(12))) {
                this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
        }
        if (this.attackStatus == 52) {
            for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(12))) {
                if (entity instanceof LivingEntity && !(entity instanceof EvilRemnant)) {
                    entity.damage(this.boss.getWorld().getDamageSources().mobAttack(boss), this.getModifiedDamage(35f));
                    this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
                    entity.addVelocity(0, 1.0, 0);
                    if (!this.boss.getWorld().isClient) {
                        ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.GROUND_RUPTURE_ID, entity.getBlockPos(), entity.getX(), (float)entity.getZ());
                    }
                }
            }
        }
        if (this.attackStatus >= 70) {
            this.resetAttack(0.5f, true, 1f);
        }
    }

    private void blindingLightLogic() {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (this.attackStatus < 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus == 9) {
            if (!this.boss.getWorld().isClient) ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.BLINDING_LIGHT_SMASH_ID, this.boss.getBlockPos(), 200);
            this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.BLINDING_LIGHT_EXPLOSION_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(3))) {
                if (entity instanceof LivingEntity living) {
                    living.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(20f));
                    living.takeKnockback(3f, -living.getX() - this.boss.getX(), -living.getZ() - this.boss.getZ());
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
                }
            }
            if (this.boss.isPhaseTwo()) {
                int r = 3;
                int y = this.boss.getBlockPos().getY() + 1;
                for (int theta = 0; theta < 360; theta+= 15) {
                    double x0 = this.boss.getX();
                    double z0 = this.boss.getZ();
                    double x = x0 + r * Math.cos(theta * Math.PI / 180);
                    double z = z0 + r * Math.sin(theta * Math.PI / 180);
                    MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_ENTITY_TYPE, this.boss.getWorld());
                    projectile.setAgeAndPoints(15, 30, 1);
                    projectile.setPos(x, y, z);
                    projectile.setVelocity(this.boss, 0, theta, 0.0f, 1.5f, 0f);
                    projectile.setDamage(this.getModifiedDamage(15f));
                    this.boss.getWorld().spawnEntity(projectile);
                }
            }
        }
        if (this.attackStatus >= 24) {
            this.resetAttack(0.5f, false, 1f);
        }
    }

    private void summonLogic(LivingEntity target) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (this.attackStatus == 30) {
            int enemyNumber = this.boss.getRandom().nextInt(6 - 3) + 3;
            for (int j = 0; j < enemyNumber; j++) {
                EvilRemnant entity = new EvilRemnant(EntityRegistry.REMNANT, this.boss.getWorld());
                this.pos = new BlockPos(this.boss.getBlockX() + this.boss.getRandom().nextInt(20) - 10, this.boss.getBlockY() - 2,  this.boss.getBlockZ() + this.boss.getRandom().nextInt(20) - 10);
                if (this.canSummon()) entity.setPos(this.pos.getX(), this.pos.getY() + .1f, this.pos.getZ());
                this.initEquip(entity);
                this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                this.boss.getWorld().spawnEntity(entity);
                if (!this.boss.getWorld().isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.SOUL_RUPTURE_PACKET_ID, pos, 100);
                }
            }
            if (!this.boss.getWorld().isClient) {
                ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.getWorld(), PacketRegistry.GROUND_RUPTURE_ID, target.getBlockPos(), target.getX(), (float) target.getZ());
            }
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 140, 1));
            this.boss.getWorld().playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 0.7f, 1f);
        }
        if (this.attackStatus >= 48) {
            this.resetAttack(1f, true, 2f);
        }
    }

    public boolean canSummon() {
        if (!this.boss.getWorld().getBlockState(this.pos).isAir()) {
            this.pos = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
            this.canSummon();
        }
        return true;
    }

    private void initEquip(LivingEntity entity) {
        HashMap<ItemStack, EquipmentSlot> equip = new HashMap<>();
        equip.put(new ItemStack(Items.NETHERITE_HELMET), EquipmentSlot.HEAD);
        equip.put(new ItemStack(Items.NETHERITE_CHESTPLATE), EquipmentSlot.CHEST);
        equip.put(new ItemStack(Items.NETHERITE_LEGGINGS), EquipmentSlot.LEGS);
        equip.put(new ItemStack(Items.NETHERITE_BOOTS), EquipmentSlot.FEET);
        for (ItemStack stack : equip.keySet()) {
            stack.addEnchantment(Enchantments.PROTECTION, 2);
            stack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
            entity.equipStack(equip.get(stack), stack);
        }
        entity.setHealth(entity.getMaxHealth() * 1.5f);
    }
}
