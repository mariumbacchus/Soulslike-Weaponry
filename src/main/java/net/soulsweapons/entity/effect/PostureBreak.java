package net.soulsweapons.entity.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class PostureBreak extends MobEffect {

    public PostureBreak() {
        super(MobEffectCategory.HARMFUL, 0x1c0000);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int k = 10 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 3));
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 9));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 9));
    }
}
