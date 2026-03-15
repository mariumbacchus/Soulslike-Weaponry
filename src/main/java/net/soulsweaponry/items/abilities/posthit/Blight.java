package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public class Blight extends ApplyStackingEffect {

    public final int ampNeededForExtraEffect;
    public final String extraEffectId;
    public final int extraEffectAmp;
    public final float bonusExtraEffectAmpPerLvl;
    public final int extraEffectDuration;
    public final int extraEffectDurationPerLvl;

    public Blight(String statusEffectId, int startAmp, int bonusAmpPostHit, float bonusAmpPostHitPerLvl,
                  int maxAmp, float bonusMaxAmpPerLvl, int duration, int bonusDurationPerLvl,
                  int ampNeededForExtraEffect, String extraEffectId, int extraEffectAmp,
                  float bonusExtraEffectAmpPerLvl, int extraEffectDuration, int extraEffectDurationPerLvl
    ) {
        super(true, statusEffectId, startAmp, bonusAmpPostHit, bonusAmpPostHitPerLvl, maxAmp, bonusMaxAmpPerLvl, duration, bonusDurationPerLvl);
        this.ampNeededForExtraEffect = ampNeededForExtraEffect;
        this.extraEffectId = extraEffectId;
        this.extraEffectAmp = extraEffectAmp;
        this.bonusExtraEffectAmpPerLvl = bonusExtraEffectAmpPerLvl;
        this.extraEffectDuration = extraEffectDuration;
        this.extraEffectDurationPerLvl = extraEffectDurationPerLvl;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHit(stack, target, attacker);
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        StatusEffect statusEffect = WeaponUtil.parseStatusEffectId(this.statusEffectId);
        if (!target.hasStatusEffect(statusEffect)) {
            return;
        }
        StatusEffectInstance effect = target.getStatusEffect(statusEffect);
        int needed = effect.getAmplifier();
        if (needed >= this.ampNeededForExtraEffect) {
            int amp = (int) (this.extraEffectAmp + this.bonusExtraEffectAmpPerLvl * lvl);
            int duration = this.extraEffectDuration + this.extraEffectDurationPerLvl * lvl;
            StatusEffect extraEffect = WeaponUtil.parseStatusEffectId(this.extraEffectId);
            target.addStatusEffect(new StatusEffectInstance(extraEffect, duration, amp));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>(super.getTooltipAbilities(stack));
        Text prevEffect = WeaponUtil.parseStatusEffectId(this.statusEffectId).getName();
        Text newEffect = WeaponUtil.parseStatusEffectId(this.extraEffectId).getName();
        MutableText formattedPrev = prevEffect.copy().formatted(Formatting.LIGHT_PURPLE);
        MutableText formattedNew = newEffect.copy().formatted(Formatting.WHITE);
        tooltip.add(Text.translatable("tooltip.soulsweapons.blight.1", formattedPrev,
                this.ampNeededForExtraEffect, formattedNew).formatted(Formatting.GRAY));
        if (WeaponUtil.parseStatusEffectId(this.statusEffectId).equals(EffectRegistry.BLIGHT)) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.blight.2").formatted(Formatting.DARK_GRAY));
        }
        return tooltip;
    }
}
