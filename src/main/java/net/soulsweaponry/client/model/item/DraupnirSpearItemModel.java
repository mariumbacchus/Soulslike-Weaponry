package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.DraupnirSpear;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DraupnirSpearItemModel extends AnimatedGeoModel<DraupnirSpear>{

    @Override
    public Identifier getAnimationFileLocation(DraupnirSpear animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/draupnir_spear.animation.json");
    }

    @Override
    public Identifier getModelLocation(DraupnirSpear object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/draupnir_spear.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DraupnirSpear object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/draupnir_spear.png");
    }
    
}
