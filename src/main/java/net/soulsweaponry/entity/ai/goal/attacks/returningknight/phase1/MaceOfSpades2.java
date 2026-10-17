package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.MaceAttack;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.MaceOfSpadesHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.MaceOfSpadesTimings;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class MaceOfSpades2 extends MaceAttack {

    private final float damageSwipe = EntityConfig.returning_knight_mace_of_spades_2_swipe_damage;
    private final float damageSmash = EntityConfig.returning_knight_mace_of_spades_2_smash_damage;

    public MaceOfSpades2(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown, MaceOfSpadesTimings.MACE_SIZE_2);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        MaceOfSpadesHitbox.updateMaceHitbox(this.hitbox, this.getBoss(), attackStatus);
        if (MaceOfSpadesHitbox.isDamageTickSwipe(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.hitbox, this.damageSwipe, 0.5f);
        }
        if (this.isTick(attackStatus, 21) || this.isTick(attackStatus, 37)) {
            this.playSound(SoundRegistry.KNIGHT_SWIPE_EVENT, 3f, 0.75f, 1f);
        }

        if (this.isTick(attackStatus, 65)) {
            this.prepareNextMaceHit(target);
        }
        if (MaceOfSpadesHitbox.isDamageTickSmash(this.getBoss(), attackStatus) && this.targetPos != null) {
            this.doObliterateDamage(this.hitbox, this.damageSmash);
        }
        if (this.isTick(attackStatus, 78)) {
            this.doObliterateParticles(this.hitbox);
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 80 && target.getBlockPos() != null && !this.cordsRegistered;
    }

    @Override
    public boolean isAvailable() {
        return !this.getBoss().isPhaseTwo();
    }
}
