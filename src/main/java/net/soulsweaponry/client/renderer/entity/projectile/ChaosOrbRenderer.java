package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.projectile.ChaosOrbModel;
import net.soulsweaponry.entity.projectile.ChaosOrbEntity;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

import java.util.Random;

public class ChaosOrbRenderer extends GeoProjectilesRenderer<ChaosOrbEntity> {

    private static final Identifier[] LAYERS = {
            new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_1.png"),
            new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_2.png"),
            new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_3.png"),
            new Identifier(SoulsWeaponry.ModId, "textures/entity/chaos_orb_4.png"),
    };
    private static final Identifier MODEL = new Identifier(SoulsWeaponry.ModId, "geo/chaos_orb.geo.json");
    private RenderLayer previousLayer;

    public ChaosOrbRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChaosOrbModel());
    }

    @Override
    public void render(ChaosOrbEntity animatable, float yaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(animatable, yaw, partialTick, poseStack, bufferSource, packedLight);

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
        this.render(this.getGeoModelProvider().getModel(MODEL), animatable, partialTick, orbLayer,
                poseStack, bufferSource, bufferSource.getBuffer(orbLayer), packedLight, OverlayTexture.DEFAULT_UV, 1f, 1f,
                1f, 1f);
    }
}