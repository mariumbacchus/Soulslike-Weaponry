package net.soulsweaponry.entity.ai.goal;

import java.util.EnumSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.RangedWeaponItem;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.DraugrBoss;

public class DraugrBossGoal extends Goal {
    private final DraugrBoss boss;
    private int attackCooldown;
    private int attackStatus;
    private int shieldTimer;
    private int postureBreakTimer;
    private double attackRange;
    private boolean hasSlowed;
    private boolean hasPostureBroken;
    private boolean attackReset;
    private int shieldCooldown = 60;
    private Path path;

    public DraugrBossGoal(DraugrBoss boss) {
        this.boss = boss;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.boss.getTarget();
        if (target != null) {
            this.path = this.boss.getNavigation().findPathTo(target, 0);
        }
        return target != null && target.isAlive() && this.boss.canTarget(target) && this.path != null;
    }

    public void resetAttackCooldown(float cooldownModifier) {
        this.attackCooldown = (int)(ConfigConstructor.old_champions_remains_attack_cooldown_ticks * cooldownModifier);
    }

    public float getModifiedDamage(float damage) {
        return damage * ConfigConstructor.old_champions_remains_damage_modifier;
    }

    @Override
    public void stop() {
        super.stop();
        this.boss.setAttacking(false);
        this.boss.updateDisableShield(false);
    }

    public void tick() {
        if (this.boss.getSpawning()) return;
        
        attackCooldown--;
        shieldTimer--;
        postureBreakTimer--;
        shieldCooldown--;
        if (attackCooldown < -25) {
            attackCooldown = 25;
        }
        if (shieldTimer < -25) {
            shieldTimer = -5;
        }
        if (postureBreakTimer < -25) {
            postureBreakTimer = -5;
        }

        LivingEntity target = this.boss.getTarget();
        
        if (target != null) {
            this.boss.setAttacking(true);

            if (this.shieldCooldown < 0) {
                this.shieldTimer = 150;
                this.attackCooldown = 0;
                this.shieldCooldown = this.shieldTimer + 60;
            }

            if (this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F && !this.hasPostureBroken) {
                this.hasPostureBroken = true;
                this.postureBreakTimer = 25;
                this.shieldTimer = 0;
                this.shieldCooldown = 60;
                this.boss.setPostureBreak(true);
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 50, 20));
            }
            if (postureBreakTimer < 0) {
                this.boss.setPostureBreak(false);
            }

            //Shield opp eller ned
            if (target.getMainHandStack().getItem() instanceof RangedWeaponItem || shieldTimer > 0) {
                this.boss.setShieldUp(true);
                this.boss.setShieldDown(false);
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1, 1));
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 3));
                if (!this.attackReset) {
                    this.resetAttackCooldown(.8f);
                    this.attackReset = true;
                    this.hasSlowed = false;
                }
            } else {
                this.boss.setShieldUp(false);
                this.boss.setShieldDown(true);
                
                if (!this.hasSlowed) {
                    this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 10));
                    this.hasSlowed = true;
                }
                this.attackReset = false;
            }

            double distanceToEntity = this.boss.squaredDistanceTo(target);
            if (this.boss.getShieldDown() && !this.boss.getShieldUp()) {
                attackRange = 6D;
            } else {
                attackRange = 14D;
            }
            if (this.path != null) {
                this.boss.getNavigation().startMovingAlong(this.path, 1D);
            }

            if (attackCooldown < 0 && postureBreakTimer < 0 && distanceToEntity < attackRange) {
                if (this.boss.getShieldDown() && !this.boss.getShieldUp()) {
                    this.boss.tryAttack(target);
                    this.resetAttackCooldown(.8f);
                } else {
                    int random = this.boss.getRandom().nextInt(2);
                    if (random == 0 && !this.boss.getCounter()) {
                        this.boss.setShieldBash(true);
                    } else if (!this.boss.getShieldBash()) {
                        this.boss.setCounter(true);
                    }
                }
            }

            if (this.boss.getShieldBash()) {
                this.attackStatus++;
                this.boss.updateDisableShield(true);
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3, 10));
                if (attackStatus == 10 && distanceToEntity < attackRange) {
                    double x = target.getX() - this.boss.getX();
                    double z = target.getZ() - this.boss.getZ();
                    target.damage(DamageSource.mob(this.boss), this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F ? this.getModifiedDamage(12f) : this.getModifiedDamage(8f));
                    target.takeKnockback(4f, -x, -z);
                }
                if (attackStatus >= 14) {
                    this.boss.setShieldBash(false);
                    this.boss.updateDisableShield(false);
                    this.resetAttackCooldown(1);
                    this.attackStatus = 0;
                }
            }
            if (this.boss.getCounter()) {
                this.attackStatus++;
                this.boss.updateDisableShield(true);
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3, 10));
                if (attackStatus == 10 && distanceToEntity < attackRange) {
                    double x = target.getX() - (this.boss.getX());
                    double z = target.getZ() - this.boss.getZ();
                    target.damage(DamageSource.mob(this.boss), this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F ? this.getModifiedDamage(24f) : this.getModifiedDamage(20f));
                    target.takeKnockback(1F, -x, -z);
                }
                if (attackStatus >= 14) {
                    this.boss.setCounter(false);
                    this.boss.updateDisableShield(false);
                    this.resetAttackCooldown(1);
                    this.attackStatus = 0;
                }
            }
            super.tick();
        }
    }
}
