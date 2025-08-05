package net.soulsweaponry.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.IPostureLossProjectile;
import net.soulsweaponry.registry.EntityRegistry;

public class SilverArrow extends ModArrow implements IPostureLossProjectile {

    private int postureLoss;
    private float bonusUndeadDamage;

    public SilverArrow(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public SilverArrow(double x, double y, double z, World world, ItemStack arrowStack, ItemStack bowStack) {
        super(EntityRegistry.SILVER_ARROW, x, y, z, world, arrowStack, bowStack);
    }

    public SilverArrow(LivingEntity owner, World world, ItemStack arrowStack, ItemStack bowStack) {
        super(EntityRegistry.SILVER_ARROW, owner, world, arrowStack, bowStack);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity target) {
            this.applyPostureLoss(target);
            if (target.hasInvertedHealingAndHarm()) {
                this.setDamage(this.getDamage() + (this.getBonusUndeadDamage() / this.getVelocity().length()));
            }
        }
        super.onEntityHit(entityHitResult);
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

    @Override
    public boolean canHaveArrowEffects() {
        return ConfigConstructor.simons_bowblade_projectile_can_apply_arrow_effects;
    }
}
