package net.soulsweaponry.client.renderer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.collision.RotatableHitboxDebugRegistry;

import java.util.List;

public final class RotatableHitboxDebugRenderer {

    private RotatableHitboxDebugRenderer() {}

    public static void register() {
        WorldRenderEvents.AFTER_ENTITIES.register(RotatableHitboxDebugRenderer::render);

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> RotatableHitboxDebugRegistry.clear());
    }

    private static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!client.getEntityRenderDispatcher().shouldRenderHitboxes()) {
            return;
        }
        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider consumers = context.consumers();
        if (matrices == null || consumers == null) {
            return;
        }
        List<RotatableHitbox> hitboxes = RotatableHitboxDebugRegistry.getActive(context.world());
        if (hitboxes.isEmpty()) {
            return;
        }

        Vec3d cameraOffset = context.camera().getPos().negate();
        VertexConsumer lines = consumers.getBuffer(RenderLayer.getLines());
        for (RotatableHitbox hitbox : hitboxes) {
            // getActive() already returns copies so this is safe to modify
            hitbox.offsetWorld(cameraOffset);

            drawBox(matrices, lines, hitbox);
            drawAxes(matrices, lines, hitbox);
        }
    }

    private static void drawBox(MatrixStack matrices, VertexConsumer lines, RotatableHitbox hitbox) {
        Vec3d[] vertices = hitbox.getVertices();
        for (int[] edge : RotatableHitbox.EDGES) {
            drawLine(matrices, lines, vertices[edge[0]], vertices[edge[1]], 1F, 0F, 0F);
        }
    }

    private static void drawAxes(MatrixStack matrices, VertexConsumer lines, RotatableHitbox hitbox) {
        Vec3d center = hitbox.getCenter();
        Vec3d halfExtents = hitbox.getHalfExtents();

        Vec3d x = center.add(hitbox.getAxisX().multiply(halfExtents.x + 0.25D));
        Vec3d y = center.add(hitbox.getAxisY().multiply(halfExtents.y + 0.25D));
        Vec3d z = center.add(hitbox.getAxisZ().multiply(halfExtents.z + 0.25D));

        drawLine(matrices, lines, center, x, 0F, 0F, 1F);
        drawLine(matrices, lines, center, y, 0F, 1F, 0F);
        drawLine(matrices, lines, center, z, 1F, 0F, 0F);
    }

    private static void drawLine(MatrixStack matrices, VertexConsumer lines, Vec3d start, Vec3d end, float red, float green, float blue) {
        Vec3d direction = end.subtract(start).normalize();
        MatrixStack.Entry entry = matrices.peek();

        float normalX = (float) direction.x;
        float normalY = (float) direction.y;
        float normalZ = (float) direction.z;

        lines.vertex(entry, (float) start.x, (float) start.y, (float) start.z)
                .color(red, green, blue, 1F)
                .normal(entry, normalX, normalY, normalZ);
        lines.vertex(entry, (float) end.x, (float) end.y, (float) end.z)
                .color(red, green, blue, 1F)
                .normal(entry, normalX, normalY, normalZ);
    }
}
