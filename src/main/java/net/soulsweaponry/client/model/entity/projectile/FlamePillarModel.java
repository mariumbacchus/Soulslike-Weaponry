package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.FlamePillar;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class FlamePillarModel extends GeoModel<FlamePillar> {

    @Override
    public Identifier getAnimationResource(FlamePillar animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/holy_moonlight_pillar.animation.json");
    }

    @Override
    public Identifier getModelResource(FlamePillar flamePillar, @Nullable GeoRenderer<FlamePillar> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/holy_moonlight_pillar.geo.json");
    }

    @Override
    public Identifier getTextureResource(FlamePillar flamePillar, @Nullable GeoRenderer<FlamePillar> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/flame_pillar.png");
    }
}
