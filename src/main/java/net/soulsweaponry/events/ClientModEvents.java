package net.soulsweaponry.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.FlameParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.registry.ArmorRenderRegistry;
import net.soulsweaponry.client.registry.BlockRenderLayers;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.client.registry.PredicateRegistry;
import net.soulsweaponry.registry.ParticleRegistry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        BlockRenderLayers.register();
        ArmorRenderRegistry.register();
        PredicateRegistry.register();
        //NOTE: 1.19 does registry differently, check https://www.youtube.com/watch?v=NN-k74NMKRc&list=PLKGarocXCE1HrC60yuTNTGRoZc6hf5Uvl&index=14 for more info.
        KeyBindRegistry.register();
    }

    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.NIGHTFALL_PARTICLE.get(), FlameParticle.Factory::new);
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.DAZZLING_PARTICLE.get(), FlameParticle.Factory::new);
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.PURPLE_FLAME.get(), FlameParticle.Factory::new);
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.DARK_STAR.get(), FlameParticle.Factory::new);
        MinecraftClient.getInstance().particleManager.registerFactory(ParticleRegistry.BLACK_FLAME.get(), FlameParticle.Factory::new);
    }
}
