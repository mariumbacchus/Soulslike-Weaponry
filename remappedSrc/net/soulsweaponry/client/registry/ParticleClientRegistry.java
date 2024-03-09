package net.soulsweaponry.client.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.soulsweaponry.registry.ParticleRegistry;

public class ParticleClientRegistry {
    
    public static void initClient() {
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.PURPLE_FLAME, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.NIGHTFALL_PARTICLE, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DAZZLING_PARTICLE, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.DARK_STAR, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BLACK_FLAME, FlameParticle.Factory::new);
    }
}
