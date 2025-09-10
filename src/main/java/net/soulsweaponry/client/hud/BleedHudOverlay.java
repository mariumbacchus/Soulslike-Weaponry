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
    public Identifier getTexture(ClientPlayerEntity player) {
        return TEXTURE;
    }

    @Override
    public int getBarPixelOffset(ClientPlayerEntity player) {
        int bleed = Math.max(0, BleedData.getBleed(player));
        int max = Math.max(1, EntityBleed.getMaxBleed(player));
        bleed = Math.min(bleed, max);
        int pixels = bleed * BAR_WIDTH / max;
        return MathHelper.clamp(pixels, 0, BAR_WIDTH);
    }

    @Override
    public boolean shouldShow(ClientPlayerEntity player) {
        int bleed = BleedData.getBleed(player);
        return bleed > 0 && !ClientConfig.disable_player_bleed_hud && !EntityBleed.isBleedDisabled(player);
    }
}
