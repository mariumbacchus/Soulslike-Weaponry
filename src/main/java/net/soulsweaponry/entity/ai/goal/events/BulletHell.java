package net.soulsweaponry.entity.ai.goal.events;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.ReturningKnightConfig;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.ReturningKnightProjectiles;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class BulletHell extends ReturningKnightEvent {

    private static final int START_TICKS = 51;
    private static final int END_TICKS = 40;

    private static final int ORB_INTERVAL = (int) ReturningKnightConfig.returning_knight_bullet_hell_darkness_orb_spawn_interval_ticks;
    private static final int ORB_LIFETIME = (int) ReturningKnightConfig.returning_knight_bullet_hell_darkness_orb_lifetime_ticks;
    private static final float ORB_DAMAGE = ReturningKnightConfig.returning_knight_bullet_hell_darkness_orb_damage;
    private static final float ORB_SPEED = ReturningKnightConfig.returning_knight_bullet_hell_darkness_orb_speed;
    private static final double ROTATION_PER_TICK = ReturningKnightConfig.returning_knight_bullet_hell_darkness_orb_rotations_degrees_per_tick;
    private static final double ORB_VERTICAL_SPACING = ReturningKnightConfig.returning_knight_bullet_hell_darkness_orb_vertical_spacing;

    private static final int STAR_INTERVAL = (int) ReturningKnightConfig.returning_knight_bullet_hell_shadow_pillar_star_spawn_interval_ticks;
    private static final int STAR_ARMS = (int) ReturningKnightConfig.returning_knight_bullet_hell_shadow_pillar_star_arms_amount;
    private static final int PILLARS_PER_ARM = (int) ReturningKnightConfig.returning_knight_bullet_hell_shadow_pillars_per_star_arm;
    private static final double PILLAR_SPACING = ReturningKnightConfig.returning_knight_bullet_hell_shadow_pillars_spacing;
    private static final int PILLAR_WARMUP = (int) ReturningKnightConfig.returning_knight_bullet_hell_shadow_pillars_warmup;
    private static final float PILLAR_RADIUS = ReturningKnightConfig.returning_knight_bullet_hell_shadow_pillars_radius;
    private static final float PILLAR_DAMAGE = ReturningKnightConfig.returning_knight_bullet_hell_shadow_pillars_damage;

    private final int shootingTicksDuration;

    public BulletHell(ReturningKnight boss, int shootingTicks) {
        super(boss, START_TICKS + shootingTicks + END_TICKS);
        this.shootingTicksDuration = shootingTicks;
    }

    @Override
    public void tickEvent(int eventTick) {
        ReturningKnight boss = this.getBoss();
        boss.getNavigation().stop();
        boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 40));
        if (eventTick == 1) {
            this.playSound(SoundRegistry.KNIGHT_DEATH_EVENT, 1f);
        }

        int startEnd = boss.getScaledMaxTicks(START_TICKS);
        int shootingEnd = boss.getScaledMaxTicks(START_TICKS + this.shootingTicksDuration);
        if (eventTick <= startEnd) {
            boss.setState(ReturningKnight.States.BULLET_HELL_START);
            return;
        }
        if (eventTick > shootingEnd) {
            boss.setState(ReturningKnight.States.BULLET_HELL_END);
            return;
        }
        boss.setState(ReturningKnight.States.BULLET_HELL_TICK);

        int shootingTick = eventTick - startEnd - 1;
        double rotation = shootingTick * Math.toRadians(ROTATION_PER_TICK);
        if (shootingTick % ORB_INTERVAL == 0) {
            this.spawnRotatingCross(rotation);
        }
        if (shootingTick % 10 == 0) {
            this.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.5F, 0.5F);
        }
        if ((shootingTick + 30) % STAR_INTERVAL == 0) {
            this.spawnPillarStar(rotation);
        }
    }

    private void spawnRotatingCross(double rotationRadians) {
        ReturningKnight boss = this.getBoss();
        float damage = ORB_DAMAGE * ReturningKnightConfig.returning_knight_damage_modifier;
        Vec3d center = boss.getPos();
        for (int arm = 0; arm < 4; arm++) {
            double angle = rotationRadians + arm * Math.PI / 2D;
            Vec3d direction = new Vec3d(Math.cos(angle), 0D, Math.sin(angle));
            for (int row = -1; row <= 1; row++) {
                Vec3d spawnPos = center.add(0D, row * ORB_VERTICAL_SPACING, 0D);
                ReturningKnightProjectiles.spawnDarknessOrb(boss, spawnPos, direction, damage, ORB_SPEED, ORB_LIFETIME);
            }
        }
    }

    private void spawnPillarStar(double rotation) {
        ReturningKnight boss = this.getBoss();
        float damage = PILLAR_DAMAGE * ReturningKnightConfig.returning_knight_damage_modifier;
        Vec3d center = boss.getPos();
        for (int arm = 0; arm < STAR_ARMS; arm++) {
            double angle = rotation + arm * Math.PI * 2D / STAR_ARMS;
            Vec3d direction = new Vec3d(Math.cos(angle), 0D, Math.sin(angle));
            for (int step = 1; step <= PILLARS_PER_ARM; step++) {
                Vec3d pos = center.add(direction.multiply(step * PILLAR_SPACING));
                ReturningKnightProjectiles.spawnDarkPillar(boss, pos, PILLAR_WARMUP, PILLAR_RADIUS, damage, 0);
            }
        }
    }

    @Override
    public boolean ownsState(ReturningKnight.States state, ReturningKnight.States registeredState) {
        return state == ReturningKnight.States.BULLET_HELL_START
                || state == ReturningKnight.States.BULLET_HELL_TICK
                || state == ReturningKnight.States.BULLET_HELL_END;
    }

    @Override
    public boolean canTrigger() {
        return this.getBoss().isPhaseTwo() && this.getBoss().getHealth() <= this.getBoss().getMaxHealth() * ReturningKnightConfig.returning_knight_bullet_hell_percent_health_trigger_phase_2;
    }
}