package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.MoonlightArrow;

public class ArrowStormEntity extends InvisibleEntity {

    private int maxArrowAge = 1000;

    public ArrowStormEntity(EntityType<? extends ArrowStormEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            for (int i = 0; i < 60; ++i) {
                this.getWorld().addParticle(ParticleTypes.CLOUD, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0D, 0.0D, 0.0D);
            }
        } else if (this.getOwner() instanceof LivingEntity) {
            for (int i = 0; i < 6; i++) {
                MoonlightArrow arrow = new MoonlightArrow(this.getWorld(), (LivingEntity) this.getOwner());
                arrow.setPos(this.getParticleX(0.5), this.getY(), this.getParticleZ(0.5));
                Vec3d vec = this.getRotationVector().multiply(0.1f).add(this.getPos());
                double e = vec.getX() - this.getX();
                double f = (this.getY() - 1f) - this.getY();
                double g = vec.getZ() - this.getZ();
                arrow.setVelocity(e, f, g);
                arrow.setDamage(this.getDamage());
                arrow.setPierceLevel((byte) 4);
                arrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                arrow.setOwner(this.getOwner());
                arrow.setMaxArrowAge(this.getMaxArrowAge());
                this.getWorld().spawnEntity(arrow);
            }
        }
        if (this.age > 30) {
            this.discard();
        }
    }

    public void setMaxArrowAge(int maxArrowAge) {
        this.maxArrowAge = maxArrowAge;
    }

    public int getMaxArrowAge() {
        return maxArrowAge;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("maxArrowAge")) {
            this.maxArrowAge = nbt.getInt("maxArrowAge");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("maxArrowAge", this.maxArrowAge);
    }
}
