package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.world.World;

public class UntargetableFireball extends FireballEntity {

    public UntargetableFireball(EntityType<? extends FireballEntity> entityType, World world) {
        super(entityType, world);
    }

    public UntargetableFireball(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ, int explosionPower) {
        super(world, owner, velocityX, velocityY, velocityZ, explosionPower);
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }
}
