package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.entity.projectile.noclip.BlackflameExplosionEntity;
import net.soulsweaponry.entity.projectile.noclip.DarknessOrb;
import net.soulsweaponry.entity.projectile.noclip.EruptionEntity;
import net.soulsweaponry.particles.ParticleHandler;

public final class ReturningKnightProjectiles {

    public static void spawnDarkPillar(ReturningKnight boss, Vec3d pos, int delay, float radius, float damage, int reducedDelay) {
        BlackflameExplosionEntity pillar = new BlackflameExplosionEntity(boss.getWorld());
        pillar.setOwner(boss);
        pillar.setRadius(radius);
        pillar.setDamage(boss.getModifiedDamage(damage));
        pillar.setWarmup(Math.max(0, delay - reducedDelay));
        pillar.setPosition(pos);
        boss.getWorld().spawnEntity(pillar);
        if (boss.getWorld() instanceof ServerWorld serverWorld) {
            ParticleHandler.warningParticle(serverWorld, pos);
        }
    }

    public static void spawnDarknessOrb(ReturningKnight boss, Vec3d pos, Vec3d direction, float damage, float speed, int lifetime) {
        DarknessOrb orb = new DarknessOrb(boss.getWorld(), boss, lifetime);
        orb.setDamage(boss.getModifiedDamage(damage));
        orb.setPosition(pos);
        orb.setVelocity(direction.x, direction.y, direction.z, speed, 0F);
        boss.getWorld().spawnEntity(orb);
    }

    public static void spawnEruption(ReturningKnight boss, Vec3d pos, float yaw, float damage, int warmup) {
        EruptionEntity entity = new EruptionEntity(boss.getWorld());
        entity.setYaw(yaw);
        entity.setOwner(boss);
        entity.setRadius(2f);
        entity.setDamage(boss.getModifiedDamage(damage));
        entity.setWarmup(warmup);
        entity.setPosition(pos);
        entity.setParticleAmountMod(10);
        boss.getWorld().spawnEntity(entity);
        if (boss.getWorld() instanceof ServerWorld serverWorld) {
            ParticleHandler.warningParticle(serverWorld, pos);
        }
    }
}
