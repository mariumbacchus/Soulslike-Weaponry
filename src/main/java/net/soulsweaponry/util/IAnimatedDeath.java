package net.soulsweaponry.util;

import net.minecraft.entity.damage.DamageSource;

/**
 * Used to do custom death animations by disabling red tint on death and extend the death time.
 * Remember to override {@link net.minecraft.entity.LivingEntity#updatePostDeath} to actually implement custom values.
 */
public interface IAnimatedDeath {

    void onDeath(DamageSource damageSource);
    int getTicksUntilDeath();
    int getDeathTicks();
    void setDeath();
}
