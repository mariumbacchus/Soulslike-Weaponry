package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.blocks.*;
import net.soulsweaponry.items.abilities.abilitykeybind.IKeybindAbility;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record CorruptGround(
        int range, float bonusRangePerLvl, float effectRange, float effectRangePerLvl, List<StatusEffectInstance> effects
) implements IKeybindAbility {

    private static final Map<Block, WitheredBlock> TURNABLE_BLOCKS = Map.of(
            Blocks.GRASS_BLOCK, BlockRegistry.WITHERED_GRASS_BLOCK.get(),
            Blocks.DIRT, BlockRegistry.WITHERED_DIRT.get()
    );
    private static final Map<Block, WitheredGrass> TURNABLE_GRASS = Map.of(
            Blocks.GRASS, BlockRegistry.WITHERED_GRASS.get(),
            Blocks.FERN, BlockRegistry.WITHERED_FERN.get(),
            Blocks.SWEET_BERRY_BUSH, BlockRegistry.WITHERED_BERRY_BUSH.get()
    );
    private static final Map<Block, WitheredTallGrass> TURNABLE_TALL_PLANT = Map.of(
            Blocks.TALL_GRASS, BlockRegistry.WITHERED_TALL_GRASS.get(),
            Blocks.LARGE_FERN, BlockRegistry.WITHERED_LARGE_FERN.get()
    );

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        NbtHelper.putBoolean(stack, NbtIds.IS_CORRUPT_ACTIVE, !this.isActivated(stack));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && this.isActivated(stack) && !world.isClient) {
            this.turnBlocks(player, world, player.getBlockPos(), stack);
            if (player.age % 40 == 0) {
                for (Entity target : world.getOtherEntities(player,
                        player.getBoundingBox().expand(this.effectRange + this.effectRangePerLvl * WeaponUtil.getUpgradeLevel(stack)))) {
                    if (!(target instanceof PlayerEntity) && target instanceof LivingEntity living && !target.isTeammate(player)) {
                        this.effects.forEach(effect -> living.addStatusEffect(new StatusEffectInstance(effect)));
                    }
                }
            }
        }
    }

    public boolean isActivated(ItemStack stack) {
        return NbtHelper.getBoolean(stack, NbtIds.IS_CORRUPT_ACTIVE, false);
    }

    public void turnBlocks(LivingEntity entity, World world, BlockPos blockPos, ItemStack stack) {
        if (!entity.isOnGround()) {
            return;
        }
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        int range = (int) (this.range + this.bonusRangePerLvl * lvl);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-range, -1, -range), blockPos.add(range, -1, range))) {
            if (!world.getBlockState(blockPos2).isAir()) {
                for (Block turnBlock : TURNABLE_BLOCKS.keySet()) {
                    if (world.getBlockState(blockPos2).getBlock() == turnBlock) {
                        BlockState blockState = TURNABLE_BLOCKS.get(turnBlock).getDefaultState();
                        if (!blockPos2.isWithinDistance(entity.getPos(), range)) continue;
                        mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                        BlockState blockState2 = world.getBlockState(mutable);
                        if (blockState2.isIn(BlockTags.SMALL_FLOWERS)) world.setBlockState(mutable, BlockRegistry.HYDRANGEA.get().getDefaultState().with(WitheredFlower.CANNOT_TURN, false));
                        for (Block turnGrass : TURNABLE_GRASS.keySet()) if (blockState2.isOf(turnGrass)) world.setBlockState(mutable, TURNABLE_GRASS.get(turnGrass).getDefaultState());
                        for (Block turnTallPlant : TURNABLE_TALL_PLANT.keySet()) if (blockState2.isOf(turnTallPlant)) {
                            world.removeBlock(mutable, false);
                            TallPlantBlock.placeAt(world, TURNABLE_TALL_PLANT.get(turnTallPlant).getDefaultState(), mutable, 2);
                        }
                        if (blockState2.isIn(BlockTags.TALL_FLOWERS)) {
                            world.removeBlock(mutable, false);
                            TallPlantBlock.placeAt(world, BlockRegistry.OLEANDER.get().getDefaultState().with(WitheredTallFlower.CANNOT_TURN, false), mutable, 2);
                        }
                        world.setBlockState(blockPos2, blockState);
                        world.scheduleBlockTick(blockPos2, TURNABLE_BLOCKS.get(turnBlock), MathHelper.nextInt(entity.getRandom(), 50, 90));
                    } else if (world.getBlockState(blockPos2).getBlock() == TURNABLE_BLOCKS.get(turnBlock)) {
                        WitheredBlock block = (WitheredBlock) world.getBlockState(blockPos2).getBlock();
                        block.resetAge(world.getBlockState(blockPos2), world, blockPos2);
                    }
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.corrupt_ground").formatted(Formatting.WHITE),
                Text.translatable("tooltip.soulsweapons.corrupt_ground.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.corrupt_ground.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.corrupt_ground.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.corrupt_ground.4").formatted(Formatting.GRAY)
        );
    }
}
