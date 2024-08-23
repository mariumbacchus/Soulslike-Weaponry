package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.SilverBulletModel;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;

public class SilverBulletRenderer extends GeoProjectileRenderer<SilverBulletEntity> {

    public SilverBulletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SilverBulletModel());
    }
}