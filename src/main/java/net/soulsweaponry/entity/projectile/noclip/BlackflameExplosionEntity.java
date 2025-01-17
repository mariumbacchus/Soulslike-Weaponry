package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;

public class BlackflameExplosionEntity extends DamagingWarmupEntity {

    public BlackflameExplosionEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void handleSoundStatus(byte status) {

    }

    public BlackflameExplosionEntity(World world) {
        super(EntityRegistry.BLACKFLAME_EXPLOSION_ENTITY, world);
    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {
        if (wasHit) {
            target.addVelocity(0, 1f, 0);
        }
    }

    @Override
    public void onTrigger() {
        this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        ParticleHandler.particleOutburstMap(this.getWorld(), 250, this.getX(), this.getY(), this.getZ(), ParticleEvents.BLACKFLAME_SNAKE_PARTICLE_MAP, 1f);
    }
}
