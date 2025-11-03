package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.Locale;

public record EffectPostHit(
        String statusEffectId, int startAmp,
        int bonusAmpPostHit, float bonusAmpPostHitPerLvl,
        int maxAmp, float bonusMaxAmpPerLvl
) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        RegistryEntry<StatusEffect> statusEffect = parseStatusEffectId(this.statusEffectId);
        if (attacker.hasStatusEffect(statusEffect)) {
            StatusEffectInstance effect = attacker.getStatusEffect(statusEffect);
            int amp = effect.getAmplifier();
            int maxAmp = (int) (this.maxAmp + this.bonusMaxAmpPerLvl * lvl);
            int newAmp = (int) Math.min(maxAmp, amp + this.bonusAmpPostHit + this.bonusAmpPostHitPerLvl * lvl);
            attacker.addStatusEffect(new StatusEffectInstance(statusEffect, 60, newAmp));
        } else {
            attacker.addStatusEffect(new StatusEffectInstance(statusEffect, 60, this.startAmp));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        Text text = parseStatusEffectId(this.statusEffectId).value().getName();
        MutableText formatted = text.copy().formatted(Formatting.GOLD);
        return List.of(
                Text.translatable("tooltip.soulsweapons.fury").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.fury.1", formatted).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.fury_cap").formatted(Formatting.YELLOW),
                Text.translatable("tooltip.soulsweapons.fury_cap.1", formatted).formatted(Formatting.GRAY)
        );
    }

    public static RegistryEntry<StatusEffect> parseStatusEffectId(String statusEffectId) {
        RegistryEntry<StatusEffect> defaultEntry = StatusEffects.HASTE;
        if (statusEffectId == null || statusEffectId.isBlank()) {
            return defaultEntry;
        }
        Identifier directId = Identifier.tryParse(statusEffectId.toLowerCase(Locale.ROOT));
        if (directId != null) {
            StatusEffect eff = Registries.STATUS_EFFECT.get(directId);
            if (eff != null) {
                return Registries.STATUS_EFFECT.getEntry(eff);
            }
        }
        return defaultEntry;
    }
}
