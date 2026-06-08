package net.soulsweaponry.client.renderer.entity.projectile;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.projectile.NightSkull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class NightSkullChargeLayer extends GeoRenderLayer<NightSkull> {

    private static final Identifier TEXTURE = Identifier.of(SoulsWeaponry.ModId, "textures/entity/night_skull_charge_layer.png");

    public NightSkullChargeLayer(GeoRenderer<NightSkull> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrices, NightSkull animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float tickDelta, int packedLight, int packedOverlay) {
        if (!animatable.isCharged()) {
            return;
        }
        float age = animatable.age + tickDelta;
        float x = age * 0.01f;
        float y = age * 0.01f;
        float scale = 1.2f;

        RenderLayer layer = RenderLayer.getEnergySwirl(TEXTURE, x % 1.0f, y % 1.0f);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(layer);
        matrices.push();
        matrices.scale(scale, scale, scale);
        matrices.translate(0, - (scale - 1f) / 4f, 0);
        this.getRenderer().reRender(model, matrices, bufferSource, animatable, layer, vertexConsumer, tickDelta, packedLight, OverlayTexture.DEFAULT_UV, -8355712);
        matrices.pop();
    }
}
