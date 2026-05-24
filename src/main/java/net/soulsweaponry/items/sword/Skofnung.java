package net.soulsweaponry.items.sword;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.BasicPostHitAbility;
import net.soulsweaponry.items.abilities.statboost.Sharpened;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class Skofnung extends ModdedSword {

    private static final BasicPostHitAbility DISABLE_HEAL = new BasicPostHitAbility(
            (stack, target, attacker) -> {
                int duration = (int) (ConfigConstructor.skofnung_disable_heal_duration
                        + WeaponUtil.getUpgradeLevel(stack) * ConfigConstructor.skofnung_disable_heal_bonus_duration_per_level);
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.DISABLE_HEAL.get(), duration, 0));
            },
            List.of(
                    Text.translatable("tooltip.soulsweapons.disable_heal").formatted(Formatting.BOLD),
                    Text.translatable("tooltip.soulsweapons.disable_heal.1").formatted(Formatting.GRAY),
                    Text.translatable("tooltip.soulsweapons.disable_heal.2").formatted(Formatting.GRAY)
            )
    );
    private static final Sharpened SHARPENED = new Sharpened(
            ConfigConstructor.skofnung_sharpened_bonus_damage,
            ConfigConstructor.skofnung_sharpened_bonus_damage_per_level,
            ConfigConstructor.skofnung_sharpened_bonus_attack_speed,
            ConfigConstructor.skofnung_sharpened_bonus_attack_speed_per_level,
            (int) ConfigConstructor.skofnung_sharpened_bleed_post_hit,
            ConfigConstructor.skofnung_sharpened_bonus_bleed_post_hit_per_level,
            (int) ConfigConstructor.skofnung_sharpened_bleed_effect_duration,
            (int) ConfigConstructor.skofnung_sharpened_bleed_effect_bonus_duration_per_level,
            (int) ConfigConstructor.skofnung_sharpened_bleed_effect_amp,
            ConfigConstructor.skofnung_sharpened_bleed_effect_bonus_amp_per_level,
            (int) ConfigConstructor.skofnung_sharpened_max_empowered_strikes
    );

    public Skofnung(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.skofnung_damage, ConfigConstructor.skofnung_attack_speed, settings);
        this.addAbility(DISABLE_HEAL, SHARPENED);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_skofnung;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_skofnung;
    }
}