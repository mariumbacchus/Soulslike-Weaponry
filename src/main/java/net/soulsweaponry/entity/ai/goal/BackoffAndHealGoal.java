package net.soulsweaponry.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.entity.mobs.EliteRemnant;

import java.util.EnumSet;

public class BackoffAndHealGoal extends Goal {

    private static final int MAX_RETREAT_TICKS = 60;
    private static final double SAFE_DISTANCE_SQUARED = 15 * 15;
    private static final float[] ESCAPE_ANGLES = {0F, 0.5F, -0.5F, 1.0F, -1.0F};

    private final EliteRemnant mob;
    private final double speed;

    private LivingEntity threat;
    private int retreatTicks;
    private int eatingTicks;
    private boolean eating;
    private boolean finished;

    public BackoffAndHealGoal(EliteRemnant mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
    }

    @Override
    public boolean canStart() {
        return this.mob.isAlive()
                && !this.mob.isSitting()
                && !this.mob.isUsingItem()
                && this.mob.goldenAppleCooldownTicks == 0
                && this.mob.getHealth() <= this.mob.getMaxHealth() * 0.3F;
    }

    @Override
    public void start() {
        this.retreatTicks = 0;
        this.eatingTicks = 0;
        this.eating = false;
        this.finished = false;
        this.threat = this.mob.getTarget();
        if (this.threat == null || !this.threat.isAlive()) {
            this.threat = this.mob.getAttacker();
        }
        this.mob.getNavigation().stop();
        if (this.threat == null || !this.threat.isAlive()) {
            this.beginEating();
            return;
        }
        this.findEscapePath();
    }

    @Override
    public boolean shouldContinue() {
        return this.mob.isAlive() && !this.mob.isSitting() && !this.finished;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.eating) {
            this.eatingTicks++;
            if (!this.mob.isEatingGoldenApple() || this.eatingTicks > 40) {
                this.finished = true;
            }
            return;
        }
        this.retreatTicks++;
        if (this.threat == null
                || !this.threat.isAlive()
                || this.retreatTicks >= MAX_RETREAT_TICKS
                || this.mob.squaredDistanceTo(this.threat) >= SAFE_DISTANCE_SQUARED) {
            this.beginEating();
            return;
        }
        if (this.retreatTicks % 10 == 0) {
            this.findEscapePath();
        }
    }

    private boolean findEscapePath() {
        // Try terrain aware positions away from threat
        for (int attempt = 0; attempt < 5; attempt++) {
            Vec3d destination = NoPenaltyTargeting.findFrom(this.mob, 12, 4, this.threat.getPos());
            if (destination != null && this.tryEscapePath(destination)) {
                return true;
            }
        }
        // or try positions in the opposite direction.
        Vec3d away = this.mob.getPos().subtract(this.threat.getPos());
        away = new Vec3d(away.x, 0, away.z);
        if (away.lengthSquared() < 0.001) {
            away = Vec3d.fromPolar(0, this.mob.getYaw());
        }
        away = away.normalize();
        for (int distance = 12; distance >= 4; distance -= 4) {
            for (float angle : ESCAPE_ANGLES) {
                Vec3d destination = this.mob.getPos().add(away.rotateY(angle).multiply(distance));
                if (this.tryEscapePath(destination)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tryEscapePath(Vec3d destination) {
        Path path = this.mob.getNavigation().findPathTo(BlockPos.ofFloored(destination), 0);
        if (path == null || path.getLength() < 2) {
            return false;
        }
        Vec3d endpoint = Vec3d.ofBottomCenter(path.getNodePos(path.getLength() - 1));
        Vec3d threatPos = this.threat.getPos();
        double currentDistance = this.horizontalDistanceSquared(this.mob.getPos(), threatPos);
        double escapeDistance = this.horizontalDistanceSquared(endpoint, threatPos);
        if (escapeDistance <= currentDistance + 1.0) {
            return false;
        }
        return this.mob.getNavigation().startMovingAlong(path, this.speed);
    }

    private double horizontalDistanceSquared(Vec3d first, Vec3d second) {
        double dx = first.x - second.x;
        double dz = first.z - second.z;
        return dx * dx + dz * dz;
    }

    private void beginEating() {
        this.eating = true;
        this.mob.getNavigation().stop();
        this.mob.beginEatingGoldenApple();
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        this.mob.endEatingGoldenApple();
        this.mob.goldenAppleCooldownTicks = EliteRemnant.GOLDEN_APPLE_COOLDOWN;
        this.threat = null;
    }
}