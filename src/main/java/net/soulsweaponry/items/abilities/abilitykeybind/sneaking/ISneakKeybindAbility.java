package net.soulsweaponry.items.abilities.abilitykeybind.sneaking;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.items.abilities.abilitykeybind.IKeybindAbility;

import java.util.List;

public interface ISneakKeybindAbility extends IKeybindAbility {

    @Override
    default boolean isSneakAbility() {
        return true;
    }

    @Override
    default List<Text> getBonusAbilityTooltip(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.sneaking_keybind_ability").formatted(Formatting.DARK_GRAY)
                        .append(KeyBindRegistry.keybindAbility.getBoundKeyLocalizedText())
        );
    }
}
