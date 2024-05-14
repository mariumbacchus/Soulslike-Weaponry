package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.MjolnirProjectileModel;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class MjolnirProjectileRenderer extends GeoProjectilesRenderer<MjolnirProjectile>{

    public MjolnirProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MjolnirProjectileModel());
    }
}
