package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemTagsProvider extends FabricTagProvider.ItemTagProvider {

    public static final List<Item> SWORDS = new ArrayList<>();
    public static final List<Item> BOWS = new ArrayList<>();
    public static final List<Item> CROSSBOWS = new ArrayList<>();
    public static final List<Item> AXES = new ArrayList<>();
    public static final List<Item> ARMORS = new ArrayList<>();

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

        this.getOrCreateTagBuilder(ConventionalItemTags.BOWS)
                .add(BOWS.toArray(Item[]::new));

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
                .addOptional(new Identifier("iceandfire", "dragonbone"));

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
                .addOptional(new Identifier("terramity", "lost_soul"));

        this.getOrCreateTagBuilder(ModTags.Items.DEMON_HEARTS)
                .add(ItemRegistry.DEMON_HEART)
                .addOptional(new Identifier("bewitchment", "demon_heart"));

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

        this.getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
                .add(ItemRegistry.CHUNGUS_DISC)
                .add(ItemRegistry.DRAUGR_BOSS_DISC)
                .add(ItemRegistry.FALLEN_ICON_DISC);

        this.getOrCreateTagBuilder(ModTags.Items.STICKS)
                .add(Items.STICK);

        this.getOrCreateTagBuilder(ModTags.Items.MELEE_ITEM_UPGRADABLES)
                .add(Items.TRIDENT)
                .addTag(ItemTags.SWORDS);

        this.getOrCreateTagBuilder(ModTags.Items.RANGED_ITEM_UPGRADABLES)
                .addTag(ConventionalItemTags.BOWS)
                .add(CROSSBOWS.toArray(Item[]::new))
                .add(Items.CROSSBOW);

        this.getOrCreateTagBuilder(ModTags.Items.HEAD_ARMOR)
                .add(Items.CHAINMAIL_HELMET)
                .add(Items.DIAMOND_HELMET)
                .add(Items.GOLDEN_HELMET)
                .add(Items.IRON_HELMET)
                .add(Items.LEATHER_HELMET)
                .add(Items.NETHERITE_HELMET)
                .add(Items.TURTLE_HELMET)
                .add(ArmorRegistry.CHAOS_CROWN)
                .add(ArmorRegistry.CHAOS_HELMET)
                .add(ArmorRegistry.FORLORN_HELMET)
                .add(ArmorRegistry.SOUL_INGOT_HELMET)
                .add(ArmorRegistry.SOUL_ROBES_HELMET);

        this.getOrCreateTagBuilder(ModTags.Items.CHEST_ARMOR)
                .add(Items.CHAINMAIL_CHESTPLATE)
                .add(Items.DIAMOND_CHESTPLATE)
                .add(Items.GOLDEN_CHESTPLATE)
                .add(Items.IRON_CHESTPLATE)
                .add(Items.LEATHER_CHESTPLATE)
                .add(Items.NETHERITE_CHESTPLATE)
                .add(ArmorRegistry.ARKENPLATE)
                .add(ArmorRegistry.CHAOS_ROBES)
                .add(ArmorRegistry.ENHANCED_ARKENPLATE)
                .add(ArmorRegistry.ENHANCED_WITHERED_CHEST)
                .add(ArmorRegistry.WITHERED_CHEST)
                .add(ArmorRegistry.FORLORN_CHESTPLATE)
                .add(ArmorRegistry.SOUL_INGOT_CHESTPLATE)
                .add(ArmorRegistry.SOUL_ROBES_CHESTPLATE);

        this.getOrCreateTagBuilder(ModTags.Items.LEG_ARMOR)
                .add(Items.CHAINMAIL_LEGGINGS)
                .add(Items.DIAMOND_LEGGINGS)
                .add(Items.GOLDEN_LEGGINGS)
                .add(Items.IRON_LEGGINGS)
                .add(Items.LEATHER_LEGGINGS)
                .add(Items.NETHERITE_LEGGINGS)
                .add(ArmorRegistry.FORLORN_LEGGINGS)
                .add(ArmorRegistry.SOUL_INGOT_LEGGINGS)
                .add(ArmorRegistry.SOUL_ROBES_LEGGINGS);

        this.getOrCreateTagBuilder(ModTags.Items.FOOT_ARMOR)
                .add(Items.CHAINMAIL_BOOTS)
                .add(Items.DIAMOND_BOOTS)
                .add(Items.GOLDEN_BOOTS)
                .add(Items.IRON_BOOTS)
                .add(Items.LEATHER_BOOTS)
                .add(Items.NETHERITE_BOOTS)
                .add(ArmorRegistry.FORLORN_BOOTS)
                .add(ArmorRegistry.SOUL_INGOT_BOOTS)
                .add(ArmorRegistry.SOUL_ROBES_BOOTS);
    }
}