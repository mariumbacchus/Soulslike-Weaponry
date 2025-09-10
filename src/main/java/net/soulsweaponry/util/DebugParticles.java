package net.soulsweaponry.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class DebugParticles {

    public static void spawnBoxCorners(ServerWorld world, Box box, ParticleEffect particle, int countPerCorner) {
        for (Vec3d c : corners(box)) {
            world.spawnParticles(particle, c.x, c.y, c.z, countPerCorner,
                    0.0, 0.0, 0.0, 0.0); // no spread, no speed
        }
    }

    private static Vec3d[] corners(Box b) {
        double x1 = b.minX, y1 = b.minY, z1 = b.minZ;
        double x2 = b.maxX, y2 = b.maxY, z2 = b.maxZ;
        return new Vec3d[] {
                new Vec3d(x1, y1, z1),
                new Vec3d(x1, y1, z2),
                new Vec3d(x1, y2, z1),
                new Vec3d(x1, y2, z2),
                new Vec3d(x2, y1, z1),
                new Vec3d(x2, y1, z2),
                new Vec3d(x2, y2, z1),
                new Vec3d(x2, y2, z2),
        };
    }
}
