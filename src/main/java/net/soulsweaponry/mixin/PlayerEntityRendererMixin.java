package net.soulsweaponry.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.bow.KrakenSlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.render.entity.PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void souls$forceKrakenSpearPose(
            AbstractClientPlayerEntity player,
            Hand hand,
            CallbackInfoReturnable<BipedEntityModel.ArmPose> cir
    ) {
        if (player.getActiveHand() == hand && player.getItemUseTimeLeft() > 0) {
            if (player.getStackInHand(hand).getItem() instanceof KrakenSlayer) {
                cir.setReturnValue(BipedEntityModel.ArmPose.THROW_SPEAR);
            }
        }
    }
}