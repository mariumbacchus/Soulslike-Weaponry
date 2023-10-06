package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.NightSkullModel;
import net.soulsweaponry.entity.projectile.NightSkull;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class NightSkullRenderer extends GeoProjectilesRenderer<NightSkull> {

    public NightSkullRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NightSkullModel());
    }
}