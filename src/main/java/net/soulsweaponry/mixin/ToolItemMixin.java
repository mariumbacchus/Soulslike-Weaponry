package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.soulsweaponry.registry.EffectRegistry;

@Mixin(SwordItem.class)
public class ToolItemMixin<T>  {

    @Inject(at = @At("TAIL"), method = "postHit")
    private void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<T> info) {
        
        if (attacker.hasStatusEffect(EffectRegistry.BLOODTHIRSTY.get())) {
            attacker.heal(1 + attacker.getStatusEffect(EffectRegistry.BLOODTHIRSTY.get()).getAmplifier());
        }
    }
}
