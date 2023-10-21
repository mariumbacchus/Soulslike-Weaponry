package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Cannonball extends SilverBulletEntity implements IAnimatable {

    public final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Cannonball(EntityType<? extends Cannonball> entityType, World world) {
        super(entityType, world);
    }

    public Cannonball(EntityType<? extends Cannonball> type, double x, double y, double z,
            World world) {
        super(type, x, y, z, world);
    }

    @Override
    public int getMaxAge() {
        return 600;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
