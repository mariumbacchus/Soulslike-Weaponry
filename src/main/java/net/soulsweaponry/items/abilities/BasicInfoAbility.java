package net.soulsweaponry.items.abilities;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Ability which doesn't do anything in particular, but needs info on the tooltip.
 * <p>
 * An example is {@link net.soulsweaponry.items.sword.Featherlight} which
 * has high attack speed despite being an ultra heavy weapon, which just means
 * that the attack speed is higher than usual.
 */
public record BasicInfoAbility(List<Text> tooltip) implements IAbility {

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return this.tooltip;
    }
}
