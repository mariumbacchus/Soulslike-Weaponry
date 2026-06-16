package net.soulsweaponry.util;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleHandler;
import org.joml.Matrix4f;

import java.awt.Color;
import java.util.List;

public class CustomDeathHandler {

    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);

    public static void deathExplosionEvent(World world, Vec3d pos, SoundEvent sound, List<ParticleEffect> particles) {
        if (!world.isClient) {
            ParticleHandler.particleSphereList(world, 1000, pos.getX(), pos.getY(), pos.getZ(), particles, 1f);
        }
        world.playSound(null, BlockPos.ofFloored(pos), sound, SoundCategory.HOSTILE, 1f, 1f);
    }

    public static void renderDeathLight(LivingEntity entity, float entityYaw, float partialTicks, MatrixStack stack, Vec3d translation, VertexConsumerProvider bufferIn, int packedLightIn, int deathTicks, Color colorOne, Color colorTwo, Color colorThree, Color colorFour) {
        if (deathTicks > 0) {
            float l = ((float)deathTicks + partialTicks) / 200.0f;
            float m = Math.min(l > 0.8f ? (l - 0.8f) / 0.2f : 0.0f, 1.0f);
            Random random = Random.create(432L);
            VertexConsumer vertexConsumer4 = bufferIn.getBuffer(RenderLayer.getLightning());
            stack.push();
            stack.translate(translation.x, translation.y, translation.z);
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
                CustomDeathHandler.renderLight_1(vertexConsumer4, matrix4f, q, colorOne);
                CustomDeathHandler.renderLight_2(vertexConsumer4, matrix4f, o, p, colorTwo);
                CustomDeathHandler.renderLight_3(vertexConsumer4, matrix4f, o, p, colorThree);
                CustomDeathHandler.renderLight_1(vertexConsumer4, matrix4f, q, colorOne);
                CustomDeathHandler.renderLight_3(vertexConsumer4, matrix4f, o, p, colorThree);
                CustomDeathHandler.renderLight_4(vertexConsumer4, matrix4f, o, p, colorFour);
                CustomDeathHandler.renderLight_1(vertexConsumer4, matrix4f, q, colorOne);
                CustomDeathHandler.renderLight_4(vertexConsumer4, matrix4f, o, p, colorFour);
                CustomDeathHandler.renderLight_2(vertexConsumer4, matrix4f, o, p, colorTwo);
            }
            stack.pop();
        }
    }

    private static void renderLight_1(VertexConsumer vertices, Matrix4f matrix, int alpha, Color color) {
        vertices.vertex(matrix, 0.0f, 0.0f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    private static void renderLight_2(VertexConsumer vertices, Matrix4f matrix, float y, float x, Color color) {
        vertices.vertex(matrix, -HALF_SQRT_3 * x, y, -0.5f * x).color(color.getRed(), color.getGreen(), color.getBlue(), 0);
    }

    private static void renderLight_3(VertexConsumer vertices, Matrix4f matrix, float y, float x, Color color) {
        vertices.vertex(matrix, HALF_SQRT_3 * x, y, -0.5f * x).color(color.getRed(), color.getGreen(), color.getBlue(), 0);
    }

    private static void renderLight_4(VertexConsumer vertices, Matrix4f matrix, float y, float z, Color color) {
        vertices.vertex(matrix, 0.0f, y, z).color(color.getRed(), color.getGreen(), color.getBlue(), 0);
    }
}