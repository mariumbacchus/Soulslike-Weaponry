package net.soulsweaponry.compat;

import it.crystalnest.prometheus.api.FireManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class PrometheusCompat {

    private static final Identifier SOUL_FIRE_ASPECT_ID = Identifier.of("soul_fire_aspect");

    public static int getSoulFireAspect(ItemStack stack, DynamicRegistryManager registryManager) {
        Registry<Enchantment> enchantmentRegistry = registryManager.get(RegistryKeys.ENCHANTMENT);
        Optional<RegistryEntry.Reference<Enchantment>> op = enchantmentRegistry.getEntry(SOUL_FIRE_ASPECT_ID);
        if (op.isPresent()) {
            RegistryEntry<Enchantment> soulFireAspect = op.get();
            return EnchantmentHelper.getLevel(soulFireAspect, stack);
        }
        return 0;
    }

    public static void igniteNormalFire(LivingEntity target, float seconds) {
        FireManager.setOnFire(target, seconds, FireManager.DEFAULT_FIRE_TYPE);
    }

    public static void igniteSoulFire(LivingEntity target, float seconds) {
        FireManager.setOnFire(target, seconds, FireManager.SOUL_FIRE_TYPE);
    }
}
