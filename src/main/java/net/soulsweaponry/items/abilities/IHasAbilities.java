package net.soulsweaponry.items.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

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

    /**
     * @return true if any ability is charge to use, meaning it will need the {@link net.minecraft.item.Item#onStoppedUsing(ItemStack, World, LivingEntity, int)}
     * call. This will make the item skip all calls from the {@link net.minecraft.item.Item#use(World, PlayerEntity, Hand)} method for abilities.
     * <p>
     * In other words, either only {@code use()} is called or only {@code onStoppedUsing()} is called.
     */
    default boolean hasChargeToUseAbility() {
        for (IAbility a : getAbilities()) {
            if (a.isChargeToUse()) return true;
        }
        return false;
    }

    /**
     * @return true if any ability requires sneaking to use, meaning regular use methods will be skipped if
     * at least one ability returns true.
     */
    default boolean hasSneakToUseAbility() {
        for (IAbility a : getAbilities()) {
            if (a.isSneakAbility()) return true;
        }
        return false;
    }

    /**
     * @return true if any ability requires the item to be in offhand to use, meaning regular use methods
     * will be skipped if at least one ability returns true.
     */
    default boolean hasOffhandToUseAbility() {
        for (IAbility a : getAbilities()) {
            if (a.isOffhandAbility()) return true;
        }
        return false;
    }

    static <T extends IAbility> Optional<T> getAbility(ItemStack stack, Class<T> type) {
        if (stack.getItem() instanceof IHasAbilities has) {
            return has.findAbility(type);
        }
        return Optional.empty();
    }

    // TODO remove scaling off of enchants & replace with scaling off of this items upgrade level instead
}
