package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Dawnbreaker;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DawnbreakerModel extends AnimatedGeoModel<Dawnbreaker> {

    @Override
    public ResourceLocation getAnimationFileLocation(Dawnbreaker animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(Dawnbreaker object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/dawnbreaker.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Dawnbreaker object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/item/dawnbreaker_texture.png");
    }
    
}
