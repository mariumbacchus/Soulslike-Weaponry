package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.fluid.PurifiedBlood;
import net.soulsweaponry.fluid.PurifiedBloodBlock;
import net.soulsweaponry.fluid.PurifiedBloodCauldronBlock;

public class FluidRegistry {

    public static FlowableFluid STILL_PURIFIED_BLOOD;
    public static FlowableFluid FLOWING_PURIFIED_BLOOD;
    public static Block PURIFIED_BLOOD_BLOCK;
    public static Item PURIFIED_BLOOD_BUCKET;

    public static PurifiedBloodCauldronBlock PURIFIED_BLOOD_CAULDRON;
    public static final CauldronBehavior.CauldronBehaviorMap BLOOD_CAULDRON_BEHAVIOR = CauldronBehavior.createMap("purified_blood");

    public static void init() {
        STILL_PURIFIED_BLOOD = registerFluid("purified_blood", new PurifiedBlood.Still());
        FLOWING_PURIFIED_BLOOD = registerFluid("flowing_purified_blood", new PurifiedBlood.Flowing());

        PURIFIED_BLOOD_BLOCK = BlockRegistry.registerBlockAlone(
                "purified_blood_block",
                s -> new PurifiedBloodBlock(STILL_PURIFIED_BLOOD, s),
                AbstractBlock.Settings.copy(Blocks.WATER)
        );

        PURIFIED_BLOOD_BUCKET = ItemRegistry.registerItem(
                "purified_blood_bucket",
                settings -> new BucketItem(
                        STILL_PURIFIED_BLOOD,
                        settings.recipeRemainder(Items.BUCKET).maxCount(1)
                )
        );
    }

    private static RegistryKey<Fluid> fluidKey(String path) {
        return RegistryKey.of(RegistryKeys.FLUID, Identifier.of(SoulsWeaponry.ModId, path));
    }

    public static FlowableFluid registerFluid(String id, FlowableFluid fluid) {
        RegistryKey<Fluid> key = fluidKey(id);
        return Registry.register(Registries.FLUID, key, fluid);
    }

    public static void registerCauldronBehavior() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(PURIFIED_BLOOD_BUCKET, (state, world, pos, player, hand, stack) -> CauldronBehavior.fillCauldron(
                world, pos, player, hand, stack,
                PURIFIED_BLOOD_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3),
                SoundEvents.ITEM_BUCKET_EMPTY
        ));

        BLOOD_CAULDRON_BEHAVIOR.map().put(
                Items.BUCKET,
                (state, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(
                        state,
                        world,
                        pos,
                        player,
                        hand,
                        stack,
                        new ItemStack(PURIFIED_BLOOD_BUCKET),
                        state1 -> state1.get(LeveledCauldronBlock.LEVEL) == 3,
                        SoundEvents.ITEM_BUCKET_FILL
                )
        );

        BLOOD_CAULDRON_BEHAVIOR.map().put(ItemRegistry.GLASS_VIAL, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, ItemRegistry.BLOOD_VIAL.getDefaultStack()));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.SUCCESS;
        });

        BLOOD_CAULDRON_BEHAVIOR.map().put(
                Items.GLASS_BOTTLE,
                (state, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(
                        state,
                        world,
                        pos,
                        player,
                        hand,
                        stack,
                        PotionContentsComponent.createStack(Items.POTION, Potions.HEALING),
                        state1 -> state1.get(LeveledCauldronBlock.LEVEL) == 3,
                        SoundEvents.ITEM_BUCKET_FILL
                )
        );

        PURIFIED_BLOOD_CAULDRON = BlockRegistry.registerBlockAlone(
                "purified_blood_cauldron",
                s -> new PurifiedBloodCauldronBlock(
                        Biome.Precipitation.NONE,
                        BLOOD_CAULDRON_BEHAVIOR,
                        s
                ),
                AbstractBlock.Settings.copy(Blocks.CAULDRON)
        );

        CauldronFluidContent.registerCauldron(
                PURIFIED_BLOOD_CAULDRON,
                STILL_PURIFIED_BLOOD,
                FluidConstants.BOTTLE,
                LeveledCauldronBlock.LEVEL
        );
    }
}