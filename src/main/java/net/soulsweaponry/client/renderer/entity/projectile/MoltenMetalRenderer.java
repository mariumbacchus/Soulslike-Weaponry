package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.projectile.MoltenMetalModel;
import net.soulsweaponry.entity.projectile.noclip.MoltenMetal;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MoltenMetalRenderer extends GeoEntityRenderer<MoltenMetal> {

    public MoltenMetalRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MoltenMetalModel());
        this.shadowRadius = 0f;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, MatrixStack poseStack, MoltenMetal animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if (animatable != null) {
            float width = animatable.getBoundingBoxWidth();
            float height = animatable.getBoundingBoxHeight() + 0.8f;
            poseStack.scale(width, height, width);
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public RenderLayer getRenderType(MoltenMetal animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucentEmissive(texture);
    }
}