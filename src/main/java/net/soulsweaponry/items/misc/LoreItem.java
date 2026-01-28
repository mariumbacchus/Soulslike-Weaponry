package net.soulsweaponry.items.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class LoreItem extends ModdedItem {

    private final int linesOfLore;
    private final boolean isInfo;

    public LoreItem(Item.Settings settings, int linesOfLore) {
        this(settings, linesOfLore, false);
    }

    public LoreItem(Item.Settings settings, int linesOfLore, boolean isInfo) {
        super(settings);
        this.linesOfLore = linesOfLore;
        this.isInfo = isInfo;
    }

    private String getIdName() {
        Identifier id = Registries.ITEM.getId(this);
        return id.getPath();
    }

    public List<Text> getInfo() {
        List<Text> tooltips = new ArrayList<>(linesOfLore);
        for (int i = 1; i <= linesOfLore; i++) {
            tooltips.add(Text.translatable("tooltip.soulsweapons." + getIdName() + ".part_" + i).formatted(Formatting.DARK_GRAY));
        }
        return tooltips;
    }

    @Override
    public List<Text> getItemLore() {
        return !this.isInfo && !this.getInfo().isEmpty() ? this.getInfo() : List.of();
    }

    @Override
    public List<Text> getAdditionalItemTooltips() {
        return this.isInfo && !this.getInfo().isEmpty() ? this.getInfo() : List.of();
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return false;
    }
}