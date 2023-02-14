package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Mjolnir;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MjolnirItemModel extends AnimatedGeoModel<Mjolnir>{

    @Override
    public Identifier getAnimationFileLocation(Mjolnir animatable) {
        return null;
    }

    @Override
    public Identifier getModelLocation(Mjolnir object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/mjolnir.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Mjolnir object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/mjolnir_texture.png");
    }
    
}
