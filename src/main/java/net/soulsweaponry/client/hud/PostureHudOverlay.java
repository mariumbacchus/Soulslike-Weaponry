package net.soulsweaponry.client.hud;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.config.ClientConfig;
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
}
