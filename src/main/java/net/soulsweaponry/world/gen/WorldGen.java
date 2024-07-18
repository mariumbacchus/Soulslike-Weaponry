package net.soulsweaponry.world.gen;

public class WorldGen {

    public static void generateCustomWorldGen() {
        // NOTE: Be wary of calling generation in the right order!
        OreGeneration.generateOres();
    }
}
