package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Cannonball extends SilverBulletEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Cannonball(EntityType<? extends Cannonball> entityType, World world) {
        super(entityType, world);
    }

    public Cannonball(World world, LivingEntity owner, ItemStack bullet) {
        super(EntityRegistry.CANNONBALL, world, owner, bullet);
    }

    public Cannonball(World world, LivingEntity owner) {
        super(EntityRegistry.CANNONBALL, world, owner, ItemRegistry.SILVER_BULLET.getDefaultStack());
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
