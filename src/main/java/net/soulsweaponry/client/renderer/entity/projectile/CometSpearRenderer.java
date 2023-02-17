package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.CometSpearModel;
import net.soulsweaponry.entity.projectile.CometSpearEntity;

public class CometSpearRenderer extends GeoProjectileRenderer<CometSpearEntity> {

    public CometSpearRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CometSpearModel());
    }
}
