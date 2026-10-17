package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1.MaceOfSpades1;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.shadow.MaceOfSpadesShadowHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.shadow.MaceOfSpadesShadowTimings;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.SoundRegistry;

public class MaceOfSpades1Shadow extends MaceOfSpades1 {

    public final RotatableHitbox shadowHitbox = BossHitboxHelper.createPlaceholder(MaceOfSpadesShadowTimings.MACE_SIZE_1);
    private final float damageSwipe = EntityConfig.returning_knight_mace_of_spades_1_phase_2_shadow_swipe_damage;
    private final float damageSmash = EntityConfig.returning_knight_mace_of_spades_1_phase_2_shadow_smash_damage;
    private final float pillarRadius = EntityConfig.returning_knight_mace_of_spades_1_phase_2_shadow_pillar_radius;
    private final float pillarDamage = EntityConfig.returning_knight_mace_of_spades_1_phase_2_shadow_pillar_damage;
    private final float pillarRippleAmount = EntityConfig.returning_knight_mace_of_spades_1_phase_2_shadow_pillar_ripple_amount;

    public MaceOfSpades1Shadow(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        MaceOfSpadesShadowHitbox.updateMaceHitbox(this.shadowHitbox, this.getBoss(), attackStatus);
        if (MaceOfSpadesShadowHitbox.isDamageTickSwipe(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.shadowHitbox, this.damageSwipe, 2f);
            if (this.isTick(attackStatus, 24)) {
                this.playSound(this.targetPos, SoundRegistry.KNIGHT_SWIPE_EVENT, 1f);
            }
        }
        if (this.isAnyTick(attackStatus, 48)) {
            this.prepareNextMaceHit(target);
        }
        if (MaceOfSpadesShadowHitbox.isDamageTickSmash(this.getBoss(), attackStatus) && this.targetPos != null) {
            this.doObliterateDamage(this.shadowHitbox, this.damageSmash);
        }
        if (this.isTick(attackStatus, 54)) {
            this.doObliterateParticles(this.shadowHitbox);
            this.summonDarkPillarsCircle(target, (int) this.pillarRippleAmount, this.pillarRadius, this.pillarDamage, 10, this.shadowHitbox);
        }
    }

    @Override
    public boolean isAvailable() {
        return this.getBoss().isPhaseTwo();
    }
}
