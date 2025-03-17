package net.soulsweaponry.client.renderer.entity.mobs;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.registry.EntityModelLayerModRegistry;
import net.soulsweaponry.entity.mobs.BigChungus;

public class BigChungusRenderer extends MobEntityRenderer<BigChungus, BigChungusModel<BigChungus>> {

    public BigChungusRenderer(Context context) {
        super(context, new BigChungusModel<>(context.getPart(EntityModelLayerModRegistry.BIG_CHUNGUS_LAYER)), 0.5f);
        this.addFeature(new RedEyesOverlay(this));
    }

    @Override
    public Identifier getTexture(BigChungus entity) {
        String id = entity.getState().equals(BigChungus.ChungusStates.NORMAL) ? "" : String.valueOf(entity.getState()).toLowerCase() + "_";
        return new Identifier(SoulsWeaponry.ModId, "textures/entity/chungus/" + id + "big_chungus.png");
    }

    public static class RedEyesOverlay extends EyesFeatureRenderer<BigChungus, BigChungusModel<BigChungus>> {

        private static final Identifier RED_EYES_TEXTURE = new Identifier(SoulsWeaponry.ModId, "textures/entity/chungus/red_eyes_overlay.png");

        public RedEyesOverlay(BigChungusRenderer renderer) {
            super(renderer);
        }

        @Override
        public RenderLayer getEyesTexture() {
            return RenderLayer.getEyes(RED_EYES_TEXTURE);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, BigChungus entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.isAggressive()) {
                super.render(matrices, vertexConsumers, light, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }
}
