package net.soulsweaponry.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.soulsweaponry.events.AttemptAttackCallback;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow public ClientWorld world;
    @Shadow @Nullable
    public ClientPlayerEntity player;
    @Final
    @Shadow public GameOptions options;
    @Final
    @Shadow public Mouse mouse;

    @Inject(method = "handleInputEvents", at = @At("TAIL"), cancellable = true)
    private void onLeftHold(CallbackInfo info) {
        boolean leftHold = this.options.attackKey.isPressed();
        if (leftHold && this.mouse.isCursorLocked()) {
            ActionResult result = AttemptAttackCallback.EVENT.invoker().useViaAttack(
                this.player, this.world);
            if (result == ActionResult.FAIL) {
                info.cancel();
            }
        }
    }
}
