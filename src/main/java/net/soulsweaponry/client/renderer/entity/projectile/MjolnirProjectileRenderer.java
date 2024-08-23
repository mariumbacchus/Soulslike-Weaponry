package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.MjolnirProjectileModel;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;

public class MjolnirProjectileRenderer extends GeoProjectileRenderer<MjolnirProjectile> {

    public MjolnirProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MjolnirProjectileModel());
    }
}