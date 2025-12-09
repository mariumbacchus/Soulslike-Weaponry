package net.soulsweaponry.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.entitydata.ClientTargetPostureData;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.mixin.BossBarHudAccessor;

import java.util.Map;
import java.util.UUID;

public class TargetPostureHudOverlay {

    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture_bars_target.png");
    public static final TargetPostureHudOverlay INSTANCE = new TargetPostureHudOverlay();
    public static final IGuiOverlay HUD_TARGET_POSTURE = (gui, drawContext, partialTicks, width, height) -> {
        INSTANCE.render(drawContext, partialTicks, width, height);
    };

    public void render(DrawContext drawContext, float tickDelta, int width, int height) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || ClientConfig.disable_target_posture_hud) {
            return;
        }

        int barX = width / 2 - 91;

        if (client.player == null || client.player.isDead()) {
            return;
        }

        int posture = ClientTargetPostureData.getTargetPosture();
        if (posture <= 0) {
            return;
        }

        float posturePerPixel = ClientTargetPostureData.getMaxPosture() / 182f;
        int pixelOffset = MathHelper.floor(posture / posturePerPixel);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        BossBarHud bossHud = client.inGameHud.getBossBarHud();
        Map<UUID, ClientBossBar> bars = ((BossBarHudAccessor) bossHud).getBossBars();

        String name = ClientTargetPostureData.getName();
        int initialBarY = 12;
        final int SLOT_HEIGHT = 19;
        int maxY = height / 3;
        int available = maxY - initialBarY;
        int slotCount = Math.min(bars.size(), (available + SLOT_HEIGHT) / SLOT_HEIGHT);
        int barY = initialBarY + slotCount * SLOT_HEIGHT;

        TextRenderer font = client.textRenderer;
        int nameWidth = font.getWidth(name);
        int textX = barX + (182 - nameWidth) / 2;
        int textY = barY - 9;

        // Name
        drawContext.drawTextWithShadow(font, name, textX, textY, 0xFFFFFF);

        // Icon
        drawContext.drawTexture(TEXTURE, barX - 25, barY - 10,
                0, 0,
                25, 25,
                207, 25);

        // Empty bar
        drawContext.drawTexture(TEXTURE, barX, barY,
                25, 10,
                182, 5,
                207, 25);

        // Filled bar
        drawContext.drawTexture(TEXTURE, barX, barY,
                25, 15,
                pixelOffset, 5,
                207, 25);
    }
}