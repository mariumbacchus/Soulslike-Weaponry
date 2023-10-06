package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import net.soulsweaponry.client.model.entity.projectile.NightsEdgeModel;
import net.soulsweaponry.entity.projectile.NightsEdge;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NightsEdgeRenderer extends GeoEntityRenderer<NightsEdge> {

    public NightsEdgeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NightsEdgeModel());
    }

    @Override
    public void render(NightsEdge entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.scale(3f, 3f, 3f);
        poseStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f - entity.getYaw()));
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}