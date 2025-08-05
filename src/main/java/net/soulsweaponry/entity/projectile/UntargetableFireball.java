package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UntargetableFireball extends FireballEntity {

    public UntargetableFireball(EntityType<? extends FireballEntity> entityType, World world) {
        super(entityType, world);
    }

    public UntargetableFireball(World world, LivingEntity owner, Vec3d velocity, int explosionPower) {
        super(world, owner, velocity, explosionPower);
    }

    @Override
    public boolean canHit() {
        return false;
    }

    public void setVelocityWithAcceleration(Vec3d velocity, double accelerationPower) {
        this.setVelocity(velocity.normalize().multiply(accelerationPower));
        this.velocityDirty = true;
    }
}
