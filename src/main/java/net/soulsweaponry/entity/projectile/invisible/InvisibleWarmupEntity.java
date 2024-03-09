package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

/**
 * Invisible entity that has the warmup values built in.
 * <p>
 * Methods like {@link net.minecraft.entity.Entity#tick()} and {@link net.minecraft.entity.Entity#onRemoved()} should
 * still be overwritten to have custom effects such as damage and particles.
 * </p>
 * <p>NB! Remember to also register the invisible model in the client.</p>
 */
public abstract class InvisibleWarmupEntity extends InvisibleEntity {

    private int warmup;

    public InvisibleWarmupEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, ItemStack stack) {
        super(entityType, world, stack);
    }

    public InvisibleWarmupEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, LivingEntity owner) {
        super(entityType, world, owner);
    }

    public InvisibleWarmupEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, LivingEntity owner, ItemStack stack) {
        super(entityType, world, owner, stack);
    }

    public void setWarmup(int warmup) {
        this.warmup = warmup;
    }

    public int getWarmup() {
        return this.warmup;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Warmup")) {
            this.warmup = nbt.getInt("Warmup");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Warmup", this.warmup);
    }
}
