package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;
import java.util.Optional;

public interface IHasAbilities {
    // TODO implement into all the abstract item classes (axes, bows, etc.) and use this to build the new ability system off of
    List<IAbility> getAbilities();
    void addAbility(IAbility... abilities);

    default <T extends IAbility> Optional<T> findAbility(Class<T> type) {
        for (IAbility a : getAbilities()) {
            if (type.isInstance(a)) return Optional.of(type.cast(a));
        }
        return Optional.empty();
    }

    static <T extends IAbility> Optional<T> getAbility(ItemStack stack, Class<T> type) {
        if (stack.getItem() instanceof IHasAbilities has) {
            return has.findAbility(type);
        }
        return Optional.empty();
    }

    // TODO remove scaling off of enchants & replace with scaling off of this items upgrade level instead
}
