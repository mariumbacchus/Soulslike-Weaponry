package net.soulsweaponry.entity.ai.goal.hitboxes;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.config.EntityConfig;

import java.util.function.BiPredicate;

/**
 * NOTE:
 * Because of a quirk with boss yaw being 90 degrees off, getting the forward rotation from yaw requires this +90 degrees which rotates the box.
 * Therefore:
 * <li>x now represents away/depth of the boss</li>
 * <li>y is up like usual</li>
 * <li>z is to the <b>left</b></li>
 */
public class BossHitboxHelper {

    private static final double EPSILON = 1.0E-6;

    public record Keyframe(int tick, Vec3d offset) {}

    /**
     * Reminder that x is local depth, y is local up and z is local left
     */
    public static RotatableHitbox createPlaceholder(Vec3d size) {
        return new RotatableHitbox(Vec3d.ZERO, size,
                new Vec3d(1, 0, 0),
                new Vec3d(0, 1, 0),
                new Vec3d(0, 0, 1));
    }

    public static boolean isTickInRange(int tick, int start, int end) {
        return tick >= start && tick <= end;
    }

    public static void updateKeyframedLocalHitbox(RotatableHitbox hitbox, Entity owner, float yaw, Vec3d baseSize, Keyframe[] keyframes, int attackTick, double extraDownExtension) {
        Vec3d forward = getForwardFromYaw(yaw);
        Vec3d up = new Vec3d(0.0, 1.0, 0.0);
        Vec3d right = up.crossProduct(forward).normalize();

        Vec3d localOffset = sampleKeyframes(keyframes, attackTick);
        Vec3d adjustedSize = new Vec3d(baseSize.x, baseSize.y + extraDownExtension, baseSize.z);
        Vec3d adjustedLocalOffset = localOffset.add(0.0D, -extraDownExtension / 2.0D, 0.0D);

        Vec3d worldCenter = owner.getPos()
                .add(right.multiply(adjustedLocalOffset.x))
                .add(up.multiply(adjustedLocalOffset.y))
                .add(forward.multiply(adjustedLocalOffset.z));

        hitbox.setTransform(worldCenter, adjustedSize, right, up, forward);
    }

    public static Vec3d sampleKeyframes(Keyframe[] keyframes, int attackTick) {
        if (keyframes == null || keyframes.length == 0) {
            return Vec3d.ZERO;
        }
        if (attackTick <= keyframes[0].tick()) {
            return keyframes[0].offset();
        }
        for (int i = 0; i < keyframes.length - 1; i++) {
            Keyframe a = keyframes[i];
            Keyframe b = keyframes[i + 1];
            if (attackTick <= b.tick()) {
                double t = (double) (attackTick - a.tick()) / (double) (b.tick() - a.tick());
                t = MathHelper.clamp(t, 0.0D, 1.0D);
                return lerp(a.offset(), b.offset(), t);
            }
        }
        return keyframes[keyframes.length - 1].offset();
    }

    public static Vec3d lerp(Vec3d a, Vec3d b, double t) {
        return new Vec3d(
                MathHelper.lerp(t, a.x, b.x),
                MathHelper.lerp(t, a.y, b.y),
                MathHelper.lerp(t, a.z, b.z)
        );
    }

    public static Vec3d getForwardFromYaw(float yaw) {
        double radians = Math.toRadians(yaw + 90.0F);
        return new Vec3d(
                -Math.sin(radians),
                0.0,
                Math.cos(radians)
        ).normalize();
    }

    public static Vec3d worldToBossLocal(Entity entity, Vec3d worldPos, float yaw) {
        Vec3d delta = worldPos.subtract(entity.getPos());

        Vec3d forward = getForwardFromYaw(yaw);
        Vec3d up = new Vec3d(0.0, 1.0, 0.0);
        Vec3d right = up.crossProduct(forward).normalize();

        return new Vec3d(
                delta.dotProduct(right),
                delta.y,
                delta.dotProduct(forward)
        );
    }

    public static RotatableHitbox createTargetedGroundHitbox(Entity owner, BlockPos targetPos, Vec3d size, Vec3d offset) {
        RotatableHitbox hitbox = createPlaceholder(size);
        updateTargetedGroundHitbox(hitbox, owner, targetPos, size, offset);
        return hitbox;
    }

    public static void updateTargetedGroundHitbox(RotatableHitbox hitbox, Entity owner, BlockPos targetPos, Vec3d size, Vec3d offset) {
        Vec3d center = Vec3d.ofCenter(targetPos).add(offset);
        Vec3d forward = center.subtract(owner.getPos());
        forward = new Vec3d(forward.x, 0.0, forward.z);

        if (forward.lengthSquared() < EPSILON) {
            forward = owner.getRotationVec(1.0F);
            forward = new Vec3d(forward.x, 0.0, forward.z);
        }

        forward = forward.normalize();

        Vec3d up = new Vec3d(0.0, 1.0, 0.0);
        Vec3d right = up.crossProduct(forward).normalize();

        hitbox.setTransform(center, size, right, up, forward);
    }

    public static void breakBlocksInsideHitbox(RotatableHitbox hitbox, World world, Entity owner, BiPredicate<BlockState, BlockPos> canBreak, boolean dropBlock) {
        if (world.isClient() || !world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) || !EntityConfig.can_bosses_break_blocks) {
            return;
        }
        Box bounds = hitbox.toAabb();
        int minX = MathHelper.floor(bounds.minX);
        int minY = MathHelper.floor(bounds.minY);
        int minZ = MathHelper.floor(bounds.minZ);

        int maxX = MathHelper.floor(bounds.maxX);
        int maxY = MathHelper.floor(bounds.maxY);
        int maxZ = MathHelper.floor(bounds.maxZ);

        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pos.set(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if (state.isAir() || !canBreak.test(state, pos)) {
                        continue;
                    }
                    Vec3d blockCenter = Vec3d.ofCenter(pos);
                    if (!hitbox.contains(blockCenter)) {
                        continue;
                    }
                    world.breakBlock(pos, dropBlock, owner);
                }
            }
        }
    }

    /**
     * Used to find a suitable ground position within the hitbox, for example for particles like in the Obliterate attack.
     */
    public static Vec3d findGroundImpactPos(World world, Vec3d start, int maxDownDistance) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        int x = MathHelper.floor(start.x);
        int y = MathHelper.floor(start.y);
        int z = MathHelper.floor(start.z);
        for (int i = 0; i <= maxDownDistance; i++) {
            pos.set(x, y - i, z);
            BlockState state = world.getBlockState(pos);
            if (!state.isAir() && state.isSolidBlock(world, pos)) {
                return new Vec3d(start.x, pos.getY() + 1.0D, start.z);
            }
        }
        return start;
    }

    /**
     * Adjust the hitbox downwards if the target is below the boss. Used in Obliterate attack.
     */
    public static double getDownExtensionFromTarget(Entity owner, BlockPos targetPos, double maxExtension) {
        if (targetPos == null) {
            return 0.0D;
        }
        double blocksBelow = owner.getY() - targetPos.getY();
        if (blocksBelow <= 0.0D) {
            return 0.0D;
        }
        return MathHelper.clamp(blocksBelow, 0.0D, maxExtension);
    }
}