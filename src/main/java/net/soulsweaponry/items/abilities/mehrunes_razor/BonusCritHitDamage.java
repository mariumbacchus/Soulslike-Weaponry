package net.soulsweaponry.items.abilities.mehrunes_razor;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

/**
 * When triggering Posture Break critical hit, deal % more damage.
 * @param percentBonus percent bonus damage, i.e. 100% bonus means the critical strike deals double damage
 *                     (not the initial attack, just the posture damage bonus when triggered is increased)
 */
public record BonusCritHitDamage(double percentBonus) implements IAbility {

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.posture_crit_bonus").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.posture_crit_bonus.1", String.format("%.1f", this.percentBonus * 100) + "%").formatted(Formatting.GRAY)
        );
    }
}
