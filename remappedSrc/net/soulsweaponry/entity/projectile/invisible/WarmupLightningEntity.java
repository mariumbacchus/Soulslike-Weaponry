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
        if (!this.method_48926().isClient) {
            this.setWarmup(this.getWarmup() - 1);
            if (this.getWarmup() < 0) {
                if (this.getWarmup() == -7) {
                    if (this.method_48926().isSkyVisible(this.getBlockPos())) {
                        LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, this.method_48926());
                        entity.setPos(this.getX(), this.getY(), this.getZ());
                        this.method_48926().spawnEntity(entity);
                    }
                    this.discard();
                }
            }
        }
    }
}