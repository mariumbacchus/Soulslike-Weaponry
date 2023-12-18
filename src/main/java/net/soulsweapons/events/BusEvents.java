package net.soulsweapons.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.soulsweapons.SoulsWeaponry;
import net.soulsweapons.registry.ParticleRegistry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.NIGHTFALL_PARTICLE.get(), FlameParticle.Provider::new);//TODO check on servers, might need to be registered on client side
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.DAZZLING_PARTICLE.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.PURPLE_FLAME.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.DARK_STAR.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.BLACK_FLAME.get(), FlameParticle.Provider::new);
    }
}
