package net.soulsweaponry.entity.ai.goal.hitboxes.returningknight.maceofspades.shadow;

import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class MaceOfSpadesShadowHitbox {

    public static void updateMaceHitbox(RotatableHitbox hitbox, ReturningKnight boss, int attackTick) {
        var path = MaceOfSpadesShadowPaths.MACE_OF_SPADES_SHADOW_1;
        Vec3d size = MaceOfSpadesShadowTimings.MACE_SIZE_1;
        switch (boss.getState()) {
            case MACE_OF_SPADES_2_PHASE_2 -> {
                path = MaceOfSpadesShadowPaths.MACE_OF_SPADES_SHADOW_2;
                size = MaceOfSpadesShadowTimings.MACE_SIZE_2;
            }
            case MACE_OF_SPADES_3_PHASE_2 -> {
                path = MaceOfSpadesShadowPaths.MACE_OF_SPADES_SHADOW_3;
                size = MaceOfSpadesShadowTimings.MACE_SIZE_3;
            }
        }
        BossHitboxHelper.updateKeyframedLocalHitbox(hitbox, boss, boss.bodyYaw, size, boss.getAnimationSpeed(), path, attackTick);
    }

    /**
     * Used for the initial swipe for all the Mace of Spades Shadow variations.
     */
    public static boolean isDamageTickSwipe(ReturningKnight boss, int attackTick) {
        int damageStart = MaceOfSpadesShadowTimings.SWIPE_DAMAGE_START_TICK_1;
        int damageEnd = MaceOfSpadesShadowTimings.SWIPE_DAMAGE_END_TICK_1;
        switch (boss.getState()) {
            case MACE_OF_SPADES_2_PHASE_2 -> {
                damageStart = MaceOfSpadesShadowTimings.SWIPE_DAMAGE_START_TICK_2;
                damageEnd = MaceOfSpadesShadowTimings.SWIPE_DAMAGE_END_TICK_2;
            }
            case MACE_OF_SPADES_3_PHASE_2 -> {
                damageStart = MaceOfSpadesShadowTimings.ONE_HANDED_SWIPE_START_TICK_3;
                damageEnd = MaceOfSpadesShadowTimings.ONE_HANDED_SWIPE_END_TICK_3;
            }
        }
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), damageStart, damageEnd);
    }

    /**
     * Used for the Obliterate smash part of the attack for variation 1 and 2.
     */
    public static boolean isDamageTickSmash(ReturningKnight boss, int attackTick) {
        int damageStart = MaceOfSpadesShadowTimings.SMASH_DAMAGE_START_TICK_1;
        int damageEnd = MaceOfSpadesShadowTimings.SMASH_DAMAGE_END_TICK_1;
        if (boss.isState(ReturningKnight.States.MACE_OF_SPADES_2_PHASE_2)) {
            damageStart = MaceOfSpadesShadowTimings.SMASH_DAMAGE_START_TICK_2;
            damageEnd = MaceOfSpadesShadowTimings.SMASH_DAMAGE_END_TICK_2;
        }
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), damageStart, damageEnd);
    }

    /**
     * Used for the spin in variation 3 (shadow).
     */
    public static boolean isDamageSpin(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), MaceOfSpadesShadowTimings.SPIN_DAMAGE_START_TICK_3, MaceOfSpadesShadowTimings.SPIN_DAMAGE_END_TICK_3);
    }

    /**
     * Used for the thrust in variation 3 (shadow).
     */
    public static boolean isDamageThrust(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), MaceOfSpadesShadowTimings.THRUST_DAMAGE_START_TICK_3, MaceOfSpadesShadowTimings.THRUST_DAMAGE_END_TICK_3);
    }

    public static boolean isDebugTick(ReturningKnight boss, int attackTick) {
        int start = MaceOfSpadesShadowTimings.DEBUG_START_TICK_1;
        int end = MaceOfSpadesShadowTimings.DEBUG_END_TICK_1;
        switch (boss.getState()) {
            case MACE_OF_SPADES_2_PHASE_2 -> {
                start = MaceOfSpadesShadowTimings.DEBUG_START_TICK_2;
                end = MaceOfSpadesShadowTimings.DEBUG_END_TICK_2;
            }
            case MACE_OF_SPADES_3_PHASE_2 -> {
                start = MaceOfSpadesShadowTimings.DEBUG_START_TICK_3;
                end = MaceOfSpadesShadowTimings.DEBUG_END_TICK_3;
            }
        }
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), start, end);
    }
}
