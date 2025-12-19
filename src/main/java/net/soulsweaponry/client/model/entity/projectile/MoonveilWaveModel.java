package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.MoonveilWave;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MoonveilWaveModel extends GeoModel<MoonveilWave> {

    @Override
    public Identifier getModelResource(MoonveilWave moonveilWave, @Nullable GeoRenderer<MoonveilWave> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/moonlight_projectile_big.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoonveilWave moonveilWave, @Nullable GeoRenderer<MoonveilWave> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/" + moonveilWave.getTextureId() + ".png");
    }

    @Override
    public Identifier getAnimationResource(MoonveilWave animatable) {
        return null;
    }
}
