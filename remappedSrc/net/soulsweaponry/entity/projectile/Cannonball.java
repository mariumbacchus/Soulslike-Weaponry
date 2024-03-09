package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

public class Cannonball extends SilverBulletEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);

    public Cannonball(EntityType<? extends Cannonball> entityType, World world) {
        super(entityType, world);
    }

    public Cannonball(World world, LivingEntity owner) {
        super(EntityRegistry.CANNONBALL, world, owner);
    }

    @Override
    public int getMaxAge() {
        return 600;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
