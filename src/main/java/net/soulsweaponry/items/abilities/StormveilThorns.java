package net.soulsweaponry.items.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDamageSource;

public class StormveilThorns {

    public static void trigger(LivingEntity user, LivingEntity attacker, float amp) {
        if (user.getRandom().nextFloat() < ConfigConstructor.tonitrus_stormveil_effect_thorns_chance) {
            attacker.damage(CustomDamageSource.create(user.getWorld(), CustomDamageSource.PLAYER_LIGHTNING, user),
                    ConfigConstructor.tonitrus_stormveil_effect_thorns_base_damage + amp * ConfigConstructor.tonitrus_stormveil_effect_thorns_damage_per_amp_level);
            user.getWorld().playSound(null, user.getBlockPos(), SoundRegistry.STORMVEIL_THORNS, SoundCategory.PLAYERS, 1f, 1f);
            if (!user.getWorld().isClient) {
                for (int i = 0; i < 50; i++) {
                    ParticleHandler.singleParticle(user.getWorld(), ParticleRegistry.SOUL_SPARK, user.getParticleX(1D), user.getBodyY(0.5) + user.getRandom().nextDouble() * 2 - 1D, user.getParticleZ(1D),
                            user.getRandom().nextGaussian() / 10f, user.getRandom().nextGaussian() / 10f, user.getRandom().nextGaussian() / 10f);
                }
            }
        }
    }
}
