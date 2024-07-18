package net.soulsweaponry.world.gen;

public class WorldGen {

    public static void generateCustomWorldGen() {
        // Be wary of calling generation in the order! See GenerationStep.Feature to set up the right order.
        // Keep in mind that data packs for ore generation can be used in 1.19.3 and up
        OreGeneration.generateOres();
    }
}