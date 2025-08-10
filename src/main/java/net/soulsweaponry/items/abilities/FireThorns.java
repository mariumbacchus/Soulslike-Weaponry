package net.soulsweaponry.items.abilities;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.DamageSourceRegistry;

public class FireThorns {

    public static void trigger(LivingEntity user, LivingEntity attacker) {
        if (user.getRandom().nextFloat() < ConfigConstructor.supernova_firethorns_chance) {
            attacker.damage(DamageSourceRegistry.create(user.getWorld(), DamageSourceRegistry.PLAYER_FIRE, user), ConfigConstructor.supernova_firethorns_damage);
            attacker.setOnFireFor((int) ConfigConstructor.supernova_firethorns_fire_seconds);
        }
    }
}
