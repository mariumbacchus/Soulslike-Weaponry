package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.LeviathanAxe;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LeviathanAxeModel extends AnimatedGeoModel<LeviathanAxe>{

    @Override
    public ResourceLocation getAnimationFileLocation(LeviathanAxe animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(LeviathanAxe object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/leviathan_axe.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(LeviathanAxe object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/item/leviathan_axe_texture.png");
    }
    
}
