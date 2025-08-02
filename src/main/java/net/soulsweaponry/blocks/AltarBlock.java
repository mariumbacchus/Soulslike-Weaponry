package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.DraugrBoss;
import net.soulsweaponry.entity.mobs.Moonknight;
import net.soulsweaponry.entity.mobs.ReturningKnight;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.ModTags;

public class AltarBlock extends SpawnBossBlock {

    public static final DirectionProperty FACING = FacingBlock.FACING;

    public AltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public boolean spawnBoss(World world, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (itemStack.isIn(ModTags.Items.LOST_SOUL)) {
            ReturningKnight entity = new ReturningKnight(EntityRegistry.RETURNING_KNIGHT, world);
            entity.setSpawning(true);
            boolean bl =spawnEntity(world, pos, player, entity, ConfigConstructor.returning_knight_disable_respawn);
            if (bl) {
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
            }
            return bl;
        } else if (itemStack.isOf(WeaponRegistry.DRAUGR)) {
            DraugrBoss entity = new DraugrBoss(EntityRegistry.DRAUGR_BOSS, world);
            entity.setSpawning();
            return spawnEntity(world, pos, player, entity, ConfigConstructor.old_champions_remains_disable_respawn);
        } else if (itemStack.isOf(ItemRegistry.ESSENCE_OF_EVENTIDE)) {
            Moonknight entity = new Moonknight(EntityRegistry.MOONKNIGHT, world);
            entity.setSpawning(true);
            boolean bl = spawnEntity(world, pos, player, entity, ConfigConstructor.fallen_icon_disable_respawn);
            if (bl) {
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
            }
            return bl;
        }
        return false;
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world.isClient && random.nextInt(5) < 2) {
            double e = .5f;
            double d = .375f;
            double f = .375f;
            world.addParticle(ParticleTypes.FLAME, (double)pos.getX() + d + e, (double)pos.getY() + e + d*1.8f, (double)pos.getZ() + f + e, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, (double)pos.getX() - d + e, (double)pos.getY() + e + d*1.8f, (double)pos.getZ() - f + e, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, (double)pos.getX() + d + e, (double)pos.getY() + e + d*1.8f, (double)pos.getZ() - f + e, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, (double)pos.getX() - d + e, (double)pos.getY() + e + d*1.8f, (double)pos.getZ() + f + e, 0.0D, 0.0D, 0.0D);

            particleCircle(world, pos, 3, 4f);
            particlePentagram(world, pos, this.getParticleType(), 4f);
        }
    }

    private void particleCircle(World world, BlockPos pos, int spaceBetweenEach, float radius) {
        for (int theta = 0; theta < 360; theta += spaceBetweenEach) {
            float x0 = pos.getX() + .5f;
            float z0 = pos.getZ() + .5f;
            double x = x0 + radius * Math.cos(theta * Math.PI / 180);
            double z = z0 + radius * Math.sin(theta * Math.PI / 180);
            world.addParticle(this.getParticleType(), x, pos.getY() + 0.2f, z, 0, 0, 0);
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

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
