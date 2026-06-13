package net.soulsweaponry.entity.ai.goal.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class Unbreakable extends ReturningKnightEvent {

    public Unbreakable(ReturningKnightGoal goal, ReturningKnight boss, int attackLength) {
        super(goal, boss, attackLength, 10, 0);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 40));
        if (attackStatus == 38) {
            this.playSound(SoundRegistry.NIGHTFALL_SHIELD_EVENT, 0.8f);
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return this.getBoss().getHealth() <= this.getBoss().getMaxHealth() / 2.0F;
    }
}
