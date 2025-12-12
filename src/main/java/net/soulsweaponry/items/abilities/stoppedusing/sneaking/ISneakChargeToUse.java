package net.soulsweaponry.items.abilities.stoppedusing.sneaking;

import net.soulsweaponry.items.abilities.stoppedusing.IChargeToUse;

/**
 * Automatically sets {@link #isSneakAbility()} to return true.
 */
public interface ISneakChargeToUse extends IChargeToUse {

    @Override
    default boolean isSneakAbility() {
        return true;
    }
}
