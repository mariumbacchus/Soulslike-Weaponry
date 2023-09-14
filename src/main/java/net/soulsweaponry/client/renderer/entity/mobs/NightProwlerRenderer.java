package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.soulsweaponry.client.model.entity.mobs.NightProwlerModel;
import net.soulsweaponry.entity.mobs.NightProwler;
import net.soulsweaponry.util.CustomDeathHandler;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NightProwlerRenderer extends GeoEntityRenderer<NightProwler> {

    int[] rgbColorOne = {54, 122, 156};
    int[] rgbColorTwo = {147, 188, 210};
    int[] rgbColorThree = {221, 255, 254};
    int[] rgbColorFour = {235, 185, 232};
    double[] translation = {0, 4, 0};

    public NightProwlerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NightProwlerModel());
        this.shadowRadius = 1F;
    }

    @Override
    protected float getDeathMaxRotation(NightProwler entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(NightProwler entity, float entityYaw, float partialTicks, MatrixStack stack,
                       VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        CustomDeathHandler.renderDeathLight(entity, entityYaw, partialTicks, stack, this.translation, bufferIn, packedLightIn,
                entity.deathTicks, this.rgbColorOne, this.rgbColorTwo, this.rgbColorThree, this.rgbColorFour);
    }
}
