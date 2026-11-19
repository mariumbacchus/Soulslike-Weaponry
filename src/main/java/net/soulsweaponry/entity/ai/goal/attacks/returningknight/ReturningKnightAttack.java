package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.BossAttack;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.EliteRemnant;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.util.WeaponUtil;

public abstract class ReturningKnightAttack extends BossAttack<ReturningKnight.States, ReturningKnight, ReturningKnightGoal> {

    public ReturningKnightAttack(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public boolean damageTarget(DamageSource source, LivingEntity target, float damage) {
        if (this.getBoss().isPhaseTwo() && this.getBoss().isTeammate(target)) {
            if (target instanceof EliteRemnant remnant) {
                remnant.flashShield();
            }
            return false;
        }
        return super.damageTarget(source, target, damage);
    }

    public void spawnDarkPillarsCircle(LivingEntity target, int pillarRippleAmount, float pillarRadius, float pillarDamage, int reducedPillarDelay, RotatableHitbox maceHitbox) {
        Vec3d maceCenter = BossHitboxHelper.findGroundImpactPos(this.getWorld(), maceHitbox.getCenter(), 2);
        this.spawnDarkPillarsCircle(target, pillarRippleAmount, pillarRadius, pillarDamage, reducedPillarDelay, new Vec2f(1.5f, 1.75f), maceCenter);
    }

    public void spawnDarkPillarsCircle(LivingEntity target, int pillarRippleAmount, float pillarRadius, float pillarDamage, int reducedPillarDelay, Vec2f pillarSize, Vec3d center) {
        float yawRad = (float) MathHelper.atan2(target.getZ() - this.getBoss().getZ(), target.getX() - this.getBoss().getX());
        float yawDeg = (float) Math.toDegrees(yawRad);
        double heightDiff = Math.abs(target.getY() - this.getBoss().getY());
        double maxYOffset = heightDiff + 3;
        WeaponUtil.doConsumerOnCircle(this.getBoss().getWorld(), yawDeg, center, maxYOffset, pillarRippleAmount, pillarSize,
                (pos, delay, yaw) -> this.spawnDarkPillar(pos, delay, pillarRadius, pillarDamage, reducedPillarDelay)
        );
    }

    public void spawnDarkPillarsPlus(LivingEntity target, int armLength, float spacing, float pillarRadius, float pillarDamage, int reducedPillarDelay, RotatableHitbox maceHitbox) {
        Vec3d center = BossHitboxHelper.findGroundImpactPos(this.getWorld(), maceHitbox.getCenter(), 2);
        float yaw = (float) Math.toDegrees(MathHelper.atan2(target.getZ() - center.z, target.getX() - center.x));
        double maxYOffset = Math.abs(target.getY() - center.y) + 3.0D;
        WeaponUtil.doConsumerOnPlus(this.getWorld(), yaw, center, maxYOffset, armLength, spacing,
                (pos, delay, pillarYaw) -> this.spawnDarkPillar(pos, delay, pillarRadius, pillarDamage, reducedPillarDelay)
        );
    }

    public void spawnDarkPillar(Vec3d pos, int delay, float pillarRadius, float pillarDamage, int reducedPillarDelay) {
        ReturningKnightProjectiles.spawnDarkPillar(this.getBoss(), pos, delay, pillarRadius, pillarDamage, reducedPillarDelay);
    }

    /**
     * To be used when the boss spins to shoot out orbs.
     */
    public void spawnDarknessOrb(Vec3d spawnPos, int darknessOrbAmountPerTick, float darknessOrbDamage, float darknessOrbSpeed) {
        Vec3d outward = new Vec3d(spawnPos.x - this.getBoss().getX(), 0.0D, spawnPos.z - this.getBoss().getZ()).normalize();
        float spacingDegrees = 6.0F;
        for (int i = 0; i < darknessOrbAmountPerTick; i++) {
            // Add deviation along x/z but ignore y-axis to avoid orbs crashing into ground
            float centeredIndex = i - (darknessOrbAmountPerTick - 1) / 2f;
            float angle = centeredIndex * spacingDegrees * MathHelper.RADIANS_PER_DEGREE;
            Vec3d direction = outward.rotateY(angle);
            ReturningKnightProjectiles.spawnDarknessOrb(this.getBoss(), spawnPos.add(0, -0.5D, 0), direction, darknessOrbDamage, darknessOrbSpeed, 30, false);
        }
        this.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 3f, 0.5f);
    }

    public void spawnEruption(Vec3d pos, float yaw, float damage, int warmup) {
        ReturningKnightProjectiles.spawnEruption(this.getBoss(), pos, yaw, damage, warmup);
    }
}
