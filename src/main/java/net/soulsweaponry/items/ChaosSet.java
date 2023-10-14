package net.soulsweaponry.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.blocks.WitheredBlock;
import net.soulsweaponry.blocks.WitheredFlower;
import net.soulsweaponry.blocks.WitheredGrass;
import net.soulsweaponry.blocks.WitheredTallFlower;
import net.soulsweaponry.blocks.WitheredTallGrass;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ChaosSet extends ArmorItem implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final HashMap<Block, WitheredBlock> turnableBlocks = new HashMap<>();
    private final HashMap<Block, WitheredGrass> turnableGrass = new HashMap<>();
    private final HashMap<Block, WitheredTallGrass> turnableTallPlant = new HashMap<>();
    
    public ChaosSet(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
        this.turnableBlocks.put(Blocks.GRASS_BLOCK, BlockRegistry.WITHERED_GRASS_BLOCK);
        this.turnableBlocks.put(Blocks.DIRT, BlockRegistry.WITHERED_DIRT);

        this.turnableGrass.put(Blocks.GRASS, BlockRegistry.WITHERED_GRASS);
        this.turnableGrass.put(Blocks.FERN, BlockRegistry.WITHERED_FERN);
        this.turnableGrass.put(Blocks.SWEET_BERRY_BUSH, BlockRegistry.WITHERED_BERRY_BUSH);

        this.turnableTallPlant.put(Blocks.TALL_GRASS, BlockRegistry.WITHERED_TALL_GRASS);
        this.turnableTallPlant.put(Blocks.LARGE_FERN, BlockRegistry.WITHERED_LARGE_FERN);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof PlayerEntity player) {
            if (this.isHelmetEquipped(player)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 40, 0));
            }
            if (this.isRobesEquipped(player)) {
                this.turnBlocks(player, world, player.getBlockPos(), 0);
                if (player.age % 40 == 0) {
                    for (LivingEntity target : world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(3D))) {
                        if (!(target instanceof PlayerEntity) && target != player) {
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1));
                        }
                    }
                }
            }
            if (this.isChestActive(player)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 1));
            }
        }
    }

    public void turnBlocks(LivingEntity entity, World world, BlockPos blockPos, int bonusRadius) {
        if (!entity.isOnGround()) {
            return;
        }
        float f = Math.min(16, 3 + bonusRadius);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-f, -1.0, -f), blockPos.add(f, -1.0, f))) {
            if (!world.getBlockState(blockPos2).isAir()) {
                for (Block turnBlock : this.turnableBlocks.keySet()) {
                    if (world.getBlockState(blockPos2).getBlock() == turnBlock) {
                        BlockState blockState = this.turnableBlocks.get(turnBlock).getDefaultState();
                        if (!blockPos2.isWithinDistance(entity.getPos(), (double)f)) continue;
                        mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                        BlockState blockState2 = world.getBlockState(mutable);
                        if (blockState2.isIn(BlockTags.SMALL_FLOWERS)) world.setBlockState(mutable, BlockRegistry.HYDRANGEA.getDefaultState().with(WitheredFlower.CANNOT_TURN, false));
                        for (Block turnGrass : this.turnableGrass.keySet()) if (blockState2.isOf(turnGrass)) world.setBlockState(mutable, this.turnableGrass.get(turnGrass).getDefaultState());
                        for (Block turnTallPlant : this.turnableTallPlant.keySet()) if (blockState2.isOf(turnTallPlant)) {
                            world.removeBlock(mutable, false);
                            //this.turnableTallPlant.get(turnTallPlant).placeThis(world, blockState2, mutable, 2, true);
                            TallPlantBlock.placeAt(world, this.turnableTallPlant.get(turnTallPlant).getDefaultState(), mutable, 2);
                        }
                        if (blockState2.isIn(BlockTags.TALL_FLOWERS)) {
                            world.removeBlock(mutable, false);
                            TallPlantBlock.placeAt(world, BlockRegistry.OLEANDER.getDefaultState().with(WitheredTallFlower.CANNOT_TURN, false), mutable, 2);
                        }
                        /* if (blockState2.isOf(Blocks.GRASS)) world.setBlockState(mutable, BlockRegistry.WITHERED_GRASS.getDefaultState());
                        if (blockState2.isOf(Blocks.TALL_GRASS)) {
                            world.removeBlock(mutable, false);
                            BlockRegistry.WITHERED_TALL_GRASS.placeThis(world, blockState2, mutable, 2);
                        }  */
                        //if (!blockState2.isAir()/*  || blockState2.getBlock() == turnBlock */ || !blockState.canPlaceAt(world, blockPos2) || !world.canPlace(blockState, blockPos2, ShapeContext.absent())) continue;
                        world.setBlockState(blockPos2, blockState);
                        world.createAndScheduleBlockTick(blockPos2, this.turnableBlocks.get(turnBlock), MathHelper.nextInt(entity.getRandom(), 50, 90));
                    } else if (world.getBlockState(blockPos2).getBlock() == this.turnableBlocks.get(turnBlock)) {
                        WitheredBlock block = (WitheredBlock) world.getBlockState(blockPos2).getBlock();
                        block.resetAge(world.getBlockState(blockPos2), world, blockPos2);
                    }
                }
            }
        }
    }

    private boolean isHelmetEquipped(PlayerEntity player) {
        ItemStack helmet = player.getInventory().getArmorStack(3);
        return !helmet.isEmpty() && (helmet.isOf(ItemRegistry.CHAOS_CROWN) || helmet.isOf(ItemRegistry.CHAOS_HELMET));
    }

    private boolean isRobesEquipped(PlayerEntity player) {
        ItemStack chest = player.getInventory().getArmorStack(2);
        return !chest.isEmpty() && chest.isOf(ItemRegistry.CHAOS_ROBES);
    }

    private boolean isChestActive(PlayerEntity player) {
        ItemStack chest = player.getInventory().getArmorStack(2);
        return !chest.isEmpty() && chest.isOf(ItemRegistry.ARKENPLATE) && player.getHealth() < player.getMaxHealth()/2;
    }

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this == ItemRegistry.CHAOS_ROBES) {
            //event.getController().setAnimation(new AnimationBuilder().addAnimation("idle")); Doesn't work for some reason.
        }
		return PlayState.CONTINUE;
	}

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));    
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            if (stack.isOf(ItemRegistry.CHAOS_CROWN) || stack.isOf(ItemRegistry.CHAOS_HELMET)) {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_crown").formatted(Formatting.DARK_RED));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_crown_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_crown_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_crown_description_3").formatted(Formatting.GRAY));
                for (int i = 1; i <= 3; i++) {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_crown_lore_" + i).formatted(Formatting.DARK_GRAY));
                }
            } else if (stack.isOf(ItemRegistry.CHAOS_ROBES)) {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_robes").formatted(Formatting.WHITE));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_robes_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_robes_description_2").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_robes_description_3").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.chaos_robes_description_4").formatted(Formatting.GRAY));
            } else if (stack.isOf(ItemRegistry.ARKENPLATE)) {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.arkenplate").formatted(Formatting.AQUA));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.arkenplate_description_1").formatted(Formatting.GRAY));
                tooltip.add(new TranslatableText("tooltip.soulsweapons.arkenplate_description_2").formatted(Formatting.GRAY));
                if (Screen.hasControlDown()) {
                    for (int i = 1; i <= 8; i++) {
                        tooltip.add(new TranslatableText("tooltip.soulsweapons.arkenplate_lore_" + i).formatted(Formatting.DARK_GRAY));
                    }
                } else {
                    tooltip.add(new TranslatableText("tooltip.soulsweapons.control"));
                }
            }
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        
        super.appendTooltip(stack, world, tooltip, context);
    }
}
