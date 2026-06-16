package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.SoulsWeaponryClient;
import net.soulsweaponry.client.model.entity.mobs.NightProwlerModel;
import net.soulsweaponry.entity.ai.goal.NightProwlerGoal;
import net.soulsweaponry.entity.mobs.boss.NightProwler;
import org.joml.Matrix4f;

import java.awt.Color;

public class NightProwlerRenderer extends GeoEntityRendererWithDeathlight<NightProwler> {

    private static final Color COLOR_ONE = new Color(54, 122, 156);
    private static final Color COLOR_TWO = new Color(147, 188, 210);
    private static final Color COLOR_THREE = new Color(221, 255, 254);
    private static final Color COLOR_FOUR = new Color(235, 185, 232);
    private static final Vec3d DEATH_LIGHT_TRANSLATION = new Vec3d(0, 3, 0);

    public NightProwlerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NightProwlerModel());
        this.shadowRadius = 1F;
    }

    @Override
    public void render(NightProwler entity, float entityYaw, float partialTicks, MatrixStack stack,
                       VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        if (this.shouldRenderPortal(entity)) {
            stack.push();
            stack.translate(0, this.getBottomYOffset(), 0);
            Matrix4f matrix = stack.peek().getPositionMatrix();
            VertexConsumer vertexConsumer = bufferIn.getBuffer(this.getLayer());
            float radius = calculatePortalRadius(NightProwlerGoal.PORTAL_RADIUS, entity.getOpenPortalTicks(), partialTicks);
            int segments = 32; // more = smoother
            renderCircle(matrix, vertexConsumer, radius, segments);
            stack.pop();
        }
    }

    /**
     * Calculates the portal radius based on the current tick count and partial ticks.
     * - From 0 to 30 ticks, the radius increases linearly from 0 to maxRadius.
     * - From 100 to 190 ticks, the radius stays constant at maxRadius.
     * - From 190 to 200 ticks, the radius decreases linearly from maxRadius to 0.
     * - For ticks beyond 200, the radius remains 0.
     *
     * @param maxRadius  The maximum portal radius (16f in your case).
     * @param ticks      The discrete tick count.
     * @param partialTicks The partial tick value (0 to 1) to smooth transitions.
     * @return The calculated portal radius.
     */
    public static float calculatePortalRadius(float maxRadius, float ticks, float partialTicks) {
        float smoothTicks = ticks + partialTicks;
        if (smoothTicks < 0f) {
            return 0f;
        } else if (smoothTicks <= 30f) {
            return maxRadius * (smoothTicks / 30f);
        } else if (smoothTicks <= 190f) {
            return maxRadius;
        } else if (smoothTicks <= 200f) {
            return maxRadius * (1f - (smoothTicks - 190f) / 10f);
        } else {
            return 0f;
        }
    }

    /**
     * Renders a filled circle (a disk) as a series of quads.
     * The disk is built by dividing it into concentric rings and angular segments.
     * This approach submits vertices in groups of 4 (quads) so that the end portal render layer handles them properly.
     *
     * @param matrix         The transformation matrix.
     * @param vertexConsumer The vertex consumer from the render layer.
     * @param radius         The overall radius of the circle.
     * @param segments       The number of angular segments (more segments = smoother circle).
     */
    private void renderCircle(Matrix4f matrix, VertexConsumer vertexConsumer, float radius, int segments) {
        // Number of concentric rings (increase for a smoother fill)
        int rings = 8;
        // A small value to avoid a degenerate (zero-area) quad at the center
        float epsilon = 0.01f;

        // Loop over each ring
        for (int ring = 0; ring < rings; ring++) {
            // For the innermost ring, use epsilon instead of 0 to avoid degenerate quads.
            float innerRadius = (ring == 0) ? epsilon : (radius * ring / rings);
            float outerRadius = radius * (ring + 1) / rings;

            // Loop over each segment of the circle
            for (int seg = 0; seg < segments; seg++) {
                double angle1 = 2 * Math.PI * seg / segments;
                double angle2 = 2 * Math.PI * (seg + 1) / segments;

                // Calculate the positions for the inner ring vertices.
                float innerX1 = (float) (innerRadius * Math.cos(angle1));
                float innerZ1 = (float) (innerRadius * Math.sin(angle1));
                float innerX2 = (float) (innerRadius * Math.cos(angle2));
                float innerZ2 = (float) (innerRadius * Math.sin(angle2));

                // Calculate the positions for the outer ring vertices.
                float outerX1 = (float) (outerRadius * Math.cos(angle1));
                float outerZ1 = (float) (outerRadius * Math.sin(angle1));
                float outerX2 = (float) (outerRadius * Math.cos(angle2));
                float outerZ2 = (float) (outerRadius * Math.sin(angle2));

                // Submit the vertices for this quad.
                // The order is chosen so that the vertices wind correctly.
                vertexConsumer.vertex(matrix, innerX1, 0, innerZ1);
                vertexConsumer.vertex(matrix, outerX1, 0, outerZ1);
                vertexConsumer.vertex(matrix, outerX2, 0, outerZ2);
                vertexConsumer.vertex(matrix, innerX2, 0, innerZ2);
            }
        }
    }

    // Render entity regardless of camera frustum when doing ECLIPSE attack
    @Override
    public boolean shouldRender(NightProwler entity, Frustum frustum, double x, double y, double z) {
        return this.shouldRenderPortal(entity) || super.shouldRender(entity, frustum, x, y, z);
    }

    protected float getBottomYOffset() {
        return 8f;
    }

    protected RenderLayer getLayer() {
        return SoulsWeaponryClient.NIGHT_PROWLER_PORTAL;
    }

    private boolean shouldRenderPortal(NightProwler entity) {
        return entity.isState(NightProwler.States.ECLIPSE) && entity.getParticleState() == 4;
    }

    @Override
    protected int getDeathTicks(NightProwler entity) {
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