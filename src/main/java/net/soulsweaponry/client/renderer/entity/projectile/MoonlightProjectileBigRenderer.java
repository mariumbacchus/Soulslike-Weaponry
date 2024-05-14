package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.MoonlightProjectileBigModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class MoonlightProjectileBigRenderer extends GeoProjectilesRenderer<MoonlightProjectile>{

    public MoonlightProjectileBigRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MoonlightProjectileBigModel());
    }
}
