package net.soulsweaponry.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * TODO: Base mixin for adding custom boss bars, must be finished!
 */
@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public abstract class BossBarHudMixin {

    private static final Identifier FILLED_BAR = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture/full.png");
    private static final Identifier EMPTY_BAR = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture/empty.png");

    @Inject(
            method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderBossBar(DrawContext ctx, int x, int y, BossBar bar, CallbackInfo ci) {
        Text name = bar.getName();
        // NOTE: Keep in mind that the name.getString() returns the display name, so this can vary between languages!
        if (name.getString().equals(EntityRegistry.RETURNING_KNIGHT.getName().getString())) {
            drawMyCustomBar(ctx, x, y, bar);
            ci.cancel(); // skip vanilla drawing for this bar only
        }
    }

    private void drawMyCustomBar(DrawContext ctx, int x, int y, BossBar bar) {
        ctx.getMatrices().push();
        RenderSystem.setShaderTexture(0, FILLED_BAR);

        // background (full width)
        ctx.drawTexture(EMPTY_BAR, x, y, 0, 0, 182, 5, 182, 10);
        // fill
        int filled = (int)(bar.getPercent() * 182);
        ctx.drawTexture(FILLED_BAR, x, y, 0, 5, filled, 5, 182, 10);

        ctx.getMatrices().pop();
    }
}
