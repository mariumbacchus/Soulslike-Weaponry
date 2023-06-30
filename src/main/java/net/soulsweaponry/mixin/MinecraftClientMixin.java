package net.soulsweaponry.mixin;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.events.AttemptAttackCallback;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    
    /* @Inject(method = "doAttack", at = @At("HEAD"), cancellable = false)
    private void onLeftClick(CallbackInfoReturnable<Boolean> info) {
        ActionResult result = AttemptAttackCallback.EVENT.invoker().useViaAttack(
                ((MinecraftClient)(Object)this).player, (World)((MinecraftClient)(Object)this).world);
        
        if (result == ActionResult.FAIL) {
            info.cancel();
        }    
    } */

    @Shadow public ClientWorld world;
    @Shadow @Nullable public ClientPlayerEntity player;
    @Final
    @Shadow public GameOptions options;
    @Final
    @Shadow public Mouse mouse;

    @Inject(method = "handleInputEvents", at = @At("TAIL"), cancellable = true)
    private void onLeftHold(CallbackInfo info) {
        boolean rightHold = this.options.useKey.isPressed() && ConfigConstructor.moonlight_shortsword_enable_right_click;
        boolean leftHold = this.options.attackKey.isPressed() && ConfigConstructor.moonlight_shortsword_enable_left_click;
        boolean hold = rightHold || leftHold;
        if (hold && this.mouse.isCursorLocked()) {
            ActionResult result = AttemptAttackCallback.EVENT.invoker().useViaAttack(
                this.player, this.world);
            if (result == ActionResult.FAIL) {
                info.cancel();
            }
            //ClientPlayNetworking.send(PacketsServer.MOONLIGHT, PacketByteBufs.create());
        }
    }
}
