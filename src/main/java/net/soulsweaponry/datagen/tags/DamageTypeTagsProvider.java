package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.DamageSourceRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagsProvider extends TagProvider<DamageType> {

    public DamageTypeTagsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, RegistryKeys.DAMAGE_TYPE, completableFuture, SoulsWeaponry.ModId, existingFileHelper);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
                .addOptional(DamageSourceRegistry.BLEED.getValue())
                .addOptional(DamageSourceRegistry.DRAGON_MIST.getValue());

        this.getOrCreateTagBuilder(DamageTypeTags.IS_LIGHTNING)
                .addOptional(DamageSourceRegistry.PLAYER_LIGHTNING.getValue());

        this.getOrCreateTagBuilder(DamageTypeTags.IS_FIRE)
                .addOptional(DamageSourceRegistry.PLAYER_FIRE.getValue());

        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_COOLDOWN)
                .addOptional(DamageSourceRegistry.MAGIC_DAMAGE_BYPASS_COOLDOWN.getValue())
                .addOptional(DamageSourceRegistry.SILVER_BULLET_BYPASS_COOLDOWN.getValue());

        this.getOrCreateTagBuilder(DamageTypeTags.IS_PROJECTILE).addOptional(DamageSourceRegistry.SILVER_BULLET_BYPASS_COOLDOWN.getValue());
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR).addOptional(DamageSourceRegistry.MAGIC_DAMAGE_BYPASS_COOLDOWN.getValue());
        this.getOrCreateTagBuilder(DamageTypeTags.NO_IMPACT).addOptional(DamageSourceRegistry.MAGIC_DAMAGE_BYPASS_COOLDOWN.getValue());
        this.getOrCreateTagBuilder(DamageTypeTags.AVOIDS_GUARDIAN_THORNS).addOptional(DamageSourceRegistry.MAGIC_DAMAGE_BYPASS_COOLDOWN.getValue());
        this.getOrCreateTagBuilder(DamageTypeTags.WITCH_RESISTANT_TO).addOptional(DamageSourceRegistry.MAGIC_DAMAGE_BYPASS_COOLDOWN.getValue());
    }
}