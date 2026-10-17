package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1.Obliterate;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.ObliterateShadowHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class ObliterateShadow extends Obliterate {

    public final RotatableHitbox shadowHitbox = BossHitboxHelper.createPlaceholder(ObliterateShadowHitbox.OBLITERATE_SHADOW_MACE_SIZE);
    private final float shadowDamage = EntityConfig.returning_knight_obliterate_phase_2_shadow_damage;
    private final float pillarRadius = EntityConfig.returning_knight_obliterate_phase_2_shadow_pillar_radius;
    private final float pillarDamage = EntityConfig.returning_knight_obliterate_phase_2_shadow_pillar_damage;
    private final float pillarRippleAmount = EntityConfig.returning_knight_obliterate_phase_2_shadow_pillar_ripple_amount;

    public ObliterateShadow(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    // Extends original Obliterate attack to still perform the main one along with the shadow one here
    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        ObliterateShadowHitbox.updateObliterateShadowMaceHitbox(this.shadowHitbox, this.getBoss(), this.targetPos, attackStatus);
        if (ObliterateShadowHitbox.isObliterateShadowDamageTick(this.getBoss(), attackStatus)) {
            this.doObliterateDamage(this.shadowHitbox, this.shadowDamage);
            if (this.isTick(attackStatus, 43)) {
                this.doObliterateParticles(this.shadowHitbox);
                this.summonDarkPillarsCircle(target, (int) this.pillarRippleAmount, this.pillarRadius, this.pillarDamage, 13, this.shadowHitbox);
            }
            BossHitboxHelper.breakBlocksInsideHitbox(this.shadowHitbox.copy().offsetWorld(0, ObliterateShadowHitbox.OBLITERATE_SHADOW_MACE_SIZE.y / 2.0D, 0), this.getWorld(), this.getBoss(), this.getBoss()::canDestroy, false);
        }
    }

    @Override
    public boolean isAvailable() {
        return this.getBoss().isPhaseTwo();
    }
}
