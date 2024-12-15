package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.MoonknightModel;
import net.soulsweaponry.entity.mobs.Moonknight;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.CustomDeathHandler;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MoonknightRenderer extends GeoEntityRenderer<Moonknight> {

    int[] rgbColorOne = {254, 200, 203};
    int[] rgbColorTwo = {254, 254, 218};
    int[] rgbColorThree = {106, 73, 156};
    int[] rgbColorFour = {176, 253, 252};
    double[] translation = {0, 4, 0};
    public static final Identifier CRYSTAL_BEAM_TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/entity/core_beam.png");
    private static final RenderLayer CRYSTAL_BEAM_LAYER = RenderLayer.getEntitySmoothCutout(CRYSTAL_BEAM_TEXTURE);
    private int currentTick = -1;
    
    public MoonknightRenderer(Context ctx) {
        super(ctx, new MoonknightModel());
        this.shadowRadius = 2.5F;
    }

    @Override
    protected float getDeathMaxRotation(Moonknight entityLivingBaseIn) {
        return 0f;
    }

    @Override
    public void render(Moonknight entity, float entityYaw, float partialTicks, MatrixStack stack,
            VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        CustomDeathHandler.renderDeathLight(entity, entityYaw, partialTicks, stack, this.translation, bufferIn, packedLightIn, 
            entity.deathTicks, this.rgbColorOne, this.rgbColorTwo, this.rgbColorThree, this.rgbColorFour);

        BlockPos blockPos = entity.getBeamLocation();
        if (entity.getCanBeam() && blockPos != null && !entity.isDead()) {
            float yOffset = 4f;
            if (entity.getIncreasingBeamHeight()) {
                entity.setBeamHeight(0.0325f + entity.getBeamHeight());
            } else {
                entity.setBeamHeight(0f);
            }
            float m = (float)blockPos.getX() + 0.5f;
            float n = (float)blockPos.getY() + entity.getBeamHeight();
            float o = (float)blockPos.getZ() + 0.5f;
            float p = (float)((double)m - entity.getX());
            float q = (float)((double)n - entity.getY());
            float r = (float)((double)o - entity.getZ());
            stack.translate(p, q, r);
            renderCoreBeam(-p, -q + yOffset, -r, partialTicks, entity.age, stack, bufferIn, packedLightIn);
        }
    }

    private static void renderCoreBeam(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float xzLength = MathHelper.sqrt(dx * dx + dz * dz);
        float vecLength = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
        matrices.push();
        matrices.translate(0.0f, 2.0f, 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float)(-Math.atan2(dz, dx)) - 1.5707964f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float)(-Math.atan2(xzLength, dy)) - 1.5707964f));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(CRYSTAL_BEAM_LAYER);
        float h = 0.0f - ((float)age + tickDelta) * 0.01f;
        float i = vecLength / 32.0f - ((float)age + tickDelta) * 0.01f;
        float k = 0.0f;
        float l = 0.75f;
        float m = 0.0f;
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        for (int n = 1; n <= 8; ++n) {
            float o = MathHelper.sin((float)n * ((float)Math.PI * 2) / 8.0f) * 0.75f;
            float p = MathHelper.cos((float)n * ((float)Math.PI * 2) / 8.0f) * 0.75f;
            float q = (float)n / 8.0f;
            vertexConsumer.vertex(matrix4f, k * 0.2f, l * 0.2f, 0.0f).color(255, 255, 255, 255).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(matrix3f, 0.0f, -1f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, k, l, vecLength).color(255, 255, 255, 255).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(matrix3f, 0.0f, -1f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o, p, vecLength).color(255, 255, 255, 255).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(matrix3f, 0.0f, -1f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, o * 0.2f, p * 0.2f, 0.0f).color(255, 255, 255, 255).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(matrix3f, 0.0f, -1f, 0.0f).next();
            k = o;
            l = p;
            m = q;
        }
        matrices.pop();
    }

    @Override
    public void renderFinal(MatrixStack poseStack, Moonknight animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (animatable.isPhaseTwo() && !animatable.isDead() && (animatable.isSwordCharging())) {
            if (this.currentTick < 0 || this.currentTick != animatable.age) {
                this.currentTick = animatable.age;
                this.model.getBone("particle1").ifPresent(sword1 ->
                        this.model.getBone("particle2").ifPresent(sword2 -> {
                            Random rand = animatable.getRandom();
                            Vector3d pos1 = sword1.getWorldPosition();
                            Vector3d pos2 = sword2.getWorldPosition();
                            int segments = 8;
                            for (int i = 0; i <= segments; i++) {
                                double t = i / (double) segments;
                                double interpolatedX = pos1.x() + t * (pos2.x() - pos1.x());
                                double interpolatedY = pos1.y() + t * (pos2.y() - pos1.y());
                                double interpolatedZ = pos1.z() + t * (pos2.z() - pos1.z());
                                for (int j = 0; j < 4; j++) {
                                    animatable.getEntityWorld().addParticle(
                                            ParticleRegistry.NIGHTFALL_PARTICLE,
                                            interpolatedX,
                                            interpolatedY,
                                            interpolatedZ,
                                            (rand.nextDouble() - 0.5D) * 0.25f,
                                            (rand.nextDouble() - 0.5D) * 0.25f,
                                            (rand.nextDouble() - 0.5D) * 0.25f
                                    );
                                }
                            }
                        })
                );
            }
        }
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
