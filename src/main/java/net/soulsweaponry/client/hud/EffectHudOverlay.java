package net.soulsweaponry.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class EffectHudOverlay implements HudRenderCallback {

    private int yOffset = 0;
    public static final int BAR_WIDTH = 182;
    public static final int BAR_HEIGHT = 5;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.player.isDead()) return;

        ClientPlayerEntity player = client.player;
        if (!shouldShow(player)) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        int x = width / 2;
        int y = height;

        int barY = y / 2 + 10 - this.yOffset;
        int barX = x / 10;

        Identifier tex = getTexture(player);
        int pixelOffset = getBarPixelOffset(player);

        drawContext.drawTexture(RenderLayer::getGuiTextured, tex,
                barX - 25, barY - 10,
                0, 0,
                25, 25,
                207, 25
        ); // Icon

        drawContext.drawTexture(RenderLayer::getGuiTextured, tex,
                barX, barY,
                25, 10,
                BAR_WIDTH, BAR_HEIGHT,
                207, 25
        ); // Empty

        drawContext.drawTexture(RenderLayer::getGuiTextured, tex,
                barX, barY,
                25, 15,
                pixelOffset, BAR_HEIGHT,
                207, 25
        ); // Filled
    }

    public abstract Identifier getTexture(ClientPlayerEntity player);
    public abstract int getBarPixelOffset(ClientPlayerEntity player);
    public abstract boolean shouldShow(ClientPlayerEntity player);

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }
}