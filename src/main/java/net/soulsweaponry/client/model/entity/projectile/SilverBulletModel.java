package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import software.bernie.geckolib.model.GeoModel;

public class SilverBulletModel extends GeoModel<SilverBulletEntity> {

    @Override
    public Identifier getAnimationResource(SilverBulletEntity animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(SilverBulletEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/silver_bullet.geo.json");
    }

    @Override
    public Identifier getTextureResource(SilverBulletEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/silver_bullet_texture.png");
    }
    
}
