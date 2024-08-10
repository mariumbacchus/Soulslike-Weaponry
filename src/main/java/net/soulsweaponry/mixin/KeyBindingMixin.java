package net.soulsweaponry.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraftforge.event.TickEvent;
import net.soulsweaponry.events.ClientForgeEvents;
import net.soulsweaponry.util.WeaponUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

    /**
     * Epic Fight mod disables the key pressed to avoid vanilla attacking. Due to this, the event to fire a moonlight
     * projectile in {@link ClientForgeEvents#clientTick(TickEvent.ClientTickEvent)} is never fired. This mixin will
     * fix that issue, allowing the projectiles to spawn everytime this
     * method is called by left click but cancelled (with {@code pressed} parameter set to false) thanks to Epic Fight.
     */
    @Inject(method = "setKeyPressed", at = @At("HEAD"))
    private static void setKeyPressed(InputUtil.Key key, boolean pressed, CallbackInfo info) {
        if (WeaponUtil.isModLoaded("epicfight") && !pressed) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.options.attackKey.getKey().equals(key)) {
                if (client.player != null) {
                    ClientForgeEvents.triggerMoonlightEvent(client.player);
                }
            }
        }
    }
}
