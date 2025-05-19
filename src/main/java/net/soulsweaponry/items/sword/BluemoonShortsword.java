package net.soulsweaponry.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;

public class BluemoonShortsword extends MoonlightShortsword {
    public BluemoonShortsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.bluemoon_shortsword_damage, ConfigConstructor.bluemoon_shortsword_attack_speed, settings);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bluemoon_shortsword;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_bluemoon_shortsword;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.bluemoon_shortsword_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.bluemoon_shortsword_enchant_reduces_cooldown_ids;
    }

    @Override
    public float getProjectileDamage() {
        return ConfigConstructor.bluemoon_shortsword_projectile_damage;
    }

    @Override
    public float getProjectileVelocity() {
        return ConfigConstructor.bluemoon_shortsword_projectile_velocity;
    }
}