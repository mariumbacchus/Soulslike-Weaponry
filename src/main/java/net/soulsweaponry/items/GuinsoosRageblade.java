package net.soulsweaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;

public class GuinsoosRageblade extends SwordItem {
    
    public GuinsoosRageblade(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.rageblade_damage, attackSpeed, settings);
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) { 
        super.postHit(stack, target, attacker);
        
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

        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.FURY, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HASTE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.FLAME_ENRAGED, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        
        super.appendTooltip(stack, world, tooltip, context);
    }
}
