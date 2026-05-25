package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class StatusEffectTagProvider extends TagProvider<StatusEffect> {

    public StatusEffectTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, RegistryKeys.STATUS_EFFECT, completableFuture, SoulsWeaponry.ModId, existingFileHelper);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModTags.Effects.DAMAGE_OVER_TIME)
                .addOptional(RegistryKeys.STATUS_EFFECT.getValue().withPath("poison"))
                .addOptional(RegistryKeys.STATUS_EFFECT.getValue().withPath("wither"));

        this.getOrCreateTagBuilder(ModTags.Effects.NIGHTLORD_ATTACK_BOOST_GAINED_FROM)
                .addOptional(EffectRegistry.BLEED.getId())
                .addOptional(EffectRegistry.FREEZING.getId())
                .addOptional(EffectRegistry.DECAY.getId())
                .addOptional(EffectRegistry.BLIGHT.getId())
                .addOptional(RegistryKeys.STATUS_EFFECT.getValue().withPath("wither"))
                .addOptional(RegistryKeys.STATUS_EFFECT.getValue().withPath("slowness"))
                .addOptional(RegistryKeys.STATUS_EFFECT.getValue().withPath("mining_fatigue"))
                .addOptional(RegistryKeys.STATUS_EFFECT.getValue().withPath("weakness"))
                .addOptional(RegistryKeys.STATUS_EFFECT.getValue().withPath("poison"));
    }
}