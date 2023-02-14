package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.CometSpearEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CometSpearModel extends AnimatedGeoModel<CometSpearEntity> {

    @Override
    public Identifier getAnimationFileLocation(CometSpearEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, null);
    }

    @Override
    public Identifier getModelLocation(CometSpearEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/comet_spear.geo.json");

    }

    @Override
    public Identifier getTextureLocation(CometSpearEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/comet_spear.png");
    }
    
}
