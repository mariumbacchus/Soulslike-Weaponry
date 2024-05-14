package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.NightsEdge;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NightsEdgeModel extends AnimatedGeoModel<NightsEdge> {

    @Override
    public Identifier getAnimationFileLocation(NightsEdge animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/nights_edge.animation.json");
    }

    @Override
    public Identifier getModelLocation(NightsEdge object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/nights_edge.geo.json");
    }

    @Override
    public Identifier getTextureLocation(NightsEdge object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/nights_edge.png");
    }
}