package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.NightSkull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class NightSkullModel extends GeoModel<NightSkull> {

    @Override
    public Identifier getModelResource(NightSkull nightSkull, @Nullable GeoRenderer<NightSkull> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/night_skull.geo.json");
    }

    @Override
    public Identifier getTextureResource(NightSkull nightSkull, @Nullable GeoRenderer<NightSkull> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/night_skull.png");
    }

    @Override
    public Identifier getAnimationResource(NightSkull animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/night_skull.animation.json");
    }
}
