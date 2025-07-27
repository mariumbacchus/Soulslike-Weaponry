package net.soulsweaponry.datagen.enchantment;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EnchantRegistry;

import java.util.concurrent.CompletableFuture;

public class EnchantmentProvider extends FabricDynamicRegistryProvider {

    public EnchantmentProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        EnchantRegistry.bootstrap(createRegisterable(wrapperLookup, entries));
    }

    @Override
    public String getName() {
        return SoulsWeaponry.ModId + " Dynamic Registries";
    }

    private static <T> Registerable<T> createRegisterable(RegistryWrapper.WrapperLookup registries, Entries entries) {
        return new Registerable<>() {
            @Override
            public RegistryEntry.Reference<T> register(RegistryKey<T> key, T value, Lifecycle lifecycle) {
                return (RegistryEntry.Reference<T>) entries.add(key, value);
            }

            @Override
            public <S> RegistryEntryLookup<S> getRegistryLookup(RegistryKey<? extends Registry<? extends S>> registryRef) {
                return registries.getWrapperOrThrow(registryRef);
            }
        };
    }
}