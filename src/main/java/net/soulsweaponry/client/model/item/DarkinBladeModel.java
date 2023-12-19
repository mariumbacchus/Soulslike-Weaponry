package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.DarkinBlade;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DarkinBladeModel extends AnimatedGeoModel<DarkinBlade>{

    @Override
    public ResourceLocation getAnimationFileLocation(DarkinBlade animatable) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "animations/darkin_blade.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DarkinBlade object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/darkin_blade.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DarkinBlade object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/item/darkin_blade_textures.png");
    }
    
}
