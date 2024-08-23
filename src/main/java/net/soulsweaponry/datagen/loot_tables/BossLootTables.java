package net.soulsweaponry.datagen.loot_tables;

import net.minecraft.data.server.loottable.EntityLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.HashMap;

public class BossLootTables extends EntityLootTableGenerator {

    public static final HashMap<String, ArrayList<Item>> BOSS_DROPS = new HashMap<>();

    protected BossLootTables() {
        super(FeatureFlags.FEATURE_MANAGER.getFeatureSet());
    }

    @Override
    public void generate() {
        EntityRegistry.registerBossDrops();
        for (String id : BOSS_DROPS.keySet()) {
            LootTable.Builder builder = LootTable.builder();
            for (Item item : BOSS_DROPS.get(id)) {
                LootPool.Builder lootPoolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1));
                lootPoolBuilder.with(ItemEntry.builder(item).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))));
                builder.pool(lootPoolBuilder);
            }
            register(ForgeRegistries.ENTITY_TYPES.getValue(new Identifier(SoulsWeaponry.ModId, "entities/" + id)), builder);
        }
    }
}
