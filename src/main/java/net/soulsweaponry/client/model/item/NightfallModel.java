package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Nightfall;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NightfallModel extends AnimatedGeoModel<Nightfall>{

    @Override
    public ResourceLocation getAnimationFileLocation(Nightfall animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(Nightfall object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/nightfall.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Nightfall object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/item/nightfall.png");
    }
    
}
