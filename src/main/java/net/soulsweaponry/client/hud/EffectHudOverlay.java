package net.soulsweaponry.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public abstract class EffectHudOverlay implements HudRenderCallback {

    private int yOffset = 0;

    @Override
    public void onHudRender(DrawContext drawContext, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            int x = width / 2;
            int y = height;
            int barY = y / 2 + 10 - this.yOffset;
            int barX = x / 10;
            if (client.player != null && !client.player.isDead()) {
                int pixelOffset = this.getBarPixelOffset(client.player);
                if (this.shouldShow(client.player)) {
                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

                    drawContext.drawTexture(this.getTexture(), barX - 25, barY - 10, 0, 0, 25, 25, 207 ,25); // Icon
                    drawContext.drawTexture(this.getTexture(), barX, barY, 25, 10, 182, 5, 207, 25); // Empty
                    drawContext.drawTexture(this.getTexture(), barX, barY, 25, 15, pixelOffset, 5, 207, 25); // Filled
                }
            }
        }
    }

    public abstract Identifier getTexture();
    public abstract int getBarPixelOffset(ClientPlayerEntity player);
    public abstract boolean shouldShow(ClientPlayerEntity player);

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }
}
