package net.soulsweaponry.items.sword;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.items.abilities.BasicInfoAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;

import java.util.List;
import java.util.Map;

public class Featherlight extends UltraHeavyWeapon {

    private static final StatusEffectInstance[] CALCULATED_FALL_EFFECTS = new StatusEffectInstance[] {
            new StatusEffectInstance(EffectRegistry.BLIGHT, 200, 4),
            new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 2)
    };
    private static final DetonateGroundAttributes ATTRIBUTES = new DetonateGroundAttributes(
            ConfigConstructor.featherlight_calculated_fall_base_radius,
            ConfigConstructor.featherlight_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.featherlight_calculated_fall_target_launch_modifier,
            ConfigConstructor.featherlight_calculated_fall_target_max_launch_power,
            ConfigConstructor.featherlight_calculated_fall_max_radius,
            ConfigConstructor.featherlight_calculated_fall_max_damage,
            ConfigConstructor.featherlight_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.featherlight_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleRegistry.PURPLE_FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {
                for (StatusEffectInstance effect : CALCULATED_FALL_EFFECTS) {
                    target.addStatusEffect(effect);
                }
            },
            (user, fallDistance, stack) -> {}
    );
    private static final BasicInfoAbility LIGHT_WEAPON = new BasicInfoAbility(List.of(
            Text.translatable("tooltip.soulsweapons.featherlight").formatted(Formatting.LIGHT_PURPLE),
            Text.translatable("tooltip.soulsweapons.featherlight.1").formatted(Formatting.GRAY)
    ));

    public Featherlight(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.featherlight_damage, ConfigConstructor.disable_use_featherlight ?
                1f : ConfigConstructor.featherlight_attack_speed, settings, (int) ConfigConstructor.featherlight_posture_loss, ATTRIBUTES);
        this.addAbility(LIGHT_WEAPON);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_featherlight;
    }
}