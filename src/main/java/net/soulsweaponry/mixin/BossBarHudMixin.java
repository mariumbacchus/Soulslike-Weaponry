package net.soulsweaponry.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.soulsweaponry.client.hud.CustomBossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public abstract class BossBarHudMixin {

    @Inject(
            method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderBossBar(DrawContext ctx, int x, int y, BossBar bar, CallbackInfo ci) {
        Text name = bar.getName();
        // NOTE: Keep in mind that the name.getString() returns the display name, so this can vary between languages!
        for (CustomBossBar customBossBar : CustomBossBar.CUSTOM_BOSS_BARS) {
            if (name.getString().equals(customBossBar.entityType().getName().getString())) {
                drawMyCustomBar(ctx, x, y, bar, customBossBar);
                ci.cancel(); // skip vanilla drawing for this bar only
            }
        }
    }

    @Unique
    private void drawMyCustomBar(DrawContext ctx, int x, int y, BossBar originalBar, CustomBossBar customBossBar) {
        ctx.getMatrices().push();
        RenderSystem.setShaderTexture(0, customBossBar.filledBar());
        // TODO these height and width values can be inserted as params in CustomBossBar record when the textures are made
        ctx.drawTexture(RenderLayer::getGuiTextured, customBossBar.emptyBar(), x, y, 0, 0, 182, 5, 182, 10);
        int filled = (int)(originalBar.getPercent() * 182);
        ctx.drawTexture(RenderLayer::getGuiTextured, customBossBar.filledBar(), x, y, 0, 5, filled, 5, 182, 10);

        ctx.getMatrices().pop();
    }
}