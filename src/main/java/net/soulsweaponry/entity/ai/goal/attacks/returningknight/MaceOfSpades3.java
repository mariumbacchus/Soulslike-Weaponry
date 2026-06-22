package net.soulsweaponry.entity.ai.goal.attacks.returningknight;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.MaceOfSpadesHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class MaceOfSpades3 extends MaceAttack {

    private final float oneHandedSwipe = EntityConfig.returning_knight_mace_of_spades_3_one_handed_swipe_damage;
    private final float twoHandedSwipe = EntityConfig.returning_knight_mace_of_spades_3_two_handed_swipe_damage;
    private final float chargedTwoHandedSwipe = EntityConfig.returning_knight_mace_of_spades_3_charged_swipe_damage;

    public MaceOfSpades3(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        MaceOfSpadesHitbox.updateMaceHitbox(this.hitbox, this.getBoss(), attackStatus);
        if (MaceOfSpadesHitbox.isDamageTickSwipe(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.oneHandedSwipe, 0.5f);
        }
        if (MaceOfSpadesHitbox.isDamageTwoHandedSwipe(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.twoHandedSwipe, 0.6f);
        }
        if (MaceOfSpadesHitbox.isDamageChargedSwipe(this.getBoss(), attackStatus)) {
            this.doMaceDamage(this.getWorld().getDamageSources().mobAttack(this.getBoss()), this.chargedTwoHandedSwipe, living -> {
                target.takeKnockback(0.7f, -(target.getX() - this.getBoss().getX()), -(target.getZ() - this.getBoss().getZ()));
                target.addVelocity(0, 0.7f, 0);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
            });
        }
        if (this.isAnyTick(attackStatus, 21, 37, 58)) {
            this.playSound(SoundRegistry.KNIGHT_SWIPE_EVENT, 3f, 0.75f, 1f);
        }
        if (this.isTick(attackStatus, 66)) {
            this.playSound(SoundEvents.ITEM_AXE_SCRAPE, 3f, 0.5f);
        }
        if (this.isTick(attackStatus, 74)) {
            this.playSound(SoundRegistry.KNIGHT_SWIPE_EVENT, 3f, 0.75f, 1f);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 3f, 0.5f);
            this.doGroundScrapeParticles();
        }
        if (this.isAnyTick(attackStatus, 34, 54, 67)) {
            this.hitEntities.clear();
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return distanceToTarget <= 80 && target.getBlockPos() != null && !this.cordsRegistered;
    }
}
