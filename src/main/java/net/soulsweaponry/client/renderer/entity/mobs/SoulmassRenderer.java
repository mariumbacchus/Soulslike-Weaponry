package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.soulsweaponry.client.model.entity.mobs.SoulmassModel;
import net.soulsweaponry.entity.mobs.Soulmass;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulmassRenderer extends GeoEntityRenderer<Soulmass> {

    int[] rgbColorOne = {13, 2, 125};
    int[] rgbColorTwo = {20, 0, 237};
    int[] rgbColorThree = {102, 88, 252};
    int[] rgbColorFour = {13, 3, 128};
    double[] translation = {0, 2.5, 0};

    public SoulmassRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SoulmassModel());
        this.shadowRadius = 0.7F;
    }
    
    @Override
    protected float getDeathMaxRotation(Soulmass entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(Soulmass entity, float entityYaw, float partialTicks, MatrixStack stack,
            VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        CustomDeathHandler.renderDeathLight(entity, entityYaw, partialTicks, stack, this.translation, bufferIn, packedLightIn, 
            entity.deathTicks, this.rgbColorOne, this.rgbColorTwo, this.rgbColorThree, this.rgbColorFour);
    }
}
