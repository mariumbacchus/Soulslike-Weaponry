package net.soulsweaponry.client.hud;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.client.entitydata.ClientPostureData;
import net.soulsweaponry.config.ClientConfig;

public class PostureHudOverlay extends EffectHudOverlay {

    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/gui/posture_bars.png");
    public static final PostureHudOverlay INSTANCE = new PostureHudOverlay();
    public static final IGuiOverlay HUD_POSTURE = (gui, drawContext, partialTicks, width, height) -> {
        INSTANCE.render(drawContext, partialTicks);
    };

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public int getBarPixelOffset(ClientPlayerEntity player) {
        int posture = ClientPostureData.getPosture();
        float posturePerPixel = EntityPosture.getMaxPostureLoss(player) / (float) 182;
        return MathHelper.floor((float) posture / posturePerPixel);
    }

    @Override
    public boolean shouldShow(ClientPlayerEntity player) {
        int posture = ClientPostureData.getPosture();
        return posture > 0 && !ClientConfig.disable_player_posture_hud;
    }
}