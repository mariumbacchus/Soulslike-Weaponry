package net.soulsweaponry.entity.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

public class HallowedDragonMist extends MobEffect {

    public HallowedDragonMist() {
        super(MobEffectCategory.NEUTRAL, 0xa915d6);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int k = 50 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player || (entity instanceof TamableAnimal tamed && tamed.getOwner() instanceof Player)) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(amplifier + 1);
            }
        } else {
            entity.hurt(DamageSource.MAGIC, 2.0F + (float) amplifier);
        }
    }
}
