package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.MoonlightProjectileModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class MoonlightProjectileRenderer extends GeoProjectilesRenderer<MoonlightProjectile>{

    public MoonlightProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MoonlightProjectileModel());
    }
}
