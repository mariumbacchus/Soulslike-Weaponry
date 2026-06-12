package net.soulsweaponry.entity.ai.goal.attacks;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.entity.ai.goal.BossGoal;
import net.soulsweaponry.entity.mobs.boss.BossEntity;

public abstract class BossAttack {

    private final BossGoal<BossEntity> goal;
    private final int attackLength;
    private final int cooldown;
    private final int specialCooldown;

    public BossAttack(BossGoal<BossEntity> goal, int attackLength, int cooldown, int specialCooldown) {
        this.goal = goal;
        this.attackLength = attackLength;
        this.cooldown = cooldown;
        this.specialCooldown = specialCooldown;
    }

    public BossAttack(BossGoal<BossEntity> goal, int attackLength, int cooldown) {
        this(goal, attackLength, cooldown, 0);
    }

    public void tick(LivingEntity target) {
        this.goal.increaseAttackStatus();
        this.tickAttack(target);
        this.goal.checkAndReset(this.cooldown, this.specialCooldown);
    }

    public abstract void tickAttack(LivingEntity target);

    /**
     * Called for whether the attack can be chosen and triggered.
     * Example of implementation include returning goal.isSpecialCoolingDown();
     */
    public abstract boolean canTrigger(LivingEntity target);

    public int getAttackLength() {
        return this.attackLength;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getSpecialCooldown() {
        return specialCooldown;
    }

    public BossGoal<BossEntity> getGoal() {
        return goal;
    }

    public boolean isSpecialAttack() {
        return this.specialCooldown > 0;
    }
}
