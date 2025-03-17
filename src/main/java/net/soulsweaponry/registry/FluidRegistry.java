package net.soulsweaponry.registry;

import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.fluid.PurifiedBlood;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FluidRegistry {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, SoulsWeaponry.ModId);

    public static final RegistryObject<FlowableFluid> STILL_PURIFIED_BLOOD = registerFluid("purified_blood", PurifiedBlood.Still::new);
    public static final RegistryObject<FlowableFluid> FLOWING_PURIFIED_BLOOD = registerFluid("flowing_purified_blood", PurifiedBlood.Flowing::new);

    public static final Predicate<Biome.Precipitation> NONE_PREDICATE = precipitation -> precipitation == Biome.Precipitation.NONE;
    public static final Map<Item, CauldronBehavior> BLOOD_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();

    public static void registerCauldronBehavior() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(ItemRegistry.PURIFIED_BLOOD_BUCKET.get(), (state, world, pos, player, hand, stack) -> CauldronBehavior.fillCauldron(
                world, pos, player, hand, stack, BlockRegistry.PURIFIED_BLOOD_CAULDRON.get().getDefaultState().with(LeveledCauldronBlock.LEVEL, 3), SoundEvents.ITEM_BUCKET_EMPTY
        ));
        BLOOD_CAULDRON_BEHAVIOR.put(
                Items.BUCKET,
                (state, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(
                        state,
                        world,
                        pos,
                        player,
                        hand,
                        stack,
                        new ItemStack(ItemRegistry.PURIFIED_BLOOD_BUCKET.get()),
                        state1 -> state1.get(LeveledCauldronBlock.LEVEL) == 3,
                        SoundEvents.ITEM_BUCKET_FILL
                )
        );
        BLOOD_CAULDRON_BEHAVIOR.put(ItemRegistry.GLASS_VIAL.get(), (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, ItemRegistry.BLOOD_VIAL.get().getDefaultStack()));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ActionResult.success(world.isClient);
        });
        BLOOD_CAULDRON_BEHAVIOR.put(
                Items.GLASS_BOTTLE,
                (state, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(
                        state,
                        world,
                        pos,
                        player,
                        hand,
                        stack,
                        PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.HEALING),
                        state1 -> state1.get(LeveledCauldronBlock.LEVEL) == 3,
                        SoundEvents.ITEM_BUCKET_FILL
                )
        );
    }

    private static <I extends Fluid> RegistryObject<I> registerFluid(String name, Supplier<I> fluid) {
        return FLUIDS.register(name, fluid);
    }

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
