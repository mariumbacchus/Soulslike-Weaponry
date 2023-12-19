package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.FreyrSword;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FreyrSwordItemModel extends AnimatedGeoModel<FreyrSword>{

    @Override
    public ResourceLocation getAnimationFileLocation(FreyrSword animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(FreyrSword object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/freyr_sword_item.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FreyrSword object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/entity/freyr_sword.png");
    }
    
}
