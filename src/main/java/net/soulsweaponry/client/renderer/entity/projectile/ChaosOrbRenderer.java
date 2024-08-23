package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.projectile.ChaosOrbModel;
import net.soulsweaponry.entity.projectile.ChaosOrbEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Random;

public class ChaosOrbRenderer extends GeoEntityRenderer<ChaosOrbEntity> {

    public ChaosOrbRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChaosOrbModel());
        addRenderLayer(new ChaosOrbColorLayer(this));
    }

    static class ChaosOrbColorLayer extends GeoRenderLayer<ChaosOrbEntity> {

        private static final Identifier[] LAYERS = {
                new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_1.png"),
                new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_2.png"),
                new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_3.png"),
                new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_4.png"),
        };
        private RenderLayer previousLayer;

        public ChaosOrbColorLayer(GeoRenderer<ChaosOrbEntity> entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(MatrixStack poseStack, ChaosOrbEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            RenderLayer orbLayer;
            if (previousLayer != null) {
                orbLayer = previousLayer;
            } else {
                orbLayer = RenderLayer.getEntityTranslucent(LAYERS[0]);
            }
            if (animatable.age % 4 == 0) {
                int random = new Random().nextInt(LAYERS.length);
                orbLayer = RenderLayer.getEntityTranslucent(LAYERS[random]);
            }
            this.previousLayer = orbLayer;
            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, orbLayer,
                    bufferSource.getBuffer(orbLayer), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                    1, 1, 1, 1);
        }
    }
}