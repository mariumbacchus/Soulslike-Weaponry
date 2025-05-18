package net.soulsweaponry.world.feature;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.BlockRegistry;

import java.util.List;

/**
 * Big thanks to KaupenJoe for showing how to do ore spawning through datagen.
 */
public class ConfiguredFeatures {

    public static final int MOONSTONE_ORE_VEIN_SIZE = 5;
    public static final int VERGLAS_ORE_VEIN_SIZE = 3;

    public static final RegistryKey<ConfiguredFeature<?, ?>> MOONSTONE_ORE_KEY = registerKey("moonstone_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> VERGLAS_ORE_KEY = registerKey("verglas_ore");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        // Nice to have these for later use even though they aren't being used
        RuleTest netherReplaceables = new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER);
        RuleTest endReplaceables = new BlockMatchRuleTest(Blocks.END_STONE);

        List<OreFeatureConfig.Target> overworldMoonstoneOres = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, BlockRegistry.MOONSTONE_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, BlockRegistry.MOONSTONE_ORE_DEEPSLATE.getDefaultState())
        );
        List<OreFeatureConfig.Target> overworldVerglasOres = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, BlockRegistry.VERGLAS_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, BlockRegistry.VERGLAS_ORE_DEEPSLATE.getDefaultState())
        );

        register(context, MOONSTONE_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldMoonstoneOres, MOONSTONE_ORE_VEIN_SIZE, 0.75f));
        register(context, VERGLAS_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldVerglasOres, VERGLAS_ORE_VEIN_SIZE, 0.75f));
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(SoulsWeaponry.ModId, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
