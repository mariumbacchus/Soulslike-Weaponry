package net.soulsweapons.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<Integer>> CHAOS_ARMOR_ARMOR_POINTS;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> CHAOS_SET_ARMOR_POINTS;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> SOUL_INGOT_ARMOR_POINTS;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> SOUL_ROBES_ARMOR_POINTS;
    public static final ForgeConfigSpec.ConfigValue<List<Integer>> FORLORN_ARMOR_POINTS;

    static {
        BUILDER.push("Client config for Soulslike Weaponry Forge");

        CHAOS_ARMOR_ARMOR_POINTS = BUILDER.comment("Armor values for the Chaos Armor set, boots on left, head on right").define("Chaos Armor Points", Arrays.asList(3, 6, 8, 3));
        CHAOS_SET_ARMOR_POINTS = BUILDER.comment("Armor values for the Chaos Set, boots on left, head on right").define("Chaos Set Points", Arrays.asList(2, 3, 4, 1));
        SOUL_INGOT_ARMOR_POINTS = BUILDER.comment("Armor values for the Soul Ingot Armor set, boots on left, head on right").define("Soul Ingot Armor Points", Arrays.asList(3, 5, 7, 3));
        SOUL_ROBES_ARMOR_POINTS = BUILDER.comment("Armor values for the Soul Robes Armor set, boots on left, head on right").define("Soul Robes Armor Points", Arrays.asList(2, 3, 4, 3));
        FORLORN_ARMOR_POINTS = BUILDER.comment("Armor values for the Forlorn Armor set, boots on left, head on right").define("Forlorn Armor Points", Arrays.asList(3, 6, 8, 3));


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
