package net.soulsweaponry.entity.ai.goal.hitboxes.returningknight;

import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class MaceOfSpadesHitbox {

    public static final Vec3d MACE_SIZE = new Vec3d(6, 5, 6);

    public static final int SWIPE_DAMAGE_START_TICK = 12;
    public static final int SWIPE_DAMAGE_END_TICK = 17;

    public static final int SMASH_DAMAGE_START_TICK = 40;
    public static final int SMASH_DAMAGE_END_TICK = 43;

    public static final int DEBUG_START_TICK = 1;
    public static final int DEBUG_END_TICK = 70;

    public static RotatableHitbox createMaceHitboxPlaceholder() {
        return BossHitboxHelper.createPlaceholder(MACE_SIZE);
    }

    public static void updateMaceHitbox(RotatableHitbox hitbox, ReturningKnight boss, int attackTick) {
        BossHitboxHelper.updateKeyframedLocalHitbox(hitbox, boss, boss.bodyYaw, MACE_SIZE, boss.getAnimationSpeed(), MACE_PATH, attackTick);
    }

    public static boolean isDamageTickSwipe(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), SWIPE_DAMAGE_START_TICK, SWIPE_DAMAGE_END_TICK);
    }

    public static boolean isDamageTickSmash(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), SMASH_DAMAGE_START_TICK, SMASH_DAMAGE_END_TICK);
    }

    public static boolean isDebugTick(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), DEBUG_START_TICK, DEBUG_END_TICK);
    }

    private static final BossHitboxHelper.Keyframe[] MACE_PATH = new BossHitboxHelper.Keyframe[] {
            new BossHitboxHelper.Keyframe(0, new Vec3d(-0.7, 7.7, 0.9)),
            new BossHitboxHelper.Keyframe(1, new Vec3d(-1.3, 8.1, 0.9)),
            new BossHitboxHelper.Keyframe(2, new Vec3d(-1.3, 8.2, 1.1)),
            new BossHitboxHelper.Keyframe(3, new Vec3d(-1.5, 8.4, 1.4)),
            new BossHitboxHelper.Keyframe(4, new Vec3d(-1.9, 8.6, 1.8)),
            new BossHitboxHelper.Keyframe(5, new Vec3d(-2.3, 8.6, 2.4)),
            new BossHitboxHelper.Keyframe(6, new Vec3d(-2.7, 8.5, 2.9)),
            new BossHitboxHelper.Keyframe(7, new Vec3d(-2.9, 8.3, 3.6)),
            new BossHitboxHelper.Keyframe(8, new Vec3d(-3.1, 8.0, 4.1)),
            new BossHitboxHelper.Keyframe(9, new Vec3d(-3.2, 7.7, 4.4)),
            new BossHitboxHelper.Keyframe(10, new Vec3d(-3.3, 7.5, 4.6)),
            new BossHitboxHelper.Keyframe(11, new Vec3d(-3.3, 7.5, 4.6)),
            new BossHitboxHelper.Keyframe(12, new Vec3d(-2.9, 7.5, 4.9)),
            new BossHitboxHelper.Keyframe(13, new Vec3d(-1.1, 7.3, 6.3)),
            new BossHitboxHelper.Keyframe(14, new Vec3d(2.6, 5.4, 6.4)),
            new BossHitboxHelper.Keyframe(15, new Vec3d(5.2, 2.8, 2.7)),
            new BossHitboxHelper.Keyframe(16, new Vec3d(4.4, 2.9, -2.3)),
            new BossHitboxHelper.Keyframe(17, new Vec3d(3.2, 4.6, -4.1)),
            new BossHitboxHelper.Keyframe(18, new Vec3d(1.1, 8.0, -4.3)),
            new BossHitboxHelper.Keyframe(19, new Vec3d(-0.9, 9.4, -3.2)),
            new BossHitboxHelper.Keyframe(20, new Vec3d(-0.4, 9.6, -3.2)),
            new BossHitboxHelper.Keyframe(21, new Vec3d(1.0, 10.5, -2.9)),
            new BossHitboxHelper.Keyframe(22, new Vec3d(1.8, 11.9, -1.2)),
            new BossHitboxHelper.Keyframe(23, new Vec3d(-0.1, 13.1, 1.1)),
            new BossHitboxHelper.Keyframe(24, new Vec3d(-3.6, 12.1, 2.3)),
            new BossHitboxHelper.Keyframe(25, new Vec3d(-5.9, 9.7, 2.8)),
            new BossHitboxHelper.Keyframe(26, new Vec3d(-6.6, 7.7, 3.1)),
            new BossHitboxHelper.Keyframe(27, new Vec3d(-7.2, 7.2, 1.9)),
            new BossHitboxHelper.Keyframe(28, new Vec3d(-7.3, 6.8, 1.3)),
            new BossHitboxHelper.Keyframe(29, new Vec3d(-7.4, 6.5, 0.8)),
            new BossHitboxHelper.Keyframe(30, new Vec3d(-7.5, 6.2, 0.3)),
            new BossHitboxHelper.Keyframe(31, new Vec3d(-7.5, 5.9, 0.0)),
            new BossHitboxHelper.Keyframe(32, new Vec3d(-7.5, 5.7, -0.4)),
            new BossHitboxHelper.Keyframe(33, new Vec3d(-7.4, 5.5, -0.7)),
            new BossHitboxHelper.Keyframe(34, new Vec3d(-7.4, 5.4, -0.9)),
            new BossHitboxHelper.Keyframe(35, new Vec3d(-7.4, 5.3, -1.0)),
            new BossHitboxHelper.Keyframe(36, new Vec3d(-7.4, 5.3, -1.0)),
            new BossHitboxHelper.Keyframe(37, new Vec3d(-7.4, 5.3, -1.0)),
            new BossHitboxHelper.Keyframe(38, new Vec3d(-7.3, 6.3, -1.0)),
            new BossHitboxHelper.Keyframe(39, new Vec3d(-4.8, 10.5, -0.6)),
            new BossHitboxHelper.Keyframe(40, new Vec3d(2.8, 11.4, 0.4)),
            new BossHitboxHelper.Keyframe(41, new Vec3d(7.7, 6.4, 0.9)),
            new BossHitboxHelper.Keyframe(42, new Vec3d(8.0, 2.9, 0.6)),
            new BossHitboxHelper.Keyframe(43, new Vec3d(7.6, 1.7, 0.2)),
            new BossHitboxHelper.Keyframe(44, new Vec3d(7.6, 1.6, 0.2)),
            new BossHitboxHelper.Keyframe(45, new Vec3d(7.6, 1.6, 0.2)),
            new BossHitboxHelper.Keyframe(46, new Vec3d(7.6, 1.6, 0.2)),
            new BossHitboxHelper.Keyframe(47, new Vec3d(7.5, 1.7, -0.1)),
            new BossHitboxHelper.Keyframe(48, new Vec3d(7.3, 1.9, -0.5)),
            new BossHitboxHelper.Keyframe(49, new Vec3d(7.0, 2.0, -1.0)),
            new BossHitboxHelper.Keyframe(50, new Vec3d(6.7, 2.1, -1.3)),
            new BossHitboxHelper.Keyframe(51, new Vec3d(6.4, 2.3, -1.7)),
            new BossHitboxHelper.Keyframe(52, new Vec3d(6.2, 2.3, -1.8)),
            new BossHitboxHelper.Keyframe(53, new Vec3d(6.1, 2.4, -1.8)),
            new BossHitboxHelper.Keyframe(54, new Vec3d(6.1, 2.4, -1.8)),
            new BossHitboxHelper.Keyframe(55, new Vec3d(6.0, 2.5, -1.7)),
            new BossHitboxHelper.Keyframe(56, new Vec3d(5.8, 2.6, -1.5)),
            new BossHitboxHelper.Keyframe(57, new Vec3d(5.7, 2.8, -1.3)),
            new BossHitboxHelper.Keyframe(58, new Vec3d(5.7, 3.0, -1.2)),
            new BossHitboxHelper.Keyframe(59, new Vec3d(5.6, 3.3, -1.0)),
            new BossHitboxHelper.Keyframe(60, new Vec3d(5.5, 3.7, -0.9)),
            new BossHitboxHelper.Keyframe(61, new Vec3d(5.4, 4.2, -0.8)),
            new BossHitboxHelper.Keyframe(62, new Vec3d(5.3, 4.7, -0.8)),
            new BossHitboxHelper.Keyframe(63, new Vec3d(5.1, 5.6, -0.5)),
            new BossHitboxHelper.Keyframe(64, new Vec3d(4.6, 6.7, 0.1)),
            new BossHitboxHelper.Keyframe(65, new Vec3d(3.7, 7.8, 0.7)),
            new BossHitboxHelper.Keyframe(66, new Vec3d(2.5, 8.5, 1.1)),
            new BossHitboxHelper.Keyframe(67, new Vec3d(1.2, 8.7, 1.3)),
            new BossHitboxHelper.Keyframe(68, new Vec3d(0.2, 8.6, 1.3)),
            new BossHitboxHelper.Keyframe(69, new Vec3d(-0.4, 8.3, 1.1)),
            new BossHitboxHelper.Keyframe(70, new Vec3d(-0.7, 8.0, 1.0))
    };
}
