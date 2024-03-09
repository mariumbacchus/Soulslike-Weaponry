package net.soulsweaponry.entity.ai.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.NightShade;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.entity.projectile.ShadowOrb;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleHandler;

import java.util.EnumSet;
import java.util.HashMap;

public class NightShadeGoal extends Goal {

    private int attackCooldown;
    private int attackStatus;
    private final NightShade boss;

    public NightShadeGoal(NightShade boss) {
        this.boss = boss;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public boolean canStart() {
        LivingEntity target = this.boss.getTarget();
        return target != null && target.isAlive() && this.boss.canTarget(target) && this.boss.getRandom().nextInt(7) == 0 && !this.boss.getSpawn();
    }

    public boolean shouldContinue() {
        return this.boss.getCharging() && this.boss.getTarget() != null && this.boss.getTarget().isAlive();
    }

    public void start() {
        LivingEntity livingEntity = this.boss.getTarget();
        if (livingEntity == null) return;
        Vec3d vec3d = livingEntity.getEyePos();
        this.boss.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
        this.boss.setCharging(true);
    }

    public void stop() {
        this.boss.setCharging(false);
        this.reset(0);
    }

    private void reset(float cooldownModifier) {
        this.attackStatus = 0;
        this.attackCooldown = (int) Math.floor((float)ConfigConstructor.frenzied_shade_cooldown * cooldownModifier);
    }

    private void damageTarget(LivingEntity target, float damage) {
        if (target instanceof NightShade) return;
        target.damage(this.boss.method_48926().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(damage));
    }

    private float getModifiedDamage(float damage) {
        return damage * ConfigConstructor.frenzied_shade_damage_modifier * (this.boss.isCopy() ? 0.35f : 1f);
    }

    private void randomAttack(LivingEntity target) {
        if (target == null) {
            this.boss.setAttackState(NightShade.AttackStates.IDLE);
            return;
        }
        int rand = this.boss.getRandom().nextInt(NightShade.AttackStates.values().length);
        NightShade.AttackStates attack = NightShade.AttackStates.values()[rand];
        switch (attack) {
            case GENERIC_CHARGE, AOE, BIG_SWIPES -> {
                this.boss.setCharging(true);
                this.boss.setAttackState(attack);
            }
            case THROW_MOONLIGHT, SHADOW_ORBS -> {
                if (!this.boss.isInsideWall()) {
                    this.boss.setCharging(true);
                    this.boss.setAttackState(attack);
                }
            }
            default -> this.boss.setAttackState(NightShade.AttackStates.IDLE);
        }
    }

    public void tick() {
        if (this.boss.isDead() || this.boss.getAttackState().equals(NightShade.AttackStates.DUPLICATE) || this.boss.getSpawn()) {
            return;
        }
        attackCooldown--;
        LivingEntity target = this.boss.getTarget();
        if (target != null) {
            Vec3d vec3d = target.getEyePos();
            if (this.attackCooldown > 0) {
                this.boss.setAttackState(NightShade.AttackStates.IDLE);
                this.moveRandomSpot(vec3d);
            }
            if (this.attackCooldown < 0 && this.boss.getAttackState().equals(NightShade.AttackStates.IDLE)) {
                this.randomAttack(target);
                this.boss.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            }
            switch (this.boss.getAttackState()) {
                case BIG_SWIPES -> this.bigSwipes(target);
                case GENERIC_CHARGE -> this.genericCharge(target);
                case AOE -> this.aoe(target);
                case THROW_MOONLIGHT -> this.throwMoonlight(target);
                case SHADOW_ORBS -> {
                    this.boss.getNavigation().stop();
                    this.shadowBall(target);
                }
            }
        }
    }

    private void moveRandomSpot(Vec3d vec3d) {
        this.boss.getMoveControl().moveTo(vec3d.x + this.boss.getRandom().nextInt(60) - 30, vec3d.y + this.boss.getRandom().nextInt(20) - 10, vec3d.z + this.boss.getRandom().nextInt(60) - 30, 1.5D);
    }

    private void bigSwipes(LivingEntity target) {
        this.attackStatus++;
        if (attackStatus == 1 && target.getBlockPos() != null) this.boss.setTargetPos(target.getBlockPos());
        if (DraugrBossGoal.isPosNotNullish(this.boss.getTargetPos())) {
            BlockPos pos = this.boss.getTargetPos();
            this.boss.getLookControl().lookAt(pos.getX(), pos.getY(), pos.getZ());
            this.boss.getMoveControl().moveTo(pos.getX() + 2.5f, pos.getY() + 2f, pos.getZ() + .5f, 3.0D);
            if (this.attackStatus == 9 || this.attackStatus == 16) {
                this.boss.method_48926().playSound(null, target.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.HOSTILE, 1f, 1f);
                if (!this.boss.method_48926().isClient) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = -10; j <= 10; j++) {
                            ParticleHandler.singleParticle(this.boss.method_48926(), ParticleTypes.GLOW, pos.getX(), pos.getY() + 0.3f + (float) i / 1.5f, pos.getZ() + (float) j / 10f, 0, 0, 0);
                        }
                    }
                }
                for (Entity entity : this.boss.method_48926().getOtherEntities(this.boss, new Box(pos).expand(2D))) {
                    if (entity instanceof LivingEntity living) {
                        this.damageTarget(living, 20f);
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 3));
                    }
                }
            }
        }
        if (attackStatus >= 22) {
            this.reset(1f);
        }
    }

    private void genericCharge(LivingEntity target) {
        this.attackStatus++;
        Vec3d vec3d = target.getEyePos();
        if (attackStatus == 1) {
            this.moveRandomSpot(vec3d);
        } else {
            this.boss.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
        }
        if (attackStatus > 6 && attackStatus % 2 == 0 && attackStatus <= 14) {
            for (Entity entity : this.boss.method_48926().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(1D))) {
                if (entity instanceof LivingEntity living) {
                    this.damageTarget(living, this.attackStatus == 14 ? 18f : 12f);
                }
            }
        }
        if (attackStatus >= 20) {
            this.reset(1f);
        }
    }

    private void aoe(LivingEntity target) {
        this.attackStatus++;
        if (attackStatus == 1 && target.getBlockPos() != null) this.boss.setTargetPos(target.getBlockPos());
        if (DraugrBossGoal.isPosNotNullish(this.boss.getTargetPos())) {
            BlockPos pos = this.boss.getTargetPos();
            this.boss.getLookControl().lookAt(pos.getX(), pos.getY(), pos.getZ());
            if (attackStatus < 10) {
                this.boss.getMoveControl().moveTo(pos.getX(), pos.getY() + 10f, pos.getZ(), 3.0D);
            }
            if (attackStatus > 10) {
                this.boss.getMoveControl().moveTo(pos.getX(), pos.getY(), pos.getZ(), 4.0D);
                if (attackStatus == 16) {
                    if (!this.boss.method_48926().isClient) {
                        HashMap<ParticleEffect, Vec3d> map = new HashMap<>();
                        map.put(ParticleTypes.LARGE_SMOKE, new Vec3d(1, 1, 1));
                        map.put(ParticleTypes.SOUL_FIRE_FLAME, new Vec3d(1, 1, 1));
                        ParticleHandler.particleOutburstMap(this.boss.method_48926(), 600, this.boss.getX(), this.boss.getY(), this.boss.getZ(), map, 1f);
                    }
                    this.boss.method_48926().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 0.8f, 1f);
                    for (Entity entity : this.boss.method_48926().getOtherEntities(this.boss, new Box(pos).expand(3D))) {
                        if (entity instanceof LivingEntity living) {
                            this.damageTarget(living, 25f);
                            double x = living.getX() - this.boss.getX();
                            double z = living.getZ() - this.boss.getZ();
                            living.takeKnockback(2f, -x, -z);
                        }
                    }
                }
            }
        }
        if (attackStatus >= 22) {
            this.reset(1f);
        }
    }

    private void throwMoonlight(LivingEntity target) {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(target);
        this.boss.getNavigation().stop();
        if (attackStatus == 6) {
            double e = target.getX() - (this.boss.getX());
            double f = target.getBodyY(0.5D) - this.boss.getBodyY(1.0D);
            double g = target.getZ() - this.boss.getZ();
            this.boss.method_48926().playSound(null, this.boss.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.HOSTILE, 1f, 0.75f);
            MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.VERTICAL_MOONLIGHT_ENTITY_TYPE, this.boss.method_48926(), this.boss);
            projectile.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
            projectile.setVelocity(e, f, g, 1.5f, 1f);
            projectile.setDamage(this.getModifiedDamage(25f));
            projectile.setHugeExplosion(true);
            projectile.setAgeAndPoints(30, 75, 10);
            this.boss.method_48926().spawnEntity(projectile);
        }
        if (attackStatus >= 10) {
            this.reset(1f);
        }
    }

    private void shadowBall(LivingEntity target) {
        this.attackStatus++;
        this.boss.getLookControl().lookAt(target);
        this.boss.getNavigation().stop();
        double e = target.getX() - (this.boss.getX());
        double f = target.getBodyY(0.5D) - this.boss.getBodyY(1.0D);
        double g = target.getZ() - this.boss.getZ();
        if (attackStatus >= 6 && attackStatus <= 15) {
            this.boss.method_48926().playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1f, 1f);
            ShadowOrb orb = new ShadowOrb(this.boss.method_48926(), this.boss, e, f, g,
                    new StatusEffect[] {StatusEffects.DARKNESS, EffectRegistry.DECAY});
            orb.setPosition(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
            orb.setVelocity(e, f, g, 2f, 1f);
            this.boss.method_48926().spawnEntity(orb);
        }
        if (attackStatus == 16) {
            this.boss.method_48926().playSound(null, this.boss.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, this.boss.method_48926(), this.boss);
            projectile.setPos(this.boss.getX(), this.boss.getEyeY(), this.boss.getZ());
            projectile.setVelocity(e, f, g, 2f, 1f);
            projectile.setAgeAndPoints(30, 150, 4);
            projectile.setDamage(this.getModifiedDamage(18f));
            projectile.setRotateState(MoonlightProjectile.RotationState.NORMAL);
            this.boss.method_48926().spawnEntity(projectile);
        }
        if (attackStatus >= 25) {
            this.reset(1f);
        }
    }
}
