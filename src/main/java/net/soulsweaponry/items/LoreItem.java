package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class LoreItem extends Item {

    private final String name;
    private final int linesOfLore;

    public LoreItem(Settings settings, String name, int linesOfLore) {
        super(settings);
        this.name = name;
        this.linesOfLore = linesOfLore;
    }

    public String getIdName() {
        return this.name;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (Screen.hasControlDown()) {
            for (int i = 1; i < linesOfLore + 1; i++) {
                tooltip.add(Text.translatable("tooltip.soulsweapons." + this.name + ".part_" + i).formatted(Formatting.DARK_GRAY));
            }
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.control"));
        }
    }
}
