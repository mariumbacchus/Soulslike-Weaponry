package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

public interface IKeybindAbility extends IAbility {

    @Override
    default boolean isKeybindAbility() {
        return true;
    }

    @Override
    default List<Text> getBonusAbilityTooltip(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.keybind_ability").formatted(Formatting.DARK_GRAY)
                        .append(KeyBindRegistry.keybindAbility.getBoundKeyLocalizedText())
        );
    }
}
