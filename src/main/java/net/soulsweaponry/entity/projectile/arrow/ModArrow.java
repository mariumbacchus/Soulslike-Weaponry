package net.soulsweaponry.entity.projectile.arrow;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import net.soulsweaponry.mixin.PersistentProjectileEntityAccessor;
import net.soulsweaponry.mixin.PersistentProjectileEntityInvoker;
import org.jetbrains.annotations.Nullable;

public abstract class ModArrow extends ArrowEntity {

    public ModArrow(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public ModArrow(EntityType<? extends ArrowEntity> type, double x, double y, double z, World world, ItemStack arrowStack, ItemStack weapon) {
        this(type, world);
        this.setPosition(x, y, z);
        this.setStack(arrowStack.copy());
        this.setCustomName(arrowStack.get(DataComponentTypes.CUSTOM_NAME));
        Unit unit = arrowStack.remove(DataComponentTypes.INTANGIBLE_PROJECTILE);
        if (unit != null) {
            this.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
        if (weapon != null && world instanceof ServerWorld serverWorld) {
            if (weapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }
            ((PersistentProjectileEntityAccessor)this).setWeaponStack(weapon.copy());
            int i = EnchantmentHelper.getProjectilePiercing(serverWorld, weapon, this.getItemStack());
            if (i > 0) {
                ((PersistentProjectileEntityInvoker)this).invokeSetPierceLevel((byte) i);
            }
            EnchantmentHelper.onProjectileSpawned(serverWorld, weapon, this, item -> ((PersistentProjectileEntityAccessor) this).setWeaponStack(null));
        }
    }

    public ModArrow(EntityType<? extends ArrowEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        this(type, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), world, stack, shotFrom);
        this.setOwner(owner);
    }

    /**
     * @return whether the arrow should apply custom status effects based on the effect-arrow-item used
     */
    public abstract boolean canHaveArrowEffects();

    @Override
    public void addEffect(StatusEffectInstance effect) {//TODO test
        if (!this.canHaveArrowEffects()) return;
        super.addEffect(effect);
    }
}
