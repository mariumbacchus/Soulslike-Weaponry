package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;

public class KrakenSlayerProjectile extends PersistentProjectileEntity {
    private float trueDamage;

    public KrakenSlayerProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public KrakenSlayerProjectile(World world, LivingEntity owner) {
        super(EntityRegistry.KRAKEN_SLAYER_PROJECTILE, owner, world);
    }

    public void setTrueDamage(float trueDamage) {
        this.trueDamage = trueDamage;
    }

    public float getTrueDamage() {
        return trueDamage;
    }

    public void tick() {
        if (!this.inGround) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y + 0.2f;
            double g = vec3d.z;
            for (int i = 0; i < 4; ++i) {
                this.getWorld().addParticle(this.getParticleType(), this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, 0, 0, 0); //-e, -f + 0.2D, -g
            }
        }
        super.tick();
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.FIREWORK;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("trueDamage")) {
            this.trueDamage = nbt.getFloat("trueDamage");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("trueDamage", this.trueDamage);
    }

    @Override
    protected ItemStack asItemStack() {
        return Items.ARROW.getDefaultStack();
    }
}
