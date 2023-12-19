package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.EmpoweredDawnbreaker;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EmpoweredDawnbreakerModel extends AnimatedGeoModel<EmpoweredDawnbreaker>{

    @Override
    public ResourceLocation getAnimationFileLocation(EmpoweredDawnbreaker animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(EmpoweredDawnbreaker object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/empowered_dawnbreaker.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EmpoweredDawnbreaker object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/item/empowered_dawnbreaker.png");
    }
}
