package net.soulsweaponry.events;

import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.hud.PostureHudOverlay;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.model.entity.projectile.DragonslayerSwordspearModel;
import net.soulsweaponry.client.registry.BlockRenderLayers;
import net.soulsweaponry.client.registry.EntityModelRegistry;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.client.registry.PredicateRegistry;
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
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        KeyBindRegistry.register(event);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("posture_bar", PostureHudOverlay.HUD_POSTURE);
    }

    public static final EntityModelLayer BIG_CHUNGUS_LAYER = new EntityModelLayer(new Identifier(SoulsWeaponry.ModId, "big_chungus"), "main");
    public static final EntityModelLayer DRAGONSLAYER_SWORDSPEAR_LAYER = new EntityModelLayer(new Identifier(SoulsWeaponry.ModId, "swordspear_entity"), "main");

    @SubscribeEvent
    public static void registerLayerDefinition(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DRAGONSLAYER_SWORDSPEAR_LAYER, DragonslayerSwordspearModel::getTexturedModelData);
        event.registerLayerDefinition(BIG_CHUNGUS_LAYER, BigChungusModel::getTexturedModelData);
    }
}
