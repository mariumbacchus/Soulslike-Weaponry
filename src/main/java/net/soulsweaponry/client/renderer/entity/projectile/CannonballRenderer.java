package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.CannonballModel;
import net.soulsweaponry.entity.projectile.Cannonball;

public class CannonballRenderer extends GeoProjectileRenderer<Cannonball> {

    public CannonballRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CannonballModel());
    }
}