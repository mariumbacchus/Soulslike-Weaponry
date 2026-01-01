package net.soulsweaponry.registry;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.datagen.DatagenUtil;
import net.soulsweaponry.datagen.advancements.AdvancementsProvider;
import net.soulsweaponry.datagen.tags.ItemTagsProvider;
import net.soulsweaponry.items.*;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.posthit.UltraHeavy;
import net.soulsweaponry.items.armor.ModdedArmor;
import net.soulsweaponry.items.material.ModToolMaterials;
import net.soulsweaponry.items.misc.*;
import net.soulsweaponry.util.RecipeHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ItemRegistry {

    public static final Set<Item> FIREPROOF_ITEMS = new HashSet<>();

    public static final LoreItem LORD_SOUL_RED = registerItem(
            "lord_soul_red",
            settings -> new LoreItem(settings.rarity(Rarity.EPIC).fireproof(), 4)
    );
    public static final LoreItem LORD_SOUL_DARK = registerItem(
            "lord_soul_dark",
            settings -> new LoreItem(settings.rarity(Rarity.EPIC).fireproof(), 3)
    );
    public static final LoreItem LORD_SOUL_VOID = registerItem(
            "lord_soul_void",
            settings -> new LoreItem(settings.rarity(Rarity.EPIC).fireproof(), 3)
    );
    public static final LoreItem LORD_SOUL_ROSE = registerItem(
            "lord_soul_rose",
            settings -> new LoreItem(settings.rarity(Rarity.EPIC).fireproof(), 3)
    );
    public static final LoreItem LORD_SOUL_PURPLE = registerItem(
            "lord_soul_purple",
            settings -> new LoreItem(settings.rarity(Rarity.EPIC).fireproof(), 3)
    );
    public static final LoreItem LORD_SOUL_WHITE = registerItem(
            "lord_soul_white",
            settings -> new LoreItem(settings.rarity(Rarity.EPIC).fireproof(), 3)
    );
    public static final LoreItem LORD_SOUL_DAY_STALKER = registerItem(
            "lord_soul_day_stalker",
            settings -> new LoreItem(settings.rarity(Rarity.EPIC).fireproof(), 2)
    );
    public static final LoreItem LORD_SOUL_NIGHT_PROWLER = registerItem(
            "lord_soul_night_prowler",
            settings -> new LoreItem(settings.rarity(Rarity.EPIC).fireproof(), 3)
    );
    public static final Item LOST_SOUL = registerItem(
            "lost_soul",
            settings -> new LoreItem(settings.rarity(Rarity.RARE), 5)
    );
    public static final Item MOONSTONE = registerItem("moonstone", Item::new);
    public static final Item CHUNGUS_EMERALD = registerItem(
            "chungus_emerald",
            settings -> new LoreItem(settings.rarity(Rarity.UNCOMMON), 1, true)
    );
    public static final Item MOLTEN_DEMON_HEART = registerItem("molten_demon_heart", Item::new);
    public static final Item DEMON_CHUNK = registerItem(
            "demon_chunk",
            settings -> new LoreItem(settings, 1, true)
    );
    public static final Item CRIMSON_INGOT = registerItem("crimson_ingot", Item::new);
    public static final Item SOUL_INGOT = registerItem("soul_ingot", Item::new);
    public static final Item SILVER_BULLET = registerGunItem(
            "silver_bullet",
            Item::new,
            s -> s.maxCount(20),
            false
    );
    public static final Item BOSS_COMPASS = registerItem(
            "boss_compass",
            BossCompass::new,
            s -> s.rarity(Rarity.RARE)
    );
    public static final Item MOONSTONE_RING = registerItem(
            "moonstone_ring",
            MoonstoneRing::new,
            s -> s.rarity(Rarity.EPIC).maxDamage(25),
            ConfigConstructor.is_fireproof_moonlight_ring
    );
    public static final Item SHARD_OF_UNCERTAINTY = registerItem(
            "shard_of_uncertainty",
            settings -> new LoreItem(settings.rarity(Rarity.RARE).fireproof(), 1, true)
    );
    public static final Item VERGLAS = registerItem("verglas", Item::new);
    public static final Item SKOFNUNG_STONE = registerItem(
            "skofnung_stone",
            SkofnungStone::new,
            s -> s.maxDamage(20)
    );
    public static final Item IRON_SKULL = registerItem("iron_skull", Item::new);
    public static final Item TWINKLING_TITANITE = registerItem(
            "twinkling_titanite",
            Item::new,
            s -> s.rarity(Rarity.RARE)
    );

    public static final Item BLOOD_VIAL_RECIPE_PAGE = registerItem(
            "blood_vial_recipe_page",
            settings -> new LoreItem(settings.rarity(Rarity.UNCOMMON).fireproof(), 12, true)
    );

    public static final Item MOONSTONE_SHOVEL = registerItem(
            "moonstone_shovel",
            settings -> new ShovelItem(ModToolMaterials.MOONSTONE_TOOL, 1.5F, -3.0F, settings)
    );

    public static final Item MOONSTONE_PICKAXE = registerItem(
            "moonstone_pickaxe",
            settings -> new PickaxeItem(ModToolMaterials.MOONSTONE_TOOL, 1.0F, -2.8F, settings)
    );

    public static final Item MOONSTONE_AXE = registerItem(
            "moonstone_axe",
            settings -> new AxeItem(ModToolMaterials.MOONSTONE_TOOL, 5.0F, -3.0F, settings)
    );

    public static final Item MOONSTONE_HOE = registerItem(
            "moonstone_hoe",
            settings -> new HoeItem(ModToolMaterials.MOONSTONE_TOOL, -3.0F, 0.0F, settings)
    );

    public static final LoreItem WITHERED_DEMON_HEART = registerItem(
            "withered_demon_heart",
            settings -> new LoreItem(settings.rarity(Rarity.RARE).fireproof(), 4)
    );
    public static final LoreItem ARKENSTONE = registerItem(
            "arkenstone",
            settings -> new LoreItem(settings.rarity(Rarity.RARE).fireproof(), 4)
    );
    public static final LoreItem ESSENCE_OF_EVENTIDE = registerItem(
            "essence_of_eventide",
            settings -> new LoreItem(settings.rarity(Rarity.RARE).fireproof(), 4, true)
    );
    public static final LoreItem ESSENCE_OF_LUMINESCENCE = registerItem(
            "essence_of_luminescence",
            settings -> new LoreItem(settings.rarity(Rarity.RARE).fireproof(), 3)
    );
    public static final Item CHAOS_ORB = registerItem(
            "chaos_orb",
            ChaosOrb::new,
            s -> s.rarity(Rarity.EPIC).fireproof()
    );
    public static final Item GLASS_VIAL = registerItem("glass_vial", Item::new);
    public static final Item BLOOD_VIAL = registerItem(
            "blood_vial",
            BloodVial::new,
            s -> s.maxCount(20)
    );

    public static final Item CHUNGUS_DISC = registerItem(
            "chungus_disc",
            Item::new,
            s -> s.jukeboxPlayable(SoundRegistry.BIG_CHUNGUS_SONG_EVENT_KEY).maxCount(1)
    );
    public static final Item FALLEN_ICON_DISC = registerItem(
            "fallen_icon_disc",
            Item::new,
            s -> s.jukeboxPlayable(SoundRegistry.FALLEN_ICON_MONO_KEY).maxCount(1)
    );
    public static final Item DRAUGR_BOSS_DISC = registerItem(
            "draugr_boss_disc",
            Item::new,
            s -> s.jukeboxPlayable(SoundRegistry.DRAUGR_BOSS_SONG_MONO_KEY).maxCount(1)
    );

    public static void init() {}

    public static RegistryKey<Item> itemKey(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(SoulsWeaponry.ModId, path));
    }

    public static <I extends Item> I registerItem(
            String name,
            Function<Item.Settings, I> factory,
            UnaryOperator<Item.Settings> settingsOp
    ) {
        RegistryKey<Item> key = itemKey(name);
        Item.Settings settings = settingsOp.apply(new Item.Settings()).registryKey(key);
        I item = factory.apply(settings);

        SoulsWeaponry.ITEM_GROUP_LIST.add(item);
        if (DatagenUtil.isDatagenRunning()) {
            switch (item) {
                case SwordItem s -> ItemTagsProvider.SWORDS.add(item);
                case AxeItem a -> ItemTagsProvider.AXES.add(item);
                case BowItem b -> ItemTagsProvider.BOWS.add(item);
                case CrossbowItem c -> ItemTagsProvider.CROSSBOWS.add(item);
                case MaceItem m -> ItemTagsProvider.MACES.add(item);
                case ArmorItem ar -> ItemTagsProvider.ARMORS.add(item);
                case ModdedArmor ar -> ItemTagsProvider.ARMORS.add(item);
                default -> {}
            }
            if (IHasAbilities.getAbility(item.getDefaultStack(), UltraHeavy.class).isPresent()) {
                ItemTagsProvider.HEAVY_WEAPONS.add(item);
            }
            if (item instanceof SoulHarvestingItem) {
                ItemTagsProvider.SOUL_HARVESTING_WEAPONS.add(item);
            }
        }
        return Registry.register(Registries.ITEM, key, item);
    }

    public static <I extends Item> I registerItem(String name, Function<Item.Settings, I> factory) {
        return registerItem(name, factory, UnaryOperator.identity());
    }

    public static <I extends Item> I registerItem(
            String name,
            Function<Item.Settings, I> factory,
            UnaryOperator<Item.Settings> settingsOp,
            boolean fireproofFlag
    ) {
        I item = registerItem(name, factory, settingsOp);
        registerFireproof(item, fireproofFlag);
        return item;
    }

    /**
     * Register an item that should be included in the all_weapons advancement
     */
    public static <I extends Item> I registerLegendaryItem(
            String name,
            Function<Item.Settings, I> factory,
            UnaryOperator<Item.Settings> settingsOp,
            boolean fireproofFlag
    ) {
        I item = registerItem(name, factory, settingsOp, fireproofFlag);
        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_WEAPONS.add(item);
        }
        return item;
    }

    public static <I extends Item> I registerItemRemovableRecipe(
            String name,
            Function<Item.Settings, I> factory,
            UnaryOperator<Item.Settings> settingsOp,
            boolean removeRecipe,
            boolean fireproofFlag
    ) {
        RecipeHandler.RECIPE_IDS.put(Identifier.of(SoulsWeaponry.ModId, name), removeRecipe);
        return registerItem(name, factory, settingsOp, fireproofFlag);
    }

    public static <I extends Item> I registerArmorItem(
            String name,
            Function<Item.Settings, I> factory,
            UnaryOperator<Item.Settings> settingsOp,
            boolean removeRecipe,
            boolean fireproof
    ) {
        boolean finalRemoveRecipe = ConfigConstructor.disable_armor_recipes || removeRecipe;
        return registerItemRemovableRecipe(name, factory, settingsOp, finalRemoveRecipe, fireproof);
    }

    /**
     * Register a weapon that has a recipe that can be disabled
     */
    public static <I extends Item> I registerWeaponItem(
            String name,
            Function<Item.Settings, I> factory,
            UnaryOperator<Item.Settings> settingsOp,
            boolean removeRecipe,
            boolean fireproof
    ) {
        boolean finalRemoveRecipe = ConfigConstructor.disable_weapon_recipes || removeRecipe;
        return registerItemRemovableRecipe(name, factory, settingsOp, finalRemoveRecipe, fireproof);
    }

    /**
     * Register a weapon/item that should be included in the all_weapons advancement and has a recipe that can be disabled
     */
    public static <I extends Item> I registerLegendaryWeapon(
            String name,
            Function<Item.Settings, I> factory,
            UnaryOperator<Item.Settings> settingsOp,
            boolean removeRecipe,
            boolean fireproof
    ) {
        I item = registerWeaponItem(name, factory, settingsOp, removeRecipe, fireproof);

        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_WEAPONS.add(item);
        }

        return item;
    }

    public static <I extends Item> I registerGunItem(
            String name,
            Function<Item.Settings, I> factory,
            UnaryOperator<Item.Settings> settingsOp,
            boolean fireproofFlag
    ) {
        I item = registerItemRemovableRecipe(
                name, factory, settingsOp,
                ConfigConstructor.disable_gun_recipes,
                fireproofFlag
        );
        if (DatagenUtil.isDatagenRunning()) {
            AdvancementsProvider.ALL_GUNS.add(item);
        }
        return item;
    }

    public static void registerFireproof(Item item, boolean fireproof) {
        if (fireproof) {
            FIREPROOF_ITEMS.add(item);
        }
    }
}