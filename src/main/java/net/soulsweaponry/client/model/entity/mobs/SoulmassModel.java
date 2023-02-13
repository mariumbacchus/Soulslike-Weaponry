package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.Soulmass;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SoulmassModel extends AnimatedGeoModel<Soulmass>{

    @Override
    public Identifier getModelResource(Soulmass object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/soulmass.geo.json");
    }

    @Override
    public Identifier getTextureResource(Soulmass object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/soulmass_texture.png");
    }

    @Override
    public Identifier getAnimationResource(Soulmass object)
    {
        return new Identifier(SoulsWeaponry.ModId, "animations/soulmass.animation.json");
    }
}
