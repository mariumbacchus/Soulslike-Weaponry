package net.soulsweaponry.entity.ai.goal.events;

import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public abstract class ReturningKnightEvent extends BossEvent<ReturningKnight.States, ReturningKnight> {

    public ReturningKnightEvent(ReturningKnight boss, int eventLength) {
        super(boss, eventLength);
    }
}