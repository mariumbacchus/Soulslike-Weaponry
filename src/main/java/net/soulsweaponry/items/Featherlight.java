package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.HashMap;
import java.util.Map;

public class Featherlight extends UltraHeavyWeapon {

    private static final StatusEffectInstance[] CALCULATED_FALL_EFFECTS = new StatusEffectInstance[] {
            new StatusEffectInstance(EffectRegistry.BLIGHT, 200, 4),
            new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 2)
    };

    public Featherlight(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.featherlight_damage, ConfigConstructor.disable_use_featherlight ? 1f : ConfigConstructor.featherlight_attack_speed, settings, true);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.FEATHERLIGHT);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public float getBaseExpansion() {
        return ConfigConstructor.featherlight_calculated_fall_base_radius;
    }

    @Override
    public float getExpansionModifier() {
        return ConfigConstructor.featherlight_calculated_fall_height_increase_radius_modifier;
    }

    @Override
    public float getLaunchModifier() {
        return ConfigConstructor.featherlight_calculated_fall_target_launch_modifier;
    }

    @Override
    public float getMaxLaunchPower() {
        return ConfigConstructor.featherlight_calculated_fall_target_max_launch_power;
    }

    @Override
    public float getMaxExpansion() {
        return ConfigConstructor.featherlight_calculated_fall_max_radius;
    }

    @Override
    public float getMaxDetonationDamage() {
        return ConfigConstructor.featherlight_calculated_fall_max_damage;
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return ConfigConstructor.featherlight_calculated_fall_height_increase_damage_modifier;
    }

    @Override
    public boolean shouldHeal() {
        return ConfigConstructor.featherlight_calculated_fall_should_heal;
    }

    @Override
    public float getHealFromDamageModifier() {
        return ConfigConstructor.featherlight_calculated_fall_heal_from_damage_modifier;
    }

    @Override
    public void doCustomEffects(LivingEntity target, LivingEntity user) {
        for (StatusEffectInstance effect : CALCULATED_FALL_EFFECTS) {
            target.addStatusEffect(effect);
        }
    }

    @Override
    public Map<ParticleEffect, Vec3d> getParticles() {
        Map<ParticleEffect, Vec3d> map = new HashMap<>();
        map.put(ParticleRegistry.PURPLE_FLAME, new Vec3d(1, 6, 1));
        return map;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_featherlight;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_featherlight;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return null;
    }
}