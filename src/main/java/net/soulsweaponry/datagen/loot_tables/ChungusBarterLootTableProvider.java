package net.soulsweaponry.datagen.loot_tables;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.*;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

import java.util.function.BiConsumer;

public class ChungusBarterLootTableProvider extends SimpleFabricLootTableProvider {

    public ChungusBarterLootTableProvider(FabricDataOutput output) {
        super(output, LootContextTypes.BARTER);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        Identifier key = new Identifier(SoulsWeaponry.ModId, "gameplay/chungus_bartering");
        LootPool.Builder pool = LootPool.builder().rolls(ConstantLootNumberProvider.create(1));

        // troll drops
        pool.with(ItemEntry.builder(Items.CARVED_PUMPKIN).weight(70)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.BINDING_CURSE, ConstantLootNumberProvider.create(1))));
        pool.with(ItemEntry.builder(Items.BOOK).weight(50)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.VANISHING_CURSE, ConstantLootNumberProvider.create(1))));
        pool.with(ItemEntry.builder(Items.BOOK).weight(50)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.BINDING_CURSE, ConstantLootNumberProvider.create(1))));
        pool.with(ItemEntry.builder(Items.SPONGE).weight(60)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.SWIFT_SNEAK, UniformLootNumberProvider.create(1.0F, 10f)))
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.SOUL_SPEED, UniformLootNumberProvider.create(1.0F, 10f)))
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.UNBREAKING, UniformLootNumberProvider.create(1.0F, 10f))));
        pool.with(ItemEntry.builder(Items.INFESTED_DEEPSLATE).weight(90).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5, 32))));
        pool.with(ItemEntry.builder(Items.INFESTED_COBBLESTONE).weight(90).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5, 32))));
        pool.with(ItemEntry.builder(Items.INFESTED_STONE).weight(90).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5, 32))));
        pool.with(ItemEntry.builder(Items.GOLDEN_HOE).weight(80)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.LOYALTY, ConstantLootNumberProvider.create(3))));
        pool.with(ItemEntry.builder(Items.SHIELD).weight(80)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.SHARPNESS, ConstantLootNumberProvider.create(5))));
        pool.with(ItemEntry.builder(Items.CHAINMAIL_BOOTS).weight(50)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.AQUA_AFFINITY, UniformLootNumberProvider.create(1.0F, 10f))));
        pool.with(ItemEntry.builder(Items.ENDERMITE_SPAWN_EGG).weight(30).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 64))));
        pool.with(ItemEntry.builder(Items.LEATHER_HORSE_ARMOR).weight(30).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(32))));
        pool.with(it(Items.RABBIT_SPAWN_EGG, 25));
        pool.with(ItemEntry.builder(Items.REINFORCED_DEEPSLATE).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 8))));
        pool.with(ItemEntry.builder(Items.END_ROD).weight(40)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 4)))
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.PROTECTION, UniformLootNumberProvider.create(1.0F, 10f))));
        pool.with(ItemEntry.builder(Items.CAKE).weight(50).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(16))));
        pool.with(ItemEntry.builder(Items.FLINT_AND_STEEL).weight(40)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.FIRE_ASPECT, UniformLootNumberProvider.create(5.0F, 10f))));
        pool.with(ItemEntry.builder(Items.POISONOUS_POTATO).weight(80)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.BANE_OF_ARTHROPODS, UniformLootNumberProvider.create(3.0F, 10f))));
        pool.with(ItemEntry.builder(Items.SUSPICIOUS_STEW).weight(80).apply(SetStewEffectLootFunction.builder()
                .withEffect(StatusEffects.BAD_OMEN, ConstantLootNumberProvider.create(6969))
                .withEffect(StatusEffects.MINING_FATIGUE, ConstantLootNumberProvider.create(6969))));

        // mid drops
        pool.with(ItemEntry.builder(Items.IRON_NUGGET).weight(100).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(10, 36))));
        pool.with(ItemEntry.builder(Items.LEATHER).weight(100).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 4))));
        pool.with(ItemEntry.builder(Items.STRING).weight(100).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 9))));
        pool.with(ItemEntry.builder(Items.WHEAT_SEEDS).weight(120).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 8))));
        pool.with(it(Items.BAMBOO, 100));
        pool.with(ItemEntry.builder(Items.POTION).weight(70).apply(SetPotionLootFunction.builder(Potions.HARMING)));
        pool.with(ItemEntry.builder(Items.POTION).weight(70).apply(SetPotionLootFunction.builder(Potions.WATER)));
        pool.with(it(Items.BUCKET, 80));
        pool.with(ItemEntry.builder(Items.YELLOW_DYE).weight(100).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3, 19))));
        pool.with(ItemEntry.builder(Items.RED_DYE).weight(100).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5, 14))));
        pool.with(ItemEntry.builder(Items.WHITE_DYE).weight(100).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5, 10))));
        pool.with(ItemEntry.builder(Items.GOLDEN_CARROT).weight(60).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 5))));
        pool.with(ItemEntry.builder(Items.CARROT).weight(100).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 14))));
        pool.with(ItemEntry.builder(Items.LEATHER_BOOTS).weight(50)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.SWIFT_SNEAK, UniformLootNumberProvider.create(1.0F, 5f))));
        pool.with(it(Items.MUSIC_DISC_STAL, 40));
        pool.with(it(Items.JUKEBOX, 100));
        pool.with(ItemEntry.builder(Items.POTION).weight(50).apply(SetPotionLootFunction.builder(Potions.LUCK)));
        pool.with(it(Items.STICKY_PISTON, 50));

        // good drops
        pool.with(it(Items.TOTEM_OF_UNDYING, 5));
        pool.with(it(Items.ELYTRA, 4));
        pool.with(it(ItemRegistry.LORD_SOUL_RED, 3));
        pool.with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(50).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 10))));
        pool.with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))));

        pool.with(ItemEntry.builder(Items.WOODEN_SWORD).weight(60)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.FIRE_ASPECT, UniformLootNumberProvider.create(1.0F, 5f)))
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.SHARPNESS, UniformLootNumberProvider.create(1.0F, 8f)))
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.SWEEPING, UniformLootNumberProvider.create(1.0F, 5f))));
        pool.with(ItemEntry.builder(Items.IRON_CHESTPLATE).weight(40)
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.BLAST_PROTECTION, UniformLootNumberProvider.create(1.0F, 6f)))
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.BINDING_CURSE, ConstantLootNumberProvider.create(1)))
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.FIRE_PROTECTION, UniformLootNumberProvider.create(1.0F, 6f)))
                .apply(new SetEnchantmentsLootFunction.Builder().enchantment(Enchantments.PROJECTILE_PROTECTION, UniformLootNumberProvider.create(1.0F, 6f))));

        // chungus drops
        pool.with(ItemEntry.builder(Items.POTION).weight(15).apply(SetPotionLootFunction.builder(EffectRegistry.CHUNGUS_TONIC_POTION)));
        pool.with(ItemEntry.builder(Items.SPLASH_POTION).weight(3).apply(SetPotionLootFunction.builder(EffectRegistry.CHUNGUS_TONIC_POTION)));
        pool.with(it(ItemRegistry.CHUNGUS_DISC, 20));
        pool.with(it(BlockRegistry.CHUNGUS_MONOLITH, 60));
        pool.with(ItemEntry.builder(ItemRegistry.CHUNGUS_EMERALD).weight(100).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 10))));
        pool.with(it(WeaponRegistry.CHUNGUS_STAFF, 20));

        exporter.accept(key, LootTable.builder().pool(pool));
    }

    private static ItemEntry.Builder<?> it(ItemConvertible item, int weight) {
        return ItemEntry.builder(item).weight(weight);
    }
}