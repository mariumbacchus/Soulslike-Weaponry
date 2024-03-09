package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.MoonlightProjectileModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;

public class MoonlightProjectileRenderer extends GeoProjectileRenderer<MoonlightProjectile> {

    public MoonlightProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MoonlightProjectileModel());
    }
}
