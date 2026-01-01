package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.NightsEdgeModel;
import net.soulsweaponry.entity.projectile.noclip.NightsEdge;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NightsEdgeRenderer extends GeoEntityRenderer<NightsEdge> {

    public NightsEdgeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NightsEdgeModel());
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, MatrixStack poseStack, NightsEdge animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        poseStack.scale(3f, 3f, 3f);
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected void applyRotations(NightsEdge animatable, MatrixStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f - animatable.getYaw()));
    }
}
