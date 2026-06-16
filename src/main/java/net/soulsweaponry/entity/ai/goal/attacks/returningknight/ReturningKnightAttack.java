package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.BossAttack;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public abstract class ReturningKnightAttack extends BossAttack<ReturningKnight.States, ReturningKnight, ReturningKnightGoal> {

    public ReturningKnightAttack(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }
}
