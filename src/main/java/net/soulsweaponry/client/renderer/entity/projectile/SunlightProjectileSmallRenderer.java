package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.projectile.SunlightProjectileSmallModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SunlightProjectileSmallRenderer extends GeoProjectilesRenderer<MoonlightProjectile> {

    public SunlightProjectileSmallRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SunlightProjectileSmallModel());
    }

    @Override
    public RenderLayer getRenderType(MoonlightProjectile animatable, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}