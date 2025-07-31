package net.soulsweaponry.items.hammer;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.items.IDetonateGround;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.DetonateGroundAttributes;

import java.util.Map;

public class Kirkhammmer extends TrickWeapon implements IDetonateGround {

    private final DetonateGroundAttributes attributes = new DetonateGroundAttributes(
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

    public Kirkhammmer(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.kirkhammer_damage, ConfigConstructor.kirkhammer_attack_speed, settings, true, (int) ConfigConstructor.kirkhammer_posture_loss, 0f, ConfigConstructor.disable_use_kirkhammer);
    }

    @Override
    public DetonateGroundAttributes getDetonationAttributes() {
        return this.attributes;
    }
}
