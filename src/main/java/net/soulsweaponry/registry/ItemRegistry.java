package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.datagen.DatagenUtil;
import net.soulsweaponry.datagen.advancements.AdvancementsProvider;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.material.ModToolMaterials;
import net.soulsweaponry.items.potion.*;
import net.soulsweaponry.util.RecipeHandler;

public class ItemRegistry {
    // Integer is lines of lore, boolean after is whether the LoreItem is fireproof or not (false by default)
    public static final LoreItem LORD_SOUL_RED = new LoreItem(new FabricItemSettings().rarity(Rarity.EPIC), 4, true);
    public static final LoreItem LORD_SOUL_DARK = new LoreItem(new FabricItemSettings().rarity(Rarity.EPIC), 3, true);
    public static final LoreItem LORD_SOUL_VOID = new LoreItem(new FabricItemSettings().rarity(Rarity.EPIC), 3, true);
    public static final LoreItem LORD_SOUL_ROSE = new LoreItem(new FabricItemSettings().rarity(Rarity.EPIC), 3, true);
    public static final LoreItem LORD_SOUL_PURPLE = new LoreItem(new FabricItemSettings().rarity(Rarity.EPIC), 3, true);
    public static final LoreItem LORD_SOUL_WHITE = new LoreItem(new FabricItemSettings().rarity(Rarity.EPIC), 3, true);
    public static final LoreItem LORD_SOUL_DAY_STALKER = new LoreItem(new FabricItemSettings().rarity(Rarity.EPIC), 2, true);
    public static final LoreItem LORD_SOUL_NIGHT_PROWLER = new LoreItem(new FabricItemSettings().rarity(Rarity.EPIC), 3, true);
    public static final Item LOST_SOUL = new LoreItem(new FabricItemSettings().rarity(Rarity.RARE), 5);
    public static final Item MOONSTONE = new Item(new FabricItemSettings());
    public static final Item CHUNGUS_EMERALD = new LoreItem(new FabricItemSettings().rarity(Rarity.UNCOMMON), 1, true, false);
    public static final Item DEMON_HEART = new LoreItem(new FabricItemSettings().food(new FoodComponent.Builder()
            .hunger(4).saturationModifier(6f).meat().alwaysEdible()
            .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 150, 0), 1)
            .statusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, 150, 0), 10)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 0), 1).build()), 3);
    public static final Item MOLTEN_DEMON_HEART= new Item(new FabricItemSettings());
    public static final Item DEMON_CHUNK = new LoreItem(new FabricItemSettings(), 1, true);
    public static final Item CRIMSON_INGOT = new Item(new FabricItemSettings());
    public static final Item SOUL_INGOT = new Item(new FabricItemSettings());
    public static final Item SILVER_BULLET = new Item(new FabricItemSettings().maxCount(20));
    public static final Item BOSS_COMPASS = new BossCompass(new FabricItemSettings().rarity(Rarity.RARE));
    public static final Item MOONSTONE_RING = new MoonstoneRing(new FabricItemSettings().rarity(Rarity.EPIC).maxDamage(25));
    public static final Item SHARD_OF_UNCERTAINTY = new LoreItem(new FabricItemSettings().rarity(Rarity.RARE), 1, true, true);
    public static final Item VERGLAS = new Item(new FabricItemSettings());
    public static final Item SKOFNUNG_STONE = new SkofnungStone(new FabricItemSettings().maxDamage(20));
    public static final Item IRON_SKULL = new Item(new FabricItemSettings());

    public static final Item BLOOD_VIAL_RECIPE_PAGE = new LoreItem(new FabricItemSettings().rarity(Rarity.UNCOMMON), 12, true, true);

    public static final Item MOONSTONE_SHOVEL = new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 1.5f, -3.0f, new FabricItemSettings());
    public static final Item MOONSTONE_PICKAXE = new PickaxeItem(ModToolMaterials.MOONSTONE_TOOL, 1, -2.8f, new FabricItemSettings());
    public static final Item MOONSTONE_AXE = new AxeItem(ModToolMaterials.MOONSTONE_TOOL, 5.0f, -3.0f, new FabricItemSettings());
    public static final Item MOONSTONE_HOE = new HoeItem(ModToolMaterials.MOONSTONE_TOOL, -3, 0.0f, new FabricItemSettings());

    public static final LoreItem WITHERED_DEMON_HEART = new LoreItem(new FabricItemSettings().rarity(Rarity.RARE), 4, true);
    public static final LoreItem ARKENSTONE = new LoreItem(new FabricItemSettings().rarity(Rarity.RARE), 4, true);
    public static final LoreItem ESSENCE_OF_EVENTIDE = new LoreItem(new FabricItemSettings().rarity(Rarity.RARE), 4, true, true);
    public static final LoreItem ESSENCE_OF_LUMINESCENCE = new LoreItem(new FabricItemSettings().rarity(Rarity.RARE), 3, true);
    public static final Item CHAOS_ORB = new ChaosOrb(new FabricItemSettings().rarity(Rarity.EPIC).fireproof());
    public static final Item GLASS_VIAL = new Item(new FabricItemSettings());
    public static final Item BLOOD_VIAL = new BloodVial(new FabricItemSettings().maxCount(20));

    public static final Item CHUNGUS_DISC = new MusicDiscItem(7, SoundRegistry.BIG_CHUNGUS_SONG_EVENT, new FabricItemSettings().maxCount(1), 112);
    public static final Item FALLEN_ICON_DISC = new MusicDiscItem(8, SoundRegistry.FALLEN_ICON_MONO, new FabricItemSettings().maxCount(1), 172);
    public static final Item DRAUGR_BOSS_DISC = new MusicDiscItem(9, SoundRegistry.DRAUGR_BOSS_SONG_MONO, new FabricItemSettings().maxCount(1), 210);
    //public static final Item FRENZIED_SHADE_DISC = new MusicDiscItem(10, SoundRegistry.FRENZIED_SHADE_SONG_MONO, new FabricItemSettings().maxCount(1), 62);

    public static void init() {
        registerItem(LORD_SOUL_RED, "lord_soul_red");
        registerItem(LORD_SOUL_DARK, "lord_soul_dark");
        registerItem(LORD_SOUL_VOID, "lord_soul_void");
        registerItem(LORD_SOUL_ROSE, "lord_soul_rose");
        registerItem(LORD_SOUL_PURPLE, "lord_soul_purple");
        registerItem(LORD_SOUL_WHITE, "lord_soul_white");
        registerItem(LORD_SOUL_DAY_STALKER, "lord_soul_day_stalker");
        registerItem(LORD_SOUL_NIGHT_PROWLER, "lord_soul_night_prowler");
        registerItem(LOST_SOUL, "lost_soul");
        registerItem(MOONSTONE, "moonstone");
        registerItem(CHUNGUS_EMERALD, "chungus_emerald");
        registerItem(DEMON_HEART, "demon_heart");
        registerItem(MOLTEN_DEMON_HEART, "molten_demon_heart");
        registerItem(DEMON_CHUNK, "demon_chunk");
        registerItem(CRIMSON_INGOT, "crimson_ingot");
        registerItem(SOUL_INGOT, "soul_ingot");
        registerGunItem(SILVER_BULLET, "silver_bullet");
        registerItem(BOSS_COMPASS, "boss_compass");
        registerItem(MOONSTONE_RING, "moonstone_ring");
        registerItem(SHARD_OF_UNCERTAINTY, "shard_of_uncertainty");
        registerItem(VERGLAS, "verglas");
        registerItem(SKOFNUNG_STONE, "skofnung_stone");
        registerItem(IRON_SKULL, "iron_skull");
        registerItem(BLOOD_VIAL_RECIPE_PAGE, "blood_vial_recipe_page");

        registerItem(MOONSTONE_SHOVEL, "moonstone_shovel");
        registerItem(MOONSTONE_PICKAXE, "moonstone_pickaxe");
        registerItem(MOONSTONE_AXE, "moonstone_axe");
        registerItem(MOONSTONE_HOE, "moonstone_hoe");

        registerItem(WITHERED_DEMON_HEART, "withered_demon_heart");
        registerItem(ARKENSTONE, "arkenstone");
        registerItem(ESSENCE_OF_EVENTIDE, "essence_of_eventide");
        registerItem(ESSENCE_OF_LUMINESCENCE, "essence_of_luminescence");
        registerItem(CHAOS_ORB, "chaos_orb");
        registerItem(GLASS_VIAL, "glass_vial");
        registerItem(BLOOD_VIAL, "blood_vial");

        registerItem(CHUNGUS_DISC, "chungus_disc");
        registerItem(FALLEN_ICON_DISC, "fallen_icon_disc");
        registerItem(DRAUGR_BOSS_DISC, "draugr_boss_disc");
        //registerItem(FRENZIED_SHADE_DISC, "frenzied_shade_disc");
    }

    public static <I extends Item> I registerItem(I item, String name) {
        SoulsWeaponry.ITEM_GROUP_LIST.add(item);
		return Registry.register(Registries.ITEM, new Identifier(SoulsWeaponry.ModId, name), item);
	}

    /**
     * Register an item that should be included in the all_weapons advancement
     */
    public static <I extends Item> I registerLegendaryItem(I item, String name) {
        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_WEAPONS.add(item);
        }
        return registerItem(item, name);
    }

    public static <I extends Item> I registerItemRemovableRecipe(I item, String name, boolean removeRecipe) {
        RecipeHandler.RECIPE_IDS.put(new Identifier(SoulsWeaponry.ModId, name), removeRecipe);
        return registerItem(item, name);
    }

    public static <I extends Item> I registerArmorItem(I item, String name, boolean removeRecipe) {
        if (ConfigConstructor.disable_armor_recipes) {
            return registerItemRemovableRecipe(item, name, true);
        } else {
            return registerItemRemovableRecipe(item, name, removeRecipe);
        }
    }

    /**
     * Register a weapon that has a recipe that can be disabled
     */
    public static <I extends Item> I registerWeaponItem(I item, String name, boolean removeRecipe) {
        if (ConfigConstructor.disable_weapon_recipes) {
            return registerItemRemovableRecipe(item, name, true);
        } else {
            return registerItemRemovableRecipe(item, name, removeRecipe);
        }
    }

    /**
     * Register a weapon/item that should be included in the all_weapons advancement and has a recipe that can be disabled
     */
    public static <I extends Item> I registerLegendaryWeapon(I item, String name, boolean removeRecipe) {
        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_WEAPONS.add(item);
        }
        return registerWeaponItem(item, name, removeRecipe);
    }

    public static <I extends Item> I registerGunItem(I item, String name) {
        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_GUNS.add(item);
        }
        return registerItemRemovableRecipe(item, name, ConfigConstructor.disable_gun_recipes);
    }
}
