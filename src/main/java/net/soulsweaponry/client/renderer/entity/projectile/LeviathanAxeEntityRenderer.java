package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.LeviathanAxeEntityModel;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class LeviathanAxeEntityRenderer extends GeoProjectileRenderer<LeviathanAxeEntity> {

    public LeviathanAxeEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LeviathanAxeEntityModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    protected void applyRotations(LeviathanAxeEntity entity, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, matrixStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        boolean noClip = entity.isNoClip();
        if (!entity.isInGround() || noClip) {
            float totalTicks = entity.age + partialTick;
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(totalTicks * 60 * (noClip ? 1 : -1)));
        }
    }
}
