package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.blocks.*;
import net.soulsweaponry.blocks.entity.CrimsonObsidianBlockEntity;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public class BlockRegistry {

    public static Block CRIMSON_OBSIDIAN;
    public static Block INFUSED_BLACKSTONE;
    public static Block CRACKED_INFUSED_BLACKSTONE;
    public static Block MOONSTONE_ORE;
    public static Block MOONSTONE_ORE_DEEPSLATE;
    public static Block MOONSTONE_BLOCK;
    public static AltarBlock ALTAR_BLOCK;
    public static WitheredBlock WITHERED_DIRT;
    public static WitheredBlock WITHERED_GRASS_BLOCK;
    public static WitheredGrass WITHERED_GRASS;
    public static WitheredGrass WITHERED_FERN;
    public static WitheredGrass WITHERED_BERRY_BUSH;
    public static WitheredFlower HYDRANGEA;
    public static WitheredTallGrass WITHERED_TALL_GRASS;
    public static WitheredTallGrass WITHERED_LARGE_FERN;
    public static WitheredTallFlower OLEANDER;
    public static BlackstonePedestal BLACKSTONE_PEDESTAL;
    public static Block VERGLAS_ORE;
    public static Block VERGLAS_ORE_DEEPSLATE;
    public static Block VERGLAS_BLOCK;
    public static Block SOULFIRE_STAIN;
    public static Block SOUL_LAMP;
    public static Block CHUNGUS_MONOLITH;
    public static Block CHUNGUS_EMERALD_BLOCK;

    public static BlockEntityType<CrimsonObsidianBlockEntity> CRIMSON_OBSIDIAN_BLOCK_ENTITY;

    public static void init() {
        CRIMSON_OBSIDIAN = register(
                "crimson_obsidian",
                CrimsonObsidian::new,
                AbstractBlock.Settings.copy(Blocks.OBSIDIAN)
                        .strength(50.0F, 1200.0F) //hardness and resistance, check wiki
                        .sounds(BlockSoundGroup.STONE)
                        .luminance(state -> 10)
                        .requiresTool().nonOpaque()
        );

        INFUSED_BLACKSTONE = register(
                "infused_blackstone",
                Block::new,
                AbstractBlock.Settings.copy(Blocks.BLACKSTONE)
                        .strength(1.8F, 7.0F)
                        .sounds(BlockSoundGroup.STONE)
                        .requiresTool()
        );

        CRACKED_INFUSED_BLACKSTONE = register(
                "cracked_infused_blackstone",
                Block::new,
                AbstractBlock.Settings.copy(Blocks.BLACKSTONE)
                        .strength(1.8F, 7.0F)
                        .sounds(BlockSoundGroup.STONE)
                        .requiresTool()
        );

        MOONSTONE_ORE = register(
                "moonstone_ore",
                s -> new ExperienceDroppingBlock(UniformIntProvider.create(4, 8), s),
                AbstractBlock.Settings.copy(Blocks.DIAMOND_ORE)
                        .strength(3.0F, 3.0F)
                        .sounds(BlockSoundGroup.STONE)
                        .luminance(state -> 9)
                        .requiresTool()
        );

        MOONSTONE_ORE_DEEPSLATE = register(
                "moonstone_ore_deepslate",
                s -> new ExperienceDroppingBlock(UniformIntProvider.create(4, 8), s),
                AbstractBlock.Settings.copy(Blocks.DEEPSLATE_DIAMOND_ORE)
                        .strength(4.5F, 3.0F)
                        .sounds(BlockSoundGroup.DEEPSLATE)
                        .luminance(state -> 9)
                        .requiresTool()
        );

        MOONSTONE_BLOCK = register(
                "moonstone_block",
                Block::new,
                AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK)
                        .strength(5.0F, 6.0F)
                        .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                        .requiresTool()
        );

        ALTAR_BLOCK = register(
                "altar_block",
                AltarBlock::new,
                AbstractBlock.Settings.copy(Blocks.OBSIDIAN)
                        .strength(30F, 800.0F)
                        .sounds(BlockSoundGroup.STONE)
                        .nonOpaque()
                        .requiresTool()
        );

        WITHERED_DIRT = register(
                "withered_dirt",
                s -> new WitheredBlock(s, Blocks.DIRT),
                AbstractBlock.Settings.copy(Blocks.GRAVEL)
                        .strength(0.3F)
                        .sounds(BlockSoundGroup.GRAVEL)
        );

        WITHERED_GRASS_BLOCK = register(
                "withered_grass_block",
                s -> new WitheredBlock(s, Blocks.GRASS_BLOCK),
                AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK)
                        .strength(0.5F)
                        .sounds(BlockSoundGroup.GRAVEL)
        );

        WITHERED_GRASS = register(
                "withered_grass",
                s -> new WitheredGrass(s, Blocks.SHORT_GRASS),
                AbstractBlock.Settings.copy(Blocks.SHORT_GRASS)
                        .noCollision()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.GRASS)
                        .offset(AbstractBlock.OffsetType.XYZ)
        );

        WITHERED_BERRY_BUSH = register(
                "withered_berry_bush",
                s -> new WitheredGrass(s, Blocks.SWEET_BERRY_BUSH),
                AbstractBlock.Settings.copy(Blocks.SHORT_GRASS)
                        .noCollision()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.GRASS)
        );

        WITHERED_FERN = register(
                "withered_fern",
                s -> new WitheredGrass(s, Blocks.FERN),
                AbstractBlock.Settings.copy(Blocks.SHORT_GRASS)
                        .noCollision()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.GRASS)
                        .offset(AbstractBlock.OffsetType.XYZ)
        );

        HYDRANGEA = register(
                "hydrangea",
                s -> new WitheredFlower(EffectRegistry.DECAY, s),
                AbstractBlock.Settings.copy(Blocks.POPPY)
                        .noCollision()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.GRASS)
        );

        WITHERED_TALL_GRASS = register(
                "withered_tall_grass",
                s -> new WitheredTallGrass(s, Blocks.TALL_GRASS),
                AbstractBlock.Settings.copy(Blocks.TALL_GRASS)
                        .noCollision()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.GRASS)
                        .offset(AbstractBlock.OffsetType.XZ)
        );

        WITHERED_LARGE_FERN = register(
                "withered_large_fern",
                s -> new WitheredTallGrass(s, Blocks.LARGE_FERN),
                AbstractBlock.Settings.copy(Blocks.TALL_GRASS)
                        .noCollision()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.GRASS)
                        .offset(AbstractBlock.OffsetType.XZ)
        );

        OLEANDER = register(
                "oleander",
                s -> new WitheredTallFlower(s, Blocks.LARGE_FERN, EffectRegistry.DECAY),
                AbstractBlock.Settings.copy(Blocks.ROSE_BUSH)
                        .noCollision()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.GRASS)
                        .offset(AbstractBlock.OffsetType.XZ)
        );

        BLACKSTONE_PEDESTAL = register(
                "blackstone_pedestal",
                BlackstonePedestal::new,
                AbstractBlock.Settings.copy(Blocks.OBSIDIAN)
                        .strength(20.0f, 400.0f)
                        .sounds(BlockSoundGroup.STONE)
                        .requiresTool()
        );

        VERGLAS_ORE = register(
                "verglas_ore",
                s -> new ExperienceDroppingBlock(UniformIntProvider.create(4, 8), s),
                AbstractBlock.Settings.copy(Blocks.EMERALD_ORE)
                        .strength(3.0F, 3.0F)
                        .sounds(BlockSoundGroup.STONE)
                        .requiresTool()
        );

        VERGLAS_ORE_DEEPSLATE = register(
                "verglas_ore_deepslate",
                s -> new ExperienceDroppingBlock(UniformIntProvider.create(4, 8), s),
                AbstractBlock.Settings.copy(Blocks.DEEPSLATE_EMERALD_ORE)
                        .strength(4.5F, 3.0F)
                        .sounds(BlockSoundGroup.DEEPSLATE)
                        .requiresTool()
        );

        VERGLAS_BLOCK = register(
                "verglas_block",
                TransparentBlock::new,
                AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK)
                        .mapColor(MapColor.DARK_AQUA)
                        .strength(5.0F, 6.0F)
                        .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                        .nonOpaque()
                        .requiresTool()
        );

        SOULFIRE_STAIN = register(
                "soulfire_stain",
                MagmaBlock::new,
                AbstractBlock.Settings.copy(Blocks.MAGMA_BLOCK)
                        .mapColor(MapColor.DARK_AQUA)
                        .requiresTool()
                        .luminance(state -> 3)
                        .ticksRandomly()
                        .strength(0.5f)
                        .allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune())
                        .postProcess((state, world, pos) -> true)
                        .emissiveLighting((state, world, pos) -> true)
        );

        SOUL_LAMP = register(
                "soul_lamp",
                SoulLampBlock::new,
                AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP)
                        .luminance(createLightLevelFromLitBlockState(15))
                        .strength(0.3f)
                        .sounds(BlockSoundGroup.GLASS)
                        .allowsSpawning((state, world, pos, type) -> true)
        );

        CHUNGUS_MONOLITH = register(
                "chungus_monolith",
                ChungusMonolith::new,
                AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES)
                        .strength(3f, 3f)
                        .sounds(BlockSoundGroup.STONE)
                        .nonOpaque()
                        .requiresTool()
        );

        CHUNGUS_EMERALD_BLOCK = register(
                "chungus_emerald_block",
                Block::new,
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.EMERALD_GREEN)
                        .instrument(NoteBlockInstrument.BIT)
                        .requiresTool()
                        .strength(5.0F, 6.0F)
                        .sounds(BlockSoundGroup.METAL)
        );

        CRIMSON_OBSIDIAN_BLOCK_ENTITY = registerBlockEntityType(
                "crimson_obsidian_block_entity",
                CrimsonObsidianBlockEntity::new,
                BlockRegistry.CRIMSON_OBSIDIAN
        );
    }

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }

    private static RegistryKey<Block> blockKey(String path) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(SoulsWeaponry.ModId, path));
    }

    private static RegistryKey<Item> itemKey(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(SoulsWeaponry.ModId, path));
    }

    private static <B extends Block> B register(String name, Function<AbstractBlock.Settings, B> factory, AbstractBlock.Settings settings) {
        RegistryKey<Block> bKey = blockKey(name);
        RegistryKey<Item> iKey = itemKey(name);
        B block = factory.apply(settings.registryKey(bKey));
        Registry.register(Registries.BLOCK, bKey, block);

        Item item = new BlockItem(block, new Item.Settings()
                .registryKey(iKey)
                .useBlockPrefixedTranslationKey()
        );
        SoulsWeaponry.ITEM_GROUP_LIST.add(item);
        Registry.register(Registries.ITEM, iKey, item);
        return block;
    }

    public static <B extends Block> B registerBlockAlone(
            String name,
            Function<AbstractBlock.Settings, B> factory,
            AbstractBlock.Settings settings
    ) {
        RegistryKey<Block> key = blockKey(name);
        B block = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, key, block);
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(
            String name,
            FabricBlockEntityTypeBuilder.Factory<T> factory,
            Block... blocks
    ) {
        RegistryKey<BlockEntityType<?>> key = RegistryKey.of(
                RegistryKeys.BLOCK_ENTITY_TYPE,
                Identifier.of(SoulsWeaponry.ModId, name)
        );

        return Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                key,
                FabricBlockEntityTypeBuilder.create(factory, blocks).build()
        );
    }
}