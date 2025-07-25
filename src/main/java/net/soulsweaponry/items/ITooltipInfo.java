package net.soulsweaponry.items;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.mixin.KeyBindingAccessor;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.TooltipUtil;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface ITooltipInfo {

    List<TooltipAbilities> getTooltipAbilities();
    Text[] getAdditionalTooltips();
    void addTooltipAbility(TooltipAbilities... abilities);

    default Text[] getLoreTooltips() {
        return new Text[0];
    }

    default boolean removeTooltipAbility(TooltipAbilities ability) {
        return this.getTooltipAbilities().remove(ability);
    }

    /**
     * Adds all tooltip abilities listed in {@link #getTooltipAbilities()} and {@link #getAdditionalTooltips()} to the
     * item tooltip. {@link WeaponUtil} handles the displaying of {@link TooltipAbilities}.
     */
    default void appendTooltipAbilities(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.getAdditionalTooltips().length > 0 || (this.getTooltipAbilities() != null && !this.getTooltipAbilities().isEmpty())) {
            if (shouldShowInfo()) {
                for (TooltipAbilities ability : this.getTooltipAbilities()) {
                    TooltipUtil.addAbilityTooltip(ability, stack, tooltip);
                }
                tooltip.addAll(Arrays.asList(this.getAdditionalTooltips()));
            } else {
                addShowInfoText(tooltip);
            }
        }
        if (this.getLoreTooltips().length > 0) {
            if (shouldShowLore()) {
                tooltip.addAll(Arrays.asList(this.getLoreTooltips()));
            } else {
                addShowLoreText(tooltip);
            }
        }
    }

    /**
     * @return Whether the info button is being held when hovering an item, button is ALT if Epic Fight mod
     * is installed or SHIFT otherwise by default, can be changed in controls settings.
     */
    static boolean shouldShowInfo() {
        if (ClientConfig.always_show_item_tooltip) {
            return true;
        }
        if (KeyBindRegistry.showItemTooltip.isUnbound()) {
            boolean epicFight = WeaponUtil.isModLoaded("epicfight");
            return epicFight ? Screen.hasAltDown() : Screen.hasShiftDown();
        }
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyBindingAccessor)KeyBindRegistry.showItemTooltip).getBoundKey().getCode());
    }

    static boolean shouldShowLore() {
        if (ClientConfig.always_show_item_lore) {
            return true;
        }
        if (KeyBindRegistry.showItemLore.isUnbound()) {
            return Screen.hasControlDown();
        }
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), ((KeyBindingAccessor)KeyBindRegistry.showItemLore).getBoundKey().getCode());
    }

    static Text getShowInfoKeyText() {
        if (KeyBindRegistry.showItemTooltip.isUnbound()) {
            boolean epicFight = WeaponUtil.isModLoaded("epicfight");
            return epicFight ? Text.translatable("key.keyboard.left.alt") : Text.translatable("key.keyboard.left.shift");
        }
        return KeyBindRegistry.showItemTooltip.getBoundKeyLocalizedText();
    }

    static Text getShowLoreKeyText() {
        if (KeyBindRegistry.showItemLore.isUnbound()) {
            return Text.translatable("key.keyboard.left.control");
        }
        return KeyBindRegistry.showItemLore.getBoundKeyLocalizedText();
    }

    static MutableText formatKeybindText(Text input) {
        MutableText text = input.copy();
        String upper = text.getString().toUpperCase(Locale.ROOT);
        return Text.literal(upper).formatted(Formatting.YELLOW);
    }

    static void addShowInfoText(List<Text> tooltip) {
        MutableText keyText = formatKeybindText(getShowInfoKeyText());
        tooltip.add(Text.translatable("tooltip.soulsweapons.show_item_info", keyText));
    }

    static void addShowLoreText(List<Text> tooltip) {
        MutableText keyText = formatKeybindText(getShowLoreKeyText());
        tooltip.add(Text.translatable("tooltip.soulsweapons.show_item_lore", keyText));
    }
}