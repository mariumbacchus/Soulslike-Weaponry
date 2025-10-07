package net.soulsweaponry.items.sword;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import net.soulsweaponry.util.TooltipAbilities;

import java.util.Map;

public class Featherlight extends UltraHeavyWeapon {

    private static final StatusEffectInstance[] CALCULATED_FALL_EFFECTS = new StatusEffectInstance[] {
            new StatusEffectInstance(EffectRegistry.BLIGHT, 200, 4),
            new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 2)
    };
    private final DetonateGroundAttributes attributes = new DetonateGroundAttributes(
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

    public Featherlight(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.featherlight_damage, ConfigConstructor.disable_use_featherlight ? 1f : ConfigConstructor.featherlight_attack_speed, settings, true);
        this.addTooltipAbility(TooltipAbilities.FEATHERLIGHT);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_featherlight;
    }

    @Override
    public DetonateGroundAttributes getDetonationAttributes() {
        return attributes;
    }

    @Override
    public int getPostureLoss() {
        return (int) ConfigConstructor.featherlight_posture_loss;
    }
}