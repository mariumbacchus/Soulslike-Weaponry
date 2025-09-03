package net.soulsweaponry.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.client.entitydata.ClientPostureData;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.PostureData;

public class PostureHudOverlay extends EffectHudOverlay {

    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture_bars.png");

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public int getBarPixelOffset(ClientPlayerEntity player) {
        int posture = PostureData.getPosture(player);
        float posturePerPixel = EntityPosture.getMaxPostureLoss(player) / (float) 182;
        return MathHelper.floor((float) posture / posturePerPixel);
    }

    @Override
    public boolean shouldShow(ClientPlayerEntity player) {
        int posture = PostureData.getPosture(player);
        return posture > 0 && !ClientConfig.disable_player_posture_hud;
    }

    //TODO this was old, implement to new parent class EffectHudOverlay
    private static final Identifier FILLED_BAR = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture/full.png");
    private static final Identifier EMPTY_BAR = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture/empty.png");
    private static final Identifier ICON = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture/icon.png");

    public static final IGuiOverlay HUD_POSTURE = ((gui, drawContext, partialTicks, width, height) -> {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            int x = width / 2;
            int y = height;
            if (client.player != null && !client.player.isDead()) {
                int posture = ClientPostureData.getPosture();
                float posturePerPixel = (float) ConfigConstructor.max_posture_loss / (float) 182;
                int posturePixel = MathHelper.floor((float) posture / posturePerPixel);
                if (posture > 0) {
                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

                    drawContext.drawTexture(ICON, x - 140, y - 90, 0, 0, 25, 25, 25 ,25);
                    drawContext.drawTexture(EMPTY_BAR, x - 140 + 25, y - 90 + 10, 0, 0, 182, 5, 182, 5);
                    drawContext.drawTexture(FILLED_BAR, x - 140 + 25, y - 90 + 10, 0, 0, posturePixel, 5, 182, 5);
                }
            }
        }
    });
}