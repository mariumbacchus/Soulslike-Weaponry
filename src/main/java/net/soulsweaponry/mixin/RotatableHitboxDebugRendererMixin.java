package net.soulsweaponry.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.collision.RotatableHitboxDebugRegistry;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugRenderer.class)
public class RotatableHitboxDebugRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void soulsweaponry$renderRotatableHitboxes(
            MatrixStack matrices,
            VertexConsumerProvider.Immediate vertexConsumers,
            double cameraX,
            double cameraY,
            double cameraZ,
            CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || !client.getEntityRenderDispatcher().shouldRenderHitboxes()) {
            return;
        }
        Camera camera = client.gameRenderer.getCamera();
        if (!camera.isReady()) {
            return;
        }
        List<RotatableHitbox> hitboxes = RotatableHitboxDebugRegistry.getActive(client.world);
        if (hitboxes.isEmpty()) {
            return;
        }
        Vec3d cameraOffset = camera.getPos().negate();

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.lineWidth(1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(
                VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION_COLOR
        );

        for (RotatableHitbox hitbox : hitboxes) {
            RotatableHitbox renderBox = hitbox.copy().offsetWorld(cameraOffset);
            renderBox.update();

            drawObb(matrices, buffer, renderBox);
            drawAxes(matrices, buffer, renderBox);
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.lineWidth(1.0F);
        RenderSystem.enableBlend();
    }

    private static void drawObb(MatrixStack matrices, BufferBuilder buffer, RotatableHitbox hitbox) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Vec3d[] vertices = hitbox.getVertices();

        for (int[] edge : RotatableHitbox.EDGES) {
            Vec3d a = vertices[edge[0]];
            Vec3d b = vertices[edge[1]];
            buffer.vertex(matrix, (float) a.x, (float) a.y, (float) a.z).color(1.0F, 0.0F, 0.0F, 1.0F);
            buffer.vertex(matrix, (float) b.x, (float) b.y, (float) b.z).color(1.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    private static void drawAxes(MatrixStack matrices, BufferBuilder buffer, RotatableHitbox hitbox) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        Vec3d center = hitbox.getCenter();

        Vec3d x = center.add(hitbox.getAxisX().multiply(hitbox.getHalfExtents().x + 0.25));
        Vec3d y = center.add(hitbox.getAxisY().multiply(hitbox.getHalfExtents().y + 0.25));
        Vec3d z = center.add(hitbox.getAxisZ().multiply(hitbox.getHalfExtents().z + 0.25));

        drawLine(matrix, buffer, center, x, 0.0F, 0.0F, 1.0F);
        drawLine(matrix, buffer, center, y, 0.0F, 1.0F, 0.0F);
        drawLine(matrix, buffer, center, z, 1.0F, 0.0F, 0.0F);
    }

    private static void drawLine(
            Matrix4f matrix,
            BufferBuilder buffer,
            Vec3d a,
            Vec3d b,
            float red,
            float green,
            float blue
    ) {
        buffer.vertex(matrix, (float) a.x, (float) a.y, (float) a.z).color(red, green, blue, 1.0F);
        buffer.vertex(matrix, (float) b.x, (float) b.y, (float) b.z).color(red, green, blue, 1.0F);
    }
}