package net.soulsweaponry.client.model.armor;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.ChaosSet;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ChaosSetModel extends AnimatedGeoModel<ChaosSet> {

    @Override
    public Identifier getAnimationResource(ChaosSet animatable) {
        return new Identifier(SoulsWeaponry.ModId, "animations/chaos_monarch.animation.json");
    }

    @Override
    public Identifier getModelResource(ChaosSet object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/chaos_set.geo.json");
    }

    @Override
    public Identifier getTextureResource(ChaosSet object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/armor/chaos_set_texture.png");
    }
    
}
