package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.BossAttack;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.DarkSorcerer;
import net.soulsweaponry.entity.mobs.EliteRemnant;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.UUID;

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
            ReturningKnightProjectiles.spawnDarknessOrb(this.getBoss(), spawnPos.add(0, -0.5D, 0), direction, darknessOrbDamage, darknessOrbSpeed, 30);
        }
        this.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 3f, 0.5f);
    }

    public void spawnEruption(Vec3d pos, float yaw, float damage, int warmup) {
        ReturningKnightProjectiles.spawnEruption(this.getBoss(), pos, yaw, damage, warmup);
    }

    public UUID summonAllies(Vec3d pos, MobEntity ally, boolean isSpecialSummon) {
        ally.setPosition(pos);
        this.getWorld().playSound(null, ally.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        this.getWorld().spawnEntity(ally);
        if (isSpecialSummon) {
            this.getBoss().addSpecialSummon(ally.getUuid());
        }
        if (!this.getWorld().isClient) {
            ParticleHandler.particleOutburstMap(this.getWorld(), 100, pos.getX(), pos.getY(), pos.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
        }
        return ally.getUuid();
    }

    public UUID summonAllies(Vec3d pos, boolean isHealer) {
        MobEntity entity = isHealer ? new DarkSorcerer(EntityRegistry.DARK_SORCERER, this.getWorld()) : new Remnant(EntityRegistry.REMNANT, this.getWorld());
        return summonAllies(pos, entity, isHealer);
    }

    public boolean isValidSpawn(BlockPos pos) {
        return this.getWorld().getBlockState(pos).isAir() && !this.getWorld().getBlockState(pos.down()).isAir();
    }
}
