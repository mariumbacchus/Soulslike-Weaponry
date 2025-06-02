package net.soulsweaponry.entity.projectile.noclip;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

public class WarmupLightningEntity extends DamagingWarmupEntity {

    public WarmupLightningEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setDamage(0);
        this.setEventId(DamagingWarmupEntityEvents.SPAWN_LIGHTNING);
    }

    @Override
    public void handleSoundStatus(byte status) {

    }

    @Override
    public void applyDamageEffects(boolean wasHit, LivingEntity target) {

    }
}