package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class StatusEffectTagProvider extends FabricTagProvider<StatusEffect> {

    public StatusEffectTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.STATUS_EFFECT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModTags.Effects.DAMAGE_OVER_TIME)
                .add(StatusEffects.POISON.value())
                .add(StatusEffects.WITHER.value());
        this.getOrCreateTagBuilder(ModTags.Effects.NIGHTLORD_ATTACK_BOOST_GAINED_FROM)
                .add(EffectRegistry.BLEED.value())
                .add(EffectRegistry.FREEZING.value())
                .add(EffectRegistry.DECAY.value())
                .add(EffectRegistry.BLIGHT.value())
                .add(StatusEffects.WITHER.value())
                .add(StatusEffects.SLOWNESS.value())
                .add(StatusEffects.MINING_FATIGUE.value())
                .add(StatusEffects.WEAKNESS.value())
                .add(StatusEffects.POISON.value());
    }
}
