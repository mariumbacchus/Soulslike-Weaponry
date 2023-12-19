package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.CometSpear;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CometSpearItemModel extends AnimatedGeoModel<CometSpear>{
    @Override
    public ResourceLocation getModelLocation(CometSpear object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/comet_spear.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CometSpear object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/entity/comet_spear.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CometSpear object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, null);
    }
}
