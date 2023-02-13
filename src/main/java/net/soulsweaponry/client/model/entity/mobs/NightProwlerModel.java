package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.NightProwler;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NightProwlerModel extends AnimatedGeoModel<NightProwler>{
    
    @Override
    public Identifier getModelResource(NightProwler object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/night_prowler.geo.json");
    }

    @Override
    public Identifier getTextureResource(NightProwler object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/night_prowler_texture.png");
    }

    @Override
    public Identifier getAnimationResource(NightProwler object)
    {
        return null;//new Identifier(SoulsWeaponry.ModId, "animations/accursed_lord.animation.json");
    }
}
