package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.MoonknightModel;
import net.soulsweaponry.entity.mobs.boss.Moonknight;
import net.soulsweaponry.registry.ParticleRegistry;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import java.awt.Color;

public class MoonknightRenderer extends GeoEntityRendererWithDeathlight<Moonknight> {

    private static final Color COLOR_ONE = new Color(254, 200, 203);
    private static final Color COLOR_TWO = new Color(254, 254, 218);
    private static final Color COLOR_THREE = new Color(106, 73, 156);
    private static final Color COLOR_FOUR = new Color(176, 253, 252);
    private static final Vec3d DEATH_LIGHT_TRANSLATION = new Vec3d(0, 4, 0);
    public static final Identifier CRYSTAL_BEAM_TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/entity/core_beam.png");
    private static final RenderLayer CRYSTAL_BEAM_LAYER = RenderLayer.getEntitySmoothCutout(CRYSTAL_BEAM_TEXTURE);
    private int currentTick = -1;
    private static final int FULLBRIGHT_LIGHT = 0xF000F0;

    public MoonknightRenderer(Context ctx) {
        super(ctx, new MoonknightModel());
        this.shadowRadius = 2.5F;
    }

    @Override
    public boolean shouldRender(Moonknight entity, Frustum frustum, double x, double y, double z) {
        return entity.getCanBeam() || super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public void render(Moonknight entity, float entityYaw, float partialTicks, MatrixStack stack,
                       VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        BlockPos blockPos = entity.getBeamLocation();
        if (entity.getCanBeam() && blockPos != null && !entity.isDead()) {
            float yOffset = 6f;
            float exactTarget = MathHelper.lerp(partialTicks, entity.prevBeamHeight, entity.getBeamHeight());
            if (exactTarget < entity.renderBeamHeight) {
                entity.renderBeamHeight = exactTarget;
            }
            entity.renderBeamHeight += (exactTarget - entity.renderBeamHeight) * 0.1f;

            float m = (float)blockPos.getX() + 0.5f;
            float n = blockPos.getY() + entity.renderBeamHeight;
            float o = (float)blockPos.getZ() + 0.5f;
            float p = (float)((double)m - entity.getX());
            float q = (float)((double)n - entity.getY());
            float r = (float)((double)o - entity.getZ());
            stack.translate(p, q, r);
            renderCoreBeam(-p, -q + yOffset, -r, partialTicks, entity.age, stack, bufferIn, packedLightIn);
        }
    }

    private void renderCoreBeam(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider consumers, int packedLight) {
        renderBeamRing(dx, dy, dz, tickDelta, age, matrices, consumers, packedLight, 1.0f, 255, 255, 255, 255);
        renderBeamRing(dx, dy, dz, tickDelta, age, matrices, consumers, packedLight, 0.8f, 200, 200, 255, 200);
        renderBeamRing(dx, dy, dz, tickDelta, age, matrices, consumers, packedLight, 0.4f, 255, 255, 255, 128);
    }

    private static void renderBeamRing(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider consumers, int packedLight, float radiusScale, int r, int g, int b, int a) {
        VertexConsumer vb = consumers.getBuffer(CRYSTAL_BEAM_LAYER);
        float xzLen = MathHelper.sqrt(dx*dx + dz*dz);
        float vecLen = MathHelper.sqrt(dx*dx + dy*dy + dz*dz);

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float)(-Math.atan2(dz,dx)) - 1.5707964f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float)(-Math.atan2(xzLen,dy)) - 1.5707964f));

        float vMin = 0.0f - ((age + tickDelta) * -0.05f);
        float vMax = vecLen/32.0f - ((age + tickDelta) * -0.05f);
        float prevX = 0, prevY = 0, prevU = 0;
        for (int i = 1; i <= 8; i++) {
            float theta = i * (MathHelper.PI * 2F) / 8F;
            float cx = MathHelper.sin(theta) * 0.75f * radiusScale;
            float cy = MathHelper.cos(theta) * 0.75f * radiusScale;
            float u = (float)i / 8F;
            vb.vertex(matrices.peek().getPositionMatrix(), prevX*radiusScale, prevY*radiusScale, 0)
                    .color(r, g, b, a).texture(prevU, vMin).overlay(OverlayTexture.DEFAULT_UV).light(FULLBRIGHT_LIGHT)
                    .normal(matrices.peek(), 0, -1, 0);

            vb.vertex(matrices.peek().getPositionMatrix(), prevX, prevY, vecLen)
                    .color(r, g, b, a).texture(prevU, vMax).overlay(OverlayTexture.DEFAULT_UV).light(FULLBRIGHT_LIGHT)
                    .normal(matrices.peek(), 0, -1, 0);

            vb.vertex(matrices.peek().getPositionMatrix(), cx, cy, vecLen)
                    .color(r, g, b, a).texture(u, vMax).overlay(OverlayTexture.DEFAULT_UV).light(FULLBRIGHT_LIGHT)
                    .normal(matrices.peek(), 0, -1, 0);

            vb.vertex(matrices.peek().getPositionMatrix(), cx*radiusScale, cy*radiusScale, 0)
                    .color(r, g, b, a).texture(u, vMin).overlay(OverlayTexture.DEFAULT_UV).light(FULLBRIGHT_LIGHT)
                    .normal(matrices.peek(), 0, -1, 0);

            prevX = cx; prevY = cy; prevU = u;
        }
        matrices.pop();
    }

    @Override
    public void renderFinal(MatrixStack poseStack, Moonknight animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int color) {
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
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, color);
    }

    @Override
    protected int getDeathTicks(Moonknight entity) {
        return entity.getDeathTicks();
    }

    @Override
    protected Vec3d getDeathLightTranslation() {
        return DEATH_LIGHT_TRANSLATION;
    }

    @Override
    protected Color getDeathLightColorOne() {
        return COLOR_ONE;
    }

    @Override
    protected Color getDeathLightColorTwo() {
        return COLOR_TWO;
    }

    @Override
    protected Color getDeathLightColorThree() {
        return COLOR_THREE;
    }

    @Override
    protected Color getDeathLightColorFour() {
        return COLOR_FOUR;
    }
}