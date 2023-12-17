package net.soulsweapons.entity.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class LifeLeach extends MobEffect {

    public LifeLeach() {
        super(MobEffectCategory.BENEFICIAL, 0x187a02);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int k = 40 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }

    //TODO: This is not used anywhere in the mod. Essentially, it would continuously heal the wielder of the effect
    //TODO: whenever the target would be damaged by other effects such as poison or wither. If implemented in some way
    //TODO: again, consider making a new icon for the effect.
    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        LivingEntity target = entity.getLastHurtByMob();
        if (target instanceof LivingEntity && target.hasEffect(MobEffects.WITHER)) {
            entity.heal(1 + target.getEffect(MobEffects.WITHER).getAmplifier());
        }
    }
}
