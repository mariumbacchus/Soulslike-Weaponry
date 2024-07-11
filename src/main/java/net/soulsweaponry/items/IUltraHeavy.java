package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.config.ConfigConstructor;

public interface IUltraHeavy {

    boolean isHeavy();

    /**
     * Should be called in post hit method to grant Strength if it has haste.
     * @param user wielder of the weapon
     */
    default void gainStrength(LivingEntity user) {
        if (this.isHeavy() && ConfigConstructor.ultra_heavy_haste_when_strength && user.hasStatusEffect(StatusEffects.STRENGTH)) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 200, 1));
        }
    }
}
