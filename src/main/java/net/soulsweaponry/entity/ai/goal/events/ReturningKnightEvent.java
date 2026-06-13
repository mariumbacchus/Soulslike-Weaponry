package net.soulsweaponry.entity.ai.goal.events;

import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public abstract class ReturningKnightEvent extends BossEvent<ReturningKnight.States, ReturningKnight, ReturningKnightGoal> {

    public ReturningKnightEvent(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, 0, cooldown, specialCooldown);
    }
}
