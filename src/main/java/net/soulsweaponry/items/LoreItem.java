package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class LoreItem extends ModdedItem {

    private final int linesOfLore;
    private final boolean isInfo;

    public LoreItem(Settings settings, int linesOfLore) {
        this(settings, linesOfLore, false);
    }

    public LoreItem(Settings settings, int linesOfLore, boolean isInfo) {
        super(settings);
        this.linesOfLore = linesOfLore;
        this.isInfo = isInfo;
    }

    private String getIdName() {
        Identifier id = Registries.ITEM.getId(this);
        return id.getPath();
    }

    public boolean isInfo() {
        return isInfo;
    }

    public Text[] getInfo() {
        Text[] tooltips = new Text[linesOfLore];
        for (int i = 1; i <= linesOfLore; i++) {
            tooltips[i - 1] = Text.translatable("tooltip.soulsweapons." + getIdName() + ".part_" + i).formatted(Formatting.DARK_GRAY);
        }
        return tooltips;
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return this.isInfo() && this.getInfo().length > 0 ? this.getInfo() : super.getAdditionalTooltips();
    }

    @Override
    public Text[] getLoreTooltips() {
        return !this.isInfo() && this.getInfo().length > 0 ? this.getInfo() : super.getAdditionalTooltips();
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return false;
    }
}
