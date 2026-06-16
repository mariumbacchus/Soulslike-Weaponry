package net.soulsweaponry.entity.ai.goal.hitboxes.returningknight;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class ObliterateHitbox {

    public static final Vec3d OBLITERATE_MACE_SIZE = new Vec3d(8, 5, 6);

    public static final int OBLITERATE_DAMAGE_START_TICK = 34;
    public static final int OBLITERATE_DAMAGE_END_TICK = 40;

    public static final int OBLITERATE_DEBUG_START_TICK = 1;
    public static final int OBLITERATE_DEBUG_END_TICK = 64;

    private static final BossHitboxHelper.Keyframe[] OBLITERATE_MACE_PATH = new BossHitboxHelper.Keyframe[] {
            new BossHitboxHelper.Keyframe(2, new Vec3d(-2, 2.0, 0)),
            new BossHitboxHelper.Keyframe(16, new Vec3d(0, 4.0, 0)),
            new BossHitboxHelper.Keyframe(26, new Vec3d(2, 4.7, 0)),
            new BossHitboxHelper.Keyframe(34, new Vec3d(4, 8.0, 0)),
            new BossHitboxHelper.Keyframe(35, new Vec3d(5.5, 4, 0)),
            new BossHitboxHelper.Keyframe(36, new Vec3d(5.5, 2, 0)),
            new BossHitboxHelper.Keyframe(37, new Vec3d(5.6, 0, 0)),
            new BossHitboxHelper.Keyframe(40, new Vec3d(5.6, 0, 0)),
            new BossHitboxHelper.Keyframe(48, new Vec3d(4.0, 2.5, 0)),
            new BossHitboxHelper.Keyframe(64, new Vec3d(-1, 3.0, 0))
    };

    public static RotatableHitbox createObliterateMaceHitboxPlaceholder() {
        return BossHitboxHelper.createPlaceholder(OBLITERATE_MACE_SIZE);
    }

    public static void updateObliterateMaceHitbox(RotatableHitbox hitbox, ReturningKnight boss, BlockPos targetPos, int attackTick) {
        double downExtension = BossHitboxHelper.getDownExtensionFromTarget(boss, targetPos, 2.0D);
        BossHitboxHelper.updateKeyframedLocalHitbox(hitbox, boss, boss.bodyYaw, OBLITERATE_MACE_SIZE, boss.getAnimationSpeed(), OBLITERATE_MACE_PATH, attackTick, downExtension);
    }

    public static boolean isObliterateDamageTick(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), OBLITERATE_DAMAGE_START_TICK, OBLITERATE_DAMAGE_END_TICK);
    }

    public static boolean isObliterateDebugTick(ReturningKnight boss, int attackTick) {
        return BossHitboxHelper.isScaledTickInRange(attackTick, boss.getAnimationSpeed(), OBLITERATE_DEBUG_START_TICK, OBLITERATE_DEBUG_END_TICK);
    }
}