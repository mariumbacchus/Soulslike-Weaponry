package net.soulsweaponry.events;

import net.minecraft.client.particle.FlameParticle;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.hud.*;
import net.soulsweaponry.client.particles.CyanSweepAttackParticle;
import net.soulsweaponry.client.particles.factory.EchoSmokeFactory;
import net.soulsweaponry.client.particles.factory.SoulSparkFactory;
import net.soulsweaponry.client.registry.*;
import net.soulsweaponry.items.abilities.posthit.SwitchPostHit;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockRenderLayers.register();
            PredicateRegistry.initClient();
            EntityModelRegistry.register();
            CustomBossBar.init();
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
        event.registerSpriteSet(ParticleRegistry.SOUL_SPARK.get(), SoulSparkFactory::new);
        event.registerSpriteSet(ParticleRegistry.ECHO_SMOKE.get(), EchoSmokeFactory::new);
        event.registerSpriteSet(ParticleRegistry.ECHO_SWEEP_ATTACK.get(), CyanSweepAttackParticle.Factory::new);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        KeyBindRegistry.register(event);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("posture_bar", PostureHudOverlay.HUD_POSTURE);
        event.registerAboveAll("bleed_bar", BleedHudOverlay.HUD_BLEED);
        event.registerAboveAll("target_posture", TargetPostureHudOverlay.HUD_TARGET_POSTURE);
        event.registerAboveAll("frost_bar", FrostHudOverlay.HUD_FROST);
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

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex > 0) {
                return ColorHelper.Argb.getAlpha(SwitchPostHit.getModelColor(stack));
            }
            return -1;
        }, WeaponRegistry.NIGHTLORDS_SWORD.get());
    }
}
