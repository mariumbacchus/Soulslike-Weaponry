package net.soulsweaponry.compat;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.UpgradeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ItemUpgradeCategory implements DisplayCategory<ItemUpgradeDisplay> {

    public static final Identifier TEXTURE = Identifier.of("minecraft", "textures/gui/container/smithing.png");

    @Override
    public CategoryIdentifier<? extends ItemUpgradeDisplay> getCategoryIdentifier() {
        return ItemUpgradeREIIds.ITEM_UPGRADE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("container.upgrade");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ItemRegistry.TWINKLING_TITANITE);
    }

    @Override
    public List<Widget> setupDisplay(ItemUpgradeDisplay display, Rectangle bounds) {
        List<Widget> w = new ArrayList<>();
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

        Slot templateSlot = null;
        Slot baseSlot = null;
        Slot addSlot = null;

        if (!inputs.isEmpty()) {
            templateSlot = Widgets.createSlot(new Point(xTemplate, ySlot)).entries(inputs.get(0)).markInput();
            w.add(templateSlot);
        }
        if (inputs.size() > 1) {
            baseSlot = Widgets.createSlot(new Point(xBase, ySlot)).entries(inputs.get(1)).markInput();
            w.add(baseSlot);
        }
        if (inputs.size() > 2) {
            addSlot = Widgets.createSlot(new Point(xAdd, ySlot)).entries(inputs.get(2)).markInput();
            w.add(addSlot);
        }

        w.add(Widgets.createArrow(new Point(xArrow, ySlot - 1)));
        w.add(Widgets.createResultSlotBackground(new Point(xResult, ySlot)));

        Slot resultSlot = Widgets.createSlot(new Point(xResult, ySlot))
                .disableBackground()
                .markOutput();

        if (!outputs.isEmpty()) {
            resultSlot.entries(outputs.get(0));
        }
        w.add(resultSlot);

        if (baseSlot != null) {
            AtomicReference<EntryStack<?>> lastBase = new AtomicReference<>(EntryStack.empty());

            Slot finalBaseSlot = baseSlot;
            w.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
                EntryStack<?> current = finalBaseSlot.getCurrentEntry();
                if (current == null || current.isEmpty()) return;

                if (!current.equals(lastBase.get())) {
                    lastBase.set(current);

                    Object v = current.getValue();
                    if (v instanceof ItemStack baseStack) {
                        ItemStack out = baseStack.copy();

                        int prev = out.getOrDefault(ComponentRegistry.ITEM_UPGRADE_LEVEL, 0);
                        int max  = (int) ConfigConstructor.item_upgrading_max_level;
                        int next = Math.min(prev + 1, max);

                        out.set(ComponentRegistry.ITEM_UPGRADE_LEVEL, next);
                        UpgradeUtil.rebuildUpgradeAttributesForCurrentForm(
                                out, next, display.primaryBonus(), display.secondaryBonus()
                        );

                        resultSlot.clearEntries().entries(EntryIngredients.of(out));
                    }
                }
            }));
        }

        return w;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }
}