package net.soulsweaponry.items.hammer;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAbility;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.items.abilities.posthit.UltraHeavy;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;

import java.util.Map;

public class Kirkhammmer extends TrickWeapon {

    private static final DetonateGroundAttributes ATTRIBUTES = new DetonateGroundAttributes(
            WeaponConfig.kirkhammer_calculated_fall_base_radius,
            WeaponConfig.kirkhammer_calculated_fall_height_increase_radius_modifier,
            WeaponConfig.kirkhammer_calculated_fall_target_launch_modifier,
            WeaponConfig.kirkhammer_calculated_fall_target_max_launch_power,
            WeaponConfig.kirkhammer_calculated_fall_max_radius,
            WeaponConfig.kirkhammer_calculated_fall_max_damage,
            WeaponConfig.kirkhammer_calculated_fall_height_increase_damage_modifier,
            WeaponConfig.kirkhammer_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleRegistry.BLACK_FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {
                int posture = (int) (WeaponConfig.kirkhammer_calculated_fall_base_posture_loss + WeaponConfig.kirkhammer_calculated_fall_height_increase_posture_loss_modifier * fallDistance);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1));
                PostureData.addPostureLoss(target, posture);
            },
            (user, fallDistance, stack) -> {}
    );
    private static final DetonateGroundAbility DETONATE_GROUND_ABILITY = new DetonateGroundAbility(ATTRIBUTES);
    private static final UltraHeavy HEAVY_WEAPON = new UltraHeavy((int) WeaponConfig.kirkhammer_posture_loss, 200, 1);

    public Kirkhammmer(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.kirkhammer_damage, WeaponConfig.kirkhammer_attack_speed, settings, WeaponConfig.disable_use_kirkhammer);
        this.addAbility(DETONATE_GROUND_ABILITY, HEAVY_WEAPON);
    }
}
