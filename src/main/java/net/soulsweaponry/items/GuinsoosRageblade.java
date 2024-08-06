package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;

public class GuinsoosRageblade extends ModdedSword {

    public GuinsoosRageblade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.rageblade_damage, ConfigConstructor.rageblade_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.FURY, WeaponUtil.TooltipAbilities.HASTE, WeaponUtil.TooltipAbilities.FLAME_ENRAGED);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (attacker.isOnFire()) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 2));
        }
        int speed = EnchantmentHelper.getLevel(Enchantments.SWEEPING, stack);
        if (attacker.hasStatusEffect(StatusEffects.HASTE)) {
            StatusEffectInstance effect = attacker.getStatusEffect(StatusEffects.HASTE);
            int amplifier = effect.getAmplifier();
            if (amplifier < 3 + speed || !ConfigConstructor.rageblade_haste_cap) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 60, amplifier + 1));
            } else {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 60, speed + 3));
            }
        } else {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 60, 0));
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_rageblade;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_rageblade;
    }
}