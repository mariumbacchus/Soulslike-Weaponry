package net.soulsweaponry.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.api.trickweapon.TrickWeaponUtil;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.items.abilities.IHasAbilities;

import java.util.List;

public class TooltipUtil {

    public static void addAbilityTooltip(TooltipAbilities ability, ItemStack stack, List<Text> tooltip) {
        switch (ability) {
            case TRICK_WEAPON -> {
                Text text = TrickWeaponUtil.getMappedItemName(stack);
                if (text != null) {
                    Item item = stack.getItem();
                    if (IHasAbilities.shouldShowInfo()) {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon").formatted(Formatting.WHITE));
                        tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon.1", IHasAbilities.formatKeybindText(KeyBindRegistry.switchWeapon.getBoundKeyLocalizedText())).formatted(Formatting.GRAY));
                        tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon.2", text.copy().formatted(Formatting.WHITE)).formatted(Formatting.DARK_GRAY));
                    } else {
                        if (item instanceof IHasAbilities has && has.getAbilities().isEmpty()) {
                            IHasAbilities.addShowInfoText(tooltip);
                        }
                    }
                }
            }
        }
    }

    public enum TooltipAbilities {
        TRICK_WEAPON
    }
}
