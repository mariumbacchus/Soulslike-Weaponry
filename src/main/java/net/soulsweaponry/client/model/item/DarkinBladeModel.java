package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.DarkinBlade;
import software.bernie.geckolib.model.GeoModel;

public class DarkinBladeModel extends GeoModel<DarkinBlade> {

    @Override
    public Identifier getAnimationResource(DarkinBlade animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/darkin_blade.animation.json");
    }

    @Override
    public Identifier getModelResource(DarkinBlade object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/darkin_blade.geo.json");
    }

    @Override
    public Identifier getTextureResource(DarkinBlade object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/darkin_blade_textures.png");
    }
    
}
