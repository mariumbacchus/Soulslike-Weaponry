package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.DarkinBlade;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DarkinBladeModel extends AnimatedGeoModel<DarkinBlade>{

    @Override
    public Identifier getAnimationResource(DarkinBlade animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/darkin_blade.animation.json");
    }

    @Override
    public Identifier getModelResource(DarkinBlade object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/darkin_blade.geo.json");
    }

    @Override
    public Identifier getTextureResource(DarkinBlade object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/darkin_blade_textures.png");
    }
    
}
