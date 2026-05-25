package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class StructureTagProvider extends TagProvider<Structure> {

    public StructureTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, RegistryKeys.STRUCTURE, completableFuture, SoulsWeaponry.ModId, existingFileHelper);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModTags.Structures.CHAMPIONS_GRAVES)
                .addOptional(new Identifier(
                        SoulsWeaponry.ModId,
                        "champions_graves"
                ));

        this.getOrCreateTagBuilder(ModTags.Structures.DECAYING_KINGDOM)
                .addOptional(new Identifier(
                        SoulsWeaponry.ModId,
                        "decaying_kingdom"
                ));
    }
}