package net.soulsweaponry.items.abilities.detonateground;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

/**
 * Also known as Meteor Strike.
 * @param detonateGroundAttributes parameters for what the ground slam AOE does to targets hit,
 *                                 radius/coverage and damage/knockup
 */
public record DetonateGroundAbility(DetonateGroundAttributes detonateGroundAttributes) implements IAbility, IDetonateGround {

    @Override
    public DetonateGroundAttributes getDetonationAttributes() {
        return this.detonateGroundAttributes;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.meteor_strike").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.meteor_strike.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.meteor_strike.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.meteor_strike.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.meteor_strike.4").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.meteor_strike.5").formatted(Formatting.GRAY)
        );
    }
}
