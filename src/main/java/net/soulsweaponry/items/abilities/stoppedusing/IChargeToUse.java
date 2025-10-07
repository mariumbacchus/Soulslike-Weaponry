package net.soulsweaponry.items.abilities.stoppedusing;

import net.soulsweaponry.items.abilities.IAbility;

/**
 * Automatically sets {@link #isChargeToUse()} to return true.
 */
public interface IChargeToUse extends IAbility {

    @Override
    default boolean isChargeToUse() {
        return true;
    }
}
