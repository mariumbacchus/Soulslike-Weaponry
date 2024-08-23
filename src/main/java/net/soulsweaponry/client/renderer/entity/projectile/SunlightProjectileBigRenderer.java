package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.SunlightProjectileBigModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;

public class SunlightProjectileBigRenderer extends GeoProjectileRenderer<MoonlightProjectile> {

    public SunlightProjectileBigRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SunlightProjectileBigModel());
    }

    @Override
    protected void applyRotations(MoonlightProjectile animatable, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(animatable, matrixStack, ageInTicks, rotationYaw, partialTick);
        switch (animatable.getRotateState()) {
            case SWIPE_FROM_LEFT -> matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
            case SWIPE_FROM_RIGHT -> matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-45.0F));
            default -> {
            }
        }
    }

    @Override
    public RenderLayer getRenderType(MoonlightProjectile animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}