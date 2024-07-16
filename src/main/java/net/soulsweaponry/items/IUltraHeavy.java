package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.config.CommonConfig;

public interface IUltraHeavy {

    boolean isHeavy();

    /**
     * Should be called in post hit method to grant Strength if it has haste.
     * @param user wielder of the weapon
     */
    default void gainStrength(LivingEntity user) {
        if (this.isHeavy() && CommonConfig.ULTRA_HEAVY_HASTE_WHEN_STRENGTH.get() && user.hasStatusEffect(StatusEffects.STRENGTH)) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 200, 1));
        }
    }
}
