package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.projectile.SunlightProjectileSmallModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;

public class SunlightProjectileSmallRenderer extends GeoProjectileRenderer<MoonlightProjectile> {

    public SunlightProjectileSmallRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SunlightProjectileSmallModel());
    }

    @Override
    public RenderLayer getRenderType(MoonlightProjectile animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}
