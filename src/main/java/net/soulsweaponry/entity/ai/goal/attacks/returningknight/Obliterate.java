package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.ObliterateHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class Obliterate extends MaceAttack {

    private final float damage = EntityConfig.returning_knight_obliterate_damage;

    public Obliterate(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        ObliterateHitbox.updateObliterateMaceHitbox(this.hitbox, this.getBoss(), this.targetPos, attackStatus);
        if (ObliterateHitbox.isObliterateDamageTick(this.getBoss(), attackStatus)) {
            this.doObliterateDamage(this.damage);
            if (this.isTick(attackStatus, 37)) {
                this.doObliterateParticles();
            }
            BossHitboxHelper.breakBlocksInsideHitbox(this.hitbox.copy().offsetWorld(0, ObliterateHitbox.OBLITERATE_MACE_SIZE.y / 2.0D, 0), this.getWorld(), this.getBoss(), this.getBoss()::canDestroy, false);
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 100D && target.getBlockPos() != null && !this.cordsRegistered;
    }
}
