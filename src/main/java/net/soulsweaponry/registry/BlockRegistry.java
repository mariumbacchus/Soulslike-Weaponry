package net.soulsweaponry.registry;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.blocks.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SoulsWeaponry.ModId);

    public static final RegistryObject<Block> CRIMSON_OBSIDIAN = registerBlockAndItem("crimson_obsidian", () -> new DrippingBlock(AbstractBlock.Settings
            .copy(Blocks.OBSIDIAN)
            .strength(50f, 1200f)
            .sounds(BlockSoundGroup.STONE)
            .luminance(state -> 10)
            .requiresTool(), ParticleTypes.FALLING_LAVA));
    public static final RegistryObject<Block> INFUSED_BLACKSTONE = registerBlockAndItem("infused_blackstone", () -> new Block(AbstractBlock.Settings.copy(Blocks.BLACKSTONE).strength(1.8F, 7.0F).sounds(BlockSoundGroup.STONE).requiresTool()));
    public static final RegistryObject<Block> CRACKED_INFUSED_BLACKSTONE = registerBlockAndItem("cracked_infused_blackstone", () -> new Block(AbstractBlock.Settings.copy(Blocks.BLACKSTONE).strength(1.8F, 7.0F).sounds(BlockSoundGroup.STONE).requiresTool()));
    public static final RegistryObject<Block> MOONSTONE_ORE = registerBlockAndItem("moonstone_ore", () -> new ExperienceDroppingBlock(AbstractBlock.Settings.copy(Blocks.DIAMOND_ORE).strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE).luminance(state -> 9).requiresTool(), UniformIntProvider.create(4, 8)));
    public static final RegistryObject<Block> MOONSTONE_ORE_DEEPSLATE = registerBlockAndItem("moonstone_ore_deepslate", () -> new ExperienceDroppingBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_DIAMOND_ORE).strength(4.5F, 3.0F).sounds(BlockSoundGroup.DEEPSLATE).luminance(state -> 9).requiresTool(), UniformIntProvider.create(4, 8)));
    public static final RegistryObject<Block> MOONSTONE_BLOCK = registerBlockAndItem("moonstone_block", () -> new Block(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).strength(5.0F, 6.0F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool()));
    public static final RegistryObject<AltarBlock> ALTAR_BLOCK = registerBlockAndItem("altar_block", () -> new AltarBlock(AbstractBlock.Settings.copy(Blocks.OBSIDIAN).strength(30F, 800.0F).sounds(BlockSoundGroup.STONE).nonOpaque().requiresTool()));
    public static final RegistryObject<WitheredBlock> WITHERED_DIRT = registerBlockAndItem("withered_dirt", () -> new WitheredBlock(AbstractBlock.Settings.copy(Blocks.GRAVEL).strength(0.3F).sounds(BlockSoundGroup.GRAVEL), Blocks.DIRT));
    public static final RegistryObject<WitheredBlock> WITHERED_GRASS_BLOCK = registerBlockAndItem("withered_grass_block", () -> new WitheredBlock(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK).strength(0.5F).sounds(BlockSoundGroup.GRAVEL), Blocks.GRASS_BLOCK));
    public static final RegistryObject<WitheredGrass> WITHERED_GRASS = registerBlockAndItem("withered_grass", () -> new WitheredGrass(AbstractBlock.Settings.copy(Blocks.GRASS).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), Blocks.GRASS));
    public static final RegistryObject<WitheredGrass> WITHERED_FERN = registerBlockAndItem("withered_fern", () -> new WitheredGrass(AbstractBlock.Settings.copy(Blocks.GRASS).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), Blocks.FERN));
    public static final RegistryObject<WitheredGrass> WITHERED_BERRY_BUSH = registerBlockAndItem("withered_berry_bush", () -> new WitheredGrass(AbstractBlock.Settings.copy(Blocks.GRASS).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), Blocks.SWEET_BERRY_BUSH));
    public static final RegistryObject<WitheredFlower> HYDRANGEA = registerBlockAndItem("hydrangea", () -> new WitheredFlower(EffectRegistry.DECAY, AbstractBlock.Settings.copy(Blocks.POPPY).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
    public static final RegistryObject<WitheredTallGrass> WITHERED_TALL_GRASS = registerBlockAndItem("withered_tall_grass", () -> new WitheredTallGrass(AbstractBlock.Settings.copy(Blocks.TALL_GRASS).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), Blocks.TALL_GRASS));
    public static final RegistryObject<WitheredTallGrass> WITHERED_LARGE_FERN = registerBlockAndItem("withered_large_fern", () -> new WitheredTallGrass(AbstractBlock.Settings.copy(Blocks.TALL_GRASS).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), Blocks.LARGE_FERN));
    public static final RegistryObject<WitheredTallFlower> OLEANDER = registerBlockAndItem("oleander", () -> new WitheredTallFlower(EffectRegistry.DECAY, AbstractBlock.Settings.copy(Blocks.ROSE_BUSH).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), Blocks.LARGE_FERN));
    public static final RegistryObject<BlackstonePedestal> BLACKSTONE_PEDESTAL = registerBlockAndItem("blackstone_pedestal", () -> new BlackstonePedestal(AbstractBlock.Settings.copy(Blocks.OBSIDIAN).strength(20.0f, 400.0f).sounds(BlockSoundGroup.STONE).requiresTool()));
    public static final RegistryObject<Block> VERGLAS_ORE = registerBlockAndItem("verglas_ore", () -> new ExperienceDroppingBlock(AbstractBlock.Settings.copy(Blocks.EMERALD_ORE).strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE).requiresTool(), UniformIntProvider.create(4, 8)));
    public static final RegistryObject<Block> VERGLAS_ORE_DEEPSLATE = registerBlockAndItem("verglas_ore_deepslate", () -> new ExperienceDroppingBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_EMERALD_ORE).strength(4.5F, 3.0F).sounds(BlockSoundGroup.DEEPSLATE).requiresTool(), UniformIntProvider.create(4, 8)));
    public static final RegistryObject<Block> VERGLAS_BLOCK = registerBlockAndItem("verglas_block", () -> new TransparentBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).mapColor(MapColor.CYAN).strength(5.0F, 6.0F).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque().requiresTool()));
    public static final RegistryObject<Block> SOULFIRE_STAIN = registerBlockAndItem("soulfire_stain", () -> new MagmaBlock(AbstractBlock.Settings.copy(Blocks.MAGMA_BLOCK).mapColor(MapColor.CYAN).requiresTool().luminance(state -> 3).ticksRandomly().strength(0.5f).allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true)));
    public static final RegistryObject<Block> SOUL_LAMP = registerBlockAndItem("soul_lamp", () -> new SoulLampBlock(AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP).luminance(BlockRegistry.createLightLevelFromLitBlockState(15)).strength(0.3f).sounds(BlockSoundGroup.GLASS).allowsSpawning((state, world, pos, type) -> true)));
    public static final RegistryObject<Block> CHUNGUS_MONOLITH = registerBlockAndItem("chungus_monolith", () -> new ChungusMonolith(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES).strength(3f, 3f).sounds(BlockSoundGroup.STONE).nonOpaque().requiresTool()));

    private static <I extends Block> RegistryObject<I> registerBlockAndItem(String name, Supplier<I> block) {
        RegistryObject<I> registeredBlock = BLOCKS.register(name, block);
        registerBlockItem(name, registeredBlock);
        return registeredBlock;
    }

    private static <I extends Block> void registerBlockItem(String name, RegistryObject<I> block) {
        Supplier<Item> item = () -> new BlockItem(block.get(), new Item.Settings());
        RegistryObject<Item> registered = ItemRegistry.registerItem(name, item);
        SoulsWeaponry.ITEM_GROUP_LIST.add(registered);
    }

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int pLightValue) {
        return (state) -> state.get(Properties.LIT) ? pLightValue : 0;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
