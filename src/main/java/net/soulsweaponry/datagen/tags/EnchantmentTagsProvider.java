package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsProvider extends FabricTagProvider.EnchantmentTagProvider {

    public EnchantmentTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(EnchantmentTags.CURSE)
                .addOptional(EnchantRegistry.MISFIRE_CURSE);

        this.getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .addOptional(EnchantRegistry.MISFIRE_CURSE);

        this.getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
                .addOptional(EnchantRegistry.FAST_HANDS)
                .addOptional(EnchantRegistry.STAGGER)
                .addOptional(EnchantRegistry.VISCERAL)
                .addOptional(EnchantRegistry.RICOCHET)
                .addOptional(EnchantRegistry.PHANTOM_TRACE)
                .addOptional(EnchantRegistry.FROSTSILVER)
                .addOptional(EnchantRegistry.MISFIRE_CURSE)
                .addOptional(EnchantRegistry.CHAIN_LIGHTNING)
                .addOptional(EnchantRegistry.EXPLOSIVE_ROUNDS)
                .addOptional(EnchantRegistry.ETHEREAL);

        this.getOrCreateTagBuilder(ModTags.Enchantments.BULLET_COLLISION_EXCLUSIVE_SET)
                .addOptional(EnchantRegistry.ETHEREAL)
                .addOptional(EnchantRegistry.RICOCHET);

        this.getOrCreateTagBuilder(ModTags.Enchantments.APPLY_FIRE)
                .addOptional(Enchantments.FLAME)
                .addOptional(Enchantments.FIRE_ASPECT);

        this.getOrCreateTagBuilder(ModTags.Enchantments.PREVENTS_AMMO_CONSUME)
                .addOptional(Enchantments.INFINITY);
    }
}
