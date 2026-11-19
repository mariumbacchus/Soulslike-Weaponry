package net.soulsweaponry.entity.ai.goal.events;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.config.ReturningKnightConfig;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class Unbreakable extends ReturningKnightEvent {

    public Unbreakable(ReturningKnight boss, int eventLength) {
        super(boss, eventLength);
    }

    @Override
    public void tickEvent(int eventTick) {
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 40));
        if (this.isTick(eventTick, 38)) {
            this.playSound(SoundRegistry.NIGHTFALL_SHIELD_EVENT, 1f);
        }
    }

    @Override
    public boolean canTrigger() {
        return !this.getBoss().isPhaseTwo() && this.getBoss().getHealth() <= this.getBoss().getMaxHealth() * ReturningKnightConfig.returning_knight_unbreakable_percent_health_trigger;
    }
}