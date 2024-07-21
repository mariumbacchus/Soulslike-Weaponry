package net.soulsweaponry.entity.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RandomSummonPos {

    private final World world;
    private final Random random;
    private final int spawnAmount;
    private final int maxFails;
    private final BlockPos start;
    private final int radius;
    private final int minYOffset;
    private final int maxYOffset;
    private final Consumer<Vec3d> consumer;

    /**
     * Helper class that handles spawning of enemies during boss fights.
     * @param world world
     * @param random random (of minecraft's own {@link Random} class)
     * @param spawnAmount amount of entities to spawn
     * @param maxFails max amount of fails before aborting
     * @param start start position/center position
     * @param radius radius out from {@code start} for position calculations
     * @param minYOffset min Y offset from {@code start} position (handled by doing {@code start.getY() - minYOffset}
     * @param maxYOffset max Y offset from {@code start} position (handled by doing {@code start.getY() + maxYOffset}
     * @param consumer method call to handle actual spawning of the entity, called as soon as positions have been calculated and are ready
     */
    public RandomSummonPos(World world, Random random, int spawnAmount, int maxFails, BlockPos start, int radius, int minYOffset, int maxYOffset, Consumer<Vec3d> consumer) {
        this.world = world;
        this.random = random;
        this.spawnAmount = spawnAmount;
        this.maxFails = maxFails;
        this.start = start;
        this.radius = radius;
        this.minYOffset = minYOffset;
        this.maxYOffset = maxYOffset;
        this.consumer = consumer;
    }

    public void applySummonSpawns() {
        int spawns = 0;
        int fails = 0;
        while (spawns < this.spawnAmount) {
            if (fails >= this.maxFails) {
                break;
            }
            BlockPos blockPos = this.getRandomValidSpawn();
            if (blockPos != null) {
                Vec3d pos = blockPos.toCenterPos();
                this.consumer.accept(pos);
                spawns++;
            } else {
                fails++;
            }
        }
    }

    @Nullable
    public BlockPos getRandomValidSpawn() {
        int xOffset = this.random.nextInt(radius * 2) - radius;
        int zOffset = this.random.nextInt(radius * 2) - radius;
        int spawnX = this.start.getX() + xOffset;
        int spawnZ = this.start.getZ() + zOffset;
        int minY = this.start.getY() - minYOffset;
        int maxY = this.start.getY() + maxYOffset;
        BlockPos spawnPos = null;
        for (int y = maxY; y >= minY; y--) {
            BlockPos currentPos = new BlockPos(spawnX, y, spawnZ);
            if (this.isValidSpawn(currentPos)) {
                spawnPos = currentPos;
                break;
            }
        }
        return spawnPos;
    }

    public boolean isValidSpawn(BlockPos pos) {
        return this.world.getBlockState(pos).isAir() && !this.world.getBlockState(pos.down()).isAir();
    }
}
