package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.WeaponUtil;

public class LichBane extends ModdedSword {

    public LichBane(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.lich_bane_damage, ConfigConstructor.lich_bane_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.MAGIC_DAMAGE, WeaponUtil.TooltipAbilities.BLAZING_BLADE);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (target.getHealth() > target.getMaxHealth()/3 && target.getHealth() > this.getBonusMagicDamage(stack)) {
            target.damage(CustomDamageSource.TRUE_MAGIC, this.getBonusMagicDamage(stack));
        }
        target.setOnFireFor(4 + 3 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
        return super.postHit(stack, target, attacker);
    }

    public float getBonusMagicDamage(ItemStack stack) {
        return ConfigConstructor.lich_bane_bonus_magic_damage + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_lich_bane;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_lich_bane;
    }
}