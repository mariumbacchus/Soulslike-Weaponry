package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.soulsweaponry.client.model.entity.mobs.ReturningKnightModel;
import net.soulsweaponry.entity.mobs.ReturningKnight;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ReturningKnightRenderer extends GeoEntityRenderer<ReturningKnight> {

    int[] rgbColorOne = {254, 200, 203};
    int[] rgbColorTwo = {254, 254, 218};
    int[] rgbColorThree = {106, 73, 156};
    int[] rgbColorFour = {176, 253, 252};
    double[] translation = {0, 4, 0};

    public ReturningKnightRenderer(Context ctx) {
        super(ctx, new ReturningKnightModel());
        this.shadowRadius = 2.5F;
    }

    @Override
    protected float getDeathMaxRotation(ReturningKnight entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(ReturningKnight entity, float entityYaw, float partialTicks, MatrixStack stack,
                       VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        CustomDeathHandler.renderDeathLight(entity, entityYaw, partialTicks, stack, this.translation, bufferIn, packedLightIn,
                entity.deathTicks, this.rgbColorOne, this.rgbColorTwo, this.rgbColorThree, this.rgbColorFour);
    }
}