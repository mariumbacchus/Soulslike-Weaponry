package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.soulsweaponry.entity.projectile.noclip.FrozenLightning;
import org.joml.Matrix4f;

public class FrozenLightningRenderer extends EntityRenderer<FrozenLightning> {

    public FrozenLightningRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(FrozenLightning entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        for (long seed : entity.seeds) {
            float[] xCoords = new float[8];
            float[] zCoords = new float[8];
            float currentX = 0.0F;
            float currentZ = 0.0F;

            Random random = Random.create(seed);
            for (int segmentIndex = 7; segmentIndex >= 0; segmentIndex--) {
                xCoords[segmentIndex] = currentX;
                zCoords[segmentIndex] = currentZ;
                currentX += (float) (random.nextInt(11) - 5);
                currentZ += (float) (random.nextInt(11) - 5);
            }
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLightning());
            Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
            for (int layer = 0; layer < 4; layer++) {
                Random layerRandom = Random.create(seed);
                float red;
                float green;
                float blue;
                if (layer >= 2) {
                    red = 0.2F;
                    green = 0.45F;
                    blue = 0.5F;
                } else {
                    red = 0.45F;
                    green = 0.92F;
                    blue = 1f;
                }
                for (int branchDepth = 0; branchDepth < 3; branchDepth++) {
                    int startSegment = 7;
                    int endSegment = 0;
                    if (branchDepth > 0) {
                        startSegment = 7 - branchDepth;
                        endSegment = startSegment - 2;
                    }
                    float prevXOffset = xCoords[startSegment] - currentX;
                    float prevZOffset = zCoords[startSegment] - currentZ;

                    for (int segment = startSegment; segment >= endSegment; segment--) {
                        float currentXOffset = prevXOffset;
                        float currentZOffset = prevZOffset;
                        if (branchDepth == 0) {
                            prevXOffset += (float) (layerRandom.nextInt(11) - 5);
                            prevZOffset += (float) (layerRandom.nextInt(11) - 5);
                        } else {
                            prevXOffset += (float) (layerRandom.nextInt(31) - 15);
                            prevZOffset += (float) (layerRandom.nextInt(31) - 15);
                        }
                        float thicknessStart = 0.1F + (float) layer * 0.2F;
                        if (branchDepth == 0) {
                            thicknessStart *= (float) segment * 0.1F + 1.0F;
                        }
                        float thicknessEnd = 0.1F + (float) layer * 0.2F;
                        if (branchDepth == 0) {
                            thicknessEnd *= ((float) segment - 1.0F) * 0.1F + 1.0F;
                        }

                        drawBranch(positionMatrix, vertexConsumer, prevXOffset, prevZOffset, segment, currentXOffset, currentZOffset,
                                red, green, blue, thicknessStart, thicknessEnd,
                                false, false, true, false);
                        drawBranch(positionMatrix, vertexConsumer, prevXOffset, prevZOffset, segment, currentXOffset, currentZOffset,
                                red, green, blue, thicknessStart, thicknessEnd,
                                true, false, true, true);
                        drawBranch(positionMatrix, vertexConsumer, prevXOffset, prevZOffset, segment, currentXOffset, currentZOffset,
                                red, green, blue, thicknessStart, thicknessEnd,
                                true, true, false, true);
                        drawBranch(positionMatrix, vertexConsumer, prevXOffset, prevZOffset, segment, currentXOffset, currentZOffset,
                                red, green, blue, thicknessStart, thicknessEnd,
                                false, true, false, false);
                    }
                }
            }
        }
    }

    private static void drawBranch(
            Matrix4f matrix,
            VertexConsumer buffer,
            float x1,
            float z1,
            int y,
            float x2,
            float z2,
            float red,
            float green,
            float blue,
            float offset2,
            float offset1,
            boolean shiftEast1,
            boolean shiftSouth1,
            boolean shiftEast2,
            boolean shiftSouth2
    ) {
        buffer.vertex(matrix, x1 + (shiftEast1 ? offset1 : -offset1), (float)(y * 16), z1 + (shiftSouth1 ? offset1 : -offset1)).color(red, green, blue, 0.3F).next();
        buffer.vertex(matrix, x2 + (shiftEast1 ? offset2 : -offset2), (float)((y + 1) * 16), z2 + (shiftSouth1 ? offset2 : -offset2))
                .color(red, green, blue, 0.3F)
                .next();
        buffer.vertex(matrix, x2 + (shiftEast2 ? offset2 : -offset2), (float)((y + 1) * 16), z2 + (shiftSouth2 ? offset2 : -offset2))
                .color(red, green, blue, 0.3F)
                .next();
        buffer.vertex(matrix, x1 + (shiftEast2 ? offset1 : -offset1), (float)(y * 16), z1 + (shiftSouth2 ? offset1 : -offset1)).color(red, green, blue, 0.3F).next();
    }

    @Override
    public Identifier getTexture(FrozenLightning entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
