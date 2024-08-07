package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.world.World;

public class NoDragWitherSkull extends WitherSkullEntity {

    public NoDragWitherSkull(EntityType<? extends WitherSkullEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected float getDrag() {
        return 1f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > 200) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0f, false, World.ExplosionSourceType.MOB);
            this.discard();
        }
    }
}
