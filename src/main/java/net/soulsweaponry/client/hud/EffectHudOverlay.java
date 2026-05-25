package net.soulsweaponry.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public abstract class EffectHudOverlay {

    private int yOffset = 0;
    public static final int BAR_WIDTH = 182;
    public static final int BAR_HEIGHT = 5;

    public void render(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        int x = width / 2;
        int y = height;
        int barY = y / 2 + 10 - this.yOffset;
        int barX = x / 10;

        ClientPlayerEntity player = client.player;
        if (player != null && !player.isDead()) {
            int pixelOffset = this.getBarPixelOffset(player);
            if (this.shouldShow(player)) {
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

                // Icon
                drawContext.drawTexture(
                        this.getTexture(client.player),
                        barX - 25, barY - 10,
                        0, 0,
                        25, 25,
                        207, 25
                );

                // Empty bar
                drawContext.drawTexture(
                        this.getTexture(client.player),
                        barX, barY,
                        25, 10,
                        BAR_WIDTH, BAR_HEIGHT,
                        207, 25
                );

                // Filled bar
                drawContext.drawTexture(
                        this.getTexture(client.player),
                        barX, barY,
                        25, 15,
                        pixelOffset, BAR_HEIGHT,
                        207, 25
                );
            }
        }
    }

    public abstract Identifier getTexture(ClientPlayerEntity player);
    public abstract int getBarPixelOffset(ClientPlayerEntity player);
    public abstract boolean shouldShow(ClientPlayerEntity player);

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }
}