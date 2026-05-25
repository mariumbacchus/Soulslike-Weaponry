package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsProvider extends TagProvider<Enchantment> {

    public EnchantmentTagsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, RegistryKeys.ENCHANTMENT, completableFuture, SoulsWeaponry.ModId, existingFileHelper);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModTags.Enchantments.DAMAGE_ENCHANTMENTS)
                .addOptional(id(Enchantments.SMITE))
                .addOptional(id(Enchantments.BANE_OF_ARTHROPODS))
                .addOptional(id(Enchantments.SHARPNESS))
                .addOptional(id(Enchantments.IMPALING));

        this.getOrCreateTagBuilder(ModTags.Enchantments.BULLET_COLLISION_EXCLUSIVE_SET)
                .addOptional(id(EnchantRegistry.ETHEREAL))
                .addOptional(id(EnchantRegistry.RICOCHET));

        this.getOrCreateTagBuilder(ModTags.Enchantments.APPLY_FIRE)
                .addOptional(id(Enchantments.FLAME))
                .addOptional(id(Enchantments.FIRE_ASPECT));

        this.getOrCreateTagBuilder(ModTags.Enchantments.PREVENTS_AMMO_CONSUME)
                .addOptional(id(Enchantments.INFINITY));
    }

    private static Identifier id(Enchantment enchantment) {
        return ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
    }
}