package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.DraupnirSpearEntityModel;
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;

public class DraupnirSpearEntityRenderer extends GeoProjectileRenderer<DraupnirSpearEntity> {

    public DraupnirSpearEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DraupnirSpearEntityModel());
    }
}