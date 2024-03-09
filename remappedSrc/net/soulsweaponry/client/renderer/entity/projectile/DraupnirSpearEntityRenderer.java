package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.DraupnirSpearEntityModel;
import net.soulsweaponry.client.model.entity.projectile.MjolnirProjectileModel;
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;

public class DraupnirSpearEntityRenderer extends GeoProjectileRenderer<DraupnirSpearEntity> {

    public DraupnirSpearEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DraupnirSpearEntityModel());
    }
}
