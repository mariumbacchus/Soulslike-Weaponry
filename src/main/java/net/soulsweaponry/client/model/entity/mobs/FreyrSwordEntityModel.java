package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import software.bernie.geckolib.model.GeoModel;

public class FreyrSwordEntityModel extends GeoModel<FreyrSwordEntity> {

    @Override
    public Identifier getAnimationResource(FreyrSwordEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/entity/freyr_sword.animation.json");
    }

    @Override
    public Identifier getModelResource(FreyrSwordEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/entity/freyr_sword.geo.json");

    }

    @Override
    public Identifier getTextureResource(FreyrSwordEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/freyr_sword.png");
    }

}