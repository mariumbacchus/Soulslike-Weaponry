package net.soulsweaponry.datagen.loot_tables;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.loottable.LootTableProvider;
import net.minecraft.loot.context.LootContextTypes;

import java.util.List;
import java.util.Set;

public class BossLootTableProvider extends LootTableProvider {

    public BossLootTableProvider(DataOutput output) {
        super(output, Set.of(), List.of(
                new LootTableProvider.LootTypeGenerator(BossLootTables::new, LootContextTypes.ENTITY)
        ));
    }
}
