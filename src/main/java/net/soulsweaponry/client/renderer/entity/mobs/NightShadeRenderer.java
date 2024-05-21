package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.GeoEntityRendererFixed;
import net.soulsweaponry.client.model.entity.mobs.NightShadeModel;
import net.soulsweaponry.entity.mobs.NightShade;
import net.soulsweaponry.util.CustomDeathHandler;
import org.jetbrains.annotations.Nullable;

public class NightShadeRenderer extends GeoEntityRendererFixed<NightShade> {

    int[] rgbColorOne = {13, 2, 125};
    int[] rgbColorTwo = {20, 0, 237};
    int[] rgbColorThree = {102, 88, 252};
    int[] rgbColorFour = {13, 3, 128};
    double[] translation = {0, 2.5, 0};
    
    public NightShadeRenderer(Context ctx) {
        super(ctx, new NightShadeModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    protected float getDeathMaxRotation(NightShade entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(NightShade entity, float entityYaw, float partialTicks, MatrixStack stack,
            VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        if (!entity.isCopy()) {
            CustomDeathHandler.renderDeathLight(entity, entityYaw, partialTicks, stack, this.translation, bufferIn, packedLightIn,
                    entity.deathTicks, this.rgbColorOne, this.rgbColorTwo, this.rgbColorThree, this.rgbColorFour);
        }
    }

    @Override
    public RenderLayer getRenderType(NightShade animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(this.getTexture(animatable));
    }
}
