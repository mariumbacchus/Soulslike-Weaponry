package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EnchantRegistry;

/**
* Damage based on charge time: <p>
* 0.1 s (no charge)	        1♥ <p>
* 0.2–0.9 s (medium charge)	5♥♥♥ <p>
* 1+ s (full charge)        6♥♥♥ <p>
* 1+ s (critical)           11♥ × 5.5 <p>
*/
@Mixin(BowItem.class)
public class BowItemMixin<T> {

    private static ItemStack stack;

    private static int enchantLevel() {
        return stack != null ? EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, stack) : 0;
    }

    @Inject(at = @At("TAIL"), method = "getPullProgress", cancellable = true)
    private static void interceptPullProgress(int useTicks, CallbackInfoReturnable<Float> info) {
        int ticksReduced = 2 * enchantLevel();
        float f = (float)useTicks / (20.0f - ticksReduced);
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        info.setReturnValue(f);            
    }

    @Inject(at = @At("HEAD"), method = "use")
    private void interceptUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<T> info) {
        stack = user.getStackInHand(hand);
    }
}
