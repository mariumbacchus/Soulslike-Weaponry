package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.ForlornScythe;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ForlornScytheModel extends AnimatedGeoModel<ForlornScythe>{

    @Override
    public Identifier getAnimationResource(ForlornScythe animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(ForlornScythe object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/forlorn_scythe.geo.json");
    }

    @Override
    public Identifier getTextureResource(ForlornScythe object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/forlorn_scythe.png");
    }
    
}
