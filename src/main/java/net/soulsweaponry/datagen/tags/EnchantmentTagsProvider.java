package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsProvider extends FabricTagProvider<Enchantment> {

    public EnchantmentTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModTags.Enchantments.DAMAGE_ENCHANTMENTS)
                .add(Enchantments.SMITE)
                .add(Enchantments.BANE_OF_ARTHROPODS)
                .add(Enchantments.SHARPNESS)
                .add(Enchantments.IMPALING);

        this.getOrCreateTagBuilder(ModTags.Enchantments.BULLET_COLLISION_EXCLUSIVE_SET)
                .add(EnchantRegistry.ETHEREAL)
                .add(EnchantRegistry.RICOCHET);

        this.getOrCreateTagBuilder(ModTags.Enchantments.APPLY_FIRE)
                .add(Enchantments.FLAME)
                .add(Enchantments.FIRE_ASPECT);

        this.getOrCreateTagBuilder(ModTags.Enchantments.PREVENTS_AMMO_CONSUME)
                .add(Enchantments.INFINITY);
    }
}
