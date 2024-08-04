package net.soulsweaponry.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.BowItem;
import net.soulsweaponry.items.IShootModProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public class BowItemMixin {

    /**
     * Inspired by how <a href="https://github.com/FabricExtras/RangedWeaponAPI">Ranged Weapon API</a> does its implementation.
     */
    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BowItem;getPullProgress(I)F"))
    private float applyCustomPullTime(int ticks, Operation<Float> original) {
        BowItem item = (BowItem) (Object) this;
        if (item instanceof IShootModProjectile custom) {
            return this.getModdedPullProgress(ticks, custom.getPullTime());
        } else {
            return original.call(ticks);
        }
    }

    @Unique
    public float getModdedPullProgress(int useTicks, int customPullTime) {
        float f = (float)useTicks / (float) customPullTime;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
}