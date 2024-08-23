package net.soulsweaponry.client.renderer.entity.projectile;

import net.soulsweaponry.client.model.entity.projectile.VerticalMoonlightProjectileModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class VerticalMoonlightProjectileRenderer extends GeoProjectileRenderer<MoonlightProjectile> {

    public VerticalMoonlightProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new VerticalMoonlightProjectileModel());
    }
}