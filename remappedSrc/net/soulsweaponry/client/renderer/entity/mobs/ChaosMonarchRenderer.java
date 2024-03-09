package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.soulsweaponry.client.model.entity.mobs.ChaosMonarchModel;
import net.soulsweaponry.entity.mobs.ChaosMonarch;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ChaosMonarchRenderer extends GeoEntityRenderer<ChaosMonarch> {

    int[] rgbColorOne = {255, 22, 206};
    int[] rgbColorTwo = {160, 14, 131};
    int[] rgbColorThree = {160, 102, 149};
    int[] rgbColorFour = {255, 163, 236};
    double[] translation = {0, 4, 0};

    public ChaosMonarchRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChaosMonarchModel());
        this.shadowRadius = 0.7F;
    }
    
    @Override
    protected float getDeathMaxRotation(ChaosMonarch entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(ChaosMonarch entity, float entityYaw, float partialTicks, MatrixStack stack,
            VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        CustomDeathHandler.renderDeathLight(entity, entityYaw, partialTicks, stack, this.translation, bufferIn, packedLightIn, 
            entity.deathTicks, this.rgbColorOne, this.rgbColorTwo, this.rgbColorThree, this.rgbColorFour);
    }
}
