package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.soulsweaponry.util.CustomDamageSource;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagsProvider extends FabricTagProvider<DamageType> {

    public DamageTypeTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
                .addOptional(CustomDamageSource.BLEED)
                .addOptional(CustomDamageSource.DRAGON_MIST);

        this.getOrCreateTagBuilder(DamageTypeTags.IS_LIGHTNING)
                .addOptional(CustomDamageSource.PLAYER_LIGHTNING);

        this.getOrCreateTagBuilder(DamageTypeTags.IS_FIRE)
                .addOptional(CustomDamageSource.PLAYER_FIRE);
    }
}
