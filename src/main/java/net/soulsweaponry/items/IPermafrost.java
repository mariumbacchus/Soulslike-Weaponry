package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.registry.EffectRegistry;

public interface IPermafrost {

    int getFrostBuildup();

    default void applyPermafrost(LivingEntity attacker, LivingEntity target, int duration, int amp) {
        FrostData.setFrostSource(target, attacker);
        FrostData.addFrost(target, this.getFrostBuildup());
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, duration, amp));
    }
}
