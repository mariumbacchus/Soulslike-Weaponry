package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.EmpoweredDawnbreaker;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EmpoweredDawnbreakerModel extends AnimatedGeoModel<EmpoweredDawnbreaker>{

    @Override
    public Identifier getAnimationFileLocation(EmpoweredDawnbreaker animatable) {
        return null;
    }

    @Override
    public Identifier getModelLocation(EmpoweredDawnbreaker object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/empowered_dawnbreaker.geo.json");
    }

    @Override
    public Identifier getTextureLocation(EmpoweredDawnbreaker object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/item/empowered_dawnbreaker.png");
    }
}
