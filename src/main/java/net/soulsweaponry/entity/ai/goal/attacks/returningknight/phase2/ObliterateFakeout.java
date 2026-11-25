package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.ReturningKnightConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.MaceAttack;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.ObliterateFakeoutHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class ObliterateFakeout extends MaceAttack {

    public final RotatableHitbox shadowHitbox = BossHitboxHelper.createPlaceholder(ObliterateFakeoutHitbox.SHADOW_MACE_SIZE);
    private final float damage = ReturningKnightConfig.obliterate_fakeout_phase_2_damage;
    private final float shadowDamage = ReturningKnightConfig.obliterate_fakeout_phase_2_shadow_spin_damage;
    private final int darknessOrbAmountPerTick = (int) ReturningKnightConfig.obliterate_fakeout_phase_2_shadow_spin_darkness_orb_amount_per_tick;
    private final float darknessOrbDamage = ReturningKnightConfig.obliterate_fakeout_phase_2_shadow_spin_darkness_orb_damage;
    private final float darknessOrbSpeed = ReturningKnightConfig.obliterate_fakeout_phase_2_shadow_spin_darkness_orb_speed;

    public ObliterateFakeout(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown, ObliterateFakeoutHitbox.MACE_SIZE);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus) {
        super.tickAttack(target, attackStatus);
        ObliterateFakeoutHitbox.updateObliterateMaceHitbox(this.hitbox, this.getBoss(), this.targetPos, attackStatus);
        ObliterateFakeoutHitbox.updateShadowMaceHitbox(this.shadowHitbox, this.getBoss(), attackStatus);
        if (ObliterateFakeoutHitbox.isObliterateDamageTick(this.getBoss(), attackStatus)) {
            this.doObliterateDamage(this.hitbox, this.damage);
            if (this.isAnyTick(attackStatus, 45, 77)) {
                this.doObliterateParticles(this.hitbox);
            }
            BossHitboxHelper.breakBlocksInsideHitbox(this.hitbox.copy().offsetWorld(0, 1, 0), this.getWorld(), this.getBoss(), this.getBoss()::canDestroy, false);
        }
        if (this.isTick(attackStatus, 52)) {
            this.prepareNextMaceHit(target);
        }
        if (ObliterateFakeoutHitbox.isSpinDamageTick(this.getBoss(), attackStatus)) {
            this.doSwingDamage(this.shadowHitbox, this.shadowDamage, 0.6f);
            if (attackStatus % 2 == 0) {
                Vec3d spawnPos = this.shadowHitbox.getCenter().add(0, -1, 0);
                this.spawnDarknessOrb(spawnPos, this.darknessOrbAmountPerTick, this.darknessOrbDamage, this.darknessOrbSpeed);
            }
        }
    }

    @Override
    public double getTriggerReachBlocks() {
        return 15;
    }

    @Override
    public boolean isAvailable() {
        return this.getBoss().isPhaseTwo();
    }
}
