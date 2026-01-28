package net.soulsweaponry.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.soulsweaponry.items.abilities.IHasAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin implements IHasAbilities {

    @Inject(method = "postHit", at = @At("RETURN"), cancellable = true)
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> info) {
        if (this.getAbilities().isEmpty()) {
            return;
        }
        boolean vanilla = info.getReturnValue();
        boolean abilities = IHasAbilities.super.postHit(stack, target, attacker);
        info.setReturnValue(vanilla || abilities);
    }
}