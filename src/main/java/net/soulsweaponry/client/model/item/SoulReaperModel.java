package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.SoulReaper;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SoulReaperModel extends AnimatedGeoModel<SoulReaper>{

    @Override
    public ResourceLocation getAnimationFileLocation(SoulReaper animatable) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "animations/soul_reaper.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(SoulReaper object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/soul_reaper.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SoulReaper object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/item/soul_reaper.png");
    }
    
}
