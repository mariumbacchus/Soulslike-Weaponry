package net.soulsweaponry.client.model.armor;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.ChaosSet;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ChaosSetModel extends AnimatedGeoModel<ChaosSet> {

    @Override
    public ResourceLocation getModelLocation(ChaosSet object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/chaos_set.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ChaosSet object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/armor/chaos_set_texture.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ChaosSet animatable) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "animations/chaos_monarch.animation.json");
    }
}
