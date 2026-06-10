package net.soulsweaponry.items.misc;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.use.Cleanse;
import net.soulsweaponry.items.abilities.use.SharpenItem;

public class SkofnungStone extends ModdedItem {

    private static final SharpenItem SHARPEN_ITEM = new SharpenItem();
    private static final Cleanse CLEANSE = new Cleanse();

    public SkofnungStone(Settings settings) {
        super(settings);
        this.addAbility(SHARPEN_ITEM, CLEANSE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_skofnung_stone;
    }
}