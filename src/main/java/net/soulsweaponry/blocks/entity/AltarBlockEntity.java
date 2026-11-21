package net.soulsweaponry.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.blocks.AltarBlock;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.SoundRegistry;

public class AltarBlockEntity extends BlockEntity {

    private static final double[][] CANDLE_OFFSETS = {
            {0.875D, 0.875D},
            {0.125D, 0.875D},
            {0.125D, 0.125D},
            {0.875D, 0.125D}
    };
    private int soundCooldown;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.MOONSTONE_ALTAR_BLOCK_ENTITY, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, AltarBlockEntity entity) {
        int charges = state.get(AltarBlock.CHARGES);
        if (charges == 0) {
            return;
        }
        if (entity.soundCooldown-- <= 0) {
            world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundRegistry.DEATH_SCREAMS_EVENT,
                    SoundCategory.BLOCKS, 1F, 1F, false);
            entity.soundCooldown = world.getRandom().nextBetween(100, 300);
        }
        if (world.getTime() % 4 != 0) {
            return;
        }
        for (int i = 0; i < charges; i++) {
            double[] offset = CANDLE_OFFSETS[i];
            world.addParticle(ParticleTypes.FLAME, pos.getX() + offset[0], pos.getY() + 1.175D, pos.getZ() + offset[1], 0D, 0D, 0D);
        }
        entity.particleCircle(world, pos, 3, 4F);
        entity.particlePentagram(world, pos, entity.getParticleType(), 4F);
    }

    private void particleCircle(World world, BlockPos pos, int spaceBetweenEach, float radius) {
        for (int theta = 0; theta < 360; theta += spaceBetweenEach) {
            float x0 = pos.getX() + .5f;
            float z0 = pos.getZ() + .5f;
            double x = x0 + radius * Math.cos(theta * Math.PI / 180);
            double z = z0 + radius * Math.sin(theta * Math.PI / 180);
            world.addParticle(getParticleType(), x, pos.getY() + 0.2f, z, 0, 0, 0);
        }
    }

    private void particlePentagram(World world, BlockPos pos, ParticleEffect particle, float radius) {
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.2;
        double centerZ = pos.getZ() + 0.5;

        double[][] points = new double[5][2];
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(72 * i - 90); // Pentagram points: 360 deg divided into 5, starting at -90 deg for proper rotation
            points[i][0] = centerX + radius * Math.cos(angle);
            points[i][1] = centerZ + radius * Math.sin(angle);
        }

        // Draw lines between points in a pentagram pattern
        int[] order = {0, 2, 4, 1, 3, 0}; // Connection order for a pentagram
        for (int i = 0; i < order.length - 1; i++) {
            double startX = points[order[i]][0];
            double startZ = points[order[i]][1];
            double endX = points[order[i + 1]][0];
            double endZ = points[order[i + 1]][1];

            drawParticleLine(world, particle, startX, centerY, startZ, endX, endZ, 20); // 20 particles per line
        }
    }

    private void drawParticleLine(World world, ParticleEffect particle, double x1, double y, double z1, double x2, double z2, int steps) {
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double x = x1 + t * (x2 - x1);
            double z = z1 + t * (z2 - z1);
            world.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    private ParticleEffect getParticleType() {
        return ParticleRegistry.NIGHTFALL_PARTICLE;
    }
}