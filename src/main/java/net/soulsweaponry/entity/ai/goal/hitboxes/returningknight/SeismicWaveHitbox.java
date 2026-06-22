package net.soulsweaponry.entity.ai.goal.hitboxes.returningknight;

import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class SeismicWaveHitbox {

    public static final Vec3d MACE_SIZE = new Vec3d(7, 5, 6);

    public static final int SWING_START = 43;
    public static final int SWING_END = 47;

    public static final int DEBUG_START = 1;
    public static final int DEBUG_END = 112;

    public static RotatableHitbox createMaceHitboxPlaceholder() {
        return BossHitboxHelper.createPlaceholder(MACE_SIZE);
    }

    public static void updateMaceHitbox(RotatableHitbox hitbox, ReturningKnight boss, int attackTick) {
        BossHitboxHelper.updateKeyframedLocalHitbox(hitbox, boss, boss.bodyYaw, MACE_SIZE, boss.getAnimationSpeed(), PATH, attackTick);
    }

    public static boolean isDamageTick(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), SWING_START, SWING_END);
    }

    public static boolean isDebugTick(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), DEBUG_START, DEBUG_END);
    }

    public static final BossHitboxHelper.Keyframe[] PATH = new BossHitboxHelper.Keyframe[] {
            new BossHitboxHelper.Keyframe(0, new Vec3d(-0.7, 8.1, 0.9)),
            new BossHitboxHelper.Keyframe(1, new Vec3d(-0.8, 7.9, 0.9)),
            new BossHitboxHelper.Keyframe(2, new Vec3d(-0.9, 7.8, 0.9)),
            new BossHitboxHelper.Keyframe(3, new Vec3d(-1.0, 7.6, 1.0)),
            new BossHitboxHelper.Keyframe(4, new Vec3d(-1.1, 7.5, 1.0)),
            new BossHitboxHelper.Keyframe(5, new Vec3d(-1.2, 7.3, 1.0)),
            new BossHitboxHelper.Keyframe(6, new Vec3d(-1.3, 7.1, 1.0)),
            new BossHitboxHelper.Keyframe(7, new Vec3d(-1.3, 7.1, 1.0)),
            new BossHitboxHelper.Keyframe(8, new Vec3d(-1.3, 7.2, 1.0)),
            new BossHitboxHelper.Keyframe(9, new Vec3d(-1.2, 7.4, 0.9)),
            new BossHitboxHelper.Keyframe(10, new Vec3d(-0.9, 7.8, 0.9)),
            new BossHitboxHelper.Keyframe(11, new Vec3d(-0.4, 8.3, 0.7)),
            new BossHitboxHelper.Keyframe(12, new Vec3d(0.7, 8.7, 0.3)),
            new BossHitboxHelper.Keyframe(13, new Vec3d(2.2, 8.4, -0.5)),
            new BossHitboxHelper.Keyframe(14, new Vec3d(3.4, 6.7, -1.7)),
            new BossHitboxHelper.Keyframe(15, new Vec3d(3.3, 3.8, -2.4)),
            new BossHitboxHelper.Keyframe(16, new Vec3d(2.6, 1.9, -2.1)),
            new BossHitboxHelper.Keyframe(17, new Vec3d(2.4, 1.9, -2.3)),
            new BossHitboxHelper.Keyframe(18, new Vec3d(2.2, 1.9, -2.6)),
            new BossHitboxHelper.Keyframe(19, new Vec3d(2.0, 1.8, -2.7)),
            new BossHitboxHelper.Keyframe(20, new Vec3d(1.8, 1.8, -2.8)),
            new BossHitboxHelper.Keyframe(21, new Vec3d(1.7, 1.8, -2.9)),
            new BossHitboxHelper.Keyframe(22, new Vec3d(1.6, 1.8, -3.0)),
            new BossHitboxHelper.Keyframe(23, new Vec3d(1.5, 1.8, -3.0)),
            new BossHitboxHelper.Keyframe(24, new Vec3d(1.6, 1.8, -2.9)),
            new BossHitboxHelper.Keyframe(25, new Vec3d(1.7, 1.8, -2.9)),
            new BossHitboxHelper.Keyframe(26, new Vec3d(1.6, 1.8, -2.9)),
            new BossHitboxHelper.Keyframe(27, new Vec3d(1.5, 1.7, -3.0)),
            new BossHitboxHelper.Keyframe(28, new Vec3d(1.5, 1.7, -3.0)),
            new BossHitboxHelper.Keyframe(29, new Vec3d(1.6, 1.7, -3.0)),
            new BossHitboxHelper.Keyframe(30, new Vec3d(1.7, 1.7, -2.9)),
            new BossHitboxHelper.Keyframe(31, new Vec3d(1.9, 1.6, -2.9)),
            new BossHitboxHelper.Keyframe(32, new Vec3d(1.9, 1.6, -2.9)),
            new BossHitboxHelper.Keyframe(33, new Vec3d(2.0, 1.6, -2.8)),
            new BossHitboxHelper.Keyframe(34, new Vec3d(2.1, 1.6, -2.8)),
            new BossHitboxHelper.Keyframe(35, new Vec3d(2.2, 1.6, -2.8)),
            new BossHitboxHelper.Keyframe(36, new Vec3d(2.1, 1.6, -2.8)),
            new BossHitboxHelper.Keyframe(37, new Vec3d(2.1, 1.6, -2.9)),
            new BossHitboxHelper.Keyframe(38, new Vec3d(2.0, 1.6, -3.0)),
            new BossHitboxHelper.Keyframe(39, new Vec3d(2.0, 1.6, -3.1)),
            new BossHitboxHelper.Keyframe(40, new Vec3d(1.9, 1.6, -3.2)),
            new BossHitboxHelper.Keyframe(41, new Vec3d(2.1, 1.6, -3.2)),
            new BossHitboxHelper.Keyframe(42, new Vec3d(2.5, 1.6, -3.0)),
            new BossHitboxHelper.Keyframe(43, new Vec3d(3.1, 1.5, -2.4)),
            new BossHitboxHelper.Keyframe(44, new Vec3d(3.6, 1.6, -1.1)),
            new BossHitboxHelper.Keyframe(45, new Vec3d(3.7, 1.8, 1.0)),
            new BossHitboxHelper.Keyframe(46, new Vec3d(2.1, 2.5, 4.4)),
            new BossHitboxHelper.Keyframe(47, new Vec3d(-1.2, 4.7, 6.4)),
            new BossHitboxHelper.Keyframe(48, new Vec3d(-3.2, 6.2, 6.2)),
            new BossHitboxHelper.Keyframe(49, new Vec3d(-3.7, 6.9, 5.9)),
            new BossHitboxHelper.Keyframe(50, new Vec3d(-3.5, 7.1, 6.0)),
            new BossHitboxHelper.Keyframe(51, new Vec3d(-2.7, 6.8, 6.4)),
            new BossHitboxHelper.Keyframe(52, new Vec3d(-1.9, 6.5, 6.7)),
            new BossHitboxHelper.Keyframe(53, new Vec3d(-1.2, 6.3, 6.8)),
            new BossHitboxHelper.Keyframe(54, new Vec3d(-1.0, 6.1, 6.9)),
            new BossHitboxHelper.Keyframe(55, new Vec3d(-1.1, 5.8, 6.8)),
            new BossHitboxHelper.Keyframe(56, new Vec3d(-1.2, 5.3, 6.8)),
            new BossHitboxHelper.Keyframe(57, new Vec3d(-1.2, 4.9, 6.7)),
            new BossHitboxHelper.Keyframe(58, new Vec3d(-1.1, 4.8, 6.7)),
            new BossHitboxHelper.Keyframe(59, new Vec3d(-0.8, 5.0, 6.8)),
            new BossHitboxHelper.Keyframe(60, new Vec3d(-0.2, 5.7, 7.0)),
            new BossHitboxHelper.Keyframe(61, new Vec3d(0.2, 6.5, 7.0)),
            new BossHitboxHelper.Keyframe(62, new Vec3d(0.1, 8.5, 6.6)),
            new BossHitboxHelper.Keyframe(63, new Vec3d(-0.7, 9.3, 6.3)),
            new BossHitboxHelper.Keyframe(64, new Vec3d(-1.3, 9.5, 6.2)),
            new BossHitboxHelper.Keyframe(65, new Vec3d(-1.5, 9.6, 6.1)),
            new BossHitboxHelper.Keyframe(66, new Vec3d(-1.5, 9.6, 6.1)),
            new BossHitboxHelper.Keyframe(67, new Vec3d(-1.2, 9.5, 6.2)),
            new BossHitboxHelper.Keyframe(68, new Vec3d(-0.9, 9.4, 6.3)),
            new BossHitboxHelper.Keyframe(69, new Vec3d(-0.8, 9.3, 6.4)),
            new BossHitboxHelper.Keyframe(70, new Vec3d(-0.7, 9.2, 6.5)),
            new BossHitboxHelper.Keyframe(71, new Vec3d(-0.3, 9.0, 6.5)),
            new BossHitboxHelper.Keyframe(72, new Vec3d(0.9, 8.3, 6.2)),
            new BossHitboxHelper.Keyframe(73, new Vec3d(3.5, 6.2, 5.0)),
            new BossHitboxHelper.Keyframe(74, new Vec3d(4.2, 2.7, 2.9)),
            new BossHitboxHelper.Keyframe(75, new Vec3d(2.9, 1.2, 1.6)),
            new BossHitboxHelper.Keyframe(76, new Vec3d(2.7, 1.1, 1.4)),
            new BossHitboxHelper.Keyframe(77, new Vec3d(2.9, 1.3, 1.5)),
            new BossHitboxHelper.Keyframe(78, new Vec3d(3.0, 1.3, 1.6)),
            new BossHitboxHelper.Keyframe(79, new Vec3d(3.0, 1.3, 1.6)),
            new BossHitboxHelper.Keyframe(80, new Vec3d(3.0, 1.3, 1.6)),
            new BossHitboxHelper.Keyframe(81, new Vec3d(3.0, 1.3, 1.6)),
            new BossHitboxHelper.Keyframe(82, new Vec3d(3.0, 1.3, 1.6)),
            new BossHitboxHelper.Keyframe(83, new Vec3d(3.0, 1.3, 1.5)),
            new BossHitboxHelper.Keyframe(84, new Vec3d(3.0, 1.3, 1.4)),
            new BossHitboxHelper.Keyframe(85, new Vec3d(3.0, 1.3, 1.4)),
            new BossHitboxHelper.Keyframe(86, new Vec3d(2.9, 1.3, 1.3)),
            new BossHitboxHelper.Keyframe(87, new Vec3d(2.9, 1.3, 1.2)),
            new BossHitboxHelper.Keyframe(88, new Vec3d(2.9, 1.3, 1.1)),
            new BossHitboxHelper.Keyframe(89, new Vec3d(2.9, 1.3, 1.0)),
            new BossHitboxHelper.Keyframe(90, new Vec3d(2.8, 1.3, 0.9)),
            new BossHitboxHelper.Keyframe(91, new Vec3d(2.8, 1.3, 0.9)),
            new BossHitboxHelper.Keyframe(92, new Vec3d(2.7, 1.2, 0.8)),
            new BossHitboxHelper.Keyframe(93, new Vec3d(2.7, 1.2, 0.8)),
            new BossHitboxHelper.Keyframe(94, new Vec3d(2.6, 1.1, 0.8)),
            new BossHitboxHelper.Keyframe(95, new Vec3d(2.5, 1.1, 0.8)),
            new BossHitboxHelper.Keyframe(96, new Vec3d(2.5, 1.1, 0.7)),
            new BossHitboxHelper.Keyframe(97, new Vec3d(2.5, 1.2, 0.5)),
            new BossHitboxHelper.Keyframe(98, new Vec3d(2.5, 1.4, 0.3)),
            new BossHitboxHelper.Keyframe(99, new Vec3d(2.7, 1.8, -0.1)),
            new BossHitboxHelper.Keyframe(100, new Vec3d(3.0, 2.5, -0.5)),
            new BossHitboxHelper.Keyframe(101, new Vec3d(3.4, 3.8, -0.8)),
            new BossHitboxHelper.Keyframe(102, new Vec3d(3.5, 5.8, -0.7)),
            new BossHitboxHelper.Keyframe(103, new Vec3d(3.0, 7.4, 0.2)),
            new BossHitboxHelper.Keyframe(104, new Vec3d(2.1, 8.2, 1.0)),
            new BossHitboxHelper.Keyframe(105, new Vec3d(1.1, 8.4, 1.3)),
            new BossHitboxHelper.Keyframe(106, new Vec3d(0.1, 8.3, 1.3)),
            new BossHitboxHelper.Keyframe(107, new Vec3d(-0.6, 8.0, 1.3)),
            new BossHitboxHelper.Keyframe(108, new Vec3d(-0.9, 7.8, 1.2)),
            new BossHitboxHelper.Keyframe(109, new Vec3d(-1.0, 7.7, 1.1)),
            new BossHitboxHelper.Keyframe(110, new Vec3d(-1.0, 7.7, 1.1)),
            new BossHitboxHelper.Keyframe(111, new Vec3d(-1.0, 7.8, 1.0))
    };
}