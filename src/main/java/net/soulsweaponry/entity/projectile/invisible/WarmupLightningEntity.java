package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

public class WarmupLightningEntity extends InvisibleWarmupEntity {

    public WarmupLightningEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.setWarmup(this.getWarmup() - 1);
            if (this.getWarmup() < 0) {
                if (this.getWarmup() == -7) {
                    if (this.getWorld().isSkyVisible(this.getBlockPos())) {
                        LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, this.getWorld());
                        entity.setPos(this.getX(), this.getY(), this.getZ());
                        this.getWorld().spawnEntity(entity);
                    }
                    this.discard();
                }
            }
        }
    }
}