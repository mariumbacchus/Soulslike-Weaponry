package net.soulsweaponry.util;

import net.minecraft.entity.damage.DamageSource;

public interface IAnimatedDeath {

    void onDeath(DamageSource damageSource);
    void updatePostDeath();
    int getTicksUntilDeath();
    int getDeathTicks();
    void setDeath();
}
