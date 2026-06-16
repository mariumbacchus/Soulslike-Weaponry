package net.soulsweaponry.entity.ai.goal.attacks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.entity.ai.goal.BossGoal;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.BossEntity;
import net.soulsweaponry.registry.DamageSourceRegistry;
import org.jetbrains.annotations.Nullable;

public abstract class BossAttack<S extends Enum<S>, B extends BossEntity<S>, G extends BossGoal<S, B, G>> {

    private final G goal;
    private final B boss;
    private final World world;
    private final int weight;
    private final int attackLength;
    private final int cooldown;
    private final int specialCooldown;

    /**
     * @param goal            parent goal the attack is tied to
     * @param boss            the boss the attack is tied to
     * @param attackLength    length of the animation/attack in ticks
     * @param weight          chance for the attack to happen, default is 10, the higher the value, the more likely to trigger
     * @param cooldown        cooldown after the attack is finished
     * @param specialCooldown special cooldown if the attack is special, set to 0 if not special
     */
    public BossAttack(G goal, B boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        this.goal = goal;
        this.boss = boss;
        this.world = boss.getWorld();
        this.weight = weight;
        this.attackLength = attackLength;
        this.cooldown = cooldown;
        this.specialCooldown = specialCooldown;
    }

    public void tick(LivingEntity target, int attackStatus, double distanceToTarget) {
        this.goal.increaseAttackStatus();
        this.tickAttack(target, attackStatus, distanceToTarget);
        this.goal.checkAndReset(this, this.cooldown, this.specialCooldown);
        if (this.boss.isDebugMode()) {
            this.boss.setAttackStatus(attackStatus);
        }
    }

    /**
     * Called once at the same time as the attack is set to be performed (set as state).
     * Use to set start variables such as saving the targets position for the attack once when beginning.
     */
    public void initiateAttackVariables(LivingEntity target) {}

    public abstract void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget);

    /**
     * Called when the attack is finished to reset values, happens at the same time as boss state is set to IDLE,
     * attack status is set to 0 and cooldowns are applied.
     */
    public void resetAttackVariables() {}

    /**
     * Called for whether the attack can be chosen and triggered.
     * Example of implementation include returning goal.isSpecialCoolingDown();
     */
    public abstract boolean canTrigger(LivingEntity target, double distanceToTarget);

    /**
     * Check if the target is within melee reach. Set {@param distanceOutMod} to extend the reach,
     * normal value is 3 for when checking whether the boss should start a melee attack or not.
     */
    public boolean isInMeleeRange(LivingEntity target, double distanceOutMod) {
        double distanceToEntity = this.boss.squaredDistanceTo(target);
        return distanceToEntity <= this.getSquaredMaxAttackDistance(target);
    }

    public boolean damageTarget(LivingEntity target, float damage) {
        return target.damage(this.world.getDamageSources().mobAttack(this.boss), this.getGoal().getModifiedDamage(damage));
    }

    public boolean damageTarget(RegistryKey<DamageType> sourceKey, LivingEntity target, float damage) {
        return target.damage(DamageSourceRegistry.create(this.getWorld(), sourceKey, this.getBoss()), this.getGoal().getModifiedDamage(damage));
    }

    public double getSquaredMaxAttackDistance(LivingEntity target) {
        float reach = this.boss.getWidth() * 2.0F;
        return reach * reach + target.getWidth();
    }

    /**
     * Helper for returning whether the attack status matches the tick. The attack status is scaled according to
     * the bosses animation speed.
     * <p>
     *     NOTE: Ticks may be the same based on the animation speed, i.e. if the animation speed is 0.5 then the tick
     *     may be the same twice so attacks, particles or sounds may trigger twice!
     * </p>
     */
    public boolean isTick(int attackStatus, int tick) {
        return BossHitboxHelper.isScaledTickEqual(attackStatus, this.getBoss().getAnimationSpeed(), tick);
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

    public void playSound(BlockPos pos, SoundEvent sound, float volume) {
        this.playSound(pos, sound, volume, 1f);
    }

    public void playSound(SoundEvent sound, float volume) {
        this.playSound(null, sound, volume, 1f);
    }

    public int getAttackLength() {
        return this.attackLength;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getSpecialCooldown() {
        return specialCooldown;
    }

    public G getGoal() {
        return goal;
    }

    public boolean isSpecialAttack() {
        return this.specialCooldown > 0;
    }

    public int getWeight() {
        return weight;
    }

    public B getBoss() {
        return boss;
    }

    public World getWorld() {
        return world;
    }
}
