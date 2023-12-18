package net.soulsweapons.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_WEAPON_RECIPES;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_GUN_RECIPES;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_ENCHANTS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_FAST_HANDS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_POSTURE_BREAKER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_STAGGER;

    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_BLOODTHIRSTER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_COMET_SPEAR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_DARKIN_BLADE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_DAWNBREAKER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_GUTS_SWORD;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_DRAGON_SWORDSPEAR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_DRAGON_STAFF;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_DRAUGR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_GALEFORCE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_RAGEBLADE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_LICH_BANE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_MOONLIGHT_GREATSWORD;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_MOONLIGHT_SHORTSWORD;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_NIGHTFALL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_SOUL_REAPER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_SAWBLADE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_WABBAJACK;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_LEVIATHAN_AXE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_SKOFNUNG;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_PURE_MOONLIGHT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_MJOLNIR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_FREYR_SWORD;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_STING;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_FEATHERLIGHT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_CRUCIBLE_SWORD;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_DARKIN_SCYTHE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_KIRKHAMMER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_LUDWIGS_HOLY_BLADE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_DRAUPNIR_SPEAR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_HOLY_MOONLIGHT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_MASTER_SWORD;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_FROSTMOURNE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_NIGHTS_EDGE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_EMPOWERED_DAWNBREAKER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_KRAKEN_SLAYER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECIPE_DARKMOON_LONGBOW;

    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERED_DEMON_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> CHUNGUS_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> FORLORN_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> CHUNGUS_MONOLITH_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SPAWN_WITHERED_DEMON;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SPAWN_CHUNGUS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SPAWN_FORLORN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CAN_BOSS_BREAK_BLOCK;

    public static final ForgeConfigSpec.ConfigValue<List<Integer>> CHAOS_ARMOR_ARMOR_POINTS;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> CHAOS_SET_ARMOR_POINTS;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> SOUL_INGOT_ARMOR_POINTS;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> SOUL_ROBES_ARMOR_POINTS;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> FORLORN_ARMOR_POINTS;

    public static final ForgeConfigSpec.ConfigValue<Integer> BLOODTHIRSTER_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BLOODTHIRSTER_OVERSHIELD;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIFE_STEAL_BASE_HEAL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LIFE_STEAL_SCALES;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIFE_STEAL_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> BLUEMOON_GREATSWORD_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> BLUEMOON_SHORTSWORD_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMET_SPEAR_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> COMET_SPEAR_PROJECTILE_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Float> COMET_SPEAR_ABILITY_DAMAGE;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMET_SPEAR_SKYFALL_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMET_SPEAR_THROW_COOLDOWN;

    static {
        BUILDER.push("Common config for Soulslike Weaponry Forge");

        DISABLE_WEAPON_RECIPES = BUILDER.define("Disable all legendary weapon recipes", false);
        DISABLE_GUN_RECIPES = BUILDER.define("Disable all gun recipes", false);

        DISABLE_ENCHANTS = BUILDER.comment("Disable registration of enchants.").define("Disable all enchants", false);
        DISABLE_FAST_HANDS = BUILDER.define("Disable Fast Hands/Quick Reload enchant", false);
        DISABLE_POSTURE_BREAKER = BUILDER.define("Disable Posture Breaker/Visceral enchant", false);
        DISABLE_STAGGER = BUILDER.define("Disable Stagger enchant", false);

        RECIPE_BLOODTHIRSTER = BUILDER.comment("Disable recipes of specific weapons, sorted from A-Z.").define("Disable Bloodthirster recipe", false);
        RECIPE_COMET_SPEAR = BUILDER.define("Disable Comet Spear recipe", false);
        RECIPE_CRUCIBLE_SWORD = BUILDER.define("Disable Crucible Sword recipe", false);
        RECIPE_DARKIN_BLADE = BUILDER.define("Disable Darkin Blade recipe", false);
        RECIPE_DARKIN_SCYTHE = BUILDER.define("Disable Darkin Scythe recipe", false);
        RECIPE_DARKMOON_LONGBOW = BUILDER.define("Disable Darkmoon Longbow recipe", false);
        RECIPE_DAWNBREAKER = BUILDER.define("Disable Dawnbreaker recipe", false);
        RECIPE_DRAGON_SWORDSPEAR = BUILDER.define("Disable Dragonslayer Swordspear recipe", false);
        RECIPE_DRAGON_STAFF = BUILDER.define("Disable Dragon Staff recipe", false);
        RECIPE_DRAUGR = BUILDER.define("Disable Draugr recipe", false);
        RECIPE_DRAUPNIR_SPEAR = BUILDER.define("Disable Draupnir Spear recipe", false);
        RECIPE_FEATHERLIGHT = BUILDER.define("Disable Featherlight recipe", false);
        RECIPE_FREYR_SWORD = BUILDER.define("Disable Sword of Freyr recipe", false);
        RECIPE_FROSTMOURNE = BUILDER.define("Disable Frostmourne recipe", true);
        RECIPE_GALEFORCE = BUILDER.define("Disable Galeforce recipe", false);
        RECIPE_GUTS_SWORD = BUILDER.define("Disable Heap of Raw Iron (Guts' Sword) recipe", false);
        RECIPE_EMPOWERED_DAWNBREAKER = BUILDER.define("Disable Genesis Fracture (Empowered Dawnbreaker) recipe", false);
        RECIPE_RAGEBLADE = BUILDER.define("Disable Guinsoo's Rageblade recipe", false);
        RECIPE_HOLY_MOONLIGHT = BUILDER.define("Disable Holy Moonlight Greatsword recipe", false);
        RECIPE_KIRKHAMMER = BUILDER.define("Disable Kirkhammer recipe", false);
        RECIPE_KRAKEN_SLAYER = BUILDER.define("Disable Kraken Slayer recipe", false);
        RECIPE_LEVIATHAN_AXE = BUILDER.define("Disable Leviathan Axe recipe", false);
        RECIPE_LICH_BANE = BUILDER.define("Disable Lich Bane recipe", false);
        RECIPE_LUDWIGS_HOLY_BLADE = BUILDER.define("Disable Ludwig's Holy Blade recipe", false);
        RECIPE_MASTER_SWORD = BUILDER.define("Disable Master Sword recipe", false);
        RECIPE_MJOLNIR = BUILDER.define("Disable Mjolnir recipe", false);
        RECIPE_MOONLIGHT_GREATSWORD = BUILDER.define("Disable Moonlight Greatsword recipe", false);
        RECIPE_MOONLIGHT_SHORTSWORD = BUILDER.define("Disable Moonlight Shortsword recipe", false);
        RECIPE_NIGHTFALL = BUILDER.define("Disable Nightfall recipe", false);
        RECIPE_NIGHTS_EDGE = BUILDER.define("Disable Night's Edge recipe", false);
        RECIPE_PURE_MOONLIGHT = BUILDER.define("Disable Pure Moonlight Greatsword recipe", false);
        RECIPE_SKOFNUNG = BUILDER.define("Disable Skofnung recipe", false);
        RECIPE_SOUL_REAPER = BUILDER.define("Disable Soul Reaper recipe", false);
        RECIPE_STING = BUILDER.define("Disable Sting recipe", false);
        RECIPE_WABBAJACK = BUILDER.define("Disable Withered Wabbajack recipe", false);
        RECIPE_SAWBLADE = BUILDER.define("Disable Whirligig Sawblade recipe", false);

        WITHERED_DEMON_SPAWN_WEIGHT = BUILDER.comment("Spawn weight for the mobs, the higher the value, the more spawns.").define("Withered Demon spawn weight", 20);
        CHUNGUS_SPAWN_WEIGHT = BUILDER.define("Moderately Sized Chungus spawn weight", 100);
        FORLORN_SPAWN_WEIGHT = BUILDER.define("Evil Forlorn spawn weight", 20);

        CHUNGUS_MONOLITH_RANGE = BUILDER.comment("Radius in blocks which is the range how far away a monolith chungusus can spawn.").define("Chungus Monolith radius", 32);

        SPAWN_WITHERED_DEMON = BUILDER.comment("Determines whether the mobs can spawn.").define("Can Withered Demon spawn", true);
        SPAWN_CHUNGUS = BUILDER.define("Can Moderately Sized Chungus spawn", true);
        SPAWN_FORLORN = BUILDER.define("Can Moderately Sized Chungus spawn", true);

        CAN_BOSS_BREAK_BLOCK = BUILDER.comment("Some bosses destroy blocks around them periodically. If false, they won't.").define("Can Bosses break blocks", true);

        CHAOS_ARMOR_ARMOR_POINTS = BUILDER.comment("Armor values for the Armor sets, boots on left, head on right.").define("Chaos Armor Points", Arrays.asList(3, 6, 8, 3));
        CHAOS_SET_ARMOR_POINTS = BUILDER.define("Chaos Set Points", Arrays.asList(2, 3, 4, 1));
        SOUL_INGOT_ARMOR_POINTS = BUILDER.define("Soul Ingot Armor Points", Arrays.asList(3, 5, 7, 3));
        SOUL_ROBES_ARMOR_POINTS = BUILDER.define("Soul Robes Armor Points", Arrays.asList(2, 3, 4, 3));
        FORLORN_ARMOR_POINTS = BUILDER.define("Forlorn Armor Points", Arrays.asList(3, 6, 8, 3));

        BLOODTHIRSTER_DAMAGE = BUILDER.comment("Values for weapon damage, ability cooldowns, etc. Sorted roughly from A-Z, excluding value names related to the weapon.").define("Bloodthirster damage", 8);
        BLOODTHIRSTER_OVERSHIELD = BUILDER.define("Bloodthirster can overshield", true);
        LIFE_STEAL_BASE_HEAL = BUILDER.define("Life Steal Item base heal", 2);
        LIFE_STEAL_SCALES = BUILDER.define("Life Steal Item healing scales with enchants", true);
        LIFE_STEAL_COOLDOWN = BUILDER.define("Life Steal Item heal cooldown", 60);
        BLUEMOON_GREATSWORD_DAMAGE = BUILDER.define("Bluemoon Greatsword damage", 8);
        BLUEMOON_SHORTSWORD_DAMAGE = BUILDER.define("Bluemoon Shortsword damage", 7);
        COMET_SPEAR_DAMAGE = BUILDER.define("Comet Spear damage", 8);
        COMET_SPEAR_PROJECTILE_DAMAGE = BUILDER.define("Comet Spear projectile damage", 8f);
        COMET_SPEAR_ABILITY_DAMAGE = BUILDER.define("Comet Spear ability damage", 10f);
        COMET_SPEAR_SKYFALL_COOLDOWN = BUILDER.define("Comet Spear skyfall ability cooldown", 400);
        COMET_SPEAR_THROW_COOLDOWN = BUILDER.define("Comet Spear throw cooldown", 25);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
