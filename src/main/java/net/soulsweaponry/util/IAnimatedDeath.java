package net.soulsweaponry.util;

import net.minecraft.world.damagesource.DamageSource;

public interface IAnimatedDeath {

    void onDeath(DamageSource damageSource);
    void updatePostDeath();
    int getTicksUntilDeath();
    int getDeathTicks();
    void setDeath();
}
