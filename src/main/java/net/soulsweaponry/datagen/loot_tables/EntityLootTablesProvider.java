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
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EntityLootTablesProvider extends SimpleFabricLootTableProvider {

    public static final HashMap<String, ArrayList<Item>> BOSS_DROPS = new HashMap<>();

    private final RegistryWrapper.WrapperLookup registryLookup;

    public EntityLootTablesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.ENTITY);
        this.registryLookup = registryLookup.join();
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        for (String id : BOSS_DROPS.keySet()) {
            LootTable.Builder builder = LootTable.builder();
            for (Item item : BOSS_DROPS.get(id)) {
                LootPool.Builder lootPoolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1));
                lootPoolBuilder.with(ItemEntry.builder(item).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))));
                builder.pool(lootPoolBuilder);
            }
            Identifier lootId = Identifier.of(SoulsWeaponry.ModId, "entities/" + id);
            RegistryKey<LootTable> lootKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, lootId);
            lootTableBiConsumer.accept(lootKey, builder);
        }

        registerEntityLootTable(lootTableBiConsumer, EntityRegistry.BIG_CHUNGUS, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(
                                ItemEntry.builder(ItemRegistry.CHUNGUS_EMERALD)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(0.0f, 1.0f)
                                        ))
                                        .apply(EnchantedCountIncreaseLootFunction.builder(
                                                registryLookup,
                                                UniformLootNumberProvider.create(0.0f, 1.0f)
                                        ))
                        )
                ));

        registerEntityLootTable(lootTableBiConsumer, EntityRegistry.DARK_SORCERER, LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                                .conditionally(KilledByPlayerLootCondition.builder())
                                .with(
                                        ItemEntry.builder(ItemRegistry.LOST_SOUL)
                                                .apply(SetCountLootFunction.builder(
                                                        UniformLootNumberProvider.create(0.0f, 1.0f))
                                                )
                                                .apply(EnchantedCountIncreaseLootFunction.builder(
                                                        registryLookup,
                                                        UniformLootNumberProvider.create(0.0f, 1.0f)
                                                ))
                                )
                )
        );

        registerEntityLootTable(lootTableBiConsumer, EntityRegistry.REMNANT, LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                                .conditionally(KilledByPlayerLootCondition.builder())
                                .with(
                                        ItemEntry.builder(ItemRegistry.SOUL_INGOT)
                                                .apply(SetCountLootFunction.builder(
                                                        UniformLootNumberProvider.create(0.0f, 1.0f)
                                                ))
                                                .apply(EnchantedCountIncreaseLootFunction.builder(
                                                        registryLookup,
                                                        UniformLootNumberProvider.create(0.0f, 1.0f)
                                                ))
                                )
                )
        );

        registerEntityLootTable(lootTableBiConsumer, EntityRegistry.SOULMASS, LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                                .conditionally(KilledByPlayerLootCondition.builder())
                                .with(
                                        ItemEntry.builder(ItemRegistry.LOST_SOUL)
                                                .apply(SetCountLootFunction.builder(
                                                        UniformLootNumberProvider.create(2f, 5f)
                                                ))
                                                .apply(EnchantedCountIncreaseLootFunction.builder(
                                                        registryLookup,
                                                        UniformLootNumberProvider.create(0.0f, 1.0f)
                                                ))
                                )
                )
        );

        registerEntityLootTable(lootTableBiConsumer, EntityRegistry.WARMTH_ENTITY, LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                                .conditionally(KilledByPlayerLootCondition.builder())
                                .with(
                                        ItemEntry.builder(Items.BLAZE_POWDER)
                                                .apply(SetCountLootFunction.builder(
                                                        UniformLootNumberProvider.create(0.0f, 1.0f)
                                                ))
                                                .apply(EnchantedCountIncreaseLootFunction.builder(
                                                        registryLookup,
                                                        UniformLootNumberProvider.create(0.0f, 1.0f)
                                                ))
                                )
                )
        );

        registerEntityLootTable(lootTableBiConsumer, EntityRegistry.WITHERED_DEMON, LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .bonusRolls(ConstantLootNumberProvider.create(0.0f))
                                .conditionally(KilledByPlayerLootCondition.builder())
                                .with(
                                        ItemEntry.builder(ItemRegistry.WITHERED_DEMON_HEART)
                                                .apply(SetCountLootFunction.builder(
                                                        UniformLootNumberProvider.create(0.0f, 1.0f)
                                                ))
                                                .apply(EnchantedCountIncreaseLootFunction.builder(
                                                        registryLookup,
                                                        UniformLootNumberProvider.create(0.0f, 1.0f)
                                                ))
                                )
                )
        );

        registerEntityLootTable(lootTableBiConsumer, EntityRegistry.FROST_GIANT, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 3.0f))
                        .with(ItemEntry.builder(ItemRegistry.SOUL_INGOT))
                )
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 4.0f))
                        .with(ItemEntry.builder(Items.ICE))
                )
        );

        registerEntityLootTable(lootTableBiConsumer, EntityRegistry.RIME_SPECTRE, LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0f, 4.0f))
                                .with(ItemEntry.builder(Items.BLUE_ICE))
                )
        );
    }

    public static void registerEntityLootTable(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer, EntityType<?> entityType, LootTable.Builder lootTable) {
        Identifier lootId = Identifier.of(SoulsWeaponry.ModId, "entities/" + entityType.toString());
        RegistryKey<LootTable> lootKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, lootId);
        lootTableBiConsumer.accept(lootKey, lootTable);
    }
}
