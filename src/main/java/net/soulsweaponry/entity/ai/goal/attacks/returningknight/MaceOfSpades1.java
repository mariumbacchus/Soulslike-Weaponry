package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.MaceOfSpadesHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class MaceOfSpades1 extends MaceAttack {

    private final float damageSwipe = EntityConfig.returning_knight_mace_of_spades_1_swipe_damage;
    private final float damageSmash = EntityConfig.returning_knight_mace_of_spades_1_smash_damage;

    public MaceOfSpades1(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        MaceOfSpadesHitbox.updateMaceHitbox(this.hitbox, this.getBoss(), attackStatus);

        if (MaceOfSpadesHitbox.isDamageTickSwipe(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.damageSwipe, 2f);
            if (this.isTick(attackStatus, 14)) {
                this.playSound(this.targetPos, SoundRegistry.KNIGHT_SWIPE_EVENT, 1f);
            }
        }
        if (this.isTick(attackStatus, 26)) {
            this.prepareNextMaceHit(target);
        }
        if (MaceOfSpadesHitbox.isDamageTickSmash(this.getBoss(), attackStatus) && this.targetPos != null) {
            this.doObliterateDamage(this.damageSmash);
        }
        if (this.isTick(attackStatus, 43)) {
            this.doObliterateParticles();
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 80 && target.getBlockPos() != null && !this.cordsRegistered;
    }
}
