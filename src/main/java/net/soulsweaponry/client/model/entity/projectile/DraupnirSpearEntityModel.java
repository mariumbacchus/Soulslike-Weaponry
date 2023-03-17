package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DraupnirSpearEntityModel extends AnimatedGeoModel<DraupnirSpearEntity> {

    @Override
    public Identifier getAnimationResource(DraupnirSpearEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/draupnir_spear.animation.json");
    }

    @Override
    public Identifier getModelResource(DraupnirSpearEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/draupnir_spear.geo.json");

    }

    @Override
    public Identifier getTextureResource(DraupnirSpearEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/draupnir_spear.png");
    }
}
