package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.soulsweaponry.util.IAnimatedDeath;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;

public abstract class GeoEntityRendererDeathLight<T extends LivingEntity & IAnimatedDeath & GeoAnimatable> extends GeoEntityRenderer<T> {

    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);
    private final Color primaryColor;
    private final Color secondaryColor;
    private final Color thirdColor;
    private final Color fourthColor;
    private final float yTranslation;

    /**
     * Geo entity that renders a death light like how the Ender Dragon does so when it dies.
     * @param renderManager render context
     * @param model entity model object
     * @param primaryColor primary deathlight color
     * @param secondaryColor secondary deathlight color
     * @param thirdColor third deathlight color
     * @param fourthColor fourth deathlight color
     * @param yTranslation y translation of the death light center
     */
    public GeoEntityRendererDeathLight(EntityRendererFactory.Context renderManager, GeoModel<T> model, Color primaryColor, Color secondaryColor, Color thirdColor, Color fourthColor, float yTranslation) {
        super(renderManager, model);
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.thirdColor = thirdColor;
        this.fourthColor = fourthColor;
        this.yTranslation = yTranslation;
    }

    @Override
    protected float getDeathMaxRotation(T animatable, float partialTick) {
        return 0f;
    }

    @Override
    public void renderFinal(MatrixStack poseStack, T animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int renderColor) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, renderColor);
        if (!animatable.isAlive()) {
            renderDeathLight(partialTick, poseStack, bufferSource, animatable.getDeathTicks());
        }
    }

    public void renderDeathLight(float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int deathTicks) {
        if (deathTicks > 0) {
            float l = ((float)deathTicks + partialTicks) / 200.0f;
            float m = Math.min(l > 0.8f ? (l - 0.8f) / 0.2f : 0.0f, 1.0f);
            Random random = Random.create(432L);
            VertexConsumer vertexConsumer4 = bufferIn.getBuffer(RenderLayer.getLightning());
            stack.push();
            stack.translate(0, this.yTranslation, 0);
            for(int n = 0; (float)n < (l + l * l) / 2.0F * 60.0F; ++n) {
                stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0f));
                stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0f));
                stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0f));
                stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0f));
                stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0f));
                stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0f + l * 90.0f));
                float o = random.nextFloat() * 20.0f + 5.0f + m * 10.0f;
                float p = random.nextFloat() * 2.0f + 1.0f + m * 2.0f;
                Matrix4f matrix4f = stack.peek().getPositionMatrix();
                int q = (int)(255.0f * (1.0f - m));
                renderLight_1(vertexConsumer4, matrix4f, q, this.primaryColor);
                renderLight_2(vertexConsumer4, matrix4f, o, p, this.secondaryColor);
                renderLight_3(vertexConsumer4, matrix4f, o, p, this.thirdColor);
                renderLight_1(vertexConsumer4, matrix4f, q, this.primaryColor);
                renderLight_3(vertexConsumer4, matrix4f, o, p, this.thirdColor);
                renderLight_4(vertexConsumer4, matrix4f, o, p, this.fourthColor);
                renderLight_1(vertexConsumer4, matrix4f, q, this.primaryColor);
                renderLight_4(vertexConsumer4, matrix4f, o, p, this.fourthColor);
                renderLight_2(vertexConsumer4, matrix4f, o, p, this.secondaryColor);
            }
            stack.pop();
        }
    }

    private static void renderLight_1(VertexConsumer vertices, Matrix4f matrix, int alpha, Color rgbColors) {
        vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(rgbColors.getRed(), rgbColors.getGreen(), rgbColors.getBlue(), alpha);
    }

    private static void renderLight_2(VertexConsumer vertices, Matrix4f matrix, float y, float x, Color rgbColors) {
        vertices.vertex(matrix, -HALF_SQRT_3 * x, y, -0.5f * x).color(rgbColors.getRed(), rgbColors.getGreen(), rgbColors.getBlue(), 0);
    }

    private static void renderLight_3(VertexConsumer vertices, Matrix4f matrix, float y, float x, Color rgbColors) {
        vertices.vertex(matrix, HALF_SQRT_3 * x, y, -0.5f * x).color(rgbColors.getRed(), rgbColors.getGreen(), rgbColors.getBlue(), 0);
    }

    private static void renderLight_4(VertexConsumer vertices, Matrix4f matrix, float y, float z, Color rgbColors) {
        vertices.vertex(matrix, 0.0f, y, z).color(rgbColors.getRed(), rgbColors.getGreen(), rgbColors.getBlue(), 0);
    }
}
