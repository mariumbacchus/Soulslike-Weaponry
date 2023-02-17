package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.Dawnbreaker;
import software.bernie.geckolib.model.GeoModel;

public class DawnbreakerModel extends GeoModel<Dawnbreaker> {

    @Override
    public Identifier getAnimationResource(Dawnbreaker animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(Dawnbreaker object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/dawnbreaker.geo.json");
    }

    @Override
    public Identifier getTextureResource(Dawnbreaker object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/dawnbreaker_texture.png");
    }
    
}
