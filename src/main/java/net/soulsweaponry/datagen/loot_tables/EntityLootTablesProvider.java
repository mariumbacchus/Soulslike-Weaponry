package net.soulsweaponry.datagen.loot_tables;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.context.LootContextTypes;
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

public class EntityLootTablesProvider extends SimpleFabricLootTableProvider {

    public static final HashMap<String, ArrayList<Item>> BOSS_DROPS = new HashMap<>();

    public EntityLootTablesProvider(FabricDataOutput output) {
        super(output, LootContextTypes.ENTITY);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
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

        registerEntityLootTable(exporter, EntityRegistry.BIG_CHUNGUS, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(ItemEntry.builder(ItemRegistry.CHUNGUS_EMERALD)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                        )
                ));

        registerEntityLootTable(exporter, EntityRegistry.DARK_SORCERER, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(ItemEntry.builder(ItemRegistry.LOST_SOUL)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                        )
                ));

        registerEntityLootTable(exporter, EntityRegistry.REMNANT, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(ItemEntry.builder(ItemRegistry.SOUL_INGOT)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                        )
                ));

        registerEntityLootTable(exporter, EntityRegistry.SOULMASS, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(ItemEntry.builder(ItemRegistry.LOST_SOUL)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 5.0f)))
                                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                        )
                ));

        registerEntityLootTable(exporter, EntityRegistry.WARMTH_ENTITY, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(ItemEntry.builder(Items.BLAZE_POWDER)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                        )
                ));

        registerEntityLootTable(exporter, EntityRegistry.WITHERED_DEMON, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(ItemEntry.builder(ItemRegistry.DEMON_HEART)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
                        )
                ));

        registerEntityLootTable(exporter, EntityRegistry.FROST_GIANT, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
                        .with(ItemEntry.builder(ItemRegistry.SOUL_INGOT))
                )
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 4.0f))
                        .with(ItemEntry.builder(Items.ICE))
                )
        );

        registerEntityLootTable(exporter, EntityRegistry.RIME_SPECTRE, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 4.0f))
                        .with(ItemEntry.builder(Items.BLUE_ICE))
                )
        );
    }

    public static void registerEntityLootTable(BiConsumer<Identifier, LootTable.Builder> exporter, EntityType<?> entityType, LootTable.Builder lootTable) {
        exporter.accept(entityType.getLootTableId(), lootTable);
    }
}