package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.WarmthEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WarmthEntityModel extends AnimatedGeoModel<WarmthEntity> {

    @Override
    public Identifier getAnimationFileLocation(WarmthEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/warmth_entity.animation.json");
    }

    @Override
    public Identifier getModelLocation(WarmthEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/warmth_entity.geo.json");

    }

    @Override
    public Identifier getTextureLocation(WarmthEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/warmth_entity.png");
    }
}