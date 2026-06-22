package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.SeismicWaveHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.entity.projectile.noclip.EruptionEntity;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class SeismicWave extends MaceAttack {

    private final float swipeDamage = EntityConfig.returning_knight_seismic_wave_swipe_damage;
    private final float eruptionDamage = EntityConfig.returning_knight_seismic_wave_eruption_damage;

    public SeismicWave(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        SeismicWaveHitbox.updateMaceHitbox(this.hitbox, this.getBoss(), attackStatus);
        if (SeismicWaveHitbox.isDamageTick(this.getBoss(), attackStatus)) {
            this.doMaceDamage(this.getWorld().getDamageSources().mobAttack(this.getBoss()), this.swipeDamage, living -> {
                target.takeKnockback(0.7f, -(target.getX() - this.getBoss().getX()), -(target.getZ() - this.getBoss().getZ()));
                target.addVelocity(0, 0.7f, 0);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
            });
        }
        if (this.isTick(attackStatus, 25)) {
            this.playSound(SoundEvents.BLOCK_GRINDSTONE_USE, 3f, 0.4f);
        }
        if (this.isTick(attackStatus, 40)) {
            this.playSound(SoundRegistry.KNIGHT_SWIPE_EVENT, 3f, 0.75f, 1f);
        }
        if (this.isTick(attackStatus, 44)) {
            this.doGroundScrapeParticles();
        }
        if (this.isTick(attackStatus, 48)) {
            this.spawnEruptionLines(target);
        }
    }

    private void spawnEruptionLines(LivingEntity target) {
        float yaw = (float) Math.toDegrees(MathHelper.atan2(target.getZ() - this.getBoss().getZ(), target.getX() - this.getBoss().getX()));
        Vec3d origin = this.getBoss().getPos();
        double maxYOffset = 10;
        int amount = 10;
        float spacing = 2f;
        int spreadCount = 2; // lines to each side

        // right direction from yaw
        float rad = (float) Math.toRadians(yaw);
        Vec3d forward = new Vec3d(Math.cos(rad), 0, Math.sin(rad));
        Vec3d rightDir = forward.crossProduct(new Vec3d(0, 1, 0)).normalize();

        // loop offsets
        for (int i = -spreadCount; i <= spreadCount; i++) {
            Vec3d start = origin.add(rightDir.multiply(i * spacing)); // flip with -i
            WeaponUtil.doConsumerOnLine(this.getWorld(), yaw, start, maxYOffset, amount, spacing, ((vec3d, warmup, y) -> {
                this.spawnEruption(vec3d, y);
            }));
        }
    }

    private void spawnEruption(Vec3d pos, Float yaw) {
        EruptionEntity entity = new EruptionEntity(this.getWorld());
        entity.setYaw(yaw);
        entity.setOwner(this.getBoss());
        entity.setRadius(2f);
        entity.setDamage(this.getGoal().getModifiedDamage(this.eruptionDamage));
        entity.setWarmup(22);
        entity.setPosition(pos);
        entity.setParticleAmountMod(10);
        this.getWorld().spawnEntity(entity);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            float spread = 0.01f;
            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), 4, this.getBoss().getRandom().nextFloat() * spread - spread / 2f, this.getBoss().getRandom().nextFloat() * spread - spread / 2f, this.getBoss().getRandom().nextFloat() * spread - spread / 2f, 0);
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 200;
    }
}
