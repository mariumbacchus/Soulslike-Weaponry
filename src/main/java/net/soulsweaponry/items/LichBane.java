package net.soulsweaponry.items;

import java.util.List;

import net.soulsweaponry.mixin.LivingEntityInvoker;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;

public class LichBane extends SwordItem {
    
    public LichBane(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.lich_bane_damage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (ConfigConstructor.disable_use_lich_bane) {
            if (ConfigConstructor.inform_player_about_disabled_use) {
                attacker.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.useDisabled","This item is disabled"));
            }
            return super.postHit(stack, target, attacker);
        }
        if (target.getHealth() > target.getMaxHealth()/3 && target.getHealth() > this.getBonusMagicDamage(stack)) {
            ((LivingEntityInvoker)target).invokeApplyDamage(attacker.getWorld().getDamageSources().magic(), this.getBonusMagicDamage(stack));
        }
        target.setOnFireFor(4 + 3 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
        return super.postHit(stack, target, attacker);
    }

    public float getBonusMagicDamage(ItemStack stack) {
        return ConfigConstructor.lich_bane_bonus_magic_damage + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (ConfigConstructor.disable_use_lich_bane) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MAGIC_DAMAGE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.BLAZING_BLADE, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        
        super.appendTooltip(stack, world, tooltip, context);
    }
}
