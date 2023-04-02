package net.soulsweaponry.entity.ai.goal;

import java.util.EnumSet;
import java.util.Objects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.DraugrBoss;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;
import org.jetbrains.annotations.Nullable;

import static net.soulsweaponry.entity.mobs.DraugrBoss.States;

public class DraugrBossGoal extends MeleeAttackGoal {

    // NOTE: Unlike Goal.java, MeleeAttackGoal.java ticks EVERY TICK, not every other tick.
    private final DraugrBoss boss;
    private int attackCooldown;
    private int specialCooldown;
    private int attackStatus;
    private int postureBreakTimer;
    private boolean hasPostureBroken;

    public DraugrBossGoal(DraugrBoss boss) {
        super(boss, 1D, false);
        this.boss = boss;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public void reset(float cooldownModifier, boolean shieldUp) {
        this.attackStatus = 0;
        this.attackCooldown = (int)Math.floor((ConfigConstructor.old_champions_remains_attack_cooldown_ticks * cooldownModifier) /
                this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F ? 2 : 1);
        this.boss.setState(States.IDLE);
        this.boss.updateDisableShield(false);
        this.boss.setShielding(shieldUp);
    }

    public boolean applyDamage(LivingEntity target, float baseDamage) {
        float modified = baseDamage * ConfigConstructor.old_champions_remains_damage_modifier;
        if (this.boss.hasStatusEffect(StatusEffects.STRENGTH)) {
            modified += 4 + Objects.requireNonNull(this.boss.getStatusEffect(StatusEffects.STRENGTH)).getAmplifier() * 4;
        }
        return target.damage(DamageSource.mob(this.boss), modified);
    }

    @Override
    public void stop() {
        super.stop();
        this.boss.setAttacking(false);
        this.boss.updateDisableShield(false);
        this.reset(0, false);
    }

    protected boolean isInMeleeRange(LivingEntity target) {
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        return distanceToEntity <= this.getSquaredMaxAttackDistance(target) * 2;
    }

    protected boolean isTargetRanged(LivingEntity target) {
        return target.getItemsHand().iterator().next().getItem() instanceof RangedWeaponItem;
    }

    protected boolean isTargetHealing(LivingEntity target) {
        ItemStack stack = target.getItemsHand().iterator().next();
        if (target.isUsingItem()) {
            switch (stack.getUseAction()) {
                case EAT, DRINK -> {
                    return true;
                }
            }
        }
        return false;
    }

    private void randomAttack(@Nullable States specificAttack, LivingEntity target, boolean ignoreChecks) {
        if (target == null || specificAttack == States.IDLE) {
            this.boss.setState(States.IDLE);
            return;
        }
        int rand = this.boss.getRandom().nextInt(States.values().length);
        States attack = States.values()[rand];
        if (specificAttack != null) {
            attack = specificAttack;
        }
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        if (this.boss.isShielding()) {
            switch (attack) {
                case COUNTER, SHIELD_BASH, GROUND_SLAM, PARRY -> {
                    if (this.isInMeleeRange(target)) {
                        this.boss.setState(attack);
                    }
                }
                case SHIELD_VAULT, LEAP -> {
                    if (distanceToEntity < 100f && !this.isInMeleeRange(target) || ignoreChecks) {
                        this.boss.setState(attack);
                    }
                }
                default -> this.boss.setState(States.IDLE);
            }
        } else {
            switch (attack) {
                case SWIPES, HEAVY -> {
                    if (this.isInMeleeRange(target)) {
                        this.boss.setState(attack);
                    }
                }
                case BACKSTEP -> {
                    if (this.isTargetRanged(target) || this.isInMeleeRange(target)) {
                        this.boss.setState(attack);
                    }
                }
                case BATTLE_CRY -> {
                    if (distanceToEntity < 240f && specialCooldown < 0) {
                        this.boss.setState(attack);
                    }
                }
                case RUN_THRUST -> {
                    if (distanceToEntity < 100f && !this.isInMeleeRange(target) || ignoreChecks) {
                        this.boss.setState(attack);
                    }
                }
                default -> this.boss.setState(States.IDLE);
            }
        }

    }

    public void tick() {
        if (this.boss.isSpawning() || this.boss.isDead()) return;
        this.attackCooldown--;
        this.specialCooldown--;
        this.postureBreakTimer--;
        if (postureBreakTimer < -25) {
            postureBreakTimer = -5;
        }
        super.tick();
        LivingEntity target = this.boss.getTarget();
        if (target != null) {
            this.boss.setAttacking(true);

            if (this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F && !this.hasPostureBroken) {
                this.hasPostureBroken = true;
                this.postureBreakTimer = 50;
                this.boss.setPostureBroken(true);
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 50, 20));
            }
            if (postureBreakTimer < 0) {
                this.boss.setPostureBroken(false);
            }
            if (this.boss.isPostureBroken()) return;

            if (this.isTargetHealing(target) && this.boss.getState().equals(States.IDLE) && !this.boss.isPostureBroken()) {
                if (this.boss.isShielding()) {
                    States[] leaps = {States.LEAP, States.SHIELD_VAULT};
                    this.randomAttack(leaps[this.boss.getRandom().nextInt(leaps.length)], target, true);
                } else {
                    this.randomAttack(States.RUN_THRUST, target, true);
                }
            }

            if (this.attackCooldown > 0 && !this.boss.isPostureBroken()) {
                this.boss.setState(States.IDLE);
            }
            if (this.attackCooldown < 0 && this.boss.getState().equals(States.IDLE) && !this.boss.isPostureBroken()) {
                this.randomAttack(null, target, false);
            }

            switch (this.boss.getState()) {
                case COUNTER -> this.singleTarget(target, 30, new int[]{18}, 20f, 0, true, true, true);
                case SHIELD_BASH -> this.singleTarget(target, 30, new int[]{18}, 10f, 4f, false, false, true);
                case SHIELD_VAULT -> this.leapAttack(target, 10f, true);
                case SWIPES -> {
                    int[] frames = {10, 18, 26};
                    this.singleTarget(target, 33, frames, 16f, 0, false, false, false);
                }
                case BACKSTEP -> this.backstep(target);
                case HEAVY -> this.heavyBlow(target);
                case GROUND_SLAM -> this.aoe(30, 14, 8f, 3f, new StatusEffect[]{}, 4D, false);
                case PARRY -> this.parry(target);
                case BATTLE_CRY -> {
                    StatusEffect[] effects = {StatusEffects.SLOWNESS, StatusEffects.WEAKNESS};
                    this.aoe(50, 30, 0, 0, effects, 10D, true);
                }
                case LEAP -> this.leapAttack(target, 20f, false);
                case RUN_THRUST -> this.runThrust(target);
                default -> this.boss.setState(States.IDLE);
            }
        }
    }

    private void singleTarget(LivingEntity target, int maxTicks, int[] frames, float damage, float knockback,
                              boolean applyBleed, boolean disableShield, boolean shieldUpWhenDone) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20, false, true));
        if (disableShield) this.boss.updateDisableShield(true);
        for (int frame : frames) {
            if (attackStatus == frame && this.isInMeleeRange(target)) {
                if (this.applyDamage(target, damage)) {
                    this.boss.world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.HOSTILE, 1f, 1f);
                    if (!this.boss.world.isClient) {
                        ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.SWORD_SWIPE_ID, target.getBlockPos(), target.getEyeY());
                    }
                    if (applyBleed) target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEED, 100, 0));
                    if (knockback > 0) {
                        double x = target.getX() - this.boss.getX();
                        double z = target.getZ() - this.boss.getZ();
                        target.takeKnockback(knockback, -x, -z);
                    }
                }
            }
        }
        if (this.attackStatus >= maxTicks) {
            this.reset(1f, shieldUpWhenDone);
        }
    }

    private void leapAttack(LivingEntity target, float damage, boolean stunTarget) {
        this.attackStatus++;
        this.boss.updateDisableShield(true);
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20, false, true));
        if (attackStatus == 18) {
            double e = target.getX() - (this.boss.getX());
            double g = target.getZ() - this.boss.getZ();
            this.boss.addVelocity(e/4, 0.35D, g/4);
        }
        if (attackStatus == 26) {
            this.boss.world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
            if (this.isInMeleeRange(target)) {
                if (this.applyDamage(target, damage)) {
                    if (stunTarget) {
                        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 10));
                    }
                    if (!this.boss.world.isClient) {
                        ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.DARK_EXPLOSION_ID, target.getBlockPos(), 10);
                    }
                }
            }
        }
        if (this.attackStatus >= 35) {
            this.reset(0f, false);
        }
    }

    private void parry(LivingEntity target) {
        int[] frame = {26};
        this.singleTarget(target, 40, frame, 18f, 0, false, true, false);
        if (attackStatus == 8 && this.isInMeleeRange(target)) {
            if (!target.hasStatusEffect(EffectRegistry.POSTURE_BREAK)) {
                this.boss.world.playSound(null, target.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT, SoundCategory.HOSTILE, .5f, 1f);
            }
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK, 40, 0));
        }
    }

    private void aoe(int maxTicks, int frame, float damage, float knockback, StatusEffect[] effects, double boxSize, boolean shieldUpWhenDone) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20, false, true));
        if (effects.length > 0 && (attackStatus == 12 || attackStatus == 20)) this.boss.world.playSound(null, this.boss.getBlockPos(), SoundRegistry.SWORD_HIT_SHIELD_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        if (attackStatus == frame) {
            if (damage > 0) {
                if (!this.boss.world.isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.OBLITERATE_ID, this.boss.getBlockPos(), 200);
                }
                this.boss.world.playSound(null, this.boss.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 0.8f, 1f);
            }
            for (Entity entity : this.boss.world.getOtherEntities(this.boss, this.boss.getBoundingBox().expand(boxSize))) {
                if (entity instanceof LivingEntity living) {
                    for (StatusEffect effect : effects) {
                        living.addStatusEffect(new StatusEffectInstance(effect, 200, 0));
                    }
                    if (damage > 0) {
                        this.applyDamage(living, damage);
                        double x = living.getX() - this.boss.getX();
                        double z = living.getZ() - this.boss.getZ();
                        living.takeKnockback(knockback, -x, -z);
                    }
                }
            }
        }
        if (this.attackStatus >= maxTicks) {
            this.reset(2f, shieldUpWhenDone);
            if (effects.length > 0) {
                this.specialCooldown = ConfigConstructor.old_champions_remains_special_cooldown_ticks;
            }
        }
    }

    private void backstep(LivingEntity target) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20, false, true));
        if (attackStatus == 6) {
            double e = target.getX() - (this.boss.getX());
            double g = target.getZ() - this.boss.getZ();
            this.boss.addVelocity(-e/4, 0.35D, -g/4);
        }
        if (attackStatus >= 30) {
            this.reset(0f, true);
        }
    }

    private void runThrust(LivingEntity target) {
        this.attackStatus++;
        if (attackStatus <= 10) {
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5, 3));
        } else {
            this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20, false, true));
        }
        if (attackStatus == 13 && this.isInMeleeRange(target)) {
            if (this.applyDamage(target, 16f)) {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEED, 100, 0));
                this.boss.world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.HOSTILE, 1f, 1f);
                if (!this.boss.world.isClient) {
                    ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.SWORD_SWIPE_ID, target.getBlockPos(), target.getEyeY());
                }
            }
        }
        if (attackStatus >= 23) {
            this.reset(1f, false);
        }
    }

    private void heavyBlow(LivingEntity target) {
        this.attackStatus++;
        this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20, false, true));
        if (attackStatus == 1 && target.getBlockPos() != null) this.boss.setTargetPos(target.getBlockPos());
        BlockPos pos = this.boss.getTargetPos();
        if (pos != null && isPosNotNullish(pos)) {
            this.boss.getLookControl().lookAt(pos.getX(), pos.getY(), pos.getZ());
        }
        if (attackStatus == 24 && pos != null && isPosNotNullish(pos)) {
            this.boss.world.playSound(null, pos, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 1f, 1f);
            if (!this.boss.world.isClient)
                ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.DARK_EXPLOSION_ID, pos, 100);
            for (Entity entity : this.boss.world.getOtherEntities(this.boss, new Box(pos).expand(1D))) {
                if (entity instanceof LivingEntity living) {
                    this.applyDamage(living, 25f);
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
                }
            }
        }
        if (attackStatus >= 40) {
            this.reset(1f, false);
        }
    }

    public static boolean isPosNotNullish(BlockPos pos) {
        return pos.getX() != 0 || pos.getY() != 0 || pos.getZ() != 0;
    }

    @Override
    protected void attack(LivingEntity target, double squaredDistance) {}
}