package net.soulsweaponry.particles;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.soulsweaponry.registry.ParticleRegistry;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ChainLightningHandler {

    // Consider adding these as input variables
    private static final float ARC_LENGTH = 1.0f;
    private static final float ARC_VARIATION = 1.5f;
    private static final float INACCURACY = 0.75f;
    private static final float MIN_SEGMENT_DIST = 3.0f;
    private static final float BRANCH_CHANCE = 2f;
    private static final int DENSITY = 8; // particles per block

    public static void spawnChainLightning(ClientWorld world, Vector3f start0, Vector3f end0) {
        Random rand = world.random;
        // work on copies only
        Vector3f start = new Vector3f(start0);
        Vector3f end   = new Vector3f(end0);

        List<Vector3f> points = new ArrayList<>();
        points.add(new Vector3f(start)); // copy once more for the list

        // build the jagged polyline
        Vector3f last = new Vector3f(start);
        while (last.distanceSquared(end) > MIN_SEGMENT_DIST * MIN_SEGMENT_DIST) {
            // non‐mutating sub + normalize
            Vector3f dir = new Vector3f(end)
                    .sub(last) // end-last, but end is the copy
                    .normalize();
            // randomize into a fresh vector
            Vector3f rnd = new Vector3f(
                    rand.nextFloat() * 2 - 1,
                    rand.nextFloat() * 2 - 1,
                    rand.nextFloat() * 2 - 1
            ).mul(INACCURACY);
            dir.add(rnd).normalize();

            float len = MathHelper.nextFloat(rand, ARC_LENGTH * ARC_VARIATION, ARC_LENGTH);
            // compute next = last + dir * len without touching last
            Vector3f next = new Vector3f(dir).mul(len).add(last);
            points.add(next);
            last = next;
        }
        points.add(new Vector3f(end));

        // particles
        for (int i = 0; i < points.size() - 1; i++) {
            Vector3f p1 = points.get(i);
            Vector3f p2 = points.get(i + 1);
            spawnParticlesOnLine(world, p1, p2);
            if (rand.nextFloat() < BRANCH_CHANCE) {
                Vector3f branchDir = new Vector3f(p2)
                        .sub(p1) // p2−p1
                        .normalize();
                // random side‐jitter
                branchDir.add(new Vector3f(
                        rand.nextFloat() * 2 - 1,
                        rand.nextFloat() * 2 - 1,
                        rand.nextFloat() * 2 - 1
                ).mul(INACCURACY*2)).normalize();

                float branchLen = MathHelper.nextFloat(rand,
                        ARC_LENGTH * ARC_VARIATION * 0.5f,
                        ARC_LENGTH * 0.5f
                );
                Vector3f branchEnd = new Vector3f(branchDir).mul(branchLen).add(p1);
                spawnParticlesOnLine(world, p1, branchEnd);
            }
        }
    }

    private static void spawnParticlesOnLine(ClientWorld world, Vector3f a, Vector3f b) {
        double dist = a.distance(b);
        int steps = MathHelper.ceil(dist * DENSITY);
        for (int i = 0; i <= steps; i++) {
            double t = (double)i / (double)steps;
            double x = MathHelper.lerp(t, a.x, b.x);
            double y = MathHelper.lerp(t, a.y, b.y);
            double z = MathHelper.lerp(t, a.z, b.z);
            world.addParticle(ParticleRegistry.SOUL_SPARK, x, y, z,0, 0, 0);
        }
    }
}
