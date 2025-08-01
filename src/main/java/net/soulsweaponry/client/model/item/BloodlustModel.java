package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.katana.Bloodlust;
import software.bernie.geckolib.model.GeoModel;

public class BloodlustModel extends GeoModel<Bloodlust> {

    @Override
    public Identifier getAnimationResource(Bloodlust animatable) {
        return Identifier.of(SoulsWeaponry.ModId, "animations/bloodlust.animation.json");
    }

    @Override
    public Identifier getModelResource(Bloodlust object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/bloodlust.geo.json");
    }

    @Override
    public Identifier getTextureResource(Bloodlust object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/bloodlust.png");
    }
    
}
