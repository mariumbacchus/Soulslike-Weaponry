package net.soulsweaponry.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Hand;
import net.soulsweaponry.items.bow.KrakenSlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererDebugMixin {

    @Inject(method = "updateHandState", at = @At("TAIL"))
    private void souls$forceKrakenSpearUseAction(
            AbstractClientPlayerEntity player,
            PlayerEntityRenderState.HandState handState,
            Hand hand,
            CallbackInfo ci
    ) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.isUsingItem() && player.getActiveHand() == hand && stack.getItem() instanceof KrakenSlayer) {
            handState.itemUseAction = UseAction.SPEAR;
        }
    }
}
