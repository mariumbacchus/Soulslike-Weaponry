package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.soulsweaponry.client.model.entity.projectile.HolyMoonlightPillarModel;
import net.soulsweaponry.entity.projectile.noclip.HolyMoonlightPillar;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HolyMoonlightPillarRenderer extends GeoEntityRenderer<HolyMoonlightPillar> {

    public HolyMoonlightPillarRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HolyMoonlightPillarModel());
    }

    @Override
    public RenderLayer getRenderType(HolyMoonlightPillar animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    protected int getBlockLight(HolyMoonlightPillar entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, MatrixStack poseStack, HolyMoonlightPillar animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if (animatable != null) {
            float size = animatable.getRadius() / 1.85f;
            poseStack.scale(size, size, size);
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}