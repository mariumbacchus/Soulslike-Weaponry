package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1.Obliterate;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.ObliterateShadowHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class ObliterateShadow extends Obliterate {

    public final RotatableHitbox shadowHitbox = BossHitboxHelper.createPlaceholder(new Vec3d(6, 5, 6));
    private final float shadowDamage = EntityConfig.returning_knight_obliterate_shadow_damage;

    public ObliterateShadow(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    //TODO right now the followup from the shadow is a bit too fast so you dont take double damage, delay it maybe a bit
    // also add pillars of darkness outward in a circle from the hit center when the shadow mace hits

    // Extends original Obliterate attack to still perform the main one along with the shadow one here
    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        ObliterateShadowHitbox.updateObliterateShadowMaceHitbox(this.shadowHitbox, this.getBoss(), this.targetPos, attackStatus);
        if (ObliterateShadowHitbox.isObliterateShadowDamageTick(this.getBoss(), attackStatus)) {
            this.doObliterateDamage(this.shadowDamage);
            if (this.isTick(attackStatus, 41)) {
                this.doObliterateParticles();
            }
            BossHitboxHelper.breakBlocksInsideHitbox(this.shadowHitbox.copy().offsetWorld(0, ObliterateShadowHitbox.OBLITERATE_SHADOW_MACE_SIZE.y / 2.0D, 0), this.getWorld(), this.getBoss(), this.getBoss()::canDestroy, false);
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) { //TODO
        return this.getBoss().isPhaseTwo(); //distanceToTarget <= 100D && target.getBlockPos() != null && !this.cordsRegistered
    }
}
