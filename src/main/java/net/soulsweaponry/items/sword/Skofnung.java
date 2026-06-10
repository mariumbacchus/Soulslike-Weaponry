package net.soulsweaponry.items.sword;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.BasicPostHitAbility;
import net.soulsweaponry.items.abilities.statboost.Sharpened;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class Skofnung extends ModdedSword {

    private static final BasicPostHitAbility DISABLE_HEAL = new BasicPostHitAbility(
            (stack, target, attacker) -> {
                int duration = (int) (WeaponConfig.skofnung_disable_heal_duration
                        + WeaponUtil.getUpgradeLevel(stack) * WeaponConfig.skofnung_disable_heal_bonus_duration_per_level);
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.DISABLE_HEAL, duration, 0));
            },
            List.of(
                    Text.translatable("tooltip.soulsweapons.disable_heal").formatted(Formatting.BOLD),
                    Text.translatable("tooltip.soulsweapons.disable_heal.1").formatted(Formatting.GRAY),
                    Text.translatable("tooltip.soulsweapons.disable_heal.2").formatted(Formatting.GRAY)
            )
    );
    private static final Sharpened SHARPENED = new Sharpened(
            WeaponConfig.skofnung_sharpened_bonus_damage,
            WeaponConfig.skofnung_sharpened_bonus_damage_per_level,
            WeaponConfig.skofnung_sharpened_bonus_attack_speed,
            WeaponConfig.skofnung_sharpened_bonus_attack_speed_per_level,
            (int) WeaponConfig.skofnung_sharpened_bleed_post_hit,
            WeaponConfig.skofnung_sharpened_bonus_bleed_post_hit_per_level,
            (int) WeaponConfig.skofnung_sharpened_bleed_effect_duration,
            (int) WeaponConfig.skofnung_sharpened_bleed_effect_bonus_duration_per_level,
            (int) WeaponConfig.skofnung_sharpened_bleed_effect_amp,
            WeaponConfig.skofnung_sharpened_bleed_effect_bonus_amp_per_level,
            (int) WeaponConfig.skofnung_sharpened_max_empowered_strikes
    );

    public Skofnung(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.skofnung_damage, WeaponConfig.skofnung_attack_speed, settings);
        this.addAbility(DISABLE_HEAL, SHARPENED);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_skofnung;
    }
}