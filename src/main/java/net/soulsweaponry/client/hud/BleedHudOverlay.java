package net.soulsweaponry.client.hud;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.entitystats.EntityBleed;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.entitydata.BleedData;

public class BleedHudOverlay extends EffectHudOverlay {

    private static final Identifier TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/gui/bleed_bars.png");

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public int getBarPixelOffset(ClientPlayerEntity player) {
        int bleed = BleedData.getBleed(player);
        float bleedPerPixel = EntityBleed.getMaxBleed(player) / (float) 182;
        return MathHelper.floor((float) bleed / bleedPerPixel);
    }

    @Override
    public boolean shouldShow(ClientPlayerEntity player) {
        int bleed = BleedData.getBleed(player);
        return bleed > 0 && !ClientConfig.disable_player_bleed_hud && !EntityBleed.isBleedDisabled(player);
    }
}
