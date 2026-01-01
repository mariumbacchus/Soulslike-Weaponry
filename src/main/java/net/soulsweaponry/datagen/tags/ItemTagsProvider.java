package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemTagsProvider extends FabricTagProvider.ItemTagProvider {

    /**
     * Items in this list will be added to tags so that they are enchanted properly due to new enchanting system
     */
    public static final List<Item> SWORDS = new ArrayList<>();
    public static final List<Item> BOWS = new ArrayList<>();
    public static final List<Item> CROSSBOWS = new ArrayList<>();
    public static final List<Item> AXES = new ArrayList<>();
    public static final List<Item> MACES = new ArrayList<>();
    public static final List<Item> ARMORS = new ArrayList<>();

    // Not used for enchanting necessarily
    public static final List<Item> HEAVY_WEAPONS = new ArrayList<>();
    public static final List<Item> SOUL_HARVESTING_WEAPONS = new ArrayList<>();

    public ItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModTags.Items.GUNS)
                .add(GunRegistry.BLUNDERBUSS)
                .add(GunRegistry.GATLING_GUN)
                .add(GunRegistry.HUNTER_CANNON)
                .add(GunRegistry.HUNTER_PISTOL);

        this.getOrCreateTagBuilder(ModTags.Items.GUN_ENCHANTABLE)
                .addTag(ModTags.Items.GUNS);

        this.getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ItemRegistry.MOONSTONE_PICKAXE);

        this.getOrCreateTagBuilder(ItemTags.HOES)
                .add(ItemRegistry.MOONSTONE_HOE);

        this.getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(ItemRegistry.MOONSTONE_SHOVEL);

        this.getOrCreateTagBuilder(ItemTags.AXES)
                .add(AXES.toArray(Item[]::new))
                .add(ItemRegistry.MOONSTONE_AXE);

        this.getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(SWORDS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ItemTags.BOW_ENCHANTABLE)
                .add(BOWS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ConventionalItemTags.BOW_TOOLS)
                .add(BOWS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ItemTags.CROSSBOW_ENCHANTABLE)
                .add(CROSSBOWS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ConventionalItemTags.CROSSBOW_TOOLS)
                .add(CROSSBOWS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ConventionalItemTags.ARMORS)
                .add(ARMORS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ItemTags.MACE_ENCHANTABLE)
                .add(MACES.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ConventionalItemTags.MELEE_WEAPON_TOOLS)
                .add(SWORDS.toArray(Item[]::new))
                .add(MACES.toArray(Item[]::new))
                .add(AXES.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ModTags.Items.HEAVY_WEAPONS)
                .add(HEAVY_WEAPONS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ModTags.Items.SCYTHES)
                .add(WeaponRegistry.DARKIN_SCYTHE_PRE)
                .add(WeaponRegistry.DARKIN_SCYTHE_PRIME)
                .add(WeaponRegistry.SHADOW_ASSASSIN_SCYTHE)
                .add(WeaponRegistry.SOUL_REAPER)
                .add(WeaponRegistry.FORLORN_SCYTHE);

        this.getOrCreateTagBuilder(ModTags.Items.SUMMONABLE_WEAPONS)
                .add(WeaponRegistry.FREYR_SWORD);

        this.getOrCreateTagBuilder(ModTags.Items.STAVES)
                .add(WeaponRegistry.DRAGON_STAFF)
                .add(WeaponRegistry.WITHERED_WABBAJACK);

        this.getOrCreateTagBuilder(ModTags.Items.UPGRADABLE_MOONLIGHT_SWORDS)
                .add(WeaponRegistry.MOONLIGHT_GREATSWORD)
                .add(WeaponRegistry.MOONLIGHT_SHORTSWORD);

        this.getOrCreateTagBuilder(ModTags.Items.SOUL_HARVESTING_WEAPONS)
                .add(SOUL_HARVESTING_WEAPONS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ModTags.Items.DRAGONBANE_MATERIAL)
                .add(Items.NETHERITE_INGOT)
                .addOptional(Identifier.of("iceandfire", "dragonbone"));

        this.getOrCreateTagBuilder(ModTags.Items.DUO_BOSS_SOULS)
                .add(ItemRegistry.LORD_SOUL_DAY_STALKER)
                .add(ItemRegistry.LORD_SOUL_NIGHT_PROWLER);

        this.getOrCreateTagBuilder(ModTags.Items.LORD_SOUL)
                .add(ItemRegistry.LORD_SOUL_VOID)
                .add(ItemRegistry.LORD_SOUL_DARK)
                .add(ItemRegistry.LORD_SOUL_RED)
                .add(ItemRegistry.LORD_SOUL_PURPLE)
                .add(ItemRegistry.LORD_SOUL_ROSE)
                .add(ItemRegistry.LORD_SOUL_WHITE)
                .add(ItemRegistry.LORD_SOUL_NIGHT_PROWLER)
                .add(ItemRegistry.LORD_SOUL_DAY_STALKER);

        this.getOrCreateTagBuilder(ModTags.Items.LOST_SOUL)
                .add(ItemRegistry.LOST_SOUL)
                .addOptional(Identifier.of("terramity", "lost_soul"));

        this.getOrCreateTagBuilder(ModTags.Items.DEMON_HEARTS)
                .add(FoodRegistry.DEMON_HEART)
                .addOptional(Identifier.of("bewitchment", "demon_heart"));

        this.getOrCreateTagBuilder(ModTags.Items.LOST_SOUL_REPAIR)
                .add(ItemRegistry.SOUL_INGOT)
                .addTag(ModTags.Items.LOST_SOUL);

        this.getOrCreateTagBuilder(ModTags.Items.TRICK_WEAPONS)
                .add(WeaponRegistry.KIRKHAMMER)
                .add(WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD)
                .add(WeaponRegistry.HOLY_MOONLIGHT_SWORD)
                .add(WeaponRegistry.SILVER_SWORD)
                .add(WeaponRegistry.SIMONS_BOWBLADE)
                .add(WeaponRegistry.SIMONS_BLADE)
                .add(WeaponRegistry.HOLY_GREATSWORD);

        this.getOrCreateTagBuilder(ConventionalItemTags.MUSIC_DISCS)
                .add(ItemRegistry.CHUNGUS_DISC)
                .add(ItemRegistry.DRAUGR_BOSS_DISC)
                .add(ItemRegistry.FALLEN_ICON_DISC);

        this.getOrCreateTagBuilder(ModTags.Items.MELEE_ITEM_UPGRADABLES)
                .addTag(ConventionalItemTags.MELEE_WEAPON_TOOLS);

        this.getOrCreateTagBuilder(ModTags.Items.RANGED_ITEM_UPGRADABLES)
                .addTag(ConventionalItemTags.BOW_TOOLS)
                .addTag(ConventionalItemTags.CROSSBOW_TOOLS);

        this.getOrCreateTagBuilder(ItemTags.HEAD_ARMOR)
                .add(ArmorRegistry.CHAOS_CROWN)
                .add(ArmorRegistry.CHAOS_HELMET)
                .add(ArmorRegistry.FORLORN_HELMET)
                .add(ArmorRegistry.SOUL_INGOT_HELMET)
                .add(ArmorRegistry.SOUL_ROBES_HELMET);

        this.getOrCreateTagBuilder(ItemTags.CHEST_ARMOR)
                .add(ArmorRegistry.ARKENPLATE)
                .add(ArmorRegistry.CHAOS_ROBES)
                .add(ArmorRegistry.ENHANCED_ARKENPLATE)
                .add(ArmorRegistry.ENHANCED_WITHERED_CHEST)
                .add(ArmorRegistry.WITHERED_CHEST)
                .add(ArmorRegistry.FORLORN_CHESTPLATE)
                .add(ArmorRegistry.SOUL_INGOT_CHESTPLATE)
                .add(ArmorRegistry.SOUL_ROBES_CHESTPLATE);

        this.getOrCreateTagBuilder(ItemTags.LEG_ARMOR)
                .add(ArmorRegistry.FORLORN_LEGGINGS)
                .add(ArmorRegistry.SOUL_INGOT_LEGGINGS)
                .add(ArmorRegistry.SOUL_ROBES_LEGGINGS);

        this.getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
                .add(ArmorRegistry.FORLORN_BOOTS)
                .add(ArmorRegistry.SOUL_INGOT_BOOTS)
                .add(ArmorRegistry.SOUL_ROBES_BOOTS);

        // Repairs
        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_CHAOS_ARMOR)
                .add(ItemRegistry.MOONSTONE)
                .add(Items.NETHERITE_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_ENHANCED_CHAOS_ARMOR)
                .add(ItemRegistry.MOONSTONE)
                .add(Items.NETHERITE_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_WITHERED_ARMOR)
                .add(ItemRegistry.CRIMSON_INGOT)
                .add(Items.NETHERITE_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_ENHANCED_WITHERED_ARMOR)
                .add(ItemRegistry.CRIMSON_INGOT)
                .add(Items.NETHERITE_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_CHAOS_SET)
                .add(ItemRegistry.MOONSTONE);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_SOUL_INGOT)
                .add(ItemRegistry.SOUL_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_SOUL_ROBES)
                .add(ItemRegistry.SOUL_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_FORLORN)
                .add(ItemRegistry.SOUL_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_IRON_BLOCK_TOOL)
                .add(Items.IRON_BLOCK);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_LOST_SOUL_TOOL)
                .add(ItemRegistry.SOUL_INGOT)
                .addTag(ModTags.Items.LOST_SOUL);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_LOST_SOUL_DURABLE_TOOL)
                .add(ItemRegistry.SOUL_INGOT)
                .addTag(ModTags.Items.LOST_SOUL);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_MOONSTONE_OR_VERGLAS_TOOL)
                .add(ItemRegistry.MOONSTONE)
                .add(ItemRegistry.VERGLAS);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_CRIMSON_INGOT_TOOL)
                .add(ItemRegistry.CRIMSON_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_MOONSTONE_TOOL)
                .add(ItemRegistry.MOONSTONE)
                .add(ItemRegistry.VERGLAS);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_ECHO_SHARD_TOOL)
                .add(Items.ECHO_SHARD);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_KRAKEN_SLAYER_BOW)
                .add(Items.GOLD_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_KRAKEN_SLAYER_CROSSBOW)
                .add(Items.GOLD_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_DARKMOON_LONGBOW)
                .add(Items.GOLD_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_SIMONS_BOWBLADE)
                .add(Items.IRON_BLOCK)
                .add(ItemRegistry.SOUL_INGOT);

        this.getOrCreateTagBuilder(ModTags.Items.REPAIRS_GALEFORCE)
                .add(ItemRegistry.VERGLAS)
                .add(ItemRegistry.MOONSTONE);
    }
}