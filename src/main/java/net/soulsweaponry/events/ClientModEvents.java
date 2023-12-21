package net.soulsweaponry.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.items.ChaosSet;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.ALTAR_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.BLACKSTONE_PEDESTAL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.CHUNGUS_MONOLITH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.VERGLAS_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_GRASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_BERRY_BUSH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_LARGE_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_TALL_GRASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.OLEANDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.HYDRANGEA.get(), RenderType.cutout());

        GeoArmorRenderer.registerArmorRenderer(ChaosSet.class, ChaosSetRenderer::new);

        //NOTE: 1.19 does registry differently, check https://www.youtube.com/watch?v=NN-k74NMKRc&list=PLKGarocXCE1HrC60yuTNTGRoZc6hf5Uvl&index=14 for more info.
        KeyBindRegistry.register();
    }

    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.NIGHTFALL_PARTICLE.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.DAZZLING_PARTICLE.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.PURPLE_FLAME.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.DARK_STAR.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.BLACK_FLAME.get(), FlameParticle.Provider::new);
    }
}
