package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.items.Bloodthirster;
import net.soulsweaponry.SoulsWeaponry;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BloodthirsterModel extends AnimatedGeoModel<Bloodthirster>{

    @Override
    public ResourceLocation getAnimationFileLocation(Bloodthirster animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(Bloodthirster object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/bloodthirster.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Bloodthirster object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/item/bloodthirster_textures.png");
    }
}
