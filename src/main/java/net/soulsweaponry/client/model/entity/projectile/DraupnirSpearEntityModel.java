package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;
import software.bernie.geckolib.model.GeoModel;

public class DraupnirSpearEntityModel extends GeoModel<DraupnirSpearEntity> {

    @Override
    public Identifier getAnimationResource(DraupnirSpearEntity animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/draupnir_spear.animation.json");
    }

    @Override
    public Identifier getModelResource(DraupnirSpearEntity object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/draupnir_spear.geo.json");

    }

    @Override
    public Identifier getTextureResource(DraupnirSpearEntity object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/draupnir_spear.png");
    }
}
