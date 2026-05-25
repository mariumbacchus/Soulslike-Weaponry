package net.soulsweaponry.compat;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

public class ItemUpgradeCategory implements DisplayCategory<ItemUpgradeDisplay> {

    public static final Identifier TEXTURE = Identifier.of("minecraft", "textures/gui/container/smithing.png");
    public static final CategoryIdentifier<ItemUpgradeDisplay> ITEM_UPGRADE =
            CategoryIdentifier.of(SoulsWeaponry.ModId, "smithing_item_upgrade");

    @Override
    public CategoryIdentifier<? extends ItemUpgradeDisplay> getCategoryIdentifier() {
        return ITEM_UPGRADE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("container.upgrade");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ItemRegistry.TWINKLING_TITANITE.get());
    }

    @Override
    public List<Widget> setupDisplay(ItemUpgradeDisplay display, Rectangle bounds) {
        List<Widget> w = new ArrayList<>();

        // standard background panel
        w.add(Widgets.createRecipeBase(bounds));

        final int x0 = bounds.getX() + 17;
        final int y0 = bounds.getY() + 10;
        final int ySlot = y0;
        final int xTemplate = x0;
        final int xBase = xTemplate + 18;
        final int xAdd = xBase + 18;
        final int xArrow = xAdd + 23;
        final int xResult = xArrow + 34;

        var inputs  = display.getInputEntries();
        var outputs = display.getOutputEntries();

        if (!inputs.isEmpty()) {
            w.add(Widgets.createSlot(new Point(xTemplate, ySlot)).entries(inputs.get(0)));
        }
        if (inputs.size() > 1) {
            w.add(Widgets.createSlot(new Point(xBase, ySlot)).entries(inputs.get(1)));
        }
        if (inputs.size() > 2) {
            w.add(Widgets.createSlot(new Point(xAdd, ySlot)).entries(inputs.get(2)));
        }

        w.add(Widgets.createArrow(new Point(xArrow, ySlot - 1)));

        w.add(Widgets.createResultSlotBackground(new Point(xResult, ySlot))); // big slot background
        if (!outputs.isEmpty()) {
            w.add(Widgets.createSlot(new Point(xResult, ySlot)).disableBackground().markOutput().entries(outputs.get(0)));
        }
        return w;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }
}