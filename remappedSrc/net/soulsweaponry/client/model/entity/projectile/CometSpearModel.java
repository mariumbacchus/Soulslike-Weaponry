package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.CometSpearEntity;
import software.bernie.geckolib.model.GeoModel;

public class CometSpearModel extends GeoModel<CometSpearEntity> {

    @Override
    public Identifier getAnimationResource(CometSpearEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, null);
    }

    @Override
    public Identifier getModelResource(CometSpearEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/comet_spear.geo.json");

    }

    @Override
    public Identifier getTextureResource(CometSpearEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/comet_spear.png");
    }
    
}
