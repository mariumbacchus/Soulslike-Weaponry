package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.WarmthEntity;
import software.bernie.geckolib.model.GeoModel;

public class WarmthEntityModel extends GeoModel<WarmthEntity> {

    @Override
    public Identifier getAnimationResource(WarmthEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/entity/warmth_entity.animation.json");
    }

    @Override
    public Identifier getModelResource(WarmthEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/entity/warmth_entity.geo.json");

    }

    @Override
    public Identifier getTextureResource(WarmthEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/warmth_entity.png");
    }
}
