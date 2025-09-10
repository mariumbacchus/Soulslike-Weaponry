package net.soulsweaponry.client.hud;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.entitystats.EntityFrost;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.entitydata.FrostData;

public class FrostHudOverlay extends EffectHudOverlay {

    private static final Identifier NORMAL = Identifier.of(SoulsWeaponry.ModId, "textures/gui/frost.png");
    private static final Identifier COOLDOWN = Identifier.of(SoulsWeaponry.ModId, "textures/gui/frost_cooldown.png");

    @Override
    public Identifier getTexture(ClientPlayerEntity player) {
        if (FrostData.isFrostCoolingDown(player)) {
            return COOLDOWN;
        }
        return NORMAL;
    }

    @Override
    public int getBarPixelOffset(ClientPlayerEntity player) {
        int frost = Math.max(0, FrostData.getFrost(player));
        int max = Math.max(1, EntityFrost.getMaxFrostBuildup(player));
        frost = Math.min(frost, max);
        int pixels = frost * BAR_WIDTH / max;
        return MathHelper.clamp(pixels, 0, BAR_WIDTH);
    }

    @Override
    public boolean shouldShow(ClientPlayerEntity player) {
        int frost = FrostData.getFrost(player);
        return frost > 0 && !ClientConfig.disable_player_frost_hud && !EntityFrost.isFrostBuildupDisabled(player);
    }
}
