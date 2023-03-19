package net.soulsweaponry.items;

import java.util.List;

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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.CustomDamageSource;

public class LichBane extends SwordItem {
    
    public LichBane(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.lich_bane_damage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHit(stack, target, attacker);
        //So, xp doesn't drop regardless of what the damage source was, and some counterplay
        //is needed to not make the weapon too op in pvp, therefore it cannot execute
        //anything or damage anything below 33% hp, problem solved😎
        if (target.getHealth() > target.getMaxHealth()/3 && target.getHealth() > this.getBonusMagicDamage(stack)) {
            target.damage(CustomDamageSource.TRUE_MAGIC, this.getBonusMagicDamage(stack));
        }
        target.setOnFireFor(4 + 3 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
        return true;
    }

    public float getBonusMagicDamage(ItemStack stack) {
        return ConfigConstructor.lich_bane_bonus_magic_damage + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MAGIC_DAMAGE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.BLAZING_BLADE, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        
        super.appendTooltip(stack, world, tooltip, context);
    }
}
