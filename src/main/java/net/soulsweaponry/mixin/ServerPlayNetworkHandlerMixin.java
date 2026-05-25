package net.soulsweaponry.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.items.abilities.IConfigDisable;
import net.soulsweaponry.items.abilities.IHasAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onUpdateSelectedSlot", at = @At("TAIL"))
    public void interceptOnUpdateSelectedSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo info) {
        ItemStack now = player.getMainHandStack();
        if (now.getItem() instanceof IConfigDisable configDisable && configDisable.isDisabled(now)) {
            return;
        }
        if (now.getItem() instanceof IHasAbilities has) {
            has.getAbilities().forEach(a -> a.onMainHandEquip(player, now));
        }
    }
}