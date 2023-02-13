package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.SilverBulletModel;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SilverBulletRenderer extends GeoProjectilesRenderer<SilverBulletEntity>{

    public SilverBulletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SilverBulletModel());
    }
}
