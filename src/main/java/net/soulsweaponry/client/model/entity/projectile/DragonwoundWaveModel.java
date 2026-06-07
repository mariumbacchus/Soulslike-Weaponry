package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.MoonveilWave;
import software.bernie.geckolib.model.GeoModel;

public class DragonwoundWaveModel extends GeoModel<MoonveilWave> {

    @Override
    public Identifier getAnimationResource(MoonveilWave animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(MoonveilWave object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/moonlight_projectile_big.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoonveilWave object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/dragonwound_wave.png");
    }
}
