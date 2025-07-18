package net.soulsweaponry.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

public class EntityLookUtil {

    /**
     * Returns a normalized direction vector pointing from the boss's eye position
     * toward the center of targetPos.
     */
    public static Vec3d getDirectionTo(Entity entity, BlockPos targetPos) {
        Vec3d origin = entity.getEyePos();
        Vec3d target = new Vec3d(
                targetPos.getX() + 0.5,
                targetPos.getY() + 0.5,
                targetPos.getZ() + 0.5
        );
        return target.subtract(origin).normalize();
    }

    /**
     * Given a unit direction vector (e.g. from getDirectionTo), return the block position
     * exactly "distance" blocks along that ray from the boss's feet.
     */
    public static BlockPos getBlockPosAhead(Entity entity, Vec3d direction, double distance) {
        Vec3d start = entity.getPos();
        Vec3d end = start.add(direction.multiply(distance));
        return BlockPos.ofFloored(end.x, end.y, end.z);
    }

    /**
     * Derive a vanilla-yaw (degrees) from a direction vector.
     * Yaw is 0 when looking along +Z, 90 deg along –X, etc.
     */
    public static float getYawFromDirection(Vec3d dir) {
        return (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
    }

    /**
     * Derive a vanilla-pitch (degrees) from a direction vector.
     * Pitch is 0 when horizontal, positive looking down, negative looking up.
     */
    public static float getPitchFromDirection(Vec3d dir) {
        double horiz = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        return (float) -Math.toDegrees(Math.atan2(dir.y, horiz));
    }

    /**
     * Gets the block position {@code blocksOut} blocks away based on the yaw of the entity,
     * where the Y coordinate is equal to the Y of the entity.
     * NOTE: For geckolib entities (especially bosses), the result pos is inconsistent
     * for some reason, always slightly to the left or right of the boss.
     */
    public static BlockPos getBlockAhead(Entity entity, int blocksOut) {
        double yawRad = Math.toRadians(entity.getYaw());
        double dx = -Math.sin(yawRad);
        double dz =  Math.cos(yawRad);
        double x = entity.getX() + dx * blocksOut;
        double y = entity.getY();
        double z = entity.getZ() + dz * blocksOut;
        return BlockPos.ofFloored(x, y, z);
    }

    public static void addParticleToCorners(Box box, World world) {
        double minX = box.getMin(Direction.Axis.X), maxX = box.getMax(Direction.Axis.X);
        double minY = box.getMin(Direction.Axis.Y), maxY = box.getMax(Direction.Axis.Y);
        double minZ = box.getMin(Direction.Axis.Z), maxZ = box.getMax(Direction.Axis.Z);
        System.out.println("Box corners:");
        for (int xi = 0; xi < 2; xi++) {
            for (int yi = 0; yi < 2; yi++) {
                for (int zi = 0; zi < 2; zi++) {
                    double x = (xi == 0) ? minX : maxX;
                    double y = (yi == 0) ? minY : maxY;
                    double z = (zi == 0) ? minZ : maxZ;
                    Vec3d corner = new Vec3d(x, y, z);
                    System.out.println(corner);
                    ParticleHandler.particleOutburstMap(world, 10, corner.getX(), corner.getY(), corner.getZ(), ParticleEvents.SOUL_FLAME_SMALL_OUTBURST_MAP, 0.01f);
                }
            }
        }
    }
}
