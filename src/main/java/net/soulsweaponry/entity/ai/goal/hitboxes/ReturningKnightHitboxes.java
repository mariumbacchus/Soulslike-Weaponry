package net.soulsweaponry.entity.ai.goal.hitboxes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;

public class ReturningKnightHitboxes {

    public static final Vec3d OBLITERATE_MACE_SIZE = new Vec3d(8, 5, 6);

    public static final int OBLITERATE_DAMAGE_START_TICK = 17;
    public static final int OBLITERATE_DAMAGE_END_TICK = 20;

    public static final int OBLITERATE_DEBUG_START_TICK = 1;
    public static final int OBLITERATE_DEBUG_END_TICK = 32;

    private static final BossHitboxHelper.Keyframe[] OBLITERATE_MACE_PATH = new BossHitboxHelper.Keyframe[] {
            new BossHitboxHelper.Keyframe(1, new Vec3d(-2, 2.0, 0)),
            new BossHitboxHelper.Keyframe(8, new Vec3d(0, 4.0, 0)),
            new BossHitboxHelper.Keyframe(13, new Vec3d(2, 4.7, 0)),
            new BossHitboxHelper.Keyframe(17, new Vec3d(4, 8.0, 0)),
            new BossHitboxHelper.Keyframe(18, new Vec3d(5.5, 4, 0)),
            new BossHitboxHelper.Keyframe(19, new Vec3d(5.6, 0, 0)),
            new BossHitboxHelper.Keyframe(20, new Vec3d(5.6, 0, 0)),
            new BossHitboxHelper.Keyframe(24, new Vec3d(4.0, 2.5, 0)),
            new BossHitboxHelper.Keyframe(32, new Vec3d(-1, 3.0, 0))
    };

    public static RotatableHitbox createObliterateMaceHitboxPlaceholder() {
        return BossHitboxHelper.createPlaceholder(OBLITERATE_MACE_SIZE);
    }

    public static void updateObliterateMaceHitbox(RotatableHitbox hitbox, ReturningKnight boss, BlockPos targetPos, int attackTick) {
        double downExtension = BossHitboxHelper.getDownExtensionFromTarget(boss, targetPos, 2.0D);
        BossHitboxHelper.updateKeyframedLocalHitbox(hitbox, boss, boss.bodyYaw, OBLITERATE_MACE_SIZE, OBLITERATE_MACE_PATH, attackTick, downExtension);
    }

    public static boolean isObliterateDamageTick(int attackTick) {
        return BossHitboxHelper.isTickInRange(attackTick, OBLITERATE_DAMAGE_START_TICK, OBLITERATE_DAMAGE_END_TICK);
    }

    public static boolean isObliterateDebugTick(int attackTick) {
        return BossHitboxHelper.isTickInRange(attackTick, OBLITERATE_DEBUG_START_TICK, OBLITERATE_DEBUG_END_TICK);
    }
}