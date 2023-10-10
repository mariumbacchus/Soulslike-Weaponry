package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.EmpoweredDawnbreaker;
import software.bernie.geckolib.model.GeoModel;

public class EmpoweredDawnbreakerModel extends GeoModel<EmpoweredDawnbreaker> {

    @Override
    public Identifier getModelResource(EmpoweredDawnbreaker animatable) {
        return new Identifier(SoulsWeaponry.ModId, "geo/empowered_dawnbreaker.geo.json");
    }

    @Override
    public Identifier getTextureResource(EmpoweredDawnbreaker animatable) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/empowered_dawnbreaker.png");
    }

    @Override
    public Identifier getAnimationResource(EmpoweredDawnbreaker animatable) {
        return null;
    }
}
