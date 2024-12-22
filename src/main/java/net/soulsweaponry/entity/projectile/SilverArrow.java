package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;

public class SilverArrow extends PersistentProjectileEntity implements IPostureLossProjectile {

    private int postureLoss;
    private float bonusUndeadDamage;

    public SilverArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public SilverArrow(double x, double y, double z, World world) {
        super(EntityRegistry.SILVER_ARROW, x, y, z, world);
    }

    public SilverArrow(LivingEntity owner, World world) {
        super(EntityRegistry.SILVER_ARROW, owner, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity target) {
            this.applyPostureLoss(target);
            if (target.isUndead()) {
                this.setDamage(this.getDamage() + (this.getBonusUndeadDamage() / this.getVelocity().length()));
            }
        }
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected ItemStack asItemStack() {
        return Items.ARROW.getDefaultStack();
    }

    @Override
    public int getPostureLoss() {
        return this.postureLoss;
    }

    @Override
    public void setPostureLoss(int postureLoss) {
        this.postureLoss = postureLoss;
    }

    public float getBonusUndeadDamage() {
        return bonusUndeadDamage;
    }

    public void setBonusUndeadDamage(float bonusUndeadDamage) {
        this.bonusUndeadDamage = bonusUndeadDamage;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("postureLoss")) {
            this.setPostureLoss(nbt.getInt("postureLoss"));
        }
        if (nbt.contains("bonusUndeadDamage")) {
            this.setBonusUndeadDamage(nbt.getFloat("bonusUndeadDamage"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("postureLoss", this.getPostureLoss());
        nbt.putFloat("bonusUndeadDamage", this.getBonusUndeadDamage());
    }
}
