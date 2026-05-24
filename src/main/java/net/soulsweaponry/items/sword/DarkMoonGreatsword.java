package net.soulsweaponry.items.sword;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.BasicKeybindAbility;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.items.abilities.predicate.FrostMoonNeeded;
import net.soulsweaponry.items.abilities.stoppedusing.ShootFrostMoonlight;
import net.soulsweaponry.registry.EffectRegistry;

import java.util.List;

public class DarkMoonGreatsword extends ModdedSword {

    private static final Permafrost PERMAFROST = new Permafrost(
            (int) ConfigConstructor.dark_moon_greatsword_frost_buildup_post_hit,
            (int) ConfigConstructor.dark_moon_greatsword_post_hit_permafrost_base_duration,
            (int) ConfigConstructor.dark_moon_greatsword_post_hit_permafrost_base_amplifier,
            ConfigConstructor.dark_moon_greatsword_post_hit_permafrost_amp_per_level
    );
    private static final BasicKeybindAbility GIVE_FROST_MOON = new BasicKeybindAbility(
            (serverWorld, stack, player) -> player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FROST_MOON.get(),
                    (int) ConfigConstructor.dark_moon_greatsword_frost_moon_duration,  (int) ConfigConstructor.dark_moon_greatsword_frost_moon_amp)),
            (clientWorld, stack, player) -> player.playSound(SoundEvents.ENTITY_SKELETON_CONVERTED_TO_STRAY, 1.0F, 1.0F),
            List.of(
                    Text.translatable("tooltip.soulsweapons.frost_moon").formatted(Formatting.WHITE),
                    Text.translatable("tooltip.soulsweapons.frost_moon.1").formatted(Formatting.GRAY),
                    Text.translatable("tooltip.soulsweapons.frost_moon.2").formatted(Formatting.GRAY)
            ), 1,
            (int) ConfigConstructor.dark_moon_greatsword_frost_moon_min_cooldown,
            (int) ConfigConstructor.dark_moon_greatsword_frost_moon_cooldown,
            (int) ConfigConstructor.dark_moon_greatsword_frost_moon_reduced_cooldown_per_level
    );
    private static final ShootFrostMoonlight FROST_MOONLIGHT = new ShootFrostMoonlight(
            (int) ConfigConstructor.dark_moon_greatsword_projectile_amount,
            ConfigConstructor.dark_moon_greatsword_bonus_projectile_amount_per_level,
            ConfigConstructor.dark_moon_greatsword_projectile_speed,
            ConfigConstructor.dark_moon_greatsword_projectile_damage,
            ConfigConstructor.dark_moon_greatsword_projectile_bonus_damage_per_level,
            (int) ConfigConstructor.dark_moon_greatsword_projectile_permafrost_amp,
            ConfigConstructor.dark_moon_greatsword_projectile_permafrost_bonus_amp_per_level,
            (int) ConfigConstructor.dark_moon_greatsword_projectile_permafrost_duration,
            ConfigConstructor.dark_moon_greatsword_projectile_permafrost_bonus_duration_per_level
    );
    private static final FrostMoonNeeded FROST_MOON_NEEDED = new FrostMoonNeeded((int) ConfigConstructor.dark_moon_greatsword_item_upgrade_needed_to_remove_frost_moon_requirement);

    public DarkMoonGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.dark_moon_greatsword_damage, ConfigConstructor.dark_moon_greatsword_attack_speed, settings);
        this.addAbility(PERMAFROST, GIVE_FROST_MOON, FROST_MOON_NEEDED, FROST_MOONLIGHT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_dark_moon_greatsword;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_dark_moon_greatsword;
    }
}