package net.soulsweaponry.mixin;

import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.soulsweaponry.items.potion.CustomLingeringPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionEntity.class)
public class PotionEntityMixin {

    @Inject(method = "isLingering", at = @At("HEAD"), cancellable = true)
    private void isLingering(CallbackInfoReturnable<Boolean> info) {
        PotionEntity entity = (PotionEntity) (Object) this;
        if (entity.getStack().getItem() instanceof CustomLingeringPotion potion && potion.canUse(entity.getStack())) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}
