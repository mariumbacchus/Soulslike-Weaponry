package net.soulsweaponry.datagen.loot_tables;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class BossLootTableProvider extends SimpleFabricLootTableProvider {

    public static final HashMap<String, ArrayList<Item>> BOSS_DROPS = new HashMap<>();

    public BossLootTableProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator, LootContextTypes.ENTITY);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> bi) {
        for (String id : BOSS_DROPS.keySet()) {
            LootTable.Builder builder = LootTable.builder();
            for (Item item : BOSS_DROPS.get(id)) {
                LootPool.Builder lootPoolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1));
                lootPoolBuilder.with(ItemEntry.builder(item).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))));
                builder.pool(lootPoolBuilder);
            }
            bi.accept(new Identifier(SoulsWeaponry.ModId, "entities/" + id), builder);
        }
    }
}