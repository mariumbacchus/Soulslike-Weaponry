package net.soulsweaponry.events;

import net.minecraft.client.particle.FlameParticle;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.hud.PostureHudOverlay;
import net.soulsweaponry.client.registry.*;
import net.soulsweaponry.registry.ParticleRegistry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockRenderLayers.register();
            PredicateRegistry.register();
            EntityModelRegistry.register();
        });
    }

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.NIGHTFALL_PARTICLE.get(), FlameParticle.Factory::new);
        event.registerSpriteSet(ParticleRegistry.DAZZLING_PARTICLE.get(), FlameParticle.Factory::new);
        event.registerSpriteSet(ParticleRegistry.PURPLE_FLAME.get(), FlameParticle.Factory::new);
        event.registerSpriteSet(ParticleRegistry.DARK_STAR.get(), FlameParticle.Factory::new);
        event.registerSpriteSet(ParticleRegistry.BLACK_FLAME.get(), FlameParticle.Factory::new);
        event.registerSpriteSet(ParticleRegistry.SUN_PARTICLE.get(), FlameParticle.Factory::new);
        event.registerSpriteSet(ParticleRegistry.MOONVEIL_PARTICLE.get(), FlameParticle.Factory::new);
        event.registerSpriteSet(ParticleRegistry.BLUE_FLAME.get(), FlameParticle.Factory::new);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        KeyBindRegistry.register(event);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("posture_bar", PostureHudOverlay.HUD_POSTURE);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        EntityModelLayerModRegistry.initClient(event);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent event) {
        if (event.getAtlas().getId().equals(new Identifier("minecraft", "textures/atlas/blocks"))) {
            event.getAtlas().getTextureLocations().add(new Identifier(SoulsWeaponry.ModId, "block/pruified_blood_still"));
            event.getAtlas().getTextureLocations().add(new Identifier(SoulsWeaponry.ModId, "block/pruified_blood_flow"));
        }
    }
}
