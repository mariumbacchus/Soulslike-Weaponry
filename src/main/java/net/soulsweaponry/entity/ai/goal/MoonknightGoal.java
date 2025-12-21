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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.soulsweaponry.config.BossConfig;
import net.soulsweaponry.entity.mobs.Moonknight;
import net.soulsweaponry.entity.mobs.Moonknight.MoonknightPhaseOne;
import net.soulsweaponry.entity.mobs.Moonknight.MoonknightPhaseTwo;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.entity.projectile.noclip.AbsorbedProjectilesOrb;
import net.soulsweaponry.entity.projectile.noclip.HolyMoonlightPillar;
import net.soulsweaponry.entity.projectile.noclip.MoonveilWave;
import net.soulsweaponry.entity.util.RandomSummonPos;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.util.EntityLookUtil;
import net.soulsweaponry.util.WeaponUtil;
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
    private Vec3d targetPos;
    private float yaw;
    private int projectileRotation = -45;
    private float bonusBeamHeight = 0f;
    private double height = 0D;

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
        return damage * BossConfig.fallen_icon_damage_modifier;
    }

    private void resetAttack(float attackCDModifier, boolean wasSpecial, float specialCDModifier) {
        if (!this.boss.isPhaseTwo()) {
            this.checkAttackPhaseOne(MoonknightPhaseOne.IDLE, this.boss.getTarget());
        } else {
            this.checkAttackPhaseTwo(MoonknightPhaseTwo.IDLE, this.boss.getTarget());
        }
        this.attackStatus = 0;
        this.attackCooldown = ((int) Math.floor((this.boss.isPhaseTwo() ? BossConfig.fallen_icon_attack_cooldown_ticks_phase_2 : BossConfig.fallen_icon_attack_cooldown_ticks_phase_1) * attackCDModifier) - this.boss.getReducedCooldownAttackers()*2);
        if (wasSpecial) this.specialCooldown = (int) Math.floor(BossConfig.fallen_icon_special_cooldown_ticks * specialCDModifier) - this.boss.getReducedCooldownAttackers()*2;
    }

    private void reset() {
        this.attackCooldown = 0;
        this.attackStatus = 0;
        this.specialCooldown = 0;
        this.bonusBeamHeight = 0f;
        this.projectileRotation = -45;
        this.boss.setCanBeam(false);
        this.boss.setBeamHeight(0f);
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
            case UNBREAKABLE -> {
                if (this.boss.getRandom().nextDouble() < 0.1) {
                    this.boss.setPhaseOneAttack(attack);
                } else {
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
                if (distance < 50) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }
            case CORE_BEAM, MOONVEIL -> {
                if (this.specialCooldown < 0 && distance < 750) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.specialCooldown > 10 || this.attackCooldown < -30) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }
            case MOONFALL, RUPTURE -> {
                if (distance < 120 && target.getBlockPos() != null) {
                    this.boss.setPhaseTwoAttack(attack);
                } else if (this.attackCooldown < -10) {
                    this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }
            case SWORD_OF_LIGHT, HEAVY_SWING -> {
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
            case UNBREAKABLE -> {
                if (!this.boss.getAbsorbedProjectileTypes().isEmpty()) {
                    this.boss.setPhaseTwoAttack(attack);
                } else {
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
        if (target != null && !this.boss.isDead() && !this.boss.isSpawning() && !this.boss.isInitiatingPhaseTwo() && this.boss.getWorld() instanceof ServerWorld serverWorld) {
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
                    case BLINDING_LIGHT -> this.blindingLightLogic(serverWorld);
                    case MACE_OF_SPADES -> this.maceOfSpadesLogic(serverWorld, target);
                    case OBLITERATE ->
                            this.obliterateLogic(serverWorld, target, 13, 3, 28, 50f, SoundRegistry.NIGHTFALL_BONK_EVENT, false);
                    case RUPTURE -> this.ruptureLogic(serverWorld);
                    case SUMMON -> this.summonLogic(target);
                    case UNBREAKABLE -> this.unbreakable(target);
                    default -> this.boss.setPhaseOneAttack(MoonknightPhaseOne.IDLE);
                }
            } else {
                switch (this.boss.getPhaseTwoAttack()) {
                    case BLINDING_LIGHT -> this.blindingLightLogic(serverWorld);
                    case CORE_BEAM -> this.coreBeam(serverWorld, target);
                    case MOONFALL -> this.moonfallLogic(serverWorld, target);
                    case MOONVEIL -> this.moonveilLogic(serverWorld, target);
                    case SWORD_OF_LIGHT -> this.swordOfLight(serverWorld, target);
                    case THRUST -> this.thrustLogic(serverWorld, target);
                    case RUPTURE -> this.rupturePhase2(serverWorld);
                    case HEAVY_SWING -> this.heavySwing(target);
                    case UNBREAKABLE -> this.unbreakable(target);
                    default -> this.boss.setPhaseTwoAttack(MoonknightPhaseTwo.IDLE);
                }
            }

            if (this.targetNotVisibleTicks < 5) {
                this.boss.getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 1.0D);
            }
        }
        super.tick();
    }

    private void unbreakable(LivingEntity target) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (this.attackStatus == 19) {
            this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            for (Entity entity : boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(20))) {
                if (entity instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 300, 1));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 300, 0));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 150, 0));
                }
            }
            if (this.boss.isPhaseTwo()) {
                AbsorbedProjectilesOrb orb = new AbsorbedProjectilesOrb(this.boss.getWorld(), target);
                orb.setPos(target.getX(), target.getEyeY() + 8f, target.getZ());
                orb.setOwner(this.boss);
                orb.setProjectiles(this.boss.getAbsorbedProjectileTypes(), this.boss.getAbsorbedProjectileDamage());
                this.boss.getWorld().spawnEntity(orb);
                this.boss.clearAbsorbedProjectiles();
                this.boss.getWorld().playSound(null, orb.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.HOSTILE, 1f, 1f);
            }
        }
        if (this.attackStatus >= 37) {
            this.resetAttack(1f, false, 1f);
        }
    }

    private void heavySwing(LivingEntity target) {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(target.getX(), target.getY(), target.getZ());
        this.boss.getNavigation().startMovingTo(target.getX(), target.getY(), target.getZ(), 0.0D);
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (this.attackStatus == 22) {
            double x = target.getX() - (this.boss.getX());
            double y = target.getEyeY() - this.boss.getBodyY(1f);
            double z = target.getZ() - this.boss.getZ();
            this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.MOONVEIL_HORIZONTAL, SoundCategory.HOSTILE, 1f, 0.75f);
            MoonveilWave projectile = new MoonveilWave(this.boss.getWorld(), this.boss, 40);
            projectile.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
            projectile.setVelocity(x, y, z, 1.5f, 1f);
            projectile.setDamage(this.getModifiedDamage(40f));
            projectile.setAreaParticleCount((byte) 20);
            projectile.setAreaParticle(ParticleRegistry.NIGHTFALL_PARTICLE);
            projectile.setDespawnParticle(ParticleTypes.SOUL_FIRE_FLAME);
            projectile.setDespawnParticleCount(50);
            projectile.setTextureId(MoonveilWave.MoonveilTextures.MOONLIGHT_PROJECTILE_BIG);
            projectile.setBoundingBoxWidth(8f);
            projectile.setAreaParticleCount((byte) 15);
            projectile.setDespawnParticleCount(40);
            this.boss.getWorld().spawnEntity(projectile);
        }
        if (this.attackStatus >= 35) {
            this.resetAttack(1f, false, 1f);
        }
    }

    private void coreBeam(ServerWorld serverWorld, LivingEntity target) {
        this.attackStatus++;
        if (attackStatus == 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus < 20 && target.getBlockPos() != null) {
            this.targetPos = target.getPos();
        }
        if (this.targetPos != null) {
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY() + this.bonusBeamHeight, this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY() + this.bonusBeamHeight, this.targetPos.getZ(), 0.0D);
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            if (this.attackStatus >= 21 && this.attackStatus <= 47) {
                if (attackStatus == 21) this.boss.getWorld().playSound(null, BlockPos.ofFloored(targetPos), SoundRegistry.KNIGHT_CORE_BEAM_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                this.boss.setBeamLocation(BlockPos.ofFloored(targetPos));
                this.boss.setCanBeam(true);
                if (this.attackStatus % 2 == 0) {
                    this.boss.getWorld().createExplosion(boss, DamageSourceRegistry.create(this.boss.getWorld(), DamageSourceRegistry.BEAM, this.boss), null, targetPos.getX(), targetPos.getY() + this.bonusBeamHeight, targetPos.getZ(), 4f, false, World.ExplosionSourceType.TRIGGER);
                    Vec3d vec = new Vec3d(targetPos.getX(), targetPos.getY() + this.bonusBeamHeight, targetPos.getZ());
                    ParticleHandler.particleOutburstMap(this.boss.getWorld(), 20, vec.getX() + boss.getRandom().nextDouble() - 0.5, vec.getY() + boss.getRandom().nextDouble() - 0.5, vec.getZ() + boss.getRandom().nextDouble() - 0.5, ParticleEvents.CORE_BEAM_EXPLOSION_MAP, 1.3f);
                    this.boss.getWorld().playSound(null, BlockPos.ofFloored(vec), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.HOSTILE, 1f, 1f);
                    for (Entity entity : this.boss.getWorld().getOtherEntities(boss, new Box(targetPos, this.boss.getPos().add(0, 4, 0)))) {
                        if (entity instanceof LivingEntity) {
                            entity.damage(serverWorld, DamageSourceRegistry.create(this.boss.getWorld(), DamageSourceRegistry.BEAM, this.boss), this.getModifiedDamage(20f));
                            entity.setOnFireFor(4);
                        }
                    }
                }
                if (this.attackStatus >= 26) {
                    this.bonusBeamHeight += 0.35f;
                    this.boss.setBeamHeight(this.bonusBeamHeight);
                }
            }
            if (this.attackStatus > 47) {
                this.targetPos = this.targetPos.add(0, MathHelper.floor(-this.bonusBeamHeight), 0);
                this.boss.setCanBeam(false);
            }
        }
        if (this.attackStatus >= 85) {
            this.boss.getNavigation().stop();
            this.bonusBeamHeight = 0f;
            this.boss.setBeamHeight(0f);
            this.resetAttack(1f, true, 0.8f);
        }
    }

    private void swordOfLight(ServerWorld serverWorld, LivingEntity target) {
        this.attackStatus++;
        double distance = this.boss.squaredDistanceTo(target);
        if (target.getBlockPos() != null) {
            if (distance < 65D) {
                this.targetPos = target.getPos();
            } else {
                Vec3d direction = new Vec3d(target.getX() - this.boss.getBlockX(), 0, target.getZ() - this.boss.getBlockZ()).multiply(.5f);
                Vec3d spot = new Vec3d(this.boss.getX(), this.boss.getY(), this.boss.getZ()).add(direction);
                this.targetPos = spot;
            }
            this.yaw = this.boss.getHeadYaw();
        }
        if (this.targetPos != null && target.getPos() != null) {
            double x = target.getX() - (this.boss.getX());
            double y = target.getEyeY() - this.boss.getBodyY(1f);
            double z = target.getZ() - this.boss.getZ();
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            switch (attackStatus) {
                case 10, 16, 22, 27 -> {
                    this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.HOSTILE, 1f, 0.75f);
                    for (Entity entity : this.boss.getWorld().getOtherEntities(boss, new Box(BlockPos.ofFloored(this.targetPos)).expand(3))) {
                        if (entity instanceof LivingEntity) {
                            entity.damage(serverWorld, this.boss.getWorld().getDamageSources().mobAttack(boss), this.getModifiedDamage(25f));
                            ((LivingEntity) entity).takeKnockback(2f, -(entity.getX() - this.boss.getX()), -(entity.getZ() - this.boss.getZ()));
                        }
                    }

                    this.boss.getWorld().playSound(null, BlockPos.ofFloored(this.targetPos), SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                    MoonveilWave projectile = new MoonveilWave(EntityRegistry.MOONVEIL_HORIZONTAL, this.boss.getWorld(), this.boss, 40);
                    projectile.setAreaParticleCount((byte) 10);
                    projectile.setAreaParticle(ParticleRegistry.NIGHTFALL_PARTICLE);
                    projectile.setDespawnParticle(ParticleTypes.SOUL_FIRE_FLAME);
                    projectile.setDespawnParticleCount(40);
                    projectile.setDamage(this.getModifiedDamage(25f));
                    projectile.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
                    projectile.setVelocity(x, y, z, 1.5f, 1f);
                    projectile.setModelRotationX(projectileRotation);
                    projectile.setTextureId(MoonveilWave.MoonveilTextures.MOONLIGHT_PROJECTILE_BIG);
                    if (projectileRotation == -45) {
                        projectileRotation = 45;
                    } else {
                        projectileRotation = 0;
                    }
                    this.boss.getWorld().spawnEntity(projectile);
                }
                case 30 -> this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.KNIGHT_CHARGE_SWORD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                case 39 -> this.boss.getWorld().playSound(null, BlockPos.ofFloored(this.targetPos), SoundRegistry.KNIGHT_SWORD_SMASH_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                case 43 -> {
                    this.smashGround(serverWorld, 30f, SoundRegistry.KNIGHT_SWORD_SMASH_EVENT, true);
                    float yaw = (this.yaw == 0f ? this.boss.getHeadYaw() : this.yaw) + 90;
                    this.summonMoonfallLine(yaw);
                }
            }
        }
        if (attackStatus >= 52) {
            this.boss.getNavigation().stop();
            this.projectileRotation = -45;
            this.resetAttack(1f, false, 1f);
        }
    }

    private void summonMoonfallLine(float yaw) {
        WeaponUtil.doConsumerOnLine(this.boss.getWorld(), yaw, this.targetPos, 10, 14, 1.75f, (Vec3d position, Integer warmup, Float yawOutput) -> {
            HolyMoonlightPillar pillar = new HolyMoonlightPillar(EntityRegistry.HOLY_MOONLIGHT_PILLAR, this.boss.getWorld());
            pillar.setOwner(this.boss);
            pillar.setParticleAmountMod(1.5f);
            pillar.setRadius(3.5f);
            pillar.setDamage(this.getModifiedDamage(30f));
            pillar.setKnockUp(1f);
            pillar.setWarmup(warmup);
            pillar.setPos(position.getX(), position.getY(), position.getZ());
            this.boss.getWorld().spawnEntity(pillar);
        });
    }

    private void thrustLogic(ServerWorld serverWorld, LivingEntity target) {
        attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (attackStatus == 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.KNIGHT_CHARGE_SWORD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus < 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus < 13 && target.getBlockPos() != null) {
            this.targetPos = target.getPos();
            this.height = target.getEyeY();
        } else if (this.targetPos != null) {
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
            switch (attackStatus) {
                case 15 -> this.boss.getWorld().playSound(null, BlockPos.ofFloored(targetPos), SoundRegistry.KNIGHT_THRUST_SWORD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                case 17 -> {
                    if (this.boss.squaredDistanceTo(target) < 128D) {
                        for (Entity entity : boss.getWorld().getOtherEntities(this.boss, new Box(BlockPos.ofFloored(this.targetPos)).expand(1))) {
                            if (entity instanceof LivingEntity living) {
                                entity.damage(serverWorld, this.boss.getWorld().getDamageSources().mobAttack(boss), this.getModifiedDamage(40f));
                                living.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200, 1));
                                living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1));
                                living.addStatusEffect(new StatusEffectInstance(EffectRegistry.DISABLE_HEAL, 100, 0));
                            }
                        }
                        ParticleHandler.particleOutburstMap(serverWorld, 250, this.targetPos.getX(), this.height == 0 ? targetPos.getY() : this.height, this.targetPos.getZ(), ParticleEvents.SOUL_FLAME_SMALL_OUTBURST_MAP, 1f);
                    }
                }
                case 29 -> this.smashGround(serverWorld, 40, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), false);
                case 39 -> {
                    Vec3d lookVec = EntityLookUtil.getDirectionTo(boss, BlockPos.ofFloored(this.targetPos));
                    BlockPos fiveAway = EntityLookUtil.getBlockPosAhead(this.boss, lookVec, 5.0);
                    BlockPos pos = new BlockPos(fiveAway.getX(), this.boss.getBlockY(), fiveAway.getZ());
                    Box box = new Box(pos).expand(2, 4, 2);
                    for (Entity entity : boss.getWorld().getOtherEntities(this.boss, box)) {
                        if (entity instanceof LivingEntity living) {
                            living.damage(serverWorld, this.boss.getWorld().getDamageSources().mobAttack(boss), this.getModifiedDamage(35));
                        }
                    }
                    this.boss.getWorld().playSound(null, BlockPos.ofFloored(this.targetPos), SoundRegistry.SCYTHE_SWIPE, SoundCategory.HOSTILE, 1f, 1f);
                }
            }
        }
        if (this.attackStatus == 48) {
            this.blindingLight(serverWorld, this.boss.getWorld().getDifficulty().equals(Difficulty.HARD));
        }
        if (this.attackStatus >= 56) {
            this.boss.getNavigation().stop();
            this.resetAttack(0.75f, false, 1f);
        }
    }

    private void moonfallLogic(ServerWorld serverWorld, LivingEntity target) {
        this.obliterateLogic(serverWorld, target, 25, 15, 43, 65f, SoundRegistry.KNIGHT_SWORD_SMASH_EVENT, true);
        if (attackStatus == 18) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.KNIGHT_SWORD_SMASH_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        if (attackStatus == 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.KNIGHT_CHARGE_SWORD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus == 26) {
            float yaw = (this.yaw == 0f ? this.boss.getHeadYaw() : this.yaw) + 90;
            for (int i = 0; i < 360; i += 45) {
                if (i == 180) continue;
                this.summonMoonfallLine(yaw + i);
            }
        }
    }

    private void moonveilLogic(ServerWorld serverWorld, LivingEntity target) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 5));
        switch (attackStatus) {
            case 24 -> {
                double e = target.getX() - (this.boss.getX());
                double g = target.getZ() - this.boss.getZ();
                this.boss.addVelocity(e/8, 1, g/8);
                this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1f, 1f);
            }
            case 38, 63 -> {
                this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.HOSTILE, 1f, 1f);
                for (Entity entity : this.boss.getWorld().getOtherEntities(boss, this.boss.getBoundingBox().expand(6))) {
                    if (entity instanceof LivingEntity livingEntity) {
                        double x = livingEntity.getX() - (this.boss.getX());
                        double z = livingEntity.getZ() - this.boss.getZ();
                        livingEntity.takeKnockback(10F, -x, -z);
                        livingEntity.damage(serverWorld, this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(50f));
                    }
                }
                ParticleHandler.particleSphereList(serverWorld, 1000, this.boss.getX(), this.boss.getY(), this.boss.getZ(), 1f, ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.LARGE_SMOKE);
            }
        }
        if (this.attackStatus >= 76) {
            this.resetAttack(1.2f, true, 0.6f);
        }
    }

    private void smashGround(ServerWorld serverWorld, float damage, SoundEvent sound, boolean isSoundDelayed) {
        for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, new Box(BlockPos.ofFloored(this.targetPos)).expand(3))) {
            if (entity instanceof LivingEntity living) {
                entity.damage(serverWorld, DamageSourceRegistry.create(this.boss.getWorld(), DamageSourceRegistry.OBLITERATED, this.boss), this.getModifiedDamage(damage));
                entity.addVelocity(0, 1, 0);
                if (living.hasInvertedHealingAndHarm() && living.isDead() && this.isValidSpawn(living.getBlockPos())) {
                    this.summonRemnant(living.getPos());
                }
            }
        }
        if (!isSoundDelayed) this.boss.getWorld().playSound(null, BlockPos.ofFloored(this.targetPos), sound, SoundCategory.HOSTILE, 1f, 1f);
        if (!this.boss.getWorld().isClient) {
            ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
        }
    }

    private boolean isValidSpawn(BlockPos pos) {
        return this.boss.getWorld().getBlockState(pos).isAir() && !this.boss.getWorld().getBlockState(pos.down()).isAir();
    }

    private void obliterateLogic(ServerWorld serverWorld, LivingEntity target, int hitFrame, int followTargetTicks, int attackFinishedTicks, float damage, SoundEvent sound, boolean isSoundDelayed) {
        this.attackStatus++;
        if (this.attackStatus < followTargetTicks && target.getBlockPos() != null) {
            this.targetPos = target.getPos();
        } else if (this.targetPos != null) {
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
            this.yaw = this.boss.getHeadYaw();
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            if (this.attackStatus == hitFrame) {
                this.smashGround(serverWorld, damage, sound, isSoundDelayed);
            }
            if (this.attackStatus >= attackFinishedTicks) {
                this.boss.getNavigation().stop();
                this.resetAttack(1f, false, 1f);
            }
        }
    }

    private void maceOfSpadesLogic(ServerWorld serverWorld, LivingEntity target) {
        this.attackStatus++;
        if (this.attackStatus < 3 && target.getBlockPos() != null) {
            this.targetPos = target.getPos();
        } else if (this.targetPos != null) {
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
            this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
            this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
            if (this.attackStatus == 7) {
                for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, new Box(targetPos.getX() - 5, targetPos.getY() - 2, targetPos.getZ() - 5, targetPos.getX() + 5, targetPos.getY() + 2, targetPos.getZ() + 5))) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.takeKnockback(2f, -(livingEntity.getX() - this.boss.getX()), -(livingEntity.getZ() - this.boss.getZ()));
                        livingEntity.damage(serverWorld, this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(20f));
                    }
                }
                this.boss.getWorld().playSound(null, BlockPos.ofFloored(this.targetPos), SoundRegistry.KNIGHT_SWIPE_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            }
            if (this.attackStatus == 13 && target.getBlockPos() != null) {
                this.targetPos = target.getPos();
            }
            if (this.attackStatus == 21 && this.targetPos != null) {
                for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, new Box(BlockPos.ofFloored(this.targetPos)).expand(3D))) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.addVelocity(0, 1, 0);
                        livingEntity.damage(serverWorld, this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(25f));
                    }
                }
                this.boss.getWorld().playSound(null, BlockPos.ofFloored(this.targetPos), SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                ParticleHandler.particleOutburstMap(serverWorld, 300, this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
            }
            if (this.attackStatus >= 36) {
                this.boss.getNavigation().stop();
                this.resetAttack(1f, false, 1f);
            }
        }
    }

    private void ruptureLogic(ServerWorld serverWorld) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (attackStatus == 21 || attackStatus == 33) {
            for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(12))) {
                this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            }
        }
        if (this.attackStatus == 52) {
            for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(12))) {
                if (entity instanceof LivingEntity && !(entity instanceof Remnant remnant && remnant.isOwner(this.boss))) {
                    entity.damage(serverWorld, this.boss.getWorld().getDamageSources().mobAttack(boss), this.getModifiedDamage(35f));
                    this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.HOSTILE, 1f, 1f);
                    entity.addVelocity(0, 1.0, 0);
                    if (!this.boss.getWorld().isClient) {
                        ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, entity.getX(), entity.getY(), entity.getZ(), ParticleEvents.GROUND_RUPTURE_MAP, 1f);
                    }
                }
            }
        }
        if (this.attackStatus >= 70) {
            this.resetAttack(0.5f, true, 1f);
        }
    }

    private void rupturePhase2(ServerWorld serverWorld) {
        this.attackStatus++;
        if (this.attackStatus == 5) {
            for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(12))) {
                this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1f, 0.6f);
            }
        }
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20, true, true));
        if (this.attackStatus == 26) {
            int per = 10;
            int delay = 0;
            for (int theta = 0; theta < 720; theta += per) {
                float r = theta * 0.03f;
                double x = r * Math.cos(theta);
                double z = r * Math.sin(theta);
                Vec3d pos = new Vec3d(this.boss.getX() + x, this.boss.getY(), this.boss.getZ() + z);
                this.spawnPillar(pos, delay);
                if (theta % 20 == 0) delay++;
            }
        }
        if (this.attackStatus == 29) {
            this.targetPos = this.boss.getPos();
            this.smashGround(serverWorld, 25f, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), false);
        }
        if (this.attackStatus >= 58) {
            this.resetAttack(0.5f, true, 1f);
        }
    }

    private void spawnPillar(Vec3d pos, int warmup) {
        HolyMoonlightPillar pillar = new HolyMoonlightPillar(EntityRegistry.HOLY_MOONLIGHT_PILLAR, this.boss.getWorld());
        pillar.setOwner(this.boss);
        pillar.setParticleAmountMod(1.5f);
        pillar.setRadius(3.5f);
        pillar.setDamage(this.getModifiedDamage(30f));
        pillar.setKnockUp(1f);
        pillar.setWarmup(warmup);
        pillar.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.boss.getWorld().spawnEntity(pillar);
    }

    private void blindingLightLogic(ServerWorld serverWorld) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (this.attackStatus < 1) this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 1f, 1f);
        if (this.attackStatus == 9) {
            this.blindingLight(serverWorld, this.boss.isPhaseTwo());
        }
        if (this.attackStatus >= 24) {
            this.resetAttack(0.5f, false, 1f);
        }
    }

    private void blindingLight(ServerWorld serverWorld, boolean shootNightWaves) {
        if (!this.boss.getWorld().isClient) {
            ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, this.boss.getX(), this.boss.getY(), this.boss.getZ(), ParticleEvents.BLINDING_LIGHT_SMASH_MAP, 1f);
        }
        this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.BLINDING_LIGHT_EXPLOSION_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(4.5, 2, 4.5))) {
            if (entity instanceof LivingEntity living) {
                living.damage(serverWorld, this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(20f));
                living.takeKnockback(3f, -living.getX() - this.boss.getX(), -living.getZ() - this.boss.getZ());
                living.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
                living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
            }
        }
        if (shootNightWaves) {
            int r = 3;
            int y = this.boss.getBlockPos().getY() + 1;
            for (int theta = 0; theta < 360; theta += 15) {
                double x0 = this.boss.getX();
                double z0 = this.boss.getZ();
                double x = x0 + r * Math.cos(theta * Math.PI / 180);
                double z = z0 + r * Math.sin(theta * Math.PI / 180);
                MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, this.boss.getWorld());
                projectile.setAgeAndPoints(15, 30, (byte) 1);
                projectile.setPos(x, y, z);
                projectile.setVelocity(this.boss, 0, theta, 0.0f, 1.5f, 0f);
                projectile.setDamage(this.getModifiedDamage(20f));
                this.boss.getWorld().spawnEntity(projectile);
            }
        }
    }

    private void summonLogic(LivingEntity target) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (this.attackStatus == 30) {
            int enemyNumber = this.boss.getRandom().nextInt(6 - 3) + 3;
            RandomSummonPos pos = new RandomSummonPos(this.boss.getWorld(), this.boss.getRandom(), enemyNumber, 10, this.boss.getBlockPos(), 10, 8, 5, this::summonRemnant);
            pos.applySummonSpawns();
            if (!this.boss.getWorld().isClient) {
                ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, target.getX(), target.getY(), target.getZ(), ParticleEvents.GROUND_RUPTURE_MAP, 1f);
            }
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 140, 1));
            this.boss.getWorld().playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        }
        if (this.attackStatus >= 48) {
            this.resetAttack(1f, true, 2f);
        }
    }

    private void summonRemnant(Vec3d pos) {
        Remnant entity = new Remnant(EntityRegistry.REMNANT, this.boss.getWorld());
        entity.setPosition(pos);
        this.initEquip(entity);
        this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        this.boss.getWorld().spawnEntity(entity);
        if (!this.boss.getWorld().isClient) {
            ParticleHandler.particleOutburstMap(this.boss.getWorld(), 100, pos.getX(), pos.getY(), pos.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
        }
    }

    private void initEquip(LivingEntity entity) {
        HashMap<ItemStack, EquipmentSlot> equip = new HashMap<>();
        equip.put(new ItemStack(Items.NETHERITE_HELMET), EquipmentSlot.HEAD);
        equip.put(new ItemStack(Items.NETHERITE_CHESTPLATE), EquipmentSlot.CHEST);
        equip.put(new ItemStack(Items.NETHERITE_LEGGINGS), EquipmentSlot.LEGS);
        equip.put(new ItemStack(Items.NETHERITE_BOOTS), EquipmentSlot.FEET);
        for (ItemStack stack : equip.keySet()) {
            WeaponUtil.applyEnchantment(this.boss.getWorld(), stack, Enchantments.PROTECTION, 2);
            WeaponUtil.applyEnchantment(this.boss.getWorld(), stack, Enchantments.VANISHING_CURSE, 1);
            entity.equipStack(equip.get(stack), stack);
        }
        entity.setHealth(entity.getMaxHealth() * 1.5f);
    }
}
