package net.soulsweapons.registry;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweapons.SoulsWeaponry;
import net.soulsweapons.blocks.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SoulsWeaponry.MOD_ID);

    public static final RegistryObject<Block> CRIMSON_OBSIDIAN = registerBlockAndItem("crimson_obsidian", () -> new DrippingBlock(
            BlockBehaviour.Properties.of(Material.STONE).strength(50f, 1200f)
                    .sound(SoundType.STONE).lightLevel(state -> 10).requiresCorrectToolForDrops(), ParticleTypes.DRIPPING_LAVA));
    public static final RegistryObject<Block> INFUSED_BLACKSTONE = registerBlockAndItem("infused_blackstone", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.8F, 7.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> CRACKED_INFUSED_BLACKSTONE = registerBlockAndItem("cracked_infused_blackstone", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.8F, 7.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOONSTONE_ORE = registerBlockAndItem("moonstone_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3.0F, 3.0F).sound(SoundType.STONE).lightLevel(state -> 9).requiresCorrectToolForDrops(), UniformInt.of(4, 8)));
    public static final RegistryObject<Block> MOONSTONE_ORE_DEEPSLATE = registerBlockAndItem("moonstone_ore_deepslate", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).lightLevel(state -> 9).requiresCorrectToolForDrops(), UniformInt.of(4, 8)));
    public static final RegistryObject<Block> MOONSTONE_BLOCK = registerBlockAndItem("moonstone_block", () -> new Block(BlockBehaviour.Properties.of(Material.AMETHYST).strength(5.0F, 6.0F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final RegistryObject<AltarBlock> ALTAR_BLOCK = registerBlockAndItem("altar_block", () -> new AltarBlock(BlockBehaviour.Properties.of(Material.STONE).strength(30F, 800.0F).sound(SoundType.STONE).noOcclusion().requiresCorrectToolForDrops()));
    public static final RegistryObject<WitheredBlock> WITHERED_DIRT = registerBlockAndItem("withered_dirt", () -> new WitheredBlock(BlockBehaviour.Properties.of(Material.DIRT).strength(0.3F).sound(SoundType.GRAVEL), Blocks.DIRT));
    public static final RegistryObject<WitheredBlock> WITHERED_GRASS_BLOCK = registerBlockAndItem("withered_grass_block", () -> new WitheredBlock(BlockBehaviour.Properties.of(Material.GRASS).strength(0.5F).sound(SoundType.GRAVEL), Blocks.GRASS_BLOCK));
    public static final RegistryObject<WitheredGrass> WITHERED_GRASS = registerBlockAndItem("withered_grass", () -> new WitheredGrass(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS), Blocks.GRASS));
    public static final RegistryObject<WitheredGrass> WITHERED_FERN = registerBlockAndItem("withered_fern", () -> new WitheredGrass(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS), Blocks.FERN));
    public static final RegistryObject<WitheredGrass> WITHERED_BERRY_BUSH = registerBlockAndItem("withered_berry_bush", () -> new WitheredGrass(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS), Blocks.SWEET_BERRY_BUSH));
    public static final RegistryObject<WitheredFlower> HYDRANGEA = registerBlockAndItem("hydrangea", () -> new WitheredFlower(EffectRegistry.DECAY, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<WitheredTallGrass> WITHERED_TALL_GRASS = registerBlockAndItem("withered_tall_grass", () -> new WitheredTallGrass(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS), Blocks.TALL_GRASS));
    public static final RegistryObject<WitheredTallGrass> WITHERED_LARGE_FERN = registerBlockAndItem("withered_large_fern", () -> new WitheredTallGrass(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS), Blocks.LARGE_FERN));
    public static final RegistryObject<WitheredTallFlower> OLEANDER = registerBlockAndItem("oleander", () -> new WitheredTallFlower(EffectRegistry.DECAY, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS), Blocks.LARGE_FERN));
    public static final RegistryObject<BlackstonePedestal> BLACKSTONE_PEDESTAL = registerBlockAndItem("blackstone_pedestal", () -> new BlackstonePedestal(BlockBehaviour.Properties.of(Material.STONE).strength(20.0f, 400.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VERGLAS_ORE = registerBlockAndItem("verglas_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3.0F, 3.0F).sound(SoundType.STONE).requiresCorrectToolForDrops(), UniformInt.of(4, 8)));
    public static final RegistryObject<Block> VERGLAS_ORE_DEEPSLATE = registerBlockAndItem("verglas_ore_deepslate", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops(), UniformInt.of(4, 8)));
    public static final RegistryObject<Block> VERGLAS_BLOCK = registerBlockAndItem("verglas_block", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_CYAN).strength(5.0F, 6.0F).sound(SoundType.AMETHYST).noOcclusion().requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SOULFIRE_STAIN = registerBlockAndItem("soulfire_stain", () -> new MagmaBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops().lightLevel(state -> 3).randomTicks().strength(0.5f).isValidSpawn((state, world, pos, entityType) -> entityType.fireImmune()).hasPostProcess((state, world, pos) -> true).emissiveRendering((state, world, pos) -> true)));
    public static final RegistryObject<Block> SOUL_LAMP = registerBlockAndItem("soul_lamp", () -> new SoulLampBlock(BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS).lightLevel(BlockRegistry.createLightLevelFromLitBlockState(15)).strength(0.3f).sound(SoundType.GLASS).isValidSpawn((state, world, pos, type) -> true)));
    public static final RegistryObject<Block> CHUNGUS_MONOLITH = registerBlockAndItem("chungus_monolith", () -> new ChungusMonolith(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_TILES).strength(3f, 3f).sound(SoundType.STONE).noOcclusion().requiresCorrectToolForDrops()));

    private static <I extends Block> RegistryObject<I> registerBlockAndItem(String name, Supplier<I> block) {
        RegistryObject<I> registeredBlock = BLOCKS.register(name, block);
        registerBlockItem(name, registeredBlock);
        return registeredBlock;
    }

    private static <I extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<I> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(SoulsWeaponry.MAIN_GROUP)));
    }

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int pLightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
