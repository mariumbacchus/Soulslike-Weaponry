package net.soulsweaponry.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class HallowedDragonMist extends StatusEffect{
    public HallowedDragonMist() {
        super(
            StatusEffectCategory.NEUTRAL,
            0xa915d6
        );
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int k = 50 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity || (entity instanceof Tameable tamed && tamed.getOwner() instanceof PlayerEntity)) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(amplifier + 1);
            }
        } else {
            entity.damage(DamageSource.MAGIC, 2.0F + (float) amplifier);
        }
    }
}
