package net.soulsweaponry.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.entity.ai.goal.attacks.BossAttack;
import net.soulsweaponry.entity.mobs.boss.BossEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BossGoal<T extends BossEntity> extends MeleeAttackGoal {
    public final T boss;
    public final World world;
    public int attackStatus;
    public int specialCooldown;
    public int attackCooldown;
    public int attackLength;
    private static final List<BossAttack> ATTACKS = new ArrayList<>();

    public BossGoal(T boss, double speed, boolean pauseWhenMobIdle) {
        super(boss, speed, pauseWhenMobIdle);
        this.boss = boss;
        this.world = boss.getWorld();
    }

    @Override
    public void stop() {
        super.stop();
        this.attackCooldown = 20;
        this.specialCooldown = 20;
        this.attackStatus = 0;
        this.attackLength = 0;
    }

    @Override
    protected void attack(LivingEntity target) {}

    @Override
    public void tick() {
        if (this.boss.isDead()) { //TODO this.boss.isSpawning()
            return;
        }
        this.attackCooldown = Math.max(this.attackCooldown - 1, 0);
        this.specialCooldown = Math.max(this.specialCooldown - 1, 0);
        super.tick(); //TODO might need to add other forms for travel method with if statement to prio that over super.tick() like in DayStalker or NightProwler cases with flying
        LivingEntity target = this.boss.getTarget();
        if (target == null) {
            return;
        }
        this.boss.setAttacking(true);
        //TODO passive stuff that should intercept regular attacks and have its own animation, for example unbreakable for returning knight or posture break for draugr boss
        if (this.isCoolingDown()) { //TODO && !this.boss.isIdle() && !passiveAttack like posture break or unbreakable
            this.checkAndSetAttack(target);
        }
        //TODO fetch the current attack and tick it based on what is set by checkAndSetAttack(target), also remember to set attackLength = attack.getAttackLength()
    }

    /**
     * Sets the next attack for the boss based on randomness and chance for the attack as well as other parameters
     * based on the attack such as "is target within range" or specialCooldown <= 0
     */
    public void checkAndSetAttack(LivingEntity target) {
        //TODO should loop over attacks and choose one randomly based on requirements/predicate for it to trigger and the chance/weight it has
    }

    //TODO may need to be remade as attacks may have own values themselves?? idk how i will do this yet
    public void checkAndReset(int attackCooldown, int specialCooldown) {
        if (this.attackStatus > this.attackLength) {
            this.attackStatus = 0;
            this.attackLength = 0;
            // TODO this.boss.setIdle()
            this.attackCooldown = this.getModifiedCooldown(attackCooldown);
            this.specialCooldown = this.getModifiedSpecialCooldown(specialCooldown);
        }
    }

    public boolean damageTarget(LivingEntity target, float damage) {
        return target.damage(this.world.getDamageSources().mobAttack(this.boss), this.getModifiedDamage(damage));
    }

    public double getSquaredMaxAttackDistance(LivingEntity target) {
        float reach = this.boss.getWidth() * 2.0F;
        return reach * reach + target.getWidth();
    }

    /**
     * Check if the target is within melee reach. Set {@param distanceOutMod} to extend the reach,
     * normal value is 3 for when checking whether the boss should start a melee attack or not.
     */
    public boolean isInMeleeRange(LivingEntity target, double distanceOutMod) {
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        return distanceToEntity <= this.getSquaredMaxAttackDistance(target);
    }

    /**
     * Easily accessible playSound function.
     * @param pos Position to play sound on, if set to null, will play on the boss' position
     * @param sound Sound to be played
     * @param volume Volume
     * @param pitch Pitch
     */
    public void playSound(@Nullable BlockPos pos, SoundEvent sound, float volume, float pitch) {
        if (pos == null) pos = this.boss.getBlockPos();
        this.boss.getWorld().playSound(null, pos, sound, SoundCategory.HOSTILE, volume, pitch);
    }

    public void playSound(SoundEvent sound, float volume) {
        this.playSound(null, sound, volume, 1f);
    }

    /**
     * Returns true if the target is currently eating or drinking to restore health,
     * basically input reading.
     */
    public boolean isTargetHealing(LivingEntity target) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = target.getStackInHand(hand);
            if (target.isUsingItem()) {
                switch (stack.getUseAction()) {
                    case EAT, DRINK -> {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public abstract int getModifiedCooldown(int cooldown);
    public abstract int getModifiedSpecialCooldown(int specialCooldown);
    public abstract float getModifiedDamage(float damage);

    public void addAttack(BossAttack attack) {
        ATTACKS.add(attack);
    }

    public boolean isSpecialCoolingDown() {
        return this.specialCooldown <= 0;
    }

    public boolean isCoolingDown() {
        return this.attackCooldown <= 0;
    }

    public void increaseAttackStatus() {
        this.attackStatus++;
    }
}
