package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class EffectAttacker extends ApplyEffect {

    public EffectAttacker(String statusEffectId, int startAmp,
                          int bonusAmpPostHit, float bonusAmpPostHitPerLvl,
                          int maxAmp, float bonusMaxAmpPerLvl,
                          int duration, int bonusDurationPerLvl
    ) {
        super(false, statusEffectId, startAmp, bonusAmpPostHit,
                bonusAmpPostHitPerLvl, maxAmp, bonusMaxAmpPerLvl, duration, bonusDurationPerLvl
        );
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        Text text = WeaponUtil.parseStatusEffectId(this.statusEffectId).value().getName();
        MutableText formatted = text.copy().formatted(Formatting.GOLD);
        return List.of(
                Text.translatable("tooltip.soulsweapons.fury").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.fury.1", formatted).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.fury_cap").formatted(Formatting.YELLOW),
                Text.translatable("tooltip.soulsweapons.fury_cap.1", formatted).formatted(Formatting.GRAY)
        );
    }
}
