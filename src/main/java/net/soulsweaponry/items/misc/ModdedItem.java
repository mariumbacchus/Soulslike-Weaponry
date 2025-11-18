package net.soulsweaponry.items.misc;

import net.minecraft.item.Item;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;

import java.util.ArrayList;
import java.util.List;

public abstract class ModdedItem extends Item implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedItem(Settings settings) {
        super(settings);
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }
}
