package net.soulsweaponry.datagen.loot_tables;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.function.BiConsumer;

public class ChestLootTableProvider extends SimpleFabricLootTableProvider {

    public ChestLootTableProvider(FabricDataOutput output) {
        super(output, LootContextTypes.CHEST);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        Identifier goodLoot = new Identifier(SoulsWeaponry.ModId, "chests/cathedral_good_loot");
        LootPool.Builder pool = LootPool.builder()
                .rolls(UniformLootNumberProvider.create(2.0f, 5.0f))

                .with(ItemEntry.builder(Items.DIAMOND).weight(4)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))))

                .with(ItemEntry.builder(Items.IRON_INGOT).weight(15)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 5))))

                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(25)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 5))))

                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(15)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 7))))

                .with(ItemEntry.builder(ItemRegistry.LOST_SOUL).weight(15)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))))

                .with(ItemEntry.builder(Items.BONE).weight(25)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4, 6))))

                .with(ItemEntry.builder(Items.CHAINMAIL_HELMET).weight(15))
                .with(ItemEntry.builder(Items.CHAINMAIL_CHESTPLATE).weight(10))
                .with(ItemEntry.builder(Items.CHAINMAIL_LEGGINGS).weight(10))
                .with(ItemEntry.builder(Items.CHAINMAIL_BOOTS).weight(15))

                .with(ItemEntry.builder(Items.BOOK).weight(20)
                        .apply(EnchantRandomlyLootFunction.builder()))

                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(15))
                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(2));
        exporter.accept(goodLoot, LootTable.builder().pool(pool));

        Identifier cathedralLoot = new Identifier(SoulsWeaponry.ModId, "chests/cathedral_loot");
        LootPool.Builder p1 = LootPool.builder()
                .rolls(UniformLootNumberProvider.create(1, 3))

                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(15))
                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(1))
                .with(ItemEntry.builder(ItemRegistry.CHUNGUS_EMERALD).weight(15))
                .with(ItemEntry.builder(Items.MUSIC_DISC_STAL).weight(10))
                .with(ItemEntry.builder(Items.NAME_TAG).weight(20))

                .with(ItemEntry.builder(Items.IRON_CHESTPLATE).weight(10))
                .with(ItemEntry.builder(Items.IRON_HELMET).weight(15))
                .with(ItemEntry.builder(Items.IRON_LEGGINGS).weight(5))
                .with(ItemEntry.builder(Items.IRON_BOOTS).weight(5))

                .with(ItemEntry.builder(Items.BOOK).weight(10)
                        .apply(EnchantRandomlyLootFunction.builder()));

        LootPool.Builder p2 = LootPool.builder()
                .rolls(UniformLootNumberProvider.create(1, 4))

                .with(ItemEntry.builder(Items.IRON_INGOT).weight(10)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))

                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(5)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))

                .with(ItemEntry.builder(Items.DEEPSLATE_TILES).weight(20))

                .with(ItemEntry.builder(ItemRegistry.LOST_SOUL).weight(20)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))

                .with(ItemEntry.builder(Items.COAL).weight(15)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4))))

                .with(ItemEntry.builder(Items.IRON_BLOCK).weight(4)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))));

        LootPool.Builder p3 = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(3))

                .with(ItemEntry.builder(Items.BONE).weight(10)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 8))))

                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(15)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 7))))

                .with(ItemEntry.builder(Items.STRING).weight(10)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 8))));

        exporter.accept(
                cathedralLoot,
                LootTable.builder().pool(p1).pool(p2).pool(p3)
        );
    }
}