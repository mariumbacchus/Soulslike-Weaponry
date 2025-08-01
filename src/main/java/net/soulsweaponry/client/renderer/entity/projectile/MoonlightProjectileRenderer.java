package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import software.bernie.geckolib.model.GeoModel;

public class MoonlightProjectileRenderer extends GeoProjectileRenderer<MoonlightProjectile> {

    public MoonlightProjectileRenderer(EntityRendererFactory.Context renderManager, GeoModel<MoonlightProjectile> model) {
        super(renderManager, model);
    }

    @Override
    protected void applyRotations(MoonlightProjectile animatable, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, matrixStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(animatable.getModelRotation()));
    }

    @Override
    public RenderLayer getRenderType(MoonlightProjectile animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}
