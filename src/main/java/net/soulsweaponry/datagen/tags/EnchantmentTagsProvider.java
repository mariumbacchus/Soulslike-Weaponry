package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
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
                .add(EnchantRegistry.MISFIRE_CURSE);

        this.getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .add(EnchantRegistry.MISFIRE_CURSE);

        this.getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
                .add(EnchantRegistry.FAST_HANDS)
                .add(EnchantRegistry.STAGGER)
                .add(EnchantRegistry.VISCERAL)
                .add(EnchantRegistry.RICOCHET)
                .add(EnchantRegistry.PHANTOM_TRACE)
                .add(EnchantRegistry.FROSTSILVER)
                .add(EnchantRegistry.MISFIRE_CURSE)
                .add(EnchantRegistry.CHAIN_LIGHTNING)
                .add(EnchantRegistry.EXPLOSIVE_ROUNDS)
                .add(EnchantRegistry.ETHEREAL);

        this.getOrCreateTagBuilder(ModTags.Enchantments.BULLET_COLLISION_EXCLUSIVE_SET)
                .add(EnchantRegistry.ETHEREAL)
                .add(EnchantRegistry.RICOCHET);
    }
}
