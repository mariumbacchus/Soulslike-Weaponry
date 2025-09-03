package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.soulsweaponry.client.model.entity.projectile.MjolnirProjectileModel;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class MjolnirProjectileRenderer extends GeoProjectileRenderer<MjolnirProjectile> {

    public MjolnirProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MjolnirProjectileModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}