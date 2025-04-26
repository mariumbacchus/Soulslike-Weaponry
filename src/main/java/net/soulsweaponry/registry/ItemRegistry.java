package net.soulsweaponry.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.datagen.DatagenUtil;
import net.soulsweaponry.datagen.advancements.AdvancementsProvider;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.armor.*;
import net.soulsweaponry.items.material.ModArmorMaterials;
import net.soulsweaponry.items.material.ModToolMaterials;
import net.soulsweaponry.items.potion.BloodVial;
import net.soulsweaponry.items.potion.CustomLingeringPotion;
import net.soulsweaponry.items.potion.CustomPotionItem;
import net.soulsweaponry.items.potion.CustomSplashPotion;
import net.soulsweaponry.util.RecipeHandler;

import java.util.function.Supplier;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulsWeaponry.ModId);

    public static final RegistryObject<LoreItem> LORD_SOUL_RED = ITEMS.register("lord_soul_red", () -> new LoreItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(), 4));
    public static final RegistryObject<LoreItem> LORD_SOUL_DARK = registerItem("lord_soul_dark", () -> new LoreItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(), 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_VOID = registerItem("lord_soul_void",() -> new LoreItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(), 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_ROSE = registerItem("lord_soul_rose",() -> new LoreItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(), 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_PURPLE = registerItem("lord_soul_purple", () -> new LoreItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(), 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_WHITE = registerItem("lord_soul_white", () -> new LoreItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(), 3));
    public static final RegistryObject<LoreItem> LORD_SOUL_DAY_STALKER = registerItem("lord_soul_day_stalker", () ->new LoreItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(), 2));
    public static final RegistryObject<LoreItem> LORD_SOUL_NIGHT_PROWLER = registerItem("lord_soul_night_prowler",() -> new LoreItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(), 3));
    public static final RegistryObject<Item> LOST_SOUL = registerItem("lost_soul", () -> new LoreItem(new Item.Settings().rarity(Rarity.RARE), 5));
    public static final RegistryObject<Item> MOONSTONE = registerItem("moonstone", () -> new Item(new Item.Settings()));
    public static final RegistryObject<Item> CHUNGUS_EMERALD = registerItem("chungus_emerald", () -> new LoreItem(new Item.Settings().rarity(Rarity.UNCOMMON), 1, true));
    public static final RegistryObject<Item> DEMON_HEART = registerItem("demon_heart", () -> new LoreItem(new Item.Settings().food(new FoodComponent.Builder()
            .hunger(4).saturationModifier(6f).meat().alwaysEdible()
            .effect(() -> new StatusEffectInstance(StatusEffects.STRENGTH, 150, 0), 1)
            .effect(() -> new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY.get(), 150, 0), 10)
            .effect(() -> new StatusEffectInstance(StatusEffects.NAUSEA, 400, 0), 1).build()), 3));
    public static final RegistryObject<Item> MOLTEN_DEMON_HEART= registerItem("molten_demon_heart", () -> new Item(new Item.Settings()));
    public static final RegistryObject<Item> DEMON_CHUNK = registerItem("demon_chunk",() -> new LoreItem(new Item.Settings(), 1, true));
    public static final RegistryObject<Item> CRIMSON_INGOT = registerItem("crimson_ingot",() -> new Item(new Item.Settings()));
    public static final RegistryObject<Item> SOUL_INGOT = registerItem("soul_ingot", () -> new Item(new Item.Settings()));
    public static final RegistryObject<Item> SILVER_BULLET = registerGunItem("silver_bullet", () -> new Item(new Item.Settings().maxCount(20)));
    public static final RegistryObject<Item> BOSS_COMPASS = registerItem("boss_compass", () -> new BossCompass(new Item.Settings().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MOONSTONE_RING = registerItem("moonstone_ring", () -> new MoonstoneRing(new Item.Settings().rarity(Rarity.EPIC).maxDamage(25)));
    public static final RegistryObject<Item> SHARD_OF_UNCERTAINTY = registerItem("shard_of_uncertainty", () -> new LoreItem(new Item.Settings().rarity(Rarity.RARE).fireproof(), 1, true));
    public static final RegistryObject<Item> VERGLAS = registerItem("verglas", () -> new Item(new Item.Settings()));
    public static final RegistryObject<Item> SKOFNUNG_STONE = registerItem("skofnung_stone", () -> new SkofnungStone(new Item.Settings().maxDamage(20)));
    public static final RegistryObject<Item> IRON_SKULL = registerItem("iron_skull", () -> new Item(new Item.Settings()));

    public static final RegistryObject<Item> BLOOD_VIAL_RECIPE_PAGE = registerItem("blood_vial_recipe_page", () -> new LoreItem(new Item.Settings().rarity(Rarity.UNCOMMON), 12, true));

    public static final RegistryObject<Item> MOONSTONE_SHOVEL = registerItem("moonstone_shovel",() -> new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 1.5f, -3.0f, new Item.Settings()));
    public static final RegistryObject<Item> MOONSTONE_PICKAXE = registerItem("moonstone_pickaxe", () -> new ModTools.ModPickaxe(ModToolMaterials.MOONSTONE_TOOL, 1, -2.8f, new Item.Settings()));
    public static final RegistryObject<Item> MOONSTONE_AXE = registerItem("moonstone_axe",() -> new ModTools.ModAxe(ModToolMaterials.MOONSTONE_TOOL, 5, -3.0f, new Item.Settings()));
    public static final RegistryObject<Item> MOONSTONE_HOE = registerItem("moonstone_hoe",() -> new ModTools.ModHoe(ModToolMaterials.MOONSTONE_TOOL, -3, 0.0f, new Item.Settings()));

    public static final RegistryObject<LoreItem> WITHERED_DEMON_HEART = registerItem("withered_demon_heart", () -> new LoreItem(new Item.Settings().rarity(Rarity.RARE).fireproof(), 4));
    public static final RegistryObject<LoreItem> ARKENSTONE = registerItem("arkenstone", () -> new LoreItem(new Item.Settings().rarity(Rarity.RARE).fireproof(), 4));
    public static final RegistryObject<LoreItem> ESSENCE_OF_EVENTIDE = registerItem("essence_of_eventide",() -> new LoreItem(new Item.Settings().rarity(Rarity.RARE).fireproof(), 4, true));
    public static final RegistryObject<LoreItem> ESSENCE_OF_LUMINESCENCE = registerItem("essence_of_luminescence", () -> new LoreItem(new Item.Settings().rarity(Rarity.RARE).fireproof(), 3));
    public static final RegistryObject<Item> CHAOS_CROWN = registerItem("chaos_crown", () -> new ChaosSet(ModArmorMaterials.CHAOS_SET, ArmorItem.Type.HELMET, new Item.Settings().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CHAOS_HELMET = registerArmorItem("chaos_helmet", () -> new ChaosSet(ModArmorMaterials.CHAOS_ARMOR, ArmorItem.Type.HELMET, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_chaos_helmet);
    public static final RegistryObject<Item> ARKENPLATE = registerArmorItem("arkenplate", () -> new ChaosSet(ModArmorMaterials.CHAOS_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_arkenplate);
    public static final RegistryObject<Item> ENHANCED_ARKENPLATE = registerArmorItem("enhanced_arkenplate", () -> new ChaosSet(ModArmorMaterials.CHAOS_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_enhanced_arkenplate);
    public static final RegistryObject<Item> WITHERED_CHEST = registerArmorItem("withered_chest", () -> new WitheredArmor(ModArmorMaterials.CHAOS_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_withered_chest);
    public static final RegistryObject<Item> ENHANCED_WITHERED_CHEST = registerArmorItem("enhanced_withered_chest", () -> new WitheredArmor(ModArmorMaterials.WITHERED_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC)), ConfigConstructor.disable_recipe_enhanced_withered_chest);
    public static final RegistryObject<Item> CHAOS_ROBES = registerItem("chaos_robes",() -> new ChaosSet(ModArmorMaterials.CHAOS_SET, ArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CHAOS_ORB = registerItem("chaos_orb", () -> new ChaosOrb(new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final RegistryObject<Item> GLASS_VIAL = registerItem("glass_vial", () -> new Item(new Item.Settings()));
    public static final RegistryObject<Item> BLOOD_VIAL = registerItem("blood_vial", () -> new BloodVial(new Item.Settings().maxCount(20)));

    public static final RegistryObject<PotionItem> CHUNGUS_TONIC_POTION = registerItem("chungus_tonic_potion", () -> new CustomPotionItem(new Item.Settings().maxCount(16), EffectRegistry.CHUNGUS_TONIC_POTION));
    public static final RegistryObject<PotionItem> CHUNGUS_TONIC_SPLASH = registerItem("chungus_tonic_splash", () -> new CustomSplashPotion(new Item.Settings().maxCount(16), EffectRegistry.CHUNGUS_TONIC_POTION));
    public static final RegistryObject<PotionItem> CHUNGUS_TONIC_LINGERING = registerItem("chungus_tonic_lingering", () -> new CustomLingeringPotion(new Item.Settings().maxCount(16), EffectRegistry.CHUNGUS_TONIC_POTION));

    public static final RegistryObject<MusicDiscItem> CHUNGUS_DISC = registerItem("chungus_disc", () -> new MusicDiscItem(7, SoundRegistry.BIG_CHUNGUS_SONG_EVENT, new Item.Settings().maxCount(1), 2240));
    public static final RegistryObject<MusicDiscItem> FALLEN_ICON_DISC = registerItem("fallen_icon_disc", () -> new MusicDiscItem(8, SoundRegistry.FALLEN_ICON_MONO, new Item.Settings().maxCount(1), 3440));
    public static final RegistryObject<MusicDiscItem> DRAUGR_BOSS_DISC = registerItem("draugr_boss_disc", () -> new MusicDiscItem(9, SoundRegistry.DRAUGR_BOSS_SONG_MONO, new Item.Settings().maxCount(1), 10060));
    public static final RegistryObject<MusicDiscItem> FRENZIED_SHADE_DISC = registerItem("frenzied_shade_disc", () -> new MusicDiscItem(10, SoundRegistry.FRENZIED_SHADE_SONG_MONO, new Item.Settings().maxCount(1), 1240));

    public static final RegistryObject<Item> PURIFIED_BLOOD_BUCKET = ItemRegistry.registerItem("purified_blood_bucket", () -> new BucketItem(FluidRegistry.STILL_PURIFIED_BLOOD, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));

    public static <I extends Item> RegistryObject<I> registerItem(String id, Supplier<I> item) {
        RegistryObject<I> registered = ITEMS.register(id, item);
        SoulsWeaponry.ITEM_GROUP_LIST.add(registered);
        return registered;
    }

    /**
     * Register an item that should be included in the all_weapons advancement
     */
    public static <I extends Item> RegistryObject<I> registerLegendaryItem(String name, Supplier<I> item) {
        RegistryObject<I> object = registerItem(name, item);
        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_WEAPONS.add(object);
        }
        return object;
    }

    public static <I extends Item> RegistryObject<I> registerItemRemovableRecipe(String name, Supplier<I> item, boolean removeRecipe) {
        RecipeHandler.RECIPE_IDS.put(new Identifier(SoulsWeaponry.ModId, name), removeRecipe);
        return registerItem(name, item);
    }

    public static <I extends Item> RegistryObject<I> registerArmorItem(String name, Supplier<I> item, boolean removeRecipe) {
        if (ConfigConstructor.disable_armor_recipes) {
            return registerItemRemovableRecipe(name, item, true);
        } else {
            return registerItemRemovableRecipe(name, item, removeRecipe);
        }
    }

    /**
     * Register a weapon that has a recipe that can be disabled
     */
    public static <I extends Item> RegistryObject<I> registerWeaponItem(String name, Supplier<I> item, boolean removeRecipe) {
        if (ConfigConstructor.disable_weapon_recipes) {
            return registerItemRemovableRecipe(name, item, true);
        } else {
            return registerItemRemovableRecipe(name, item, removeRecipe);
        }
    }

    /**
     * Register a weapon/item that should be included in the all_weapons advancement and has a recipe that can be disabled
     */
    public static <I extends Item> RegistryObject<I> registerLegendaryWeapon(String name, Supplier<I> item, boolean removeRecipe) {
        RegistryObject<I> object = registerWeaponItem(name, item, removeRecipe);
        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_WEAPONS.add(object);
        }
        return object;
    }

    public static <I extends Item> RegistryObject<I> registerGunItem(String name, Supplier<I> item) {
        RegistryObject<I> object = registerItemRemovableRecipe(name, item, ConfigConstructor.disable_gun_recipes);
        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_GUNS.add(object);
        }
        return object;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
