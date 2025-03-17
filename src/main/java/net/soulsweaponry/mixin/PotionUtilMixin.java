package net.soulsweaponry.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.soulsweaponry.items.potion.CustomPotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PotionUtil.class)
public class PotionUtilMixin {

    @Inject(method = "getPotionEffects(Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void getPotionEffects(ItemStack stack, CallbackInfoReturnable<List<StatusEffectInstance>> info) {
        if (stack.getItem() instanceof CustomPotionItem potionItem && potionItem.canUse(stack)) {
            info.setReturnValue(potionItem.getEffects());
            info.cancel();
        }
    }

    @Inject(method = "getPotion(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/potion/Potion;", at = @At("HEAD"), cancellable = true)
    private static void getPotion(ItemStack stack, CallbackInfoReturnable<Potion> info) {
        if (stack.getItem() instanceof CustomPotionItem potionItem && potionItem.canUse(stack)) {
            info.setReturnValue(potionItem.getPotion());
            info.cancel();
        }
    }
}
