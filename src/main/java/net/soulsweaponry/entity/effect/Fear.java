package net.soulsweaponry.entity.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class Fear extends MobEffect {
    int destinationReset;
    double x;
    double z;

    public Fear() {
        super(MobEffectCategory.HARMFUL, 0xc76700);
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

    //TODO poorly implemented, will apply the same path for every mob affected. Look for fix
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        this.destinationReset--;
        if (/*!(entity instanceof BossEntity) && */entity instanceof Mob victim) { //TODO add boss entity class
            if (destinationReset < 0) {
                this.x = victim.getX() + victim.getRandom().nextInt(25 - (-25)) + 25;
                this.z = victim.getZ() + victim.getRandom().nextInt(25 - (-25)) + 25;
                this.destinationReset = 40;
            } else {
                victim.getNavigation().createPath(x, victim.getY(), z, 1);
            }
        }
    }
}
