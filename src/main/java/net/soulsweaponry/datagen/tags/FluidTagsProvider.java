package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.FluidTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.FluidRegistry;

import java.util.concurrent.CompletableFuture;

public class FluidTagsProvider extends TagProvider<Fluid> {

    public FluidTagsProvider(
            DataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, RegistryKeys.FLUID, registriesFuture, SoulsWeaponry.ModId, existingFileHelper);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(FluidTags.WATER)
                .addOptional(FluidRegistry.STILL_PURIFIED_BLOOD.getId())
                .addOptional(FluidRegistry.FLOWING_PURIFIED_BLOOD.getId());
    }
}