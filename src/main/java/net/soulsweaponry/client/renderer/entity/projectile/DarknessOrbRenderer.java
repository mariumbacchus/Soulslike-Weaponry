package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.soulsweaponry.client.model.entity.projectile.DarknessOrbModel;
import net.soulsweaponry.entity.projectile.noclip.DarknessOrb;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class DarknessOrbRenderer extends GeoEntityRenderer<DarknessOrb> {

    public DarknessOrbRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DarknessOrbModel());
    }

    @Override
    public RenderLayer getRenderType(DarknessOrb animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }

    @Override
    public Color getRenderColor(DarknessOrb animatable, float partialTick, int packedLight) {
        float fadeTicks = 20.0F;
        float remainingAge = animatable.getMaxAge() - (animatable.age + partialTick);
        float fade = MathHelper.clamp(remainingAge / fadeTicks, 0.0F, 1.0F);
        int alpha = Math.round(128 * fade);
        return Color.ofRGBA(255, 255, 255, alpha);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, MatrixStack poseStack, DarknessOrb animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float time = animatable.age + partialTick;
        float offset = animatable.getId() * 37.0F;
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 8.0F + offset));
        poseStack.scale(5f, 5f, 5f);
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}
