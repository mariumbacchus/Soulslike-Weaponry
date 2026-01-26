package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.FluidTags;
import net.soulsweaponry.registry.FluidRegistry;

import java.util.concurrent.CompletableFuture;

public class FluidTagsProvider extends FabricTagProvider.FluidTagProvider {

    public FluidTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(FluidTags.WATER)
                .add(FluidRegistry.STILL_PURIFIED_BLOOD)
                .add(FluidRegistry.FLOWING_PURIFIED_BLOOD);
    }
}
