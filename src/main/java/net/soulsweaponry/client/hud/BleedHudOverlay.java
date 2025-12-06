package net.soulsweaponry.client.hud;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.entitystats.EntityBleed;
import net.soulsweaponry.client.entitydata.ClientBleedData;
import net.soulsweaponry.config.ClientConfig;

public class BleedHudOverlay extends EffectHudOverlay {

    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/gui/bleed_bars.png");

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public int getBarPixelOffset(ClientPlayerEntity player) {
        int bleed = ClientBleedData.getBleed();
        float bleedPerPixel = EntityBleed.getMaxBleed(player) / (float) 182;
        return MathHelper.floor((float) bleed / bleedPerPixel);
    }

    @Override
    public boolean shouldShow(ClientPlayerEntity player) {
        int bleed = ClientBleedData.getBleed();
        return bleed > 0 && !ClientConfig.disable_player_bleed_hud && !EntityBleed.isBleedDisabled(player);
    }
}