package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ApplyMagicFrailty(
        int duration, float durationPerLvl, int amp, float ampPerLvl
) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int duration = (int) (this.duration + WeaponUtil.getUpgradeLevel(stack) * this.durationPerLvl);
        int amp = (int) (this.amp + WeaponUtil.getUpgradeLevel(stack) * this.ampPerLvl);
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.MAGIC_FRAILTY, duration, amp));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.magic_frailty").formatted(Formatting.BLUE),
                Text.translatable("tooltip.soulsweapons.magic_frailty.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.magic_frailty.2").formatted(Formatting.GRAY)
        );
    }
}
