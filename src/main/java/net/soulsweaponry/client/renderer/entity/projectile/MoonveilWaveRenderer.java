package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.MoonveilWaveModel;
import net.soulsweaponry.entity.projectile.noclip.MoonveilWave;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class MoonveilWaveRenderer extends GeoProjectileRenderer<MoonveilWave> {

    public MoonveilWaveRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new MoonveilWaveModel());
    }

    @Override
    public RenderLayer getRenderType(MoonveilWave animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    protected void applyRotations(MoonveilWave animatable, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, matrixStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(animatable.getModelRotationX()));
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, MatrixStack poseStack, MoonveilWave animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float width = Math.max(animatable.getBoundingBoxWidth() - 0.35f * animatable.getBoundingBoxWidth(), 1f);
        float height = 1.5f;
        poseStack.scale(width, height, width);
        poseStack.translate(0, animatable.getModelTranslationY(), 0);
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected int getBlockLight(MoonveilWave entity, BlockPos pos) {
        return 15;
    }
}
