package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ReturningKnightShieldLayer extends GeoRenderLayer<ReturningKnight> {

    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/wither/wither_armor.png");

    public ReturningKnightShieldLayer(GeoRenderer<ReturningKnight> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrices, ReturningKnight entity, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider vertexConsumers, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!entity.isAlive() || entity.isInvisible() || !entity.isBulletHellActive()) {
            return;
        }
        GeoBone shadow = bakedModel.getBone("shadow").orElse(null);
        boolean shadowWasHidden = shadow != null && shadow.isHidden();
        boolean childrenWereHidden = shadow != null && shadow.isHidingChildren();

        float age = entity.age + partialTick;
        float scrollX = MathHelper.cos(age * 0.02F) * 3F % 1F;
        float scrollY = age * 0.01F % 1F;

        RenderLayer shieldLayer = RenderLayer.getEnergySwirl(TEXTURE, scrollX, scrollY);
        matrices.push();
        try {
            if (shadow != null) {
                shadow.setHidden(true);
                shadow.setChildrenHidden(true);
            }
            matrices.scale(1.03F, 1.03F, 1.03F);
            this.getRenderer().reRender(bakedModel, matrices, vertexConsumers, entity, shieldLayer, vertexConsumers.getBuffer(shieldLayer),
                    partialTick, packedLight, OverlayTexture.DEFAULT_UV, 0xFF808080);
        } finally {
            if (shadow != null) {
                shadow.setHidden(shadowWasHidden);
                shadow.setChildrenHidden(childrenWereHidden);
            }
            matrices.pop();
        }
    }
}