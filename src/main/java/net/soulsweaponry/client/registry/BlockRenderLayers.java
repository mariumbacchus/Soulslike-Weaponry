package net.soulsweaponry.client.registry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.FluidRegistry;

public class BlockRenderLayers {

    public static void register() {
        RenderLayers.setRenderLayer(BlockRegistry.ALTAR_BLOCK.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.BLACKSTONE_PEDESTAL.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.CHUNGUS_MONOLITH.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.VERGLAS_BLOCK.get(), RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(BlockRegistry.WITHERED_GRASS.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.WITHERED_BERRY_BUSH.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.WITHERED_FERN.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.WITHERED_LARGE_FERN.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.WITHERED_TALL_GRASS.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.OLEANDER.get(), RenderLayer.getCutout());
        RenderLayers.setRenderLayer(BlockRegistry.HYDRANGEA.get(), RenderLayer.getCutout());

        RenderLayers.setRenderLayer(FluidRegistry.STILL_PURIFIED_BLOOD.get(), RenderLayer.getTranslucent());
        RenderLayers.setRenderLayer(FluidRegistry.FLOWING_PURIFIED_BLOOD.get(), RenderLayer.getTranslucent());

        RenderLayers.setRenderLayer(BlockRegistry.PURIFIED_BLOOD_BLOCK.get(), RenderLayer.getTranslucent());
    }
}
