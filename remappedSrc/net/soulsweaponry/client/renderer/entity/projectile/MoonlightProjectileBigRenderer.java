package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.MoonlightProjectileBigModel;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class MoonlightProjectileBigRenderer extends GeoProjectileRenderer<MoonlightProjectile> {

    public MoonlightProjectileBigRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MoonlightProjectileBigModel());
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
}
