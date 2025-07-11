package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.PostureData;

public interface IUltraHeavy {

    boolean isHeavy();
    int getPostureLoss();

    /**
     * Should be called in post hit method to grant Strength if it has haste.
     * @param user wielder of the weapon
     */
    default void gainStrength(LivingEntity user) {
        if (this.isHeavy() && ConfigConstructor.ultra_heavy_haste_when_strength && user.hasStatusEffect(StatusEffects.STRENGTH)) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 200, 1));
        }
    }

    /**
     * Should be called in post hit method to apply Posture Loss on the target.
     */
    default void applyPostureLoss(LivingEntity target) {
        if (this.isHeavy()) {
            PostureData.addPostureLoss(target, this.getPostureLoss());
        }
    }
}
