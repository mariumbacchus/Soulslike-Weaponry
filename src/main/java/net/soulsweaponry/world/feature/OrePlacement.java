package net.soulsweaponry.world.feature;

import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class OrePlacement {

    public static List<PlacementModifier> orePlacement(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightModifier) {
        return orePlacement(CountPlacementModifier.of(count), heightModifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightModifier) {
        return orePlacement(RarityFilterPlacementModifier.of(chance), heightModifier);
    }
}
