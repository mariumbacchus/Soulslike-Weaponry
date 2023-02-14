package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SilverBulletModel extends AnimatedGeoModel<SilverBulletEntity>{

    @Override
    public Identifier getAnimationFileLocation(SilverBulletEntity animatable) {
        return null;
    }

    @Override
    public Identifier getModelLocation(SilverBulletEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "geo/silver_bullet.geo.json");
    }

    @Override
    public Identifier getTextureLocation(SilverBulletEntity object) {
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/silver_bullet_texture.png");
    }
    
}
