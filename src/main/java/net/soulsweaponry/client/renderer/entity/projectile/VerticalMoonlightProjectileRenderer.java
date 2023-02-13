package net.soulsweaponry.client.renderer.entity.projectile;

import net.soulsweaponry.client.model.entity.projectile.VerticalMoonlightProjectileModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class VerticalMoonlightProjectileRenderer extends GeoProjectilesRenderer<MoonlightProjectile>{

    public VerticalMoonlightProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new VerticalMoonlightProjectileModel());
    }
}
