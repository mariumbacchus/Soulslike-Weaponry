package net.soulsweaponry.entity.ai.goal;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.ai.goal.attacks.BossAttack;
import net.soulsweaponry.entity.ai.goal.events.BossEvent;
import net.soulsweaponry.entity.mobs.boss.BossEntity;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BossGoal<S extends Enum<S>, B extends BossEntity<S>, G extends BossGoal<S, B, G>> extends MeleeAttackGoal {
    public final B boss;
    public final World world;
    public int attackStatus;
    public int specialCooldown;
    public int attackCooldown;
    public int attackLength;
    /**
     * Map of states and the attack tied to the state, such as Returning Knight's Obliterate attack
     */
    private final Map<S, BossAttack<S, B, G>> attacks = new LinkedHashMap<>();
    /**
     * Map of states and the event tied to the state, such as Returning Knight's Unbreakable event
     * which only happens once when the boss is below 50% health
     */
    private final Map<S, BossEvent<S, B, G>> events = new LinkedHashMap<>();
    @Nullable
    private BossAttack<S, B, G> currentAttack;

    public BossGoal(B boss, double speed, boolean pauseWhenMobIdle) {
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
        this.resetAttackVariables();
        this.currentAttack = null;
    }

    @Override
    public void start() {
        super.start();
        this.boss.setIdle();
    }

    @Override
    protected void attack(LivingEntity target) {}

    @Override
    public void tick() {
        if (this.boss.isDead() || this.boss.isSpawning()) {
            return;
        }
        this.attackCooldown = Math.max(this.attackCooldown - 1, 0);
        this.specialCooldown = Math.max(this.specialCooldown - 1, 0);
        super.tick();
        // Might need to add other forms for travel method with if statement to prio that over super.tick() like in DayStalker or NightProwler cases with flying,
        // that may need to be its own goal instead though
        LivingEntity target = this.boss.getTarget();
        if (target == null) {
            return;
        }
        double distanceToTarget = this.boss.squaredDistanceTo(target);
        // Event logic, will be performed before the attack and only once
        if (this.canIssueAttack()) {
            for (Map.Entry<S, BossEvent<S, B, G>> eventEntry : this.events.entrySet()) {
                S state = eventEntry.getKey();
                BossEvent<S, B, G> event = eventEntry.getValue();
                if (!event.hasCompleted() && event.canTrigger(target, distanceToTarget)) {
                    this.prepareAttack(state, event, target);
                }
            }
        }
        // Attack logic, will choose a random attack based on weight and perform it
        // Got to have this check twice since the attack would override the event animation after the event was triggered so the event would never happen
        if (this.canIssueAttack()) {
            if (FabricLoader.getInstance().isDevelopmentEnvironment() && this.getDebugState() != null) {
                S state = this.getDebugState();
                BossAttack<S, B, G> attack = this.attacks.get(state);
                if (attack.canTrigger(target, distanceToTarget)) {
                    SoulsWeaponry.LOGGER.warn("Debug mode enabled for boss {}, state set to {}", this.boss, state);
                    this.prepareAttack(state, attack, target);
                }
            } else {
                this.checkAndSetAttack(target);
            }
        }
        // Fetch the current attack and tick it based on what is set by checkAndSetAttack(target)
        // NB! states that aren't attacks can still be fetched like IDLE and SPAWN
        S state = this.boss.getState();
        if (this.isValidState(state) && this.currentAttack != null) {
            this.currentAttack.tick(target, this.attackStatus, distanceToTarget);
        }
    }

    public boolean canIssueAttack() {
        return !this.isCoolingDown() && this.boss.isIdle();
    }

    public boolean isValidState(S state) {
        return this.attacks.containsKey(state) || this.events.containsKey(state);
    }

    /**
     * Sets the next attack for the boss, chosen randomly with the weight of the attacks increasing or decreasing the chances
     * for it to happen.
     * <p>
     *     {@link BossAttack#canTrigger(LivingEntity, double)} will be called once the attack is chosen along with checking
     *     whether the attack is a special attack and the special attack cooldown is still ticking down, if it returns false
     *     then the attack is canceled and the method tries to pick a new attack again. If attempts go past the
     *     <b>attempts</b> variable then the method gives up and tries again next tick.
     * </p>
     */
    public void checkAndSetAttack(LivingEntity target) {
        double distanceToTarget = this.boss.squaredDistanceTo(target);
        int totalWeight = this.getTotalAttackWeight();
        int attempts = this.attacks.size();
        for (int i = 0; i < attempts; i++) {
            int randomWeight = this.boss.getRandom().nextInt(totalWeight);
            for (Map.Entry<S, BossAttack<S, B, G>> entry : this.attacks.entrySet()) {
                S state = entry.getKey();
                BossAttack<S, B, G> attack = entry.getValue();
                randomWeight -= attack.getWeight();
                if (randomWeight < 0) {
                    if (!attack.canTrigger(target, distanceToTarget) || (attack.isSpecialAttack() && this.isSpecialCoolingDown())) {
                        break;
                    }
                    this.prepareAttack(state, attack, target);
                    return;
                }
            }
        }
    }

    public void prepareAttack(S state, BossAttack<S, B, G> attack, LivingEntity target) {
        this.attackStatus = 0;
        this.attackLength = attack.getAttackLength();
        attack.initiateAttackVariables(target);
        this.boss.setState(state);
        this.currentAttack = attack;
    }

    public void checkAndReset(BossAttack<S, B, G> attack, int attackCooldown, int specialCooldown) {
        if (this.attackStatus > this.getScaledAttackLength()) {
            this.attackStatus = 0;
            this.attackLength = 0;
            this.currentAttack = null;
            this.boss.setIdle();
            this.attackCooldown = this.getModifiedCooldown(attackCooldown);
            this.specialCooldown = this.getModifiedSpecialCooldown(specialCooldown);
            attack.resetAttackVariables();
        }
    }

    public int getScaledAttackLength() {
        return (int) Math.ceil(this.attackLength / this.boss.getAnimationSpeed());
    }

    public void resetAttackVariables() {
        for (BossAttack<S, B, G> attack : this.attacks.values()) {
            attack.resetAttackVariables();
        }
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

    public void addAttack(S state, BossAttack<S, B, G> attack) {
        this.attacks.put(state, attack);
    }

    public void addEvent(S state, BossEvent<S, B, G> event) {
        this.events.put(state, event);
    }

    public boolean isSpecialCoolingDown() {
        return this.specialCooldown > 0;
    }

    public boolean isCoolingDown() {
        return this.attackCooldown > 0;
    }

    public void increaseAttackStatus() {
        this.attackStatus++;
    }

    @SuppressWarnings("unchecked")
    protected G self() {
        return (G) this;
    }

    public World getWorld() {
        return world;
    }

    public B getBoss() {
        return boss;
    }

    public int getTotalAttackWeight() {
        return this.attacks.values().stream()
                .mapToInt(BossAttack::getWeight)
                .sum();
    }

    @Nullable
    public BossAttack<S, B, G> getCurrentAttack() {
        return currentAttack;
    }

    /**
     * Returns null by default, if not null then this will override any other attack so the boss
     * will only do this state/attack. Meant for debugging specific states/attacks.
     */
    @Nullable
    public S getDebugState() {
        return null;
    }
}
