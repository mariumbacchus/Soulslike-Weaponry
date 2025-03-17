package net.soulsweaponry.items;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class LoreItem extends Item {

    private final int linesOfLore;
    private final boolean isInfo;

    public LoreItem(Settings settings, int linesOfLore) {
        super(settings);
        this.linesOfLore = linesOfLore;
        this.isInfo = false;
    }

    public LoreItem(Settings settings, int linesOfLore, boolean isInfo) {
        super(settings);
        this.linesOfLore = linesOfLore;
        this.isInfo = isInfo;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        boolean bl = this.isInfo ? Screen.hasShiftDown() : Screen.hasControlDown();
        if (bl) {
            for (int i = 1; i < linesOfLore + 1; i++) {
                tooltip.add(Text.translatable("tooltip.soulsweapons." + this.getIdName(stack) + ".part_" + i).formatted(Formatting.DARK_GRAY));
            }
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons." + (this.isInfo ? "shift" : "control")));
        }
    }

    private String getIdName(ItemStack stack) {
        Identifier id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return id.getPath();
    }

    public boolean isInfo() {
        return isInfo;
    }
}