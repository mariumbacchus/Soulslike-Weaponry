package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class ApplyStackingEffect implements IAbility {

    public final boolean applyOnTarget;
    public final String statusEffectId;
    public final int startAmp;
    public final int bonusAmpPostHit;
    public final float bonusAmpPostHitPerLvl;
    public final int maxAmp;
    public final float bonusMaxAmpPerLvl;
    public final int duration;
    public final int bonusDurationPerLvl;

    public ApplyStackingEffect(boolean applyOnTarget, String statusEffectId, int startAmp,
                               int bonusAmpPostHit, float bonusAmpPostHitPerLvl,
                               int maxAmp, float bonusMaxAmpPerLvl,
                               int duration, int bonusDurationPerLvl
    ) {
        this.applyOnTarget = applyOnTarget;
        this.statusEffectId = statusEffectId;
        this.startAmp = startAmp;
        this.bonusAmpPostHit = bonusAmpPostHit;
        this.bonusAmpPostHitPerLvl = bonusAmpPostHitPerLvl;
        this.maxAmp = maxAmp;
        this.bonusMaxAmpPerLvl = bonusMaxAmpPerLvl;
        this.duration = duration;
        this.bonusDurationPerLvl = bonusDurationPerLvl;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        LivingEntity entity = this.applyOnTarget ? target : attacker;
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        StatusEffect statusEffect = WeaponUtil.parseStatusEffectId(this.statusEffectId);
        int duration = this.duration + this.bonusDurationPerLvl * lvl;
        if (entity.hasStatusEffect(statusEffect)) {
            StatusEffectInstance effect = entity.getStatusEffect(statusEffect);
            int amp = effect.getAmplifier();
            int maxAmp = (int) (this.maxAmp + this.bonusMaxAmpPerLvl * lvl);
            int newAmp = (int) Math.min(maxAmp, amp + this.bonusAmpPostHit + this.bonusAmpPostHitPerLvl * lvl);
            entity.addStatusEffect(new StatusEffectInstance(statusEffect, duration, newAmp));
        } else {
            entity.addStatusEffect(new StatusEffectInstance(statusEffect, duration, this.startAmp));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        Text text = WeaponUtil.parseStatusEffectId(this.statusEffectId).getName();
        MutableText formatted = text.copy().formatted(Formatting.LIGHT_PURPLE);
        return List.of(
                Text.translatable("tooltip.soulsweapons.tainted_edge").formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.BOLD),
                Text.translatable("tooltip.soulsweapons.tainted_edge.1", formatted).formatted(Formatting.GRAY)
        );
    }
}
