package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Mjolnir;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MjolnirItemModel extends AnimatedGeoModel<Mjolnir>{

    @Override
    public Identifier getAnimationResource(Mjolnir animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(Mjolnir object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/mjolnir.geo.json");
    }

    @Override
    public Identifier getTextureResource(Mjolnir object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/mjolnir_texture.png");
    }
    
}
