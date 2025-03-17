package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.noclip.GhostGlaiveEntity;
import software.bernie.geckolib.model.GeoModel;

public class GhostGlaiveModel extends GeoModel<GhostGlaiveEntity> {

    @Override
    public Identifier getAnimationResource(GhostGlaiveEntity animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(GhostGlaiveEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/ghost_glaive.geo.json");
    }

    @Override
    public Identifier getTextureResource(GhostGlaiveEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/ghost_glaive.png");
    }
}