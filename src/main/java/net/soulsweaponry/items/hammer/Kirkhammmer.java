package net.soulsweaponry.items.hammer;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAbility;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.items.abilities.posthit.UltraHeavy;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;

import java.util.Map;

public class Kirkhammmer extends TrickWeapon {
    //TODO turn into mace? turn most trickweapon stuff into interface?
    private static final DetonateGroundAttributes ATTRIBUTES = new DetonateGroundAttributes(
            ConfigConstructor.kirkhammer_calculated_fall_base_radius,
            ConfigConstructor.kirkhammer_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.kirkhammer_calculated_fall_target_launch_modifier,
            ConfigConstructor.kirkhammer_calculated_fall_target_max_launch_power,
            ConfigConstructor.kirkhammer_calculated_fall_max_radius,
            ConfigConstructor.kirkhammer_calculated_fall_max_damage,
            ConfigConstructor.kirkhammer_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.kirkhammer_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleRegistry.BLACK_FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {
                int posture = (int) (ConfigConstructor.kirkhammer_calculated_fall_base_posture_loss + ConfigConstructor.kirkhammer_calculated_fall_height_increase_posture_loss_modifier * fallDistance);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1));
                PostureData.addPostureLoss(target, posture);
            },
            (user, fallDistance, stack) -> {}
    );
    private static final DetonateGroundAbility DETONATE_GROUND_ABILITY = new DetonateGroundAbility(ATTRIBUTES);
    private static final UltraHeavy HEAVY_WEAPON = new UltraHeavy((int) ConfigConstructor.kirkhammer_posture_loss, 200, 1);

    public Kirkhammmer(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.kirkhammer_damage, ConfigConstructor.kirkhammer_attack_speed, settings, ConfigConstructor.disable_use_kirkhammer);
        this.addAbility(DETONATE_GROUND_ABILITY, HEAVY_WEAPON);
    }
}
