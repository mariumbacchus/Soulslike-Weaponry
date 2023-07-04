package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.projectile.VerticalSunlightProjectileModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;

public class VerticalSunlightProjectileRenderer extends GeoProjectileRenderer<MoonlightProjectile> {

    public VerticalSunlightProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new VerticalSunlightProjectileModel());
    }

    @Override
    public RenderLayer getRenderType(MoonlightProjectile animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}