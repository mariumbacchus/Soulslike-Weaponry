package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.config.ReturningKnightConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.MaceAttack;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.ObliterateHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class Obliterate extends MaceAttack {

    private final float damage = ReturningKnightConfig.obliterate_damage;

    public Obliterate(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown, ObliterateHitbox.OBLITERATE_MACE_SIZE);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus) {
        super.tickAttack(target, attackStatus);
        ObliterateHitbox.updateObliterateMaceHitbox(this.hitbox, this.getBoss(), this.targetPos, attackStatus);
        if (ObliterateHitbox.isObliterateDamageTick(this.getBoss(), attackStatus)) {
            this.doObliterateDamage(this.hitbox, this.damage);
            if (this.isTick(attackStatus, 37)) {
                this.doObliterateParticles(this.hitbox);
            }
            BossHitboxHelper.breakBlocksInsideHitbox(this.hitbox.copy().offsetWorld(0, ObliterateHitbox.OBLITERATE_MACE_SIZE.y / 2.0D, 0), this.getWorld(), this.getBoss(), this.getBoss()::canDestroy, false);
        }
    }

    @Override
    public double getTriggerReachBlocks() {
        return 11;
    }

    @Override
    public boolean isAvailable() {
        return !this.getBoss().isPhaseTwo();
    }
}
