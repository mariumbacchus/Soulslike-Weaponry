package net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades;

import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class MaceOfSpadesHitbox {

    public static RotatableHitbox createMaceHitboxPlaceholder() {
        return BossHitboxHelper.createPlaceholder(MaceOfSpadesTimings.MACE_SIZE_1);
    }

    public static void updateMaceHitbox(RotatableHitbox hitbox, ReturningKnight boss, int attackTick) {
        var path = MaceOfSpadesPaths.PATH_1;
        Vec3d size = MaceOfSpadesTimings.MACE_SIZE_1;
        switch (boss.getState()) {
            case MACE_OF_SPADES_2 -> {
                path = MaceOfSpadesPaths.PATH_2;
                size = MaceOfSpadesTimings.MACE_SIZE_2;
            }
            case MACE_OF_SPADES_3 -> {
                path = MaceOfSpadesPaths.PATH_3;
                size = MaceOfSpadesTimings.MACE_SIZE_3;
            }
            case MACE_OF_SPADES_4_SPIN -> {
                path = MaceOfSpadesPaths.PATH_4;
                size = MaceOfSpadesTimings.MACE_SIZE_4;
            }
        }
        BossHitboxHelper.updateKeyframedLocalHitbox(hitbox, boss, boss.bodyYaw, size, boss.getAnimationSpeed(), path, attackTick);
    }

    /**
     * Used for the initial swipe for all the Mace of Spades variations.
     */
    public static boolean isDamageTickSwipe(ReturningKnight boss, int attackTick) {
        int damageStart = MaceOfSpadesTimings.SWIPE_DAMAGE_START_TICK_1;
        int damageEnd = MaceOfSpadesTimings.SWIPE_DAMAGE_END_TICK_1;
        switch (boss.getState()) {
            case MACE_OF_SPADES_2 -> {
                damageStart = MaceOfSpadesTimings.SWIPE_DAMAGE_START_TICK_2;
                damageEnd = MaceOfSpadesTimings.SWIPE_DAMAGE_END_TICK_2;
            }
            case MACE_OF_SPADES_3 -> {
                damageStart = MaceOfSpadesTimings.ONE_HANDED_SWIPE_DAMAGE_START_TICK_3;
                damageEnd = MaceOfSpadesTimings.ONE_HANDED_SWIPE_DAMAGE_END_TICK_3;
            }
            case MACE_OF_SPADES_4_SPIN -> {
                damageStart = MaceOfSpadesTimings.ONE_HANDED_SWIPE_AND_SPIN_DAMAGE_START_TICK_4;
                damageEnd = MaceOfSpadesTimings.ONE_HANDED_SWIPE_AND_SPIN_DAMAGE_END_TICK_4;
            }
        }
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), damageStart, damageEnd);
    }

    /**
     * Used for the Obliterate smash part of the attack for variation 1 and 2.
     */
    public static boolean isDamageTickSmash(ReturningKnight boss, int attackTick) {
        int damageStart = MaceOfSpadesTimings.SMASH_DAMAGE_START_TICK_1;
        int damageEnd = MaceOfSpadesTimings.SMASH_DAMAGE_END_TICK_1;
        if (boss.isState(ReturningKnight.States.MACE_OF_SPADES_2)) {
            damageStart = MaceOfSpadesTimings.SMASH_DAMAGE_START_TICK_2;
            damageEnd = MaceOfSpadesTimings.SMASH_DAMAGE_END_TICK_2;
        }
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), damageStart, damageEnd);
    }

    /**
     * Used for the two-handed swipe in variation 3.
     */
    public static boolean isDamageTwoHandedSwipe(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), MaceOfSpadesTimings.TWO_HANDED_SWIPE_DAMAGE_START_TICK_3, MaceOfSpadesTimings.TWO_HANDED_SWIPE_DAMAGE_END_TICK_3);
    }

    /**
     * Used for the charged swipe in variation 3.
     */
    public static boolean isDamageChargedSwipe(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), MaceOfSpadesTimings.CHARGED_SWIPE_DAMAGE_START_TICK_3, MaceOfSpadesTimings.CHARGED_SWIPE_DAMAGE_END_TICK_3);
    }

    /**
     * Used for the spin in variation 4.
     */
    public static boolean isDamageSpin(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), MaceOfSpadesTimings.SPIN_DAMAGE_START_TICK_4, MaceOfSpadesTimings.SPIN_DAMAGE_END_TICK_4);
    }

    /**
     * Used for the thrust in variation 4.
     */
    public static boolean isDamageThrust(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), MaceOfSpadesTimings.THRUST_DAMAGE_START_TICK_4, MaceOfSpadesTimings.THRUST_DAMAGE_END_TICK_4);
    }

    public static boolean isDebugTick(ReturningKnight boss, int attackTick) {
        int start = MaceOfSpadesTimings.DEBUG_START_TICK_1;
        int end = MaceOfSpadesTimings.DEBUG_END_TICK_1;
        switch (boss.getState()) {
            case MACE_OF_SPADES_2 -> {
                start = MaceOfSpadesTimings.DEBUG_START_TICK_2;
                end = MaceOfSpadesTimings.DEBUG_END_TICK_2;
            }
            case MACE_OF_SPADES_3 -> {
                start = MaceOfSpadesTimings.DEBUG_START_TICK_3;
                end = MaceOfSpadesTimings.DEBUG_END_TICK_3;
            }
            case MACE_OF_SPADES_4_SPIN -> {
                start = MaceOfSpadesTimings.DEBUG_START_TICK_4;
                end = MaceOfSpadesTimings.DEBUG_END_TICK_4;
            }
        }
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), start, end);
    }
}
