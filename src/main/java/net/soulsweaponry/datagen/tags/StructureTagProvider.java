package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class StructureTagProvider extends FabricTagProvider<Structure> {

    public StructureTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.STRUCTURE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModTags.Structures.CHAMPIONS_GRAVES)
                .addOptional(Identifier.of(SoulsWeaponry.ModId, "champions_graves"));

        this.getOrCreateTagBuilder(ModTags.Structures.DECAYING_KINGDOM)
                .addOptional(Identifier.of(SoulsWeaponry.ModId, "decaying_kingdom"));
    }
}
