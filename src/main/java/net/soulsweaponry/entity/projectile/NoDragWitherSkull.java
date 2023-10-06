package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

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
            Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0f, false, destructionType);
            this.discard();
        }
    }
}