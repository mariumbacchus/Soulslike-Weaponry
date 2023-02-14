package net.soulsweaponry.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.soulsweaponry.entity.mobs.DraugrBoss;
import net.soulsweaponry.entity.mobs.Moonknight;
import net.soulsweaponry.entity.mobs.ReturningKnight;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class AltarBlock extends Block {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public AltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)));
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

        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(ItemRegistry.LOST_SOUL)) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            ReturningKnight boss = new ReturningKnight(EntityRegistry.RETURNING_KNIGHT, world);
            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
            boss.setSpawning(true);
            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            world.spawnEntity(boss);
            world.removeBlock(pos, false);
            return ActionResult.SUCCESS;
        } else if (itemStack.isOf(WeaponRegistry.DRAUGR)) {
            DraugrBoss boss = new DraugrBoss(EntityRegistry.DRAUGR_BOSS, world);
            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
            boss.setSpawning(true);
            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            world.spawnEntity(boss);
            world.removeBlock(pos, false);
            return ActionResult.SUCCESS;
        } else if (itemStack.isOf(ItemRegistry.ESSENCE_OF_EVENTIDE)) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            Moonknight boss = new Moonknight(EntityRegistry.MOONKNIGHT, world);
            boss.setPos(pos.getX(), pos.getY() + .1f, pos.getZ());
            boss.setSpawning(true);
            world.playSound(null, pos, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
            world.spawnEntity(boss);
            world.removeBlock(pos, false);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
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
