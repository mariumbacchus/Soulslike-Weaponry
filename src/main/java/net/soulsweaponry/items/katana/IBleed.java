package net.soulsweaponry.items.katana;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.registry.EffectRegistry;

public interface IBleed {

    int getBleedAmount();

    default void applyBleed(LivingEntity attacker, LivingEntity target) {
        int amp = (attacker.hasStatusEffect(EffectRegistry.BLOODTHIRSTY.get()) ? attacker.getStatusEffect(EffectRegistry.BLOODTHIRSTY.get()).getAmplifier() : 0) + 1;
        BleedData.addBleed(target, (int) (this.getBleedAmount() * ConfigConstructor.bleed_post_hit_bloodthirsty_effect_increase_mod * amp));
    }
}