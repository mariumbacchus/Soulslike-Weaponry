package net.soulsweaponry.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.soulsweaponry.blocks.entity.AltarBlockEntity;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.config.ReturningKnightConfig;
import net.soulsweaponry.entity.mobs.boss.DraugrBoss;
import net.soulsweaponry.entity.mobs.boss.Moonknight;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ModTags;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends SpawnBossBlock implements BlockEntityProvider {

    public static final DirectionProperty FACING = FacingBlock.FACING;
    public static final IntProperty CHARGES = IntProperty.of("charges", 0, 3);

    public AltarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(CHARGES, 0));
    }

    @Override
    public boolean spawnBoss(World world, BlockPos pos, PlayerEntity player, ItemStack itemStack) {
        if (itemStack.isIn(ModTags.Items.LOST_SOUL)) {
            if (ReturningKnightConfig.disable_respawn) {
                if (!world.isClient) {
                    this.notifyDisabledBossRespawning(player);
                }
                return false;
            }
            if (world.isClient) {
                return true;
            }
            BlockState state = world.getBlockState(pos);
            int charges = state.get(CHARGES);
            if (charges < 3) {
                world.setBlockState(pos, state.with(CHARGES, charges + 1), Block.NOTIFY_ALL);
                if (!player.getAbilities().creativeMode && ReturningKnightConfig.consume_item_on_summoning) {
                    itemStack.decrement(1);
                }
                world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1f, 1f);
                return true;
            }
            // Three offerings stored, fourth offering summons the boss
            ReturningKnight entity = new ReturningKnight(EntityRegistry.RETURNING_KNIGHT, world);
            entity.setSpawning();
            return this.spawnEntity(world, pos, player, entity, ReturningKnightConfig.disable_respawn, itemStack, ReturningKnightConfig.consume_item_on_summoning);
        } else if (itemStack.isOf(WeaponRegistry.DRAUGR)) {
            DraugrBoss entity = new DraugrBoss(EntityRegistry.DRAUGR_BOSS, world);
            entity.setSpawning();
            return spawnEntity(world, pos, player, entity, EntityConfig.old_champions_remains_disable_respawn, itemStack, EntityConfig.old_champions_remains_consume_item_on_summoning);
        } else if (itemStack.isOf(ItemRegistry.ESSENCE_OF_EVENTIDE)) {
            Moonknight entity = new Moonknight(EntityRegistry.MOONKNIGHT, world);
            entity.setSpawning();
            return spawnEntity(world, pos, player, entity, EntityConfig.fallen_icon_disable_respawn, itemStack, EntityConfig.fallen_icon_consume_item_on_summoning);
        }
        return false;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient || type != BlockRegistry.MOONSTONE_ALTAR_BLOCK_ENTITY) {
            return null;
        }
        return (tickWorld, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof AltarBlockEntity altar) {
                AltarBlockEntity.clientTick(tickWorld, pos, tickState, altar);
            }
        };
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
        builder.add(FACING, CHARGES);
    }
}
