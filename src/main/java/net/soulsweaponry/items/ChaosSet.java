package net.soulsweaponry.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.soulsweaponry.blocks.*;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.List;

public class ChaosSet extends GeoArmorItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final HashMap<Block, WitheredBlock> turnableBlocks = new HashMap<>();
    private final HashMap<Block, WitheredGrass> turnableGrass = new HashMap<>();
    private final HashMap<Block, WitheredTallGrass> turnableTallPlant = new HashMap<>();

    public ChaosSet(ArmorMaterial pMaterial, EquipmentSlot pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
        this.turnableBlocks.put(Blocks.GRASS_BLOCK, BlockRegistry.WITHERED_GRASS_BLOCK.get());
        this.turnableBlocks.put(Blocks.DIRT, BlockRegistry.WITHERED_DIRT.get());

        this.turnableGrass.put(Blocks.GRASS, BlockRegistry.WITHERED_GRASS.get());
        this.turnableGrass.put(Blocks.FERN, BlockRegistry.WITHERED_FERN.get());
        this.turnableGrass.put(Blocks.SWEET_BERRY_BUSH, BlockRegistry.WITHERED_BERRY_BUSH.get());

        this.turnableTallPlant.put(Blocks.TALL_GRASS, BlockRegistry.WITHERED_TALL_GRASS.get());
        this.turnableTallPlant.put(Blocks.LARGE_FERN, BlockRegistry.WITHERED_LARGE_FERN.get());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, world, entity, pSlotId, pIsSelected);
        if (entity instanceof Player player) {
            if (this.isHelmetEquipped(player)) {
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 40, 0));
            }
            if (this.isRobesEquipped(player)) {
                this.turnBlocks(player, world, player.blockPosition(), 0);
                if (player.tickCount % 40 == 0) {
                    for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(3D))) {
                        if (!(target instanceof Player)) {
                            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));
                        }
                    }
                }
            }
            if (this.isChestActive(player)) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 1));
            }
        }
    }

    public void turnBlocks(LivingEntity entity, Level world, BlockPos blockPos, int bonusRadius) {
        if (!entity.isOnGround()) {
            return;
        }
        float f = Math.min(16, 3 + bonusRadius);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-f, 0, -f), blockPos.offset(f, -1.0, f))) {
            if (!world.getBlockState(blockPos2).isAir()) {
                for (Block turnBlock : this.turnableBlocks.keySet()) {
                    if (world.getBlockState(blockPos2).getBlock() == turnBlock) {
                        BlockState blockState = this.turnableBlocks.get(turnBlock).defaultBlockState();
                        if (!blockPos2.closerToCenterThan(entity.position(), f)) continue;
                        mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                        BlockState blockState2 = world.getBlockState(mutable);
                        if (blockState2.is(BlockTags.SMALL_FLOWERS)) world.setBlock(mutable, BlockRegistry.HYDRANGEA.get().defaultBlockState().setValue(WitheredFlower.CANNOT_TURN, false), 2);
                        for (Block turnGrass : this.turnableGrass.keySet()) if (blockState2.is(turnGrass)) world.setBlock(mutable, this.turnableGrass.get(turnGrass).defaultBlockState(), 2);
                        for (Block turnTallPlant : this.turnableTallPlant.keySet()) if (blockState2.is(turnTallPlant)) {
                            world.removeBlock(mutable, false);
                            DoublePlantBlock.placeAt(world, this.turnableTallPlant.get(turnTallPlant).defaultBlockState(), mutable, 2);
                        }
                        if (blockState2.is(BlockTags.TALL_FLOWERS)) {
                            world.removeBlock(mutable, false);
                            DoublePlantBlock.placeAt(world, BlockRegistry.OLEANDER.get().defaultBlockState().setValue(WitheredTallFlower.CANNOT_TURN, false), mutable, 2);
                        }
                        world.setBlockAndUpdate(blockPos2, blockState);
                        world.scheduleTick(blockPos2, this.turnableBlocks.get(turnBlock), Mth.nextInt(entity.getRandom(), 50, 90));
                    } else if (world.getBlockState(blockPos2).getBlock() == this.turnableBlocks.get(turnBlock)) {
                        WitheredBlock block = (WitheredBlock) world.getBlockState(blockPos2).getBlock();
                        block.resetAge(world.getBlockState(blockPos2), world, blockPos2);
                    }
                }
            }
        }
    }

    private boolean isHelmetEquipped(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        return !helmet.isEmpty() && (helmet.is(ItemRegistry.CHAOS_CROWN.get()) || helmet.is(ItemRegistry.CHAOS_HELMET.get()));
    }

    private boolean isRobesEquipped(Player player) {
        ItemStack chest = player.getInventory().getArmor(2);
        return !chest.isEmpty() && chest.is(ItemRegistry.CHAOS_ROBES.get());
    }

    private boolean isChestActive(Player player) {
        ItemStack chest = player.getInventory().getArmor(2);
        return !chest.isEmpty() && chest.is(ItemRegistry.ARKENPLATE.get()) && player.getHealth() < player.getMaxHealth()/2;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        //if (this == ItemRegistry.CHAOS_ROBES) {
            //event.getController().setAnimation(new AnimationBuilder().addAnimation("idle")); Doesn't work for some reason.
        //}//TODO implement in other version
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
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            if (stack.is(ItemRegistry.CHAOS_CROWN.get()) || stack.is(ItemRegistry.CHAOS_HELMET.get())) {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_crown").withStyle(ChatFormatting.DARK_RED));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_crown_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_crown_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_crown_description_3").withStyle(ChatFormatting.GRAY));
                if (stack.is(ItemRegistry.CHAOS_CROWN.get())) {
                    if (Screen.hasControlDown()) {
                        for (int i = 1; i <= 4; i++) {
                            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_crown_lore_" + i).withStyle(ChatFormatting.DARK_GRAY));
                        }
                    } else {
                        tooltip.add(new TranslatableComponent("tooltip.soulsweapons.control"));
                    }
                } else {
                    if (Screen.hasControlDown()) {
                        for (int i = 1; i <= 4; i++) {
                            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_helm_lore_" + i).withStyle(ChatFormatting.DARK_GRAY));
                        }
                    } else {
                        tooltip.add(new TranslatableComponent("tooltip.soulsweapons.control"));
                    }
                }
            } else if (stack.is(ItemRegistry.CHAOS_ROBES.get())) {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_robes").withStyle(ChatFormatting.WHITE));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_robes_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_robes_description_2").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_robes_description_3").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_robes_description_4").withStyle(ChatFormatting.GRAY));
                if (Screen.hasControlDown()) {
                    for (int i = 1; i <= 4; i++) {
                        tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_robes_lore_" + i).withStyle(ChatFormatting.DARK_GRAY));
                    }
                } else {
                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.control"));
                }
            } else if (stack.is(ItemRegistry.ARKENPLATE.get())) {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.arkenplate").withStyle(ChatFormatting.AQUA));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.arkenplate_description_1").withStyle(ChatFormatting.GRAY));
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.arkenplate_description_2").withStyle(ChatFormatting.GRAY));
                if (Screen.hasControlDown()) {
                    for (int i = 1; i <= 4; i++) {
                        tooltip.add(new TranslatableComponent("tooltip.soulsweapons.arkenplate_lore_" + i).withStyle(ChatFormatting.DARK_GRAY));
                    }
                } else {
                    tooltip.add(new TranslatableComponent("tooltip.soulsweapons.control"));
                }
            }
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
