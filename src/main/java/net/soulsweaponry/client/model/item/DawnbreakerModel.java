package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.sword.Dawnbreaker;
import software.bernie.geckolib.model.GeoModel;

public class DawnbreakerModel extends GeoModel<Dawnbreaker> {

    @Override
    public Identifier getAnimationResource(Dawnbreaker animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(Dawnbreaker object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/dawnbreaker.geo.json");
    }

    @Override
    public Identifier getTextureResource(Dawnbreaker object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/dawnbreaker_texture.png");
    }
    
}
