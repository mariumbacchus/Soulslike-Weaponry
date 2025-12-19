package net.soulsweaponry.client.model.entity.projectile;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class SilverBulletModel extends GeoModel<SilverBulletEntity> {

    @Override
    public Identifier getModelResource(SilverBulletEntity silverBulletEntity, @Nullable GeoRenderer<SilverBulletEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/silver_bullet.geo.json");
    }

    @Override
    public Identifier getTextureResource(SilverBulletEntity silverBulletEntity, @Nullable GeoRenderer<SilverBulletEntity> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/entity/silver_bullet_texture.png");
    }

    @Override
    public Identifier getAnimationResource(SilverBulletEntity animatable) {
        return null;
    }
}
