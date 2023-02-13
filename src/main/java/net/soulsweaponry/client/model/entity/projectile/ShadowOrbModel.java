package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.ShadowOrb;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShadowOrbModel extends AnimatedGeoModel<ShadowOrb>{

    @Override
    public Identifier getAnimationResource(ShadowOrb animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(ShadowOrb object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/cannonball.geo.json");
    }

    @Override
    public Identifier getTextureResource(ShadowOrb object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/shadow_orb.png");
    }
    
}
