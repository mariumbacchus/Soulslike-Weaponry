package net.soulsweaponry.compat;

import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class PrometheusCompat {

    private static final Identifier SOUL_FIRE_ASPECT_ID = new Identifier("soul_fire_aspect");

    public static int getSoulFireAspect(ItemStack stack) {
        Enchantment enchantment = Registries.ENCHANTMENT.get(SOUL_FIRE_ASPECT_ID);
        if (enchantment != null) {
            return EnchantmentHelper.getLevel(enchantment, stack);
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