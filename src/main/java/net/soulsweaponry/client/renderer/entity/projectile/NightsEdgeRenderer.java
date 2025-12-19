package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.NightsEdgeModel;
import net.soulsweaponry.entity.projectile.NightsEdge;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NightsEdgeRenderer extends GeoEntityRenderer<NightsEdge> {

    public NightsEdgeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NightsEdgeModel());
    }

    @Override
    public void renderFinal(MatrixStack poseStack, NightsEdge animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int renderColor) {
        poseStack.scale(3f, 3f, 3f);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f - animatable.getYaw()));
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, renderColor);
    }
}
