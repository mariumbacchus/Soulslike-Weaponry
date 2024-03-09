package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.soulsweaponry.entity.mobs.BossEntity;

public class Fear extends StatusEffect {

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
        if (!(entity instanceof BossEntity) && entity instanceof MobEntity target) {
            if (target.getTarget() != null) {
                target.setTarget(null);
            }
            double x = target.getX() + target.getRandom().nextBetween(-25, 25);
            double z = target.getZ() + target.getRandom().nextBetween(-25, 25);
            target.getNavigation().startMovingTo(x, target.getY(), z, 1.1f);
        }      
    }
}
