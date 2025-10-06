package net.soulsweaponry.items.abilities.detonateground;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.DetonateGroundAttributes;

import java.util.List;

/**
 * Also known as Meteor Strike.
 * @param detonateGroundAttributes parameters for what the ground slam AOE does to targets hit,
 *                                 radius/coverage and damage/knockup
 */
public record DetonateGroundAbility(DetonateGroundAttributes detonateGroundAttributes) implements IAbility {

    @Override
    public DetonateGroundAttributes getDetonationAttributes() {
        return this.detonateGroundAttributes;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                //TODO add text describing the detonate ground thingy
        );
    }
}
