package net.soulsweaponry.client.hud;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.entitydata.PostureData;

public class PostureHudOverlay extends EffectHudOverlay {

    private static final Identifier TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/gui/posture_bars.png");

    @Override
    public Identifier getTexture(ClientPlayerEntity player) {
        return TEXTURE;
    }

    @Override
    public int getBarPixelOffset(ClientPlayerEntity player) {
        int posture = Math.max(0, PostureData.getPosture(player));
        int max = Math.max(1, EntityPosture.getMaxPostureLoss(player));
        posture = Math.min(posture, max);
        int pixels = posture * BAR_WIDTH / max;
        return MathHelper.clamp(pixels, 0, BAR_WIDTH);
    }

    @Override
    public boolean shouldShow(ClientPlayerEntity player) {
        int posture = PostureData.getPosture(player);
        return posture > 0 && !ClientConfig.disable_player_posture_hud;
    }
}
