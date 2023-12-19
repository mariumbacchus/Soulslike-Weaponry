package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.ForlornScythe;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ForlornScytheModel extends AnimatedGeoModel<ForlornScythe>{

    @Override
    public ResourceLocation getAnimationFileLocation(ForlornScythe animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(ForlornScythe object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/forlorn_scythe.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ForlornScythe object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/item/forlorn_scythe.png");
    }
    
}
