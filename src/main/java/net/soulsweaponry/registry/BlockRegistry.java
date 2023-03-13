package net.soulsweaponry.registry;

import java.util.function.ToIntFunction;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.blocks.AltarBlock;
import net.soulsweaponry.blocks.BlackstonePedestal;
import net.soulsweaponry.blocks.DrippingBlock;
import net.soulsweaponry.blocks.SoulLampBlock;
import net.soulsweaponry.blocks.WitheredBlock;
import net.soulsweaponry.blocks.WitheredFlower;
import net.soulsweaponry.blocks.WitheredGrass;
import net.soulsweaponry.blocks.WitheredTallFlower;
import net.soulsweaponry.blocks.WitheredTallGrass;

public class BlockRegistry {
    
    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;

    public static final Block CRIMSON_OBSIDIAN = new DrippingBlock(FabricBlockSettings
        .of(Material.STONE) //break lyd
        .strength(50.0F, 1200.0F) //hardness og resistence, sjekk wiki
        .sounds(BlockSoundGroup.STONE) //lyd når du går på
        .luminance(10) //lysning
        .requiresTool(), ParticleTypes.FALLING_LAVA);
    public static final Block INFUSED_BLACKSTONE = new Block(FabricBlockSettings.of(Material.STONE).strength(1.8F, 7.0F).sounds(BlockSoundGroup.STONE).requiresTool());
    public static final Block CRACKED_INFUSED_BLACKSTONE = new Block(FabricBlockSettings.of(Material.STONE).strength(1.8F, 7.0F) .sounds(BlockSoundGroup.STONE).requiresTool());
    public static final Block MOONSTONE_ORE = new ExperienceDroppingBlock(FabricBlockSettings.of(Material.STONE).strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE).luminance(9).requiresTool(), UniformIntProvider.create(4, 8));
    public static final Block MOONSTONE_ORE_DEEPSLATE = new ExperienceDroppingBlock(FabricBlockSettings.of(Material.STONE).strength(4.5F, 3.0F).sounds(BlockSoundGroup.STONE).luminance(9).requiresTool(), UniformIntProvider.create(4, 8));
    public static final Block MOONSTONE_BLOCK = new Block(FabricBlockSettings.of(Material.AMETHYST).strength(5.0F, 6.0F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool());
    public static final AltarBlock ALTAR_BLOCK = new AltarBlock(FabricBlockSettings.of(Material.STONE).strength(30F, 800.0F).sounds(BlockSoundGroup.STONE).nonOpaque().requiresTool());
    public static final WitheredBlock WITHERED_DIRT = new WitheredBlock(FabricBlockSettings.of(Material.SOIL).strength(0.3F).sounds(BlockSoundGroup.GRAVEL), Blocks.DIRT);
    public static final WitheredBlock WITHERED_GRASS_BLOCK = new WitheredBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC).strength(0.5F).sounds(BlockSoundGroup.GRAVEL), Blocks.GRASS_BLOCK);
    public static final WitheredGrass WITHERED_GRASS = new WitheredGrass(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).offsetType(AbstractBlock.OffsetType.XYZ), Blocks.GRASS);
    public static final WitheredGrass WITHERED_FERN = new WitheredGrass(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).offsetType(AbstractBlock.OffsetType.XYZ), Blocks.FERN);
    public static final WitheredGrass WITHERED_BERRY_BUSH = new WitheredGrass(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), Blocks.SWEET_BERRY_BUSH);
    public static final WitheredFlower HYDRANGEA = new WitheredFlower(EffectRegistry.DECAY, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS), Blocks.WITHER_ROSE);
    public static final WitheredTallGrass WITHERED_TALL_GRASS = new WitheredTallGrass(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).offsetType(AbstractBlock.OffsetType.XZ), Blocks.TALL_GRASS);
    public static final WitheredTallGrass WITHERED_LARGE_FERN = new WitheredTallGrass(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).offsetType(AbstractBlock.OffsetType.XZ), Blocks.LARGE_FERN);
    public static final WitheredTallFlower OLEANDER = new WitheredTallFlower(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).offsetType(AbstractBlock.OffsetType.XZ), Blocks.LARGE_FERN, EffectRegistry.DECAY);
    public static final BlackstonePedestal BLACKSTONE_PEDESTAL = new BlackstonePedestal(FabricBlockSettings.of(Material.STONE).strength(20.0f, 400.0f).sounds(BlockSoundGroup.STONE).requiresTool());
    public static final Block VERGLAS_ORE = new ExperienceDroppingBlock(FabricBlockSettings.of(Material.STONE).strength(3.0F, 3.0F).sounds(BlockSoundGroup.STONE).requiresTool(), UniformIntProvider.create(4, 8));
    public static final Block VERGLAS_ORE_DEEPSLATE = new ExperienceDroppingBlock(FabricBlockSettings.of(Material.STONE).strength(4.5F, 3.0F).sounds(BlockSoundGroup.STONE).requiresTool(), UniformIntProvider.create(4, 8));
    public static final Block VERGLAS_BLOCK = new TransparentBlock(FabricBlockSettings.of(Material.AMETHYST, MapColor.DARK_AQUA).strength(5.0F, 6.0F).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque().requiresTool());
    public static final Block SOULFIRE_STAIN = new MagmaBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.DARK_AQUA).requiresTool().luminance(state -> 3).ticksRandomly().strength(0.5f).allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune()).postProcess((state, world, pos) -> true).emissiveLighting((state, world, pos) -> true));
    public static final Block SOUL_LAMP = new SoulLampBlock(AbstractBlock.Settings.of(Material.REDSTONE_LAMP).luminance(BlockRegistry.createLightLevelFromLitBlockState(15)).strength(0.3f).sounds(BlockSoundGroup.GLASS).allowsSpawning((state, world, pos, type) -> true));

    public static void init() {
        registerBlock(CRIMSON_OBSIDIAN, "crimson_obsidian");
        registerBlock(INFUSED_BLACKSTONE, "infused_blackstone");
        registerBlock(CRACKED_INFUSED_BLACKSTONE, "cracked_infused_blackstone");
        registerBlock(MOONSTONE_ORE, "moonstone_ore");
        registerBlock(MOONSTONE_ORE_DEEPSLATE, "moonstone_ore_deepslate");
        registerBlock(MOONSTONE_BLOCK, "moonstone_block");
        registerBlock(ALTAR_BLOCK, "altar_block");
        registerBlock(WITHERED_DIRT, "withered_dirt");
        registerBlock(WITHERED_GRASS_BLOCK, "withered_grass_block");
        registerBlock(WITHERED_GRASS, "withered_grass");
        registerBlock(WITHERED_BERRY_BUSH, "withered_berry_bush");
        registerBlock(WITHERED_FERN, "withered_fern");
        registerBlock(HYDRANGEA, "hydrangea");
        registerBlock(WITHERED_TALL_GRASS, "withered_tall_grass");
        registerBlock(WITHERED_LARGE_FERN, "withered_large_fern");
        registerBlock(OLEANDER, "oleander");
        registerBlock(BLACKSTONE_PEDESTAL, "blackstone_pedestal");
        registerBlock(VERGLAS_ORE, "verglas_ore");
        registerBlock(VERGLAS_ORE_DEEPSLATE, "verglas_ore_deepslate");
        registerBlock(VERGLAS_BLOCK, "verglas_block");
        registerBlock(SOULFIRE_STAIN, "soulfire_stain");
        registerBlock(SOUL_LAMP, "soul_lamp");
    }

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) != false ? litLevel : 0;
    }

    public static <I extends Block> I registerBlock(I block, String name) {
        registerBlockItem(block, name);
		return Registry.register(Registries.BLOCK, new Identifier(SoulsWeaponry.ModId, name), block);
	}

    public static Item registerBlockItem(Block block, String name) {
        Item item = Registry.register(Registries.ITEM, new Identifier(SoulsWeaponry.ModId, name),
                new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(MAIN_GROUP).register(entries -> entries.add(item));
        return item;
    }
}
