package net.soulsweaponry.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LoreItem extends Item {

    private final String name;
    private final int linesOfLore;

    public LoreItem(Properties pProperties, String name, int linesOfLore) {
        super(pProperties);
        this.name = name;
        this.linesOfLore = linesOfLore;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
        if (Screen.hasControlDown()) {
            for (int i = 1; i < linesOfLore + 1; i++) {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons." + this.name + ".part_" + i).withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.control"));
        }
    }
}
