package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1.Obliterate;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.ObliterateShadowHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.entity.projectile.noclip.BlackflameExplosionEntity;
import net.soulsweaponry.util.WeaponUtil;

public class ObliterateShadow extends Obliterate {

    public final RotatableHitbox shadowHitbox = BossHitboxHelper.createPlaceholder(new Vec3d(6, 5, 6));
    private final float shadowDamage = EntityConfig.returning_knight_obliterate_shadow_damage;
    private final float pillarRadius = EntityConfig.returning_knight_obliterate_shadow_pillar_radius;
    private final float pillarDamage = EntityConfig.returning_knight_obliterate_shadow_pillar_damage;
    private final float pillarRippleAmount = EntityConfig.returning_knight_obliterate_shadow_pillar_ripple_amount;

    public ObliterateShadow(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    // Extends original Obliterate attack to still perform the main one along with the shadow one here
    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        super.tickAttack(target, attackStatus, distanceToTarget);
        ObliterateShadowHitbox.updateObliterateShadowMaceHitbox(this.shadowHitbox, this.getBoss(), this.targetPos, attackStatus);
        if (ObliterateShadowHitbox.isObliterateShadowDamageTick(this.getBoss(), attackStatus)) {
            this.doObliterateDamage(this.shadowDamage);
            if (this.isTick(attackStatus, 43)) {
                this.doObliterateParticles();

                Vec3d maceCenter = BossHitboxHelper.findGroundImpactPos(this.getWorld(), this.hitbox.getCenter(), 2);
                float yawRad = (float) MathHelper.atan2(
                        target.getZ() - this.getBoss().getZ(),
                        target.getX() - this.getBoss().getX()
                );
                float yawDeg = (float) Math.toDegrees(yawRad);
                double heightDiff = Math.abs(target.getY() - this.getBoss().getY());
                double maxYOffset = heightDiff + 2.0;
                WeaponUtil.doConsumerOnCircle(this.getBoss().getWorld(), yawDeg, maceCenter, maxYOffset, (int) this.pillarRippleAmount, new Vec2f(1.5f, 1.75f), (pos, delay, yaw) -> {
                    BlackflameExplosionEntity entity = new BlackflameExplosionEntity(this.getBoss().getWorld());
                    entity.setOwner(this.getBoss());
                    entity.setRadius(this.pillarRadius);
                    entity.setDamage(this.pillarDamage);
                    entity.setWarmup(delay - 13);
                    entity.setPosition(pos);
                    this.getBoss().getWorld().spawnEntity(entity);
                    if (this.getBoss().getWorld() instanceof ServerWorld serverWorld) {
                        float spread = 0.01f;
                        serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), 1,
                                this.getBoss().getRandom().nextFloat() * spread - spread / 2f, this.getBoss().getRandom().nextFloat() * spread - spread / 2f, this.getBoss().getRandom().nextFloat() * spread - spread / 2f, 0);
                    }
                });
            }
            BossHitboxHelper.breakBlocksInsideHitbox(this.shadowHitbox.copy().offsetWorld(0, ObliterateShadowHitbox.OBLITERATE_SHADOW_MACE_SIZE.y / 2.0D, 0), this.getWorld(), this.getBoss(), this.getBoss()::canDestroy, false);
        }
    }

    @Override
    public boolean isAvailable() {
        return this.getBoss().isPhaseTwo();
    }
}
