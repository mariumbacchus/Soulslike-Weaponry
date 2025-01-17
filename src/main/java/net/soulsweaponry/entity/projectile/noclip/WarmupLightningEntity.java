package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

public class WarmupLightningEntity extends DamagingWarmupEntity {

    public WarmupLightningEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setDamage(0);
    }

    @Override
    public void handleSoundStatus(byte status) {

    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {

    }

    @Override
    public void onTrigger() {
        if (this.getWorld().isSkyVisible(this.getBlockPos())) {
            LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, this.getWorld());
            entity.setPos(this.getX(), this.getY(), this.getZ());
            this.getWorld().spawnEntity(entity);
        }
    }
}