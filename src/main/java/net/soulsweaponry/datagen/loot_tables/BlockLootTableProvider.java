package net.soulsweaponry.datagen.loot_tables;

import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.function.BiConsumer;

public class BlockLootTableProvider implements LootTableGenerator {

    private BiConsumer<Identifier, LootTable.Builder> exporter;

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        this.exporter = exporter;

        addDrop(BlockRegistry.MOONSTONE_ORE.get(),
                oreDrops(BlockRegistry.MOONSTONE_ORE.get(), ItemRegistry.MOONSTONE.get()));

        addDrop(BlockRegistry.MOONSTONE_ORE_DEEPSLATE.get(),
                oreDrops(BlockRegistry.MOONSTONE_ORE_DEEPSLATE.get(), ItemRegistry.MOONSTONE.get()));

        addDrop(BlockRegistry.VERGLAS_ORE.get(),
                oreDrops(BlockRegistry.VERGLAS_ORE.get(), ItemRegistry.VERGLAS.get()));

        addDrop(BlockRegistry.VERGLAS_ORE_DEEPSLATE.get(),
                oreDrops(BlockRegistry.VERGLAS_ORE_DEEPSLATE.get(), ItemRegistry.VERGLAS.get()));

        addDrop(BlockRegistry.ALTAR_BLOCK.get());
        addDrop(BlockRegistry.CHUNGUS_EMERALD_BLOCK.get());
        addDrop(BlockRegistry.CHUNGUS_MONOLITH.get());
        addDrop(BlockRegistry.CRACKED_INFUSED_BLACKSTONE.get());
        addDrop(BlockRegistry.CRIMSON_OBSIDIAN.get());
        addDrop(BlockRegistry.HYDRANGEA.get());
        addDrop(BlockRegistry.INFUSED_BLACKSTONE.get());
        addDrop(BlockRegistry.MOONSTONE_BLOCK.get());
        addDrop(BlockRegistry.SOUL_LAMP.get());
        addDrop(BlockRegistry.SOULFIRE_STAIN.get());
        addDrop(BlockRegistry.VERGLAS_BLOCK.get());
        addDrop(BlockRegistry.BLACKSTONE_PEDESTAL.get());

        addDrop(BlockRegistry.WITHERED_DIRT.get());

        addDrop(BlockRegistry.WITHERED_GRASS_BLOCK.get(),
                dropsWithSilkTouch(
                        BlockRegistry.WITHERED_GRASS_BLOCK.get(),
                        BlockRegistry.WITHERED_DIRT.get()
                ));

        addDrop(BlockRegistry.OLEANDER.get(),
                doorDrops(BlockRegistry.OLEANDER.get()));
    }

    private void addDrop(Block block) {
        addDrop(block, drops(block));
    }

    private void addDrop(Block block, LootTable.Builder table) {
        exporter.accept(block.getLootTableId(), table);
    }

    private static LootTable.Builder drops(ItemConvertible item) {
        return LootTable.builder().pool(
                LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .with(ItemEntry.builder(item))
        );
    }

    private static LootTable.Builder oreDrops(Block ore, ItemConvertible drop) {
        return LootTable.builder().pool(
                LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(AlternativeEntry.builder(
                                ItemEntry.builder(ore)
                                        .conditionally(silkTouch()),
                                ItemEntry.builder(drop)
                                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                                        .apply(ExplosionDecayLootFunction.builder())
                        ))
        );
    }

    private static LootTable.Builder dropsWithSilkTouch(Block silkTouchDrop, ItemConvertible normalDrop) {
        return LootTable.builder().pool(
                LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(AlternativeEntry.builder(
                                ItemEntry.builder(silkTouchDrop)
                                        .conditionally(silkTouch()),
                                ItemEntry.builder(normalDrop)
                                        .conditionally(SurvivesExplosionLootCondition.builder())
                        ))
        );
    }

    private static LootTable.Builder doorDrops(Block block) {
        return drops(block);
    }

    private static LootCondition.Builder silkTouch() {
        return MatchToolLootCondition.builder(
                ItemPredicate.Builder.create()
                        .enchantment(new EnchantmentPredicate(
                                Enchantments.SILK_TOUCH,
                                NumberRange.IntRange.atLeast(1)
                        ))
        );
    }
}