package net.soulsweaponry.entity.ai.goal.events;

import net.soulsweaponry.entity.ai.goal.BossGoal;
import net.soulsweaponry.entity.ai.goal.attacks.BossAttack;
import net.soulsweaponry.entity.mobs.boss.BossEntity;

public abstract class BossEvent<S extends Enum<S>, B extends BossEntity<S>, G extends BossGoal<S, B, G>> extends BossAttack<S, B, G> {

    private boolean completed;

    /**
     * Events such as Returning Knight's Unbreakable event, they aren't strictly attacks and shouldn't be in
     * the attack map. These should also likely only happen once, so weight is obsolete. When the event
     * is finished, {@link #completed} will be set to true so that this event will no longer trigger again.
     * @param goal            parent goal the event is tied to
     * @param boss            the boss the event is tied to
     * @param attackLength    length of the animation/event in ticks
     * @param cooldown        cooldown after the event is finished before starting to attack again
     * @param specialCooldown special cooldown if the event was special, set to 0 if not special
     */
    public BossEvent(G goal, B boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    public boolean hasCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public void resetAttackVariables() {
        this.setCompleted(true);
    }
}
