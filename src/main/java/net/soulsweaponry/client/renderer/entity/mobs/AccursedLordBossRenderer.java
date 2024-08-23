package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.soulsweaponry.client.model.entity.mobs.AccursedLordBossModel;
import net.soulsweaponry.entity.mobs.AccursedLordBoss;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AccursedLordBossRenderer extends GeoEntityRenderer<AccursedLordBoss> {

    int[] rgbColorOne = {247, 94, 94};
    int[] rgbColorTwo = {140, 1, 1};
    int[] rgbColorThree = {209, 0, 0};
    int[] rgbColorFour = {110, 1, 1};
    double[] translation = {0, 3, 0};

    public AccursedLordBossRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new AccursedLordBossModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    protected float getDeathMaxRotation(AccursedLordBoss entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(AccursedLordBoss entity, float entityYaw, float partialTicks, MatrixStack stack,
                       VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        CustomDeathHandler.renderDeathLight(entity, entityYaw, partialTicks, stack, this.translation, bufferIn, packedLightIn,
                entity.deathTicks, this.rgbColorOne, this.rgbColorTwo, this.rgbColorThree, this.rgbColorFour);
    }
}