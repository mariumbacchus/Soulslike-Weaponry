package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.GhostGlaiveEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GhostGlaiveModel extends GeoModel<GhostGlaiveEntity> {

    @Override
    public Identifier getModelResource(GhostGlaiveEntity ghostGlaiveEntity, @Nullable GeoRenderer<GhostGlaiveEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/ghost_glaive.geo.json");
    }

    @Override
    public Identifier getTextureResource(GhostGlaiveEntity ghostGlaiveEntity, @Nullable GeoRenderer<GhostGlaiveEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/ghost_glaive.png");
    }

    @Override
    public Identifier getAnimationResource(GhostGlaiveEntity animatable) {
        return null;
    }
}
