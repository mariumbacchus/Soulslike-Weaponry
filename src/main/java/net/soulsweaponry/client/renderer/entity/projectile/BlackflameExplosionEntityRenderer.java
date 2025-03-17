package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.soulsweaponry.client.model.entity.projectile.BlackflameExplosionEntityModel;
import net.soulsweaponry.entity.projectile.noclip.BlackflameExplosionEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BlackflameExplosionEntityRenderer extends GeoEntityRenderer<BlackflameExplosionEntity> {

    public BlackflameExplosionEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new BlackflameExplosionEntityModel());
    }

    @Override
    public RenderLayer getRenderType(BlackflameExplosionEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    protected int getBlockLight(BlackflameExplosionEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, MatrixStack poseStack, BlackflameExplosionEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if (animatable != null) {
            float size = animatable.getRadius() / 1.85f;
            poseStack.scale(size, size, size);
        }
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}