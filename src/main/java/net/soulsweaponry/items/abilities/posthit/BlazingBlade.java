package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record BlazingBlade(float baseFireSeconds, float bonusSecondsPerLvl, float fireAspectLvlBonus) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(this.baseFireSeconds
                + this.bonusSecondsPerLvl * WeaponUtil.getUpgradeLevel(stack)
                + this.fireAspectLvlBonus * WeaponUtil.getLevel(stack, Enchantments.FIRE_ASPECT));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.blazing_blade").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.blazing_blade.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.blazing_blade.2").formatted(Formatting.GRAY)
        );
    }
}
