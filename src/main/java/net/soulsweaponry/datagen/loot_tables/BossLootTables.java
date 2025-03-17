package net.soulsweaponry.datagen.loot_tables;

import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EntityRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BossLootTables implements LootTableGenerator {

    public static final Map<String, List<Item>> BOSS_DROPS = new HashMap<>();

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        EntityRegistry.registerBossDrops();
        for (Map.Entry<String, List<Item>> entry : BOSS_DROPS.entrySet()) {
            String bossId = entry.getKey();
            LootTable.Builder builder = LootTable.builder();
            for (Item item : entry.getValue()) {
                LootPool.Builder lootPoolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1));
                lootPoolBuilder.with(ItemEntry.builder(item).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))));
                lootPoolBuilder.build();
                builder.pool(lootPoolBuilder);
            }
            Identifier lootTableLocation = new Identifier(SoulsWeaponry.ModId, "entities/" + bossId);
            exporter.accept(lootTableLocation, builder);
        }
    }
}
