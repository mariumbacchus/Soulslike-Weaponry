package net.soulsweaponry.client.model.item;

import net.minecraft.resources.ResourceLocation;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Mjolnir;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MjolnirItemModel extends AnimatedGeoModel<Mjolnir>{

    @Override
    public ResourceLocation getAnimationFileLocation(Mjolnir animatable) {
        return null;
    }

    @Override
    public ResourceLocation getModelLocation(Mjolnir object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "geo/mjolnir.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Mjolnir object) {
        return new ResourceLocation(SoulsWeaponry.MOD_ID, "textures/entity/mjolnir_texture.png");
    }
    
}
