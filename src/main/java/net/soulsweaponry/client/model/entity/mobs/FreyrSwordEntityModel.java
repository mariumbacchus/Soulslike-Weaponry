package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FreyrSwordEntityModel extends AnimatedGeoModel<FreyrSwordEntity> {

    @Override
    public Identifier getAnimationFileLocation(FreyrSwordEntity animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/freyr_sword.animation.json");
    }

    @Override
    public Identifier getModelLocation(FreyrSwordEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/freyr_sword.geo.json");

    }

    @Override
    public Identifier getTextureLocation(FreyrSwordEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/freyr_sword.png");
    }
    
}
