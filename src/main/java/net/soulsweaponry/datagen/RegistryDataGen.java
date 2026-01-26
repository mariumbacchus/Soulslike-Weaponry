package net.soulsweaponry.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.soulsweaponry.SoulsWeaponry;

import java.util.concurrent.CompletableFuture;

public class RegistryDataGen extends FabricDynamicRegistryProvider {

    public RegistryDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.DAMAGE_TYPE));
    }

    @Override
    public String getName() {
        return SoulsWeaponry.ModId + " Dynamic Registries";
    }
}
