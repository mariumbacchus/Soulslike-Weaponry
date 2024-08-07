package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.SoulsWeaponryClient;
import net.soulsweaponry.client.model.entity.projectile.NightsEdgeOldModel;
import net.soulsweaponry.entity.projectile.NightsEdge;

public class NightsEdgeOldRenderer extends EntityRenderer<NightsEdge> {
    private static final Identifier TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/entity/nights_edge_noised.png");
    private final NightsEdgeOldModel model;

    public NightsEdgeOldRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new NightsEdgeOldModel(context.getPart(SoulsWeaponryClient.NIGHTS_EDGE_LAYER));
    }

    @Override
    public void render(NightsEdge entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
//        float h = entity.getAnimationProgress(g);
//        if (h == 0.0f) {
//            return;
//        }
//        float j = 2.0f;
//        if (h > 0.9f) {
//            j *= (1.0f - h) / 0.1f;
//        }
//        matrixStack.push();
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f - entity.getYaw()));
//        matrixStack.scale(-j, -j, j);
//        matrixStack.translate(0.0, -0.626, 0.0);
//        matrixStack.scale(2f, 2f, 1f);
//        this.model.setAngles(entity, h, 0.0f, 0.0f, entity.getYaw(), entity.getPitch());
//        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
//        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
//        matrixStack.pop();
//        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(NightsEdge entity) {
        return TEXTURE;
    }
}
