package net.soulsweaponry.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.soulsweaponry.blocks.entity.CrimsonObsidianBlockEntity;
import net.soulsweaponry.fluid.PurifiedBloodCauldronBlock;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class CrimsonObsidian extends BlockWithEntity {

    public static final VoxelShape DRIP_COLLISION_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public CrimsonObsidian(Settings settings) {
        super(settings.ticksRandomly());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.scheduleBlockTick(pos, this, 20 + random.nextInt(40));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            this.addParticles(world, random, pos, state, 1);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrimsonObsidianBlockEntity(pos, state);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient) {
            CrimsonObsidianBlockEntity blockEntity = (CrimsonObsidianBlockEntity) world.getBlockEntity(pos);
            if (blockEntity != null && !entity.getType().isIn(ModTags.Entities.SKELETONS) && entity.damage(world.getDamageSources().hotFloor(), 1.0F)) {
                blockEntity.increaseBloodCount();
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient) {
            BlockPos blockPos = getCauldronPos(world, pos, BlockRegistry.PURIFIED_BLOOD_CAULDRON.get());
            if (blockPos != null) {
                CrimsonObsidianBlockEntity blockEntity = (CrimsonObsidianBlockEntity) world.getBlockEntity(pos);
                BlockState blockState = world.getBlockState(blockPos);
                if (blockEntity != null && blockEntity.getBloodCount() >= 5) {
                    if (blockState.isOf(Blocks.CAULDRON)) {
                        world.setBlockState(blockPos, BlockRegistry.PURIFIED_BLOOD_CAULDRON.get().getDefaultState().with(LeveledCauldronBlock.LEVEL, 1));
                        this.addParticles(world, random, pos, state, 8);
                    } else if (blockState.isOf(BlockRegistry.PURIFIED_BLOOD_CAULDRON.get())) {
                        PurifiedBloodCauldronBlock cauldron = (PurifiedBloodCauldronBlock) blockState.getBlock();
                        if (!cauldron.isFull(blockState) && blockEntity.dripBlood()) {
                            cauldron.incrementFluidLevel(world, blockPos, blockState);
                            this.addParticles(world, random, pos, state, 8);
                        }
                    }
                }
            }
            world.scheduleBlockTick(pos, this, 20 + random.nextInt(40));
        }
    }

    private void addParticles(World world, Random random, BlockPos pos, BlockState state, int amount) {
        Direction direction = Direction.DOWN;
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
            for (int i = 0; i < amount; i++) {
                double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double)direction.getOffsetX() * 0.6D;
                double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5D + (double)direction.getOffsetY() * 0.6D;
                double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double)direction.getOffsetZ() * 0.6D;
                if (world.isClient) {
                    world.addParticle(ParticleTypes.FALLING_LAVA, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + f, 0.0D, 0.0D, 0.0D);
                } else {
                    ((ServerWorld)world).spawnParticles(ParticleTypes.FALLING_LAVA, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + f, 1, 0.0D, 0.0D, 0.0D, 0);
                }
            }
        }
    }

    @Nullable
    public static BlockPos getCauldronPos(World world, BlockPos pos, AbstractCauldronBlock acceptableCauldron) {
        Predicate<BlockState> predicate = state -> state.getBlock() instanceof AbstractCauldronBlock cauldron
                && (cauldron.equals(acceptableCauldron) || cauldron.equals(Blocks.CAULDRON));
        BiPredicate<BlockPos, BlockState> biPredicate = (posx, state) -> canDripThrough(world, posx, state);
        return searchInDirection(world, pos, Direction.DOWN.getDirection(), biPredicate, predicate, 11).orElse(null);
    }

    public static boolean canDripThrough(BlockView world, BlockPos pos, BlockState state) {
        if (state.isAir()) {
            return true;
        } else if (state.isOpaqueFullCube(world, pos)) {
            return false;
        } else if (!state.getFluidState().isEmpty()) {
            return false;
        } else {
            VoxelShape voxelShape = state.getCollisionShape(world, pos);
            return !VoxelShapes.matchesAnywhere(DRIP_COLLISION_SHAPE, voxelShape, BooleanBiFunction.AND);
        }
    }

    public static Optional<BlockPos> searchInDirection(
            WorldAccess world,
            BlockPos pos,
            Direction.AxisDirection direction,
            BiPredicate<BlockPos, BlockState> continuePredicate,
            Predicate<BlockState> stopPredicate,
            int range
    ) {
        Direction direction2 = Direction.get(direction, Direction.Axis.Y);
        BlockPos.Mutable mutable = pos.mutableCopy();

        for (int i = 1; i < range; i++) {
            mutable.move(direction2);
            BlockState blockState = world.getBlockState(mutable);
            if (stopPredicate.test(blockState)) {
                return Optional.of(mutable.toImmutable());
            }

            if (world.isOutOfHeightLimit(mutable.getY()) || !continuePredicate.test(mutable, blockState)) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}