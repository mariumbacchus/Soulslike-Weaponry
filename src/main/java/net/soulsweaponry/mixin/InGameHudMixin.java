package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EffectRegistry;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    private static final Identifier DAWNBREAKER_OVERLAY = new Identifier(SoulsWeaponry.ModId, "textures/misc/dawnbreaker_overlay.png");
    MinecraftClient client = MinecraftClient.getInstance();
    @Inject(at = @At("RETURN"), method = "render")
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        if (this.client.player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY)) {
            InGameHud hud = (InGameHud)(Object)this;
            ((InGameHudAccessor)hud).invokeRenderOverlay(DAWNBREAKER_OVERLAY, 1f);
        }
    }
}
