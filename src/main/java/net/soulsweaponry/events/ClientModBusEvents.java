package net.soulsweaponry.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.hud.PostureHudOverlay;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.model.entity.projectile.DragonslayerSwordspearModel;
import net.soulsweaponry.client.registry.*;
import net.soulsweaponry.registry.ParticleRegistry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockRenderLayers.register();
            ArmorRenderRegistry.register();
            PredicateRegistry.register();
            EntityModelRegistry.register();
            //NOTE: 1.19 does registry differently through a client mod bus event instead, see https://www.youtube.com/watch?v=J3a7JT0rxTM
            OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "posture_bar", PostureHudOverlay.HUD_POSTURE);
        });
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

    @SubscribeEvent
    public static void registerLayerDefinition(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        EntityModelLayerModRegistry.initClient(event);
    }
}
