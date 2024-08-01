package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class EntityTagsProvider extends FabricTagProvider.EntityTypeTagProvider {

    public EntityTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(ModTags.Entities.RANGED_MOBS)
                .add(EntityType.SKELETON)
                .add(EntityType.BLAZE)
                .add(EntityType.GUARDIAN)
                .add(EntityType.EVOKER)
                .add(EntityType.ELDER_GUARDIAN)
                .add(EntityType.ILLUSIONER)
                .add(EntityType.SHULKER)
                .add(EntityType.SNOW_GOLEM)
                .add(EntityType.WITCH)
                .add(EntityType.WITHER);
        this.getOrCreateTagBuilder(ModTags.Entities.SKELETONS)
                .add(EntityType.SKELETON)
                .add(EntityType.WITHER_SKELETON)
                .add(EntityType.SKELETON_HORSE);
        this.getOrCreateTagBuilder(ModTags.Entities.BOSSES)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityRegistry.ACCURSED_LORD_BOSS)
                .add(EntityRegistry.DRAUGR_BOSS)
                .add(EntityRegistry.NIGHT_SHADE)
                .add(EntityRegistry.RETURNING_KNIGHT)
                .add(EntityRegistry.CHAOS_MONARCH)
                .add(EntityRegistry.MOONKNIGHT)
                .add(EntityRegistry.DAY_STALKER)
                .add(EntityRegistry.NIGHT_PROWLER);
    }
}