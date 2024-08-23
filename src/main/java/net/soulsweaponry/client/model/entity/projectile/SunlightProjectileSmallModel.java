package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import software.bernie.geckolib.model.GeoModel;

public class SunlightProjectileSmallModel extends GeoModel<MoonlightProjectile> {

    @Override
    public Identifier getAnimationResource(MoonlightProjectile animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(MoonlightProjectile object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/moonlight_projectile.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoonlightProjectile object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/sunlight_projectile_small.png");
    }

}