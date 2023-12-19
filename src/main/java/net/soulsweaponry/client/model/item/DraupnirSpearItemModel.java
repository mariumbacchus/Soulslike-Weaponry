package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.DraupnirSpear;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DraupnirSpearItemModel extends AnimatedGeoModel<DraupnirSpear>{

    @Override
    public ResourceLocation getAnimationFileLocation(DraupnirSpear animatable) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "animations/draupnir_spear.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DraupnirSpear object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/draupnir_spear.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DraupnirSpear object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/entity/draupnir_spear.png");
    }
    
}
