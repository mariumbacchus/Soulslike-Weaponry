package net.soulsweaponry.client.renderer.item;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.soulsweaponry.client.registry.ShaderLayerRegistry;
import net.soulsweaponry.items.sword.LargeMoonlightSword;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class MoonlightBladeShaderLayer extends GeoRenderLayer<LargeMoonlightSword> {

    public MoonlightBladeShaderLayer(GeoRenderer<LargeMoonlightSword> renderer) {
        super(renderer);
    }

    @Override
    public void render(
            MatrixStack matrices,
            LargeMoonlightSword sword,
            BakedGeoModel bakedModel,
            RenderLayer renderType,
            VertexConsumerProvider bufferSource,
            VertexConsumer buffer,
            float tickDelta,
            int packedLight,
            int packedOverlay
    ) {
        GeoBone blade = bakedModel.getBone("blade").orElse(null);
        if (blade == null) return;

        RenderLayer layer = ShaderLayerRegistry.moonlightBladeShader();
        VertexConsumer portalBuffer = bufferSource.getBuffer(layer);

        getRenderer().renderRecursively(
                matrices,
                sword,
                blade,
                layer,
                bufferSource,
                portalBuffer,
                true,
                tickDelta,
                packedLight,
                packedOverlay,
                0xFFFFFFFF
        );
    }
}