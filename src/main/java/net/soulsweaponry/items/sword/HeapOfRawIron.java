package net.soulsweaponry.items.sword;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.items.abilities.abilitykeybind.BasicKeybindAbility;
import net.soulsweaponry.items.abilities.bonusdamage.DragonBonus;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.Map;

public class HeapOfRawIron extends UltraHeavyWeapon {

    private static final StatusEffectInstance[] CALCULATED_FALL_EFFECTS = new StatusEffectInstance[] {
            new StatusEffectInstance(StatusEffects.WITHER, 140, 1)
    };
    private static final DetonateGroundAttributes ATTRIBUTES = new DetonateGroundAttributes(
            ConfigConstructor.heap_of_raw_iron_calculated_fall_base_radius,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_target_launch_modifier,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_target_max_launch_power,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_max_radius,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_max_damage,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.heap_of_raw_iron_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.FLAME, new Vec3d(1, 6, 1), ParticleRegistry.DARK_STAR, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {
                for (StatusEffectInstance effect : CALCULATED_FALL_EFFECTS) {
                    target.addStatusEffect(effect);
                }
            },
            (user, fallDistance, stack) -> {}
    );
    private static final DragonBonus DRAGON_BONUS = new DragonBonus(
            ConfigConstructor.heap_of_raw_iron_dragons_scourge_bonus, ConfigConstructor.heap_of_raw_iron_dragons_scourge_bonus_per_level
    );
    private static final BasicKeybindAbility RAGE = new BasicKeybindAbility(
            (serverWorld, stack, player) -> {
                int lvl = WeaponUtil.getUpgradeLevel(stack);
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY,
                        (int) (ConfigConstructor.heap_of_raw_iron_rage_bloodthristy_duration
                                + ConfigConstructor.heap_of_raw_iron_rage_bloodthristy_bonus_duration_per_lvl * lvl),
                        (int) (ConfigConstructor.heap_of_raw_iron_rage_bloodthristy_amp
                                + ConfigConstructor.heap_of_raw_iron_rage_bloodthristy_bonus_amp_per_lvl * lvl)));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,
                        (int) (ConfigConstructor.heap_of_raw_iron_rage_strenth_duration
                                + ConfigConstructor.heap_of_raw_iron_rage_strenth_bonus_duration_per_lvl * lvl),
                        (int) (ConfigConstructor.heap_of_raw_iron_rage_strenth_amp
                                + ConfigConstructor.heap_of_raw_iron_rage_strenth_bonus_amp_per_lvl * lvl)));
            },
            (clientWorld, stack, player) -> player.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 0.8f, 1.0F),
            List.of(
                    Text.translatable("tooltip.soulsweapons.rage").formatted(Formatting.DARK_RED),
                    Text.translatable("tooltip.soulsweapons.rage.1").formatted(Formatting.GRAY)
            ), 3,
            (int) ConfigConstructor.heap_of_raw_iron_rage_min_cooldown,
            (int) ConfigConstructor.heap_of_raw_iron_rage_cooldown,
            (int) ConfigConstructor.heap_of_raw_iron_rage_reduced_cooldown_per_level
    );

    public HeapOfRawIron(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.heap_of_raw_iron_damage, ConfigConstructor.heap_of_raw_iron_attack_speed, settings, (int) ConfigConstructor.heap_of_raw_iron_posture_loss, ATTRIBUTES);
        this.addAbility(DRAGON_BONUS, RAGE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_heap_of_raw_iron;
    }
}