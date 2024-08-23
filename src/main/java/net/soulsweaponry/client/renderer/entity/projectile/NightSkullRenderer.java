package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.NightSkullModel;
import net.soulsweaponry.entity.projectile.NightSkull;

public class NightSkullRenderer extends GeoProjectileRenderer<NightSkull> {

    public NightSkullRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NightSkullModel());
    }
}