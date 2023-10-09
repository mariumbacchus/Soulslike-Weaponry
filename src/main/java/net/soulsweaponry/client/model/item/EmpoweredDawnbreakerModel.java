package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.EmpoweredDawnbreaker;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EmpoweredDawnbreakerModel extends AnimatedGeoModel<EmpoweredDawnbreaker>{

    @Override
    public Identifier getAnimationResource(EmpoweredDawnbreaker animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(EmpoweredDawnbreaker object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/empowered_dawnbreaker.geo.json");
    }

    @Override
    public Identifier getTextureResource(EmpoweredDawnbreaker object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/empowered_dawnbreaker.png");
    }
}