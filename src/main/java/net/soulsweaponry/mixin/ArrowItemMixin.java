package net.soulsweaponry.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.customarrows.ICustomArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {

    @Inject(method = "createArrow", at = @At("TAIL"), cancellable = true)
    public void interceptCreateArrow(World world, ItemStack arrowStack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> info) {
        ItemStack bowStack = shooter.getStackInHand(shooter.getActiveHand());
        if (bowStack.getItem() instanceof IHasAbilities bow) {
            Optional<ICustomArrow> op = bow.findAbility(ICustomArrow.class);
            if (op.isPresent()) {
                PersistentProjectileEntity projectile = op.get().getModifiedProjectile(world, bowStack, arrowStack, shooter, info.getReturnValue());
                if (projectile != null) {
                    info.setReturnValue(projectile);
                    info.cancel();
                }
            }
        }
    }
}