package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.NightsEdge;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class NightsEdgeModel extends GeoModel<NightsEdge> {

    @Override
    public Identifier getModelResource(NightsEdge nightsEdge, @Nullable GeoRenderer<NightsEdge> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/entity/nights_edge.geo.json");
    }

    @Override
    public Identifier getTextureResource(NightsEdge nightsEdge, @Nullable GeoRenderer<NightsEdge> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/nights_edge.png");
    }

    @Override
    public Identifier getAnimationResource(NightsEdge animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/entity/nights_edge.animation.json");
    }
}
