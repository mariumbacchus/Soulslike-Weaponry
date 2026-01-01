package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.projectile.GrowingFireballModel;
import net.soulsweaponry.entity.projectile.GrowingFireball;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GrowingFireballRenderer extends GeoEntityRenderer<GrowingFireball> {

    public GrowingFireballRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GrowingFireballModel());
    }

    @Override
    public RenderLayer getRenderType(GrowingFireball animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, MatrixStack poseStack, GrowingFireball animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float r = animatable.getRadius();
        poseStack.scale(r, r, r);
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}