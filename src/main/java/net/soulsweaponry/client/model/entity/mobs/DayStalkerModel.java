package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.DayStalker;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DayStalkerModel extends AnimatedGeoModel<DayStalker>{
    
    @Override
    public Identifier getModelResource(DayStalker object)
    {
        return new Identifier(SoulsWeaponry.ModId, "geo/day_stalker.geo.json");
    }

    @Override
    public Identifier getTextureResource(DayStalker object)
    {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/day_stalker_texture.png");
    }

    @Override
    public Identifier getAnimationResource(DayStalker object)
    {
        return null;//new Identifier(SoulsWeaponry.ModId, "animations/accursed_lord.animation.json");
    }
}
