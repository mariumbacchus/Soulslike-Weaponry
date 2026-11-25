package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.ReturningKnightConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.ReturningKnightAttack;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.entity.projectile.noclip.DarknessOrb;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.List;

public class DarkCircle extends ReturningKnightAttack {

    private static final float SLAM_DAMAGE = ReturningKnightConfig.dark_circle_phase_2_slam_damage;
    private static final float SLAM_RADIUS_HORIZONTAL = ReturningKnightConfig.dark_circle_phase_2_slam_radius_horizontal;
    private static final int SLAM_SLOWNESS_AMP = (int) ReturningKnightConfig.dark_circle_phase_2_slam_slowness_amp;
    private static final int SLAM_SLOWNESS_DURATION = (int) ReturningKnightConfig.dark_circle_phase_2_slam_slowness_duration;
    private static final int SLAM_DARKNESS_DURATION = (int) ReturningKnightConfig.dark_circle_phase_2_slam_darkness_duration;

    private static final int ORB_SPAWN_TICK = 28;
    private static final int ORB_RELEASE_TICK = 98;

    private static final boolean BOOST_DARKNESS = ReturningKnightConfig.dark_circle_phase_2_darkness_orb_turns_darkness_into_blindness;
    private static final int ORB_COUNT = (int) ReturningKnightConfig.dark_circle_phase_2_darkness_orb_count;
    private static final float ORB_RING_RADIUS = ReturningKnightConfig.dark_circle_phase_2_darkness_orb_ring_radius;
    private static final float ORB_DAMAGE = ReturningKnightConfig.dark_circle_phase_2_darkness_orb_damage;
    private static final float ORB_SPEED = ReturningKnightConfig.dark_circle_phase_2_darkness_orb_speed;
    private static final int ORB_FLIGHT_TICKS = (int) ReturningKnightConfig.dark_circle_phase_2_darkness_orb_lifetime_ticks;

    private final List<DarknessOrb> waitingOrbs = new ArrayList<>();

    public DarkCircle(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus) {
        this.getBoss().getNavigation().stop();
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 40));
        if (this.isTick(attackStatus, 28)) {
            this.playSound(SoundRegistry.NIGHTFALL_SHIELD_EVENT, 2f);
        }
        if (this.isAnyTick(attackStatus, 37, 47, 57, 67, 77)) {
            this.playSound(SoundEvents.ENTITY_GUARDIAN_ATTACK, 3f);
        }
        if (this.isTick(attackStatus, ORB_SPAWN_TICK)) {
            this.spawnOrbCircle();
        }
        if (this.isTick(attackStatus, ORB_RELEASE_TICK)) {
            this.releaseOrbCircle();
        }
        if (this.isTick(attackStatus, 98)) {
            for (LivingEntity living : this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoss().getBoundingBox()
                            .expand(SLAM_RADIUS_HORIZONTAL, -2, SLAM_RADIUS_HORIZONTAL).offset(0, -3, 0),
                    e -> !e.equals(this.getBoss()))) {
                if (this.damageTarget(living, SLAM_DAMAGE)) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, SLAM_SLOWNESS_DURATION, SLAM_SLOWNESS_AMP));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, SLAM_DARKNESS_DURATION, 0));
                }
            }
            ParticleHandler.particleOutburstMap(this.getWorld(), 500, this.getBoss().getX(), this.getBoss().getY(), this.getBoss().getZ(), ParticleEvents.BLINDING_LIGHT_SMASH_MAP, 3f);
            this.playSound(SoundRegistry.BLINDING_LIGHT_EXPLOSION_EVENT, 3.5f);
        }
    }

    private void spawnOrbCircle() {
        this.clearWaitingOrbs();
        ReturningKnight boss = this.getBoss();
        Vec3d center = boss.getPos();

        int waitingTicks = boss.getScaledMaxTicks(ORB_RELEASE_TICK) - boss.getScaledMaxTicks(ORB_SPAWN_TICK);
        int totalLifetime = waitingTicks + ORB_FLIGHT_TICKS;
        float damage = this.getBoss().getModifiedDamage(ORB_DAMAGE);

        for (int i = 0; i < ORB_COUNT; i++) {
            double angle = i * Math.PI * 2D / ORB_COUNT;
            Vec3d spawnPos = center.add(Math.cos(angle) * ORB_RING_RADIUS, 0D, Math.sin(angle) * ORB_RING_RADIUS);

            DarknessOrb orb = new DarknessOrb(this.getWorld(), boss, totalLifetime, BOOST_DARKNESS);
            orb.setDamage(damage);
            orb.setPosition(spawnPos);
            orb.setNoGravity(true);
            orb.setVelocity(Vec3d.ZERO);
            if (this.getWorld().spawnEntity(orb)) {
                this.waitingOrbs.add(orb);
            }
        }
    }

    private void releaseOrbCircle() {
        Vec3d center = this.getBoss().getPos();
        for (DarknessOrb orb : this.waitingOrbs) {
            if (orb.isRemoved()) {
                continue;
            }
            Vec3d direction = new Vec3d(center.x - orb.getX(), 0D, center.z - orb.getZ()).normalize();
            orb.setVelocity(direction.x, 0D, direction.z, ORB_SPEED, 0F);
            orb.velocityModified = true;
        }
        this.waitingOrbs.clear();
    }

    private void clearWaitingOrbs() {
        for (DarknessOrb orb : this.waitingOrbs) {
            orb.discard();
        }
        this.waitingOrbs.clear();
    }

    @Override
    public void resetAttackVariables() {
        super.resetAttackVariables();
        this.clearWaitingOrbs();
    }

    @Override
    public boolean canTrigger(LivingEntity target) {
        return this.isWithinDistance(target, 20);
    }

    @Override
    public boolean isAvailable() {
        return this.getBoss().isPhaseTwo();
    }
}
