package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.ShadowOrb;
import software.bernie.geckolib.model.GeoModel;

public class ShadowOrbModel extends GeoModel<ShadowOrb> {

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