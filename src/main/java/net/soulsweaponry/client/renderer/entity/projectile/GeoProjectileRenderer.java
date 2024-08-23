package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GeoProjectileRenderer<T extends PersistentProjectileEntity & GeoEntity> extends GeoEntityRenderer<T> {

    public GeoProjectileRenderer(EntityRendererFactory.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    @Override
    protected void applyRotations(T animatable, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(animatable, matrixStack, ageInTicks, rotationYaw, partialTick);
        if (animatable != null) {
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(rotationYaw, animatable.prevYaw, animatable.getYaw()) + 90.0F));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(rotationYaw, animatable.prevPitch, animatable.getPitch())));
            float s = (float)animatable.shake - rotationYaw;
            if (s > 0.0F) {
                float t = -MathHelper.sin(s * 3.0F) * s;
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t));
            }
        }
    }
}