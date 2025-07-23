package net.soulsweaponry.mixin.create;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = PotionFluidHandler.class, remap = false)
public class PotionFluidHandlerMixin {

    @ModifyReturnValue(
            method = "fillBottle",
            at = @At("RETURN")
    )
    private static ItemStack sw$swapReturnedStack(ItemStack original) {
        Potion potion = PotionUtil.getPotion(original);
        if (potion.equals(EffectRegistry.CHUNGUS_TONIC_POTION)) {
            if (original.isOf(Items.LINGERING_POTION)) {
                return ItemRegistry.CHUNGUS_TONIC_LINGERING.getDefaultStack();
            } else if (original.isOf(Items.SPLASH_POTION)) {
                return ItemRegistry.CHUNGUS_TONIC_SPLASH.getDefaultStack();
            } else {
                return ItemRegistry.CHUNGUS_TONIC_POTION.getDefaultStack();
            }
        }
        return original;
    }
}
