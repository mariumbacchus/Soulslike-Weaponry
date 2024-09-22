package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;

public class MoonlightArrow extends PersistentProjectileEntity {

    private int maxArrowAge = 1000;

    public MoonlightArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world, ItemStack stack) {
        super(entityType, world, stack);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public MoonlightArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world, Items.ARROW.getDefaultStack());
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public MoonlightArrow(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.MOONLIGHT_ARROW, owner, world, stack);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
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
        if (this.age > this.maxArrowAge) {
            this.discard();
        }
    }

    @Override
    public byte getPierceLevel() {
        return 4;
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        if (this.maxArrowAge > 100) {
            return super.tryPickup(player);
        } else {
            return false;
        }
    }

    protected ParticleEffect getParticleType() {
        return ParticleRegistry.NIGHTFALL_PARTICLE;
    }

    @Override
    protected ItemStack asItemStack() {
        return Items.ARROW.getDefaultStack();
    }

    public void setMaxArrowAge(int maxArrowAge) {
        this.maxArrowAge = maxArrowAge;
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