package net.soulsweaponry.items.abilities.posthit;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.compat.PrometheusCompat;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record BlazingBlade(float baseFireSeconds, float bonusSecondsPerLvl, float fireAspectLvlBonus) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int time = (int) (this.baseFireSeconds + this.bonusSecondsPerLvl * WeaponUtil.getUpgradeLevel(stack));
        if (FabricLoader.getInstance().isModLoaded("soul_fire_d")) {
            int soulFireLvl = PrometheusCompat.getSoulFireAspect(stack);
            if (soulFireLvl > 0) {
                time += (int) (this.fireAspectLvlBonus * soulFireLvl);
                PrometheusCompat.igniteSoulFire(target, time);
                return;
            }
        }
        time += (int) (this.fireAspectLvlBonus * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
        target.setOnFireFor(time);
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
