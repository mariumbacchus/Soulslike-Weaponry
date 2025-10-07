package net.soulsweaponry.items.abilities.stoppedusing;

/**
 * Automatically sets {@link #isSneakAbility()} to return true.
 */
public interface ISneakChargeToUse extends IChargeToUse {

    @Override
    default boolean isSneakAbility() {
        return true;
    }
}
