package net.soulsweaponry.items.abilities.predicate;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

/**
 * Prevents calls by the item if it isn't equipped in the right slot.
 * Mainly used by armor items.
 * @param slot equipment slot to only allow the item to activate in
 */
public record Equipped(EquipmentSlot slot) implements IAbility {

    public static final Equipped HEAD_SLOT = new Equipped(EquipmentSlot.HEAD);
    public static final Equipped CHEST_SLOT = new Equipped(EquipmentSlot.CHEST);
    public static final Equipped LEGGINGS_SLOT = new Equipped(EquipmentSlot.LEGS);
    public static final Equipped FEET_SLOT = new Equipped(EquipmentSlot.FEET);

    @Override
    public boolean preventUsePredicate(ItemStack stack, PlayerEntity user) {
        return !ItemStack.areItemsEqual(stack, user.getEquippedStack(this.slot));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of();
    }
}
