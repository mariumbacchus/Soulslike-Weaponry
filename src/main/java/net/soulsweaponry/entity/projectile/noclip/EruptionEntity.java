package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;

public class EruptionEntity extends DamagingWarmupEntity {

    public EruptionEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public EruptionEntity(World world) {
        super(EntityRegistry.ERUPTION, world);
    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {
        if (wasHit) {
            target.addVelocity(0, 1f, 0);
        }
    }

    @Override
    public void handleSoundStatus(byte status) {
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), this.getSoundCategory(), 1f, 1f, true);
    }

    @Override
    public void onTrigger() {
        super.onTrigger();
        if (this.getParticleAmountMod() > 0) {
            ParticleHandler.particleOutburstMap(this.getWorld(), Math.min(20 * (int) this.getParticleAmountMod(), 100), this.getX(), this.getY(), this.getZ(), ParticleEvents.GROUND_RUPTURE_MAP, 1f);
        }
    }
}
