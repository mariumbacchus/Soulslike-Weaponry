package net.soulsweaponry.datagen.loot_tables;

import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EntityLootTablesProvider implements LootTableGenerator {

    public static final HashMap<String, ArrayList<Item>> BOSS_DROPS = new HashMap<>();

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        EntityRegistry.registerBossDrops();
        for (String id : BOSS_DROPS.keySet()) {
            LootTable.Builder table = LootTable.builder();
            for (Item item : BOSS_DROPS.get(id)) {
                table.pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .with(ItemEntry.builder(item)
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))))
                );
            }
            Identifier lootId = new Identifier(SoulsWeaponry.ModId, "entities/" + id);
            exporter.accept(lootId, table);
        }

        registerEntityLootTable(exporter, EntityRegistry.BIG_CHUNGUS,
                LootTable.builder().pool(killedByPlayerDrop(
                        ItemRegistry.CHUNGUS_EMERALD.get(),
                        0.0f, 1.0f,
                        0.0f, 1.0f
                )));

        registerEntityLootTable(exporter, EntityRegistry.DARK_SORCERER,
                LootTable.builder().pool(killedByPlayerDrop(
                        ItemRegistry.LOST_SOUL.get(),
                        0.0f, 1.0f,
                        0.0f, 1.0f
                )));

        registerEntityLootTable(exporter, EntityRegistry.REMNANT,
                LootTable.builder().pool(killedByPlayerDrop(
                        ItemRegistry.SOUL_INGOT.get(),
                        0.0f, 1.0f,
                        0.0f, 1.0f
                )));

        registerEntityLootTable(exporter, EntityRegistry.SOULMASS,
                LootTable.builder().pool(killedByPlayerDrop(
                        ItemRegistry.LOST_SOUL.get(),
                        2.0f, 5.0f,
                        0.0f, 1.0f
                )));

        registerEntityLootTable(exporter, EntityRegistry.WARMTH_ENTITY,
                LootTable.builder().pool(killedByPlayerDrop(
                        Items.BLAZE_POWDER,
                        0.0f, 1.0f,
                        0.0f, 1.0f
                )));

        registerEntityLootTable(exporter, EntityRegistry.WITHERED_DEMON,
                LootTable.builder().pool(killedByPlayerDrop(
                        ItemRegistry.DEMON_HEART.get(),
                        0.0f, 1.0f,
                        0.0f, 1.0f
                )));

        registerEntityLootTable(exporter, EntityRegistry.FROST_GIANT,
                LootTable.builder()
                        .pool(LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
                                .with(ItemEntry.builder(ItemRegistry.SOUL_INGOT.get())))
                        .pool(LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0f, 4.0f))
                                .with(ItemEntry.builder(Items.ICE)))
        );

        registerEntityLootTable(exporter, EntityRegistry.RIME_SPECTRE,
                LootTable.builder()
                        .pool(LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0f, 4.0f))
                                .with(ItemEntry.builder(Items.BLUE_ICE)))
        );
    }

    private static LootPool.Builder killedByPlayerDrop(
            Item item,
            float minCount,
            float maxCount,
            float minLootingBonus,
            float maxLootingBonus
    ) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1.0f))
                .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                .conditionally(KilledByPlayerLootCondition.builder())
                .with(ItemEntry.builder(item)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minCount, maxCount)))
                        .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(minLootingBonus, maxLootingBonus))));
    }

    public static void registerEntityLootTable(
            BiConsumer<Identifier, LootTable.Builder> exporter,
            Supplier<? extends EntityType<?>> entityType,
            LootTable.Builder lootTable
    ) {
        exporter.accept(entityType.get().getLootTableId(), lootTable);
    }
}