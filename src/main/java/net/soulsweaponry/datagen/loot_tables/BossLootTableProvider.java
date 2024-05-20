package net.soulsweaponry.datagen.loot_tables;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.LootTableProvider;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BossLootTableProvider extends LootTableProvider {

    public BossLootTableProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<Identifier, LootTable.Builder>>>, LootContextType>> getTables() {
        return ImmutableList.of(Pair.of(BossLootTables::new, LootContextTypes.EMPTY));
    }

    @Override
    protected void validate(Map<Identifier, LootTable> tables, LootTableReporter validationtracker) {
        tables.forEach((name, table) -> LootManager.validate(validationtracker, name, table));
    }
}
