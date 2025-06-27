package net.soulsweaponry.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.entitydata.TargetPostureData;
import net.soulsweaponry.mixin.BossBarHudAccessor;

import java.util.Map;
import java.util.UUID;

public class TargetPostureHudOverlay implements HudRenderCallback {

    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture_bars_target.png");

    @Override
    public void onHudRender(DrawContext drawContext, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && !ClientConfig.disable_target_posture_hud) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            int barX = width / 2 - 91;
            if (client.player != null && !client.player.isDead()) {
                int posture = TargetPostureData.getTargetPosture(client.player);
                float posturePerPixel = TargetPostureData.getTargetsMaxPosture(client.player) / (float) 182;
                int pixelOffset = MathHelper.floor((float) posture / posturePerPixel);
                if (posture > 0) {
                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

                    BossBarHud bossHud = MinecraftClient.getInstance().inGameHud.getBossBarHud();
                    Map<UUID, ClientBossBar> bars = ((BossBarHudAccessor)bossHud).getBossBars();
                    String name = TargetPostureData.getTargetName(client.player);
                    int initialBarY = 12;
                    final int SLOT_HEIGHT = 19;
                    int maxY = height / 3;
                    int available = maxY - initialBarY;
                    int slotCount = Math.min(bars.size(), (available + SLOT_HEIGHT) / SLOT_HEIGHT);
                    int barY = initialBarY + slotCount * SLOT_HEIGHT;
                    TextRenderer font = client.textRenderer;
                    int nameWidth = font.getWidth(name);
                    int textX = barX + (182 - nameWidth) / 2; // Centered
                    int textY = barY - 9;
                    drawContext.drawTextWithShadow(font, name, textX, textY, 0xFFFFFF);

                    drawContext.drawTexture(TEXTURE, barX - 25, barY - 10, 0, 0, 25, 25, 207 ,25); // Icon
                    drawContext.drawTexture(TEXTURE, barX, barY, 25, 10, 182, 5, 207, 25); // Empty
                    drawContext.drawTexture(TEXTURE, barX, barY, 25, 15, pixelOffset, 5, 207, 25); // Filled
                }
            }
        }
    }
}
