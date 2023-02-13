package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;

public class Fear extends StatusEffect {
    int destinationReset;
    double x;
    double z;

    public Fear() {
        super(StatusEffectCategory.HARMFUL, 0xc76700);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int k = 10 >> amplifier;
         if (k > 0) {
            return duration % k == 0;
         } else {
            return true;
         }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        this.destinationReset--;
        if (entity instanceof MobEntity) {
            MobEntity victim = (MobEntity)entity;
            if (destinationReset < 0) {
                this.x = victim.getX() + victim.getRandom().nextBetween(-25, 25);
                this.z = victim.getZ() + victim.getRandom().nextBetween(-25, 25);
                this.destinationReset = 40;
            } else {
                victim.getNavigation().startMovingTo(x, victim.getY(), z, 1.1f);
            }
        }      
    }
}
