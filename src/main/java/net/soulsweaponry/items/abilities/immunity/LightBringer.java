package net.soulsweaponry.items.abilities.immunity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.Set;

public class LightBringer extends EffectImmunity {

    private final int positiveEffectsDuration;
    private final float effectsBonusDurationPerLvl;
    private final int positiveEffectsAmp;
    private final float effectsBonusAmpPerLvl;

    public LightBringer(int positiveEffectsDuration, float effectsBonusDurationPerLvl, int positiveEffectsAmp, float effectsBonusAmpPerLvl) {
        super(Set.of(StatusEffects.DARKNESS, StatusEffects.BLINDNESS));
        this.positiveEffectsDuration = positiveEffectsDuration;
        this.effectsBonusDurationPerLvl = effectsBonusDurationPerLvl;
        this.positiveEffectsAmp = positiveEffectsAmp;
        this.effectsBonusAmpPerLvl = effectsBonusAmpPerLvl;
    }

    @Override
    public void onStatusEffectDeclined(LivingEntity entity, StatusEffectInstance declinedEffectInstance, ItemStack stack) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        int duration = (int) (this.positiveEffectsDuration + this.effectsBonusDurationPerLvl * lvl);
        int amp = (int) (this.positiveEffectsAmp + this.effectsBonusAmpPerLvl * lvl);
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, duration, amp));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, amp));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, duration, amp));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.lightbringer").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.lightbringer.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.lightbringer.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.lightbringer.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.lightbringer.4").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC),
                Text.translatable("tooltip.soulsweapons.lightbringer.5").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC)
        );
    }
}
